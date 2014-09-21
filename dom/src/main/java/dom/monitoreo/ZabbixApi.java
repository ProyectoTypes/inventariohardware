package dom.monitoreo;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dom.monitoreo.exceptions.ZabbixApiException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 14.04.2014
 *
 * @author Alexander Fedulov
 */

public class ZabbixApi {

    private static final Logger log = LoggerFactory.getLogger(ZabbixApi.class);
    private static final String API_URL = "/zabbix/api_jsonrpc.php";

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    static final GsonFactory GSON_FACTORY = new GsonFactory();

    private String authToken;
    private HttpRequestFactory requestFactory;
    private String host;
    private ZabbixUrl zabbixUrl;

    public ZabbixApi(){
        requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
    }

    public void login(String host, String user, String pass) {
        this.host = host;
        zabbixUrl = new ZabbixUrl();

        JsonObject authRequestJSON = new JsonObject();
        authRequestJSON.addProperty("method", "user.authenticate");

        JsonObject paramsJSON = new JsonObject();
        paramsJSON.addProperty("user", user);
        paramsJSON.addProperty("password", pass);

        authRequestJSON.add("params", paramsJSON);
        authRequestJSON.addProperty("auth", "");
        authRequestJSON.addProperty("id", 0);

        HttpResponse httpResp = sendAndGetResponse(authRequestJSON);
        ResponseSimple resp = parseResponseSimple(httpResp);
        authToken = resp.getResult();

        log.debug("Acquired AuthToken: " + authToken);
    }

    private HttpResponse sendAndGetResponse(JsonObject json) {
        json.addProperty("jsonrpc", "2.0");
        HttpContent content = ByteArrayContent.fromString("application/json", json.toString());
        HttpResponse resp = null;

        try {
            HttpRequest request = requestFactory.buildPostRequest(zabbixUrl, content);
            resp = request.execute();
        } catch (IOException e) {
            throw new ZabbixApiException("Error executing request", e);
        }

        return resp;
    }

    public List<Host> getAllHosts() throws IOException {
        List<Host> hosts = new ArrayList<Host>(50);
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("method", "host.get");
        jsonRequest.addProperty("auth", authToken);

        JsonObject paramsJSON = new JsonObject();
        paramsJSON.addProperty("output", "extend");

        jsonRequest.add("params", paramsJSON);
        jsonRequest.addProperty("id", 1);

        System.out.println(jsonRequest);

        HttpResponse httpResp = sendAndGetResponse(jsonRequest);
        Response resp = parseResponse(httpResp);
        log.error(resp+"");

        log.info("Got hosts with ids:");
        for (Result result : resp.getResult()) {
            Host host = new Host(result);
            hosts.add(host);
        }
        return hosts;
    }


    public List<HostGroup> getHostGroupByName(String name){
        List<HostGroup> hostGroups = new ArrayList<HostGroup>(50);
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("method", "hostgroup.get");
        jsonRequest.addProperty("auth", authToken);

        /* Params */
        JsonObject paramsJSON = new JsonObject();
        paramsJSON.addProperty("output", "extend");

        /* Params -> Filter*/
        JsonObject hostGroupFilter = new JsonObject();
        hostGroupFilter.addProperty("name", name);
        paramsJSON.add("filter", hostGroupFilter);

        jsonRequest.add("params", paramsJSON);
        jsonRequest.addProperty("id", 1);

        HttpResponse httpResp = sendAndGetResponse(jsonRequest);
//        String resp = httpResp.parseAsString();
        Response resp = parseResponse(httpResp);

        log.info("Got hosts with ids:");
        for (Result result : resp.getResult()) {
            HostGroup hostGroup = new HostGroup(result);
            hostGroups.add(hostGroup);
            log.error("hostgroup: {}", hostGroup);

        }
        return hostGroups;
    }

    public HostWithBuilder getHostByName(String name) throws IOException {
//        Host hosts = new Host();
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("method", "host.get");
        jsonRequest.addProperty("auth", authToken);

        JsonObject paramsJSON = new JsonObject();
        paramsJSON.addProperty("output","extend");

        JsonObject hostFilter = new JsonObject();
        hostFilter.addProperty("host",name);

        paramsJSON.add("filter", hostFilter);

        jsonRequest.add("params", paramsJSON);
        jsonRequest.addProperty("id", 1);

        HttpResponse httpResp = sendAndGetResponse(jsonRequest);
//        String resp = httpResp.parseAsString();
        Response resp = parseResponse(httpResp);
        log.error(resp+"");

        return null;
    }


    //TODO: add name to params. Check if exists.
    public int createHost(List<Integer> groupIds) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("method", "host.create");
        jsonRequest.addProperty("auth", authToken);

        /* Params */
        JsonObject params = new JsonObject();
        params.addProperty("host", "StatsCollector");


