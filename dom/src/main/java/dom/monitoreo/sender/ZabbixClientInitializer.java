package dom.monitoreo.sender;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * Creates a newly configured {@link io.netty.channel.ChannelPipeline} for a new channel.
 */
public class ZabbixClientInitializer extends ChannelInitializer<SocketChannel> {
//    private static final ByteArrayDecoder DECODER = new ByteArrayDecoder();
    private static final ByteArrayEncoder ENCODER = new ByteArrayEncoder();
    private static final ByteClientHandler CLIENTHANDLER = new ByteClientHandler();

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


//        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
//        pipeline.addLast("bytesDecoder", DECODER);

        // Encoder
//        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("bytesEncoder", ENCODER);

//        pipeline.addLast("handler", net ByteClientHandler(mBytes));
        pipeline.addLast("handler", CLIENTHANDLER);

    }
}