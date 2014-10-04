package dom.monitoreo.sender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Simplistic telnet client.
 */
public class ZabbixSender {

    private final String host;
    private final int port;
    private boolean connected;
    private Channel ch;
    private EventLoopGroup group = new NioEventLoopGroup();
    private ZabbixClientInitializer initializer = new ZabbixClientInitializer();

    public ZabbixSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(String h, Map<String, String> values) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(initializer);

            // Start the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String sendString = buildJSonString(h, values);
            System.out.println(sendString);
            byte[] header = getHeader(sendString);

            // Sends the received line to the server.
            // lastWriteFuture = ch.writeAndFlush((line + "\r\n").getBytes());
            lastWriteFuture = ch.writeAndFlush(header);
            lastWriteFuture = ch.writeAndFlush(sendString.getBytes());

            // If user typed the 'bye' command, wait until the server closes
            // the connection.
            ch.closeFuture().sync();

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }


    //TODO: add additional connectivity checks! Connection timeout etc.
    private void connect() throws InterruptedException {
        if(!connected){
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ZabbixClientInitializer());

            // Start the connection attempt.
            ch = b.connect(host, port).sync().channel();
            connected = true;
        }
    }

    public void send(String h, String key, String value) throws InterruptedException {
        if(!connected){
            connect();
        }

        try {
            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            String sendString = buildJSonString(h, key, value);
            System.out.println(sendString);
            byte[] header = getHeader(sendString);

            // Sends the received line to the server.
            // lastWriteFuture = ch.writeAndFlush((line + "\r\n").getBytes());
            lastWriteFuture = ch.writeAndFlush(header);
            lastWriteFuture = ch.writeAndFlush(sendString.getBytes());

            // If user typed the 'bye' command, wait until the server closes
            // the connection.

            ch.closeFuture().sync();

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }


    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ZabbixClientInitializer());

            // Start the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                String sendString = buildJSonString("StatsCollector", "calls.96", line);
                System.out.println(sendString);
                byte[] header = getHeader(sendString);

                // Sends the received line to the server.
//                lastWriteFuture = ch.writeAndFlush((line + "\r\n").getBytes());
                lastWriteFuture = ch.writeAndFlush(header);
                lastWriteFuture = ch.writeAndFlush(sendString.getBytes());

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    private String buildJSonString(String host, Map<String, String> valuesMap){
        String header = "{\"request\":\"sender data\", \"data\":[";
        String footer =  "]}\n";

        StringBuilder sb = new StringBuilder();
        for (String key : valuesMap.keySet()) {
            String value = valuesMap.get(key).replace("\\", "\\\\");

            sb.append("{\"host\": \"" + host + "\",");
            sb.append("\"key\": \"" + key + "\",");
            sb.append("\"value\": \"" + value + "\"");
            sb.append("},");
        }
        //Delete last comma
        sb.deleteCharAt(sb.length()-1);
        return header + sb.toString() + footer;
    }

    private String buildJSonString(String host, String item, String value){
        return 		  "{"
                + "\"request\":\"sender data\",\n"
                + "\"data\":[\n"
                +        "{\n"
                +                "\"host\":\"" + host + "\",\n"
                +                "\"key\":\"" + item + "\",\n"
                +                "\"value\":\"" + value.replace("\\", "\\\\") + "\"}]}\n" ;
    }


    private byte[] getHeader(String string){
        byte[] data = string.getBytes();
        int length = data.length;

        return new byte[] {
                'Z', 'B', 'X', 'D',
                '\1',
                (byte)(length & 0xFF),
                (byte)((length >> 8) & 0x00FF),
                (byte)((length >> 16) & 0x0000FF),
                (byte)((length >> 24) & 0x000000FF),
                '\0','\0','\0','\0'};
    }

    public static void main(String[] args) throws Exception {
        String host = "172.16.18.3";
        int port = 10051;

//      new ZabbixSender(host, port).run();
//      new ZabbixSender(host, port).send("StatsCollector", "calls.80", "0");

        ZabbixSender zabis = new ZabbixSender(host, port);

        Map<String, String> values = new HashMap<String, String>();
       /* values.put("calls.94", "0");
        values.put("agents.94", "0");
        values.put("calls.87", "0");
        values.put("agents.87", "0");
        values.put("calls.88", "0");
        values.put("agents.88", "0");
        values.put("calls.91", "0");
        values.put("agents.91", "0");*/
        values.put("calls.85", "0");
        values.put("agents.85", "0");
//        System.out.println(zabis.buildJSonString("StatsCollector", values));
        zabis.send("StatsCollector", values);
    }
}