        //TODO: Warning: this section is different in the 1.8 API.
        /* Params -> Interfaces*/
        Interfaze inter = new Interfaze();
        inter.setType(1);
        inter.setMain(1);
        inter.setIp("1.1.1.1");
        inter.setPort("1111");
        inter.setDns("dns");
        inter.setUseip(1);

        List<Interfaze> interfacesList = new ArrayList<Interfaze>();
        interfacesList.add(inter);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement interElem = gson.toJsonTree(interfacesList);
        params.add("interfaces", interElem);
        Map<String, Integer> groupIdsMap = unfoldListToMap(groupIds, "groupid");
        List<Map<String, Integer>> groupIdsMapList = new ArrayList<Map<String, Integer>>();
        groupIdsMapList.add(groupIdsMap);

        JsonElement groupsJson = gson.toJsonTree(groupIdsMapList);
        params.add("groups", groupsJson);

        jsonRequest.add("params", params);
        jsonRequest.addProperty("id", 1);

        String json = gson.toJson(jsonRequest);
        System.out.println(json);

        HttpResponse httpResp = sendAndGetResponse(jsonRequest);
        String resp = httpResp.parseAsString();
        log.error(resp);


       /* Response resp = parseResponse(httpResp);
        log.error(resp+"");

        log.info("Got hosts with ids:");
        for (Result result : resp.getResult()) {
            log.error("result {}", result);
        }*/

