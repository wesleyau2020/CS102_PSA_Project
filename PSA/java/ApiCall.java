import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class ApiCall {

    /**
     * Generates a HttpRequest object for HTTP POST
     * 
     * @param uri      the uri string of the API
     * @param apiKey   the API Key string
     * @param dateFrom A string of the start date of query (inclusive) yyyy-MM-dd
     * @param dateTo   A string of end date of query (inclusive) yyyy-MM-dd
     * @return The HttpRequest object for HTTP POST
     */
    public static HttpRequest createHttpPostRequest(String uri, String apiKey, String dateFrom, String dateTo) {
        JSONObject query = new JSONObject();
        query.put("dateFrom", dateFrom);
        query.put("dateTo", dateTo);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("accept", "application/json")
                .header("Apikey", apiKey)
                .header("content-type", "application/json")
                .POST(BodyPublishers.ofString(query.toString()))
                .build();

        return request;
    }

    /**
     * Generates a HttpRequest object for HTTP GET
     * 
     * @param uri    The uri string of the API
     * @param apiKey The API Key string
     * @param vslvoy The string of vessel and voyage number to be queried <fullVsIM
     *               in firstAPI without space><inVoyN in First API>
     * @return The HttpRequest object for HTTP GET
     */
    public static HttpRequest createHttpGetRequest(String uri, String apiKey, String vslvoy) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + vslvoy))
                .header("accept", "application/json")
                .header("Apikey", apiKey)
                .GET()
                .build();

        return request;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String retrieveByBerthingDateUri = "https://api.portnet.com/vsspp/pp/bizfn/berthingSchedule/retrieveByBerthingDate/v1.2";
        // String predictedBtrUri =
        // "https://api.portnet.com/extapi/vessels/predictedbtr/?vslvoy=";

        String apiKey = "8f765e3bf8534243bceeb5341a78f5f2";

        String dateFrom = "2021-02-04";
        String dateTo = "2021-02-04";
        HttpRequest request = createHttpPostRequest(retrieveByBerthingDateUri, apiKey, dateFrom, dateTo);

        // String vslvoy = "ALZUBARA014W";
        // HttpRequest request2 = createHttpGetRequest(predictedBtrUri, apiKey, vslvoy);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println(response.statusCode());

        // JSONObject output = new JSONObject(response.body());
    }
}