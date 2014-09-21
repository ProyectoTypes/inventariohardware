package dom.monitoreo;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created on 14.04.2014
 */
public class HttpTestZabbix{

    private static final String API_KEY =
            "Enter API Key from https://code.google.com/apis/console/?api=plus into API_KEY";

    private static final String API_URL = "/zabbix/api_jsonrpc.php";

    private static final String USER_ID = "116899029375914044550";
    private static final int MAX_RESULTS = 3;

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();


    /** Feed of Google+ activities. */
    public static class ActivityFeed {

        /** List of Google+ activities. */
        @Key("items")
        private List<Activity> activities;

        public List<Activity> getActivities() {
            return activities;
        }
    }

    /** Google+ activity. */
    public static class Activity extends GenericJson {

        /** Activity URL. */
        @Key
        private String url;

        public String getUrl() {
            return url;
        }

        /** Activity object. */
        @Key("object")
        private ActivityObject activityObject;

        public ActivityObject getActivityObject() {
            return activityObject;
        }
    }

    /** Google+ activity object. */
    public static class ActivityObject {

        /** HTML-formatted content. */
        @Key
        private String content;

        public String getContent() {
            return content;
        }

        /** People who +1'd this activity. */
        @Key
        private PlusOners plusoners;

        public PlusOners getPlusOners() {
            return plusoners;
        }
    }

    /** People who +1'd an activity. */
    public static class PlusOners {

        /** Total number of people who +1'd this activity. */
        @Key
        private long totalItems;

        public long getTotalItems() {
            return totalItems;
        }
    }

    /** Google+ URL. */
    public static class ZabbixUrl extends GenericUrl {

        public ZabbixUrl(String encodedUrl) {
            super(encodedUrl);
        }

//        @Key
//        private final String key = API_KEY;

        /** Maximum number of results. */
//        @Key
//        private int maxResults;

//        public int getMaxResults() {
//            return maxResults;
//        }

        public ZabbixUrl setMaxResults(int maxResults) {
//            this.maxResults = maxResults;
            return this;
        }

        /** Lists the public activities for the given Google+ user ID. */
        public static ZabbixUrl listPublicActivities(String userId) {
            return new ZabbixUrl(
                    "http://172.16.18.3/" + API_URL);
//                    "https://www.googleapis.com/plus/v1/people/" + userId + "/activities/public");
        }
    }

    private static void parseResponse(HttpResponse response) throws IOException {
        String responseString = response.parseAsString();
        System.out.println(responseString);

        ActivityFeed feed = response.parseAs(ActivityFeed.class);
        if (feed.getActivities().isEmpty()) {
            System.out.println("No activities found.");
        } else {
            if (feed.getActivities().size() == MAX_RESULTS) {
                System.out.print("First ");
            }
            System.out.println(feed.getActivities().size() + " activities found:");
            for (Activity activity : feed.getActivities()) {
                System.out.println();
                System.out.println("-----------------------------------------------");
                System.out.println("HTML Content: " + activity.getActivityObject().getContent());
                System.out.println("+1's: " + activity.getActivityObject().getPlusOners().getTotalItems());
                System.out.println("URL: " + activity.getUrl());
                System.out.println("ID: " + activity.get("id"));
            }
        }
    }

    private static void run() throws IOException {
        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
        ZabbixUrl url = ZabbixUrl.listPublicActivities(USER_ID).setMaxResults(MAX_RESULTS);

        url.put("fields", "items(id,url,object(content,plusoners/totalItems))");

        JSONObject authRequestJSON = new JSONObject();
        authRequestJSON.put("jsonrpc", "2.0");
        authRequestJSON.put("method", "user.authenticate");

        JSONObject paramsJSON = new JSONObject();
        paramsJSON.put("user","afedulov");
        paramsJSON.put("password","JjoFO6BI");

        authRequestJSON.put("params", paramsJSON);
        authRequestJSON.put("auth", "");
        authRequestJSON.put("id", 0);

        HttpContent content = ByteArrayContent.fromString("application/json", authRequestJSON.toString());
        HttpRequest request = requestFactory.buildPostRequest(url, content);
        parseResponse(request.execute());


        //TODO: try GenericJson, if it does not work - try result with String impl
    }


    public static void main(String[] args) {
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");

        try {
            try {
                run();
                return;
            } catch (HttpResponseException e) {
                System.err.println(e.getMessage());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }
}