        //TODO: return id of created item
        return -1;
    }



    //TODO: add name to params. Check if exists.

    /**
     * Items types:
     * 0 - Zabbix agent
     * 2 - trap
     * 3 - Simple check
     * 7 - Zabbix agent (active)
     *
     * @param name
     * @return
     * @throws IOException
     */

    public int createItem(String hostId, String interfaceId, String name, String key, int type) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("method", "item.create");
        jsonRequest.addProperty("auth", authToken);

        JsonObject paramsJSON = new JsonObject();
        paramsJSON.addProperty("name", name);
        paramsJSON.addProperty("key_", key);
        paramsJSON.addProperty("hostid", hostId);
        paramsJSON.addProperty("type", type);
        paramsJSON.addProperty("value_type", 3);
        paramsJSON.addProperty("interfaceid", interfaceId);
        paramsJSON.addProperty("delay", 30);

        jsonRequest.add("params", paramsJSON);
        jsonRequest.addProperty("id", 1);

        HttpResponse httpResp = sendAndGetResponse(jsonRequest);
        String resp = httpResp.parseAsString();
        log.error(resp+"");

        //TODO: return an id of created item
        return -1;
    }


    /** Zabbix API request. */
    public static class Request extends GenericJson {

        /** Request id. */
        @Key
        private String id;

        public String getId() {
            return id;
        }

        /** Activity object. */
        @Key("params")
        private ParamsObject paramsObject;

        public ParamsObject getParamsObject() {
            return paramsObject;
        }
    }

    /** Zabbix API request. */
    public static class Response extends ResponseGeneric {
        /** Result object. */
        @Key("result")
        private List<Result> result;
        public List<Result> getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "id=" + id +
                    ", result=" + result +
                    '}';
        }
    }

    /** Zabbix API request. */
    public static class ResponseSimple extends ResponseGeneric {
        /** Result object. */
        @Key("result")
        private String result;
        public String getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "ResponseSimple{" +
                    "id=" + id +
                    ", result=" + result +
                    '}';
        }
    }

    /** Zabbix API request. */
    public static class ResponseGeneric extends GenericJson {

        /** Response id. */
        @Key
        protected Integer id;

        protected Integer getId() {
            return id;
        }
    }

    /** Zabbix API Request params object. */
    public static class ParamsObject {

        /** HTML-formatted content. */
        @Key
        private String content;

        public String getContent() {
            return content;
        }

        /** People who +1'd this activity. */
        @Key
        private JsonObject plusoners;

        public JsonObject getPlusOners() {
            return plusoners;
        }
    }


    /** Zabbix API Result part of the Response. */
    public static class Result extends GenericJson {
        @Override
        public String toString() {
            try {
                return super.toPrettyString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return super.toString();
        }
    }

    /** Zabbix URL. */
    public class ZabbixUrl extends GenericUrl {
        public ZabbixUrl() {
            super("http://" + host +"/" + API_URL);
        }
    }

    private static Response parseResponse(HttpResponse response){
        Response resp;
        try{
            resp = response.parseAs(Response.class);
        } catch (IOException e) {
            throw new ZabbixApiException("Error parsing response", e);
        }
        return resp;
    }


    private static ResponseSimple parseResponseSimple(HttpResponse response) {
        ResponseSimple resp;
        try{
            resp = response.parseAs(ResponseSimple.class);
            log.warn("Response: {}", resp);
        } catch (IOException e) {
            throw new ZabbixApiException("Error parsing simple response", e);
        }
        return resp;
    }

    private <K, T> Map<K, T> unfoldListToMap(List<T> list, K key){
        Map<K, T> map = new HashMap<K, T>();
        for (T value : list) {
            map.put(key, value);
        }
        return map;
    }


    public static void main(String[] args) {
       /* System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");*/

        try {
            try {
                ZabbixApi zabi = new ZabbixApi();

                Configuration config = new PropertiesConfiguration("credentials.properties");

                String ip = config.getString("ip");
                String user = config.getString("user");
                String password = config.getString("password");
                zabi.login(ip, user, password);
                log.info("Logging in as {}@{}", user, ip);

                /*List<Host> hosts = zabi.getAllHosts();
                for (Host host : hosts) {
                    log.info("hostid: {} , name: {}", host.getHostId(), host.getName());
                }*/

                /* Create host */
               /* List<Integer> groups = new ArrayList<Integer>();
                groups.add(10);
                zabi.createHost(groups);
                */


              /*  List<HostGroup> hostGroups =  zabi.getHostGroupByName("Discovered hosts");
                for (HostGroup hostGroup : hostGroups) {
                    log.info("groupid: {} , name: {}", hostGroup.getGroupId(), hostGroup.getName());
                }*/


                zabi.getHostByName("StatsCollector");

//                zabi.createItem("Calls by partner 96");

             /*   zabi.createItem("10142", "67", "Calls by partner 81", "calls.81", 2);
                zabi.createItem("10142", "67", "Calls by partner 85", "calls.85", 2);
                zabi.createItem("10142", "67", "Calls by partner 87", "calls.87", 2);
                zabi.createItem("10142", "67", "Calls by partner 88", "calls.88", 2);
                zabi.createItem("10142", "67", "Calls by partner 91", "calls.91", 2);
                zabi.createItem("10142", "67", "Calls by partner 92", "calls.92", 2);
                zabi.createItem("10142", "67", "Calls by partner 93", "calls.93", 2);
                zabi.createItem("10142", "67", "Calls by partner 94", "calls.94", 2);*/

/*                zabi.createItem("10142", "67", "Active agents of partner 80", "agents.80", 2);
                zabi.createItem("10142", "67", "Active agents of partner 81", "agents.81", 2);
                zabi.createItem("10142", "67", "Active agents of partner 85", "agents.85", 2);
                zabi.createItem("10142", "67", "Active agents of partner 87", "agents.87", 2);
                zabi.createItem("10142", "67", "Active agents of partner 88", "agents.88", 2);
                zabi.createItem("10142", "67", "Active agents of partner 91", "agents.91", 2);
                zabi.createItem("10142", "67", "Active agents of partner 92", "agents.92", 2);
                zabi.createItem("10142", "67", "Active agents of partner 93", "agents.93", 2);
                zabi.createItem("10142", "67", "Active agents of partner 94", "agents.94", 2);
                zabi.createItem("10142", "67", "Active agents of partner 96", "agents.96", 2); */

                return;
            } catch (ZabbixApiException e) {
                System.err.println(e.getMessage());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }


    //Currently not used
    public static class HostWithBuilder {
        
        private final String name;
        private boolean cheese;

        public static class Builder {
            //required
            private final String name;
            //optional
            private boolean cheese = false;

            public Builder(String name) {
                this.name = name;
            }

            public Builder cheese(boolean value) {
                cheese = value;
                return this;
            }

            public HostWithBuilder build() {
                return new HostWithBuilder(this);
            }
        }

        private HostWithBuilder(Builder builder) {
            name = builder.name;
            cheese = builder.cheese;
        }
    }

    //Currently not used
    public static class Host {
        GenericJson backingMap;

        public Host(GenericJson json){
            this.backingMap = json;
        }

        public String getName(){
            return backingMap.get("name").toString();
        }

        public String getHostId(){
            return backingMap.get("hostid").toString();
        }
    }

    public static class HostGroup {
        GenericJson backingMap;

        public HostGroup(GenericJson json){
            this.backingMap = json;
        }

        public String getName(){
            return backingMap.get("name").toString();
        }

        public String getGroupId(){
            return backingMap.get("groupid").toString();
        }
    }


    //Currently not used
    public static class Interfaze {
        private int type;
        private int main;
        private String ip;
        private String port;
        private int useip;
        private String dns;

        public void setType(int type) {
            this.type = type;
        }

        public void setMain(int main) {
            this.main = main;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }


        public void setPort(String port) {
            this.port = port;
        }

        public void setUseip(int useip) {
            this.useip = useip;
        }

        public void setDns(String dns) {
            this.dns = dns;
        }
    }
}
