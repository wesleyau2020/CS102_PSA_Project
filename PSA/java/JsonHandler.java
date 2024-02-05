import org.json.*;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonHandler {
    /**
     * Generates an ArrayList of BerthingDetails from a json input
     * 
     * @param json A JSON string result from the retrieveByBerthingDate API
     * @return An ArrayList of BerthingDetails
     */
    public static ArrayList<BerthingDetails> getBerthingDetailsFromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        ArrayList<BerthingDetails> berthingDetailsList = new ArrayList<>();

        if (jsonObject.isNull("errors")) {

            System.out.println("No errors");

            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject details = results.getJSONObject(i);
                String lName = details.getString("fullVslM");
                String sName = details.getString("abbrVslM");
                String inVoyN = details.getString("inVoyN");
                String outVoyN = details.getString("outVoyN");
                String btrDate = details.getString("bthgDt");
                String berthN = details.isNull("berthN") ? "" : details.getString("berthN");
                String status = details.getString("status");
                berthingDetailsList.add(new BerthingDetails(lName, sName, inVoyN, outVoyN, btrDate, berthN, status));
            }
        } else {
            System.out.println("Errors");
            System.out.println(jsonObject.getJSONObject("errors").toString());
        }

        return berthingDetailsList;
    }

    /**
     * Generates an PredictedBtr object from a json input
     * 
     * @param json A JSON string result from the predicted_btr API
     * @return A PredictedBtr object
     */
    public static PredictedBtr getPredictedBtrFromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);

        if (!jsonObject.has("Error")) {

            System.out.println("No errors");

            JSONObject details = jsonObject;
            String lName = details.getString("VESSEL_NAME");
            String sName = details.getString("VESSEL_NAME");
            String inVoyN = details.getString("VOYAGE_CODE_INBOUND");
            String outVoyN = details.getString("VOYAGE_CODE_OUTBOUND");
            double averageSpeed = details.getDouble("AVG_SPEED");
            double distance = details.getDouble("DISTANCE_TO_GO");
            boolean isPatching = details.getInt("IS_PATCHING_ACTIVATED") == 1 ? true : false;
            double maxSpeed = details.getDouble("MAX_SPEED");
            String predictedBtr = details.getString("PREDICTED_BTR");
            String patchingPredictedBtr = details.getString("PATCHING_PREDICTED_BTR");

            return new PredictedBtr(lName, sName, inVoyN, outVoyN, averageSpeed, distance, isPatching, maxSpeed,
                    patchingPredictedBtr, predictedBtr);

        } else {
            System.out.println("Errors");
            System.out.println(jsonObject.getString("Error"));
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("API1_output.json");
        String jsonString = Files.readString(fileName);
        System.out.println(getBerthingDetailsFromJson(jsonString).get(0));

        Path fileName2 = Path.of("API2_output.json");
        String jsonString2 = Files.readString(fileName);
        System.out.println(getPredictedBtrFromJson(jsonString2));
    }
}
