package main.java.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.model.Habit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConsoleClient {

    private static final String BASE = "http://localhost:7070";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws Exception {

        System.out.println("=== Console Client CRUD Test (Habits) ===");

        // 1) CREATE habit
        Habit newHabit = new Habit(
                0,          // habit_id (ignored on insert)
                1,          // user_id (make sure user 1 exists in your DB seed)
                "Water",    // name
                "cups",     // unit  ✅ REQUIRED
                8.0,        // target_value
                true        // is_active
        );

        HttpResult createRes = post("/api/habits", gson.toJson(newHabit));
        System.out.println("POST /api/habits -> " + createRes.status);
        System.out.println(createRes.body);

        if (createRes.status != 201) {
            System.out.println("\nCreate failed (expected 201). Fix request/DB then re-run.");
            return;
        }

        Habit created = gson.fromJson(createRes.body, Habit.class);
        int habitId = created.getHabitId(); // assumes your Habit model has getHabitId()
        System.out.println("Created habitId = " + habitId);

        // 2) GET habit
        HttpResult getRes1 = get("/api/habits/" + habitId);
        System.out.println("\nGET /api/habits/" + habitId + " -> " + getRes1.status);
        System.out.println(getRes1.body);

        // 3) UPDATE target + active
        String updateJson = """
                {
                  "targetValue": 10.0,
                  "active": true
                }
                """;

        HttpResult putRes = put("/api/habits/" + habitId + "/target", updateJson);
        System.out.println("\nPUT /api/habits/" + habitId + "/target -> " + putRes.status);
        System.out.println(putRes.body.isBlank() ? "(no body)" : putRes.body);

        // 4) GET again
        HttpResult getRes2 = get("/api/habits/" + habitId);
        System.out.println("\nGET /api/habits/" + habitId + " (after update) -> " + getRes2.status);
        System.out.println(getRes2.body);

        // 5) DELETE
        HttpResult delRes = delete("/api/habits/" + habitId);
        System.out.println("\nDELETE /api/habits/" + habitId + " -> " + delRes.status);
        System.out.println(delRes.body.isBlank() ? "(no body)" : delRes.body);

        // 6) GET after delete (should be 404)
        HttpResult getRes3 = get("/api/habits/" + habitId);
        System.out.println("\nGET /api/habits/" + habitId + " (after delete) -> " + getRes3.status);
        System.out.println(getRes3.body);

        System.out.println("\n=== DONE ===");
    }

    // ---------------- HTTP helpers ----------------

    private static HttpResult get(String path) throws Exception {
        return request("GET", path, null);
    }

    private static HttpResult post(String path, String json) throws Exception {
        return request("POST", path, json);
    }

    private static HttpResult put(String path, String json) throws Exception {
        return request("PUT", path, json);
    }

    private static HttpResult delete(String path) throws Exception {
        return request("DELETE", path, null);
    }

    private static HttpResult request(String method, String path, String jsonBody) throws Exception {
        URL url = new URL(BASE + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        if (jsonBody != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes());
            }
        }

        int status = conn.getResponseCode();

        BufferedReader br;
        if (status >= 200 && status < 400) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line).append("\n");

        conn.disconnect();
        return new HttpResult(status, sb.toString().trim());
    }

    private static class HttpResult {
        int status;
        String body;
        HttpResult(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }
}
