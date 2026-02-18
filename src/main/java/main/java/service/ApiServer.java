package main.java.service;

import io.javalin.Javalin;
import main.java.business.*;
import main.java.dao.*;
import main.java.model.*;

import java.util.Map;

import static main.java.service.JsonUtil.gson;

/**
 * CSCE 548 Project 2 - Service Layer (Microservice)
 *
 * Local run:
 *   Run Main.java (calls ApiServer.start()) then hit:
 *     GET http://localhost:7070/health
 *
 * Hosting notes (Render):
 *   - Create a new Web Service
 *   - Use Docker (recommended) or Java build if your repo supports it
 *   - Expose PORT env var (Render sets it); this server reads PORT if present
 */
public class ApiServer {
    

    public static Javalin start() {
        // Wire DAOs -> Managers (Business Layer)
        UserManager userManager = new UserManagerImpl(new UserDAO());
        HabitManager habitManager = new HabitManagerImpl(new HabitDAO());
        DailyLogManager dailyLogManager = new DailyLogManagerImpl(new DailyLogDAO());
        HabitEntryManager habitEntryManager = new HabitEntryManagerImpl(new HabitEntryDAO());

        int port = 7070;
        String envPort = System.getenv("PORT");
        if (envPort != null) {
            try { port = Integer.parseInt(envPort); } catch (Exception ignored) {}
        }

        Javalin app = Javalin.create(config -> config.http.defaultContentType = "application/json");

        // Global error handling -> JSON
        app.exception(IllegalArgumentException.class, (e, ctx) ->
                ctx.status(400).result(gson.toJson(new ErrorResponse(e.getMessage())))
        );

        app.exception(RuntimeException.class, (e, ctx) -> {
            String msg = (e.getMessage() == null) ? "Server error" : e.getMessage();
            int status = msg.toLowerCase().contains("not found") ? 404 : 500;
            ctx.status(status).result(gson.toJson(new ErrorResponse(msg)));
        });

        // If a DAO throws SQLException, Javalin will treat it as Exception; return 500 with message
        app.exception(Exception.class, (e, ctx) ->
                ctx.status(500).result(gson.toJson(new ErrorResponse(e.getMessage())))
        );

        app.get("/health", ctx -> ctx.result("{\"status\":\"ok\"}"));

        // ------------------------------------------------------------
        // USERS
        // DAO/Manager create requires email + fullName
        // ------------------------------------------------------------

        // POST /api/users  body: {"email":"a@b.com","fullName":"Alice"}
        app.post("/api/users", ctx -> {
            Map<?, ?> body = gson.fromJson(ctx.body(), Map.class);
            String email = body.get("email") == null ? null : body.get("email").toString();
            String fullName = body.get("fullName") == null ? null : body.get("fullName").toString();

            AppUser created = userManager.create(email, fullName);
            ctx.status(201).result(gson.toJson(created));
        });

        // GET /api/users?limit=50
        app.get("/api/users", ctx -> {
            int limit = 50;
            String limitStr = ctx.queryParam("limit");
            if (limitStr != null) limit = Integer.parseInt(limitStr);
            ctx.result(gson.toJson(userManager.getAll(limit)));
        });

        // GET /api/users/{id}
        app.get("/api/users/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.result(gson.toJson(userManager.getById(id)));
        });

        // PUT /api/users/{id}/name  body: {"fullName":"New Name"}
        app.put("/api/users/{id}/name", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<?, ?> body = gson.fromJson(ctx.body(), Map.class);
            String fullName = body.get("fullName") == null ? null : body.get("fullName").toString();
            userManager.updateName(id, fullName);
            ctx.status(204);
        });

        // DELETE /api/users/{id}
        app.delete("/api/users/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            userManager.delete(id);
            ctx.status(204);
        });

        // ------------------------------------------------------------
        // HABITS
        // HabitManager: create(Habit)->Habit, getByUser(userId), updateTarget, delete
        // ------------------------------------------------------------

        // POST /api/habits (body is a full Habit json)
        app.post("/api/habits", ctx -> {
            Habit habit = gson.fromJson(ctx.body(), Habit.class);
            Habit created = habitManager.create(habit);
            ctx.status(201).result(gson.toJson(created));
        });

        // GET /api/users/{userId}/habits
        app.get("/api/users/{userId}/habits", ctx -> {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            ctx.result(gson.toJson(habitManager.getByUser(userId)));
        });

        // GET /api/habits/{id}
        app.get("/api/habits/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.result(gson.toJson(habitManager.getById(id)));
        });

        // PUT /api/habits/{id}/target  body: {"targetValue":8.0,"active":true}
        app.put("/api/habits/{id}/target", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<?, ?> body = gson.fromJson(ctx.body(), Map.class);

            Double targetValue = null;
            if (body.get("targetValue") != null && !body.get("targetValue").toString().isBlank()) {
                targetValue = Double.valueOf(body.get("targetValue").toString());
            }

            boolean active = true;
            if (body.get("active") != null) {
                active = Boolean.parseBoolean(body.get("active").toString());
            }

            habitManager.updateTarget(id, targetValue, active);
            ctx.status(204);
        });

        // DELETE /api/habits/{id}
        app.delete("/api/habits/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            habitManager.delete(id);
            ctx.status(204);
        });

        // ------------------------------------------------------------
        // DAILY LOGS
        // DailyLogManager: create(DailyLog)->DailyLog, getByUser(userId), updateRatings, delete
        // ------------------------------------------------------------

        // POST /api/dailyLogs  (body is a full DailyLog json)
        app.post("/api/dailyLogs", ctx -> {
            DailyLog log = gson.fromJson(ctx.body(), DailyLog.class);
            DailyLog created = dailyLogManager.create(log);
            ctx.status(201).result(gson.toJson(created));
        });

        // GET /api/users/{userId}/dailyLogs
        app.get("/api/users/{userId}/dailyLogs", ctx -> {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            ctx.result(gson.toJson(dailyLogManager.getByUser(userId)));
        });

        // GET /api/dailyLogs/{id}
        app.get("/api/dailyLogs/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.result(gson.toJson(dailyLogManager.getById(id)));
        });

        // PUT /api/dailyLogs/{id}/ratings
        // body: {"sleepHours":7.5,"mood":4,"stress":2,"energy":4,"notes":"ok"}
        app.put("/api/dailyLogs/{id}/ratings", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<?, ?> body = gson.fromJson(ctx.body(), Map.class);

            Double sleepHours = body.get("sleepHours") == null ? null : Double.valueOf(body.get("sleepHours").toString());
            Integer mood = body.get("mood") == null ? null : Integer.valueOf(body.get("mood").toString());
            Integer stress = body.get("stress") == null ? null : Integer.valueOf(body.get("stress").toString());
            Integer energy = body.get("energy") == null ? null : Integer.valueOf(body.get("energy").toString());
            String notes = body.get("notes") == null ? null : body.get("notes").toString();

            dailyLogManager.updateRatings(id, sleepHours, mood, stress, energy, notes);
            ctx.status(204);
        });

        // DELETE /api/dailyLogs/{id}
        app.delete("/api/dailyLogs/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dailyLogManager.delete(id);
            ctx.status(204);
        });

        // ------------------------------------------------------------
        // HABIT ENTRIES
        // HabitEntryManager: create(HabitEntry)->HabitEntry, getByDailyLog, updateEntry, delete, pretty list
        // ------------------------------------------------------------

        // POST /api/habitEntries (body full HabitEntry json)
        app.post("/api/habitEntries", ctx -> {
            HabitEntry entry = gson.fromJson(ctx.body(), HabitEntry.class);
            HabitEntry created = habitEntryManager.create(entry);
            ctx.status(201).result(gson.toJson(created));
        });

        // GET /api/dailyLogs/{dailyLogId}/habitEntries
        app.get("/api/dailyLogs/{dailyLogId}/habitEntries", ctx -> {
            int dailyLogId = Integer.parseInt(ctx.pathParam("dailyLogId"));
            ctx.result(gson.toJson(habitEntryManager.getByDailyLog(dailyLogId)));
        });

        // GET /api/habitEntries/{id}
        app.get("/api/habitEntries/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.result(gson.toJson(habitEntryManager.getById(id)));
        });

        // PUT /api/habitEntries/{id}
        // body: {"completed":true,"actualValue":3.5,"note":"nice"}
        app.put("/api/habitEntries/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<?, ?> body = gson.fromJson(ctx.body(), Map.class);

            boolean completed = body.get("completed") != null && Boolean.parseBoolean(body.get("completed").toString());

            Double actualValue = null;
            if (body.get("actualValue") != null && !body.get("actualValue").toString().isBlank()) {
                actualValue = Double.valueOf(body.get("actualValue").toString());
            }

            String note = body.get("note") == null ? null : body.get("note").toString();

            habitEntryManager.updateEntry(id, completed, actualValue, note);
            ctx.status(204);
        });

        // DELETE /api/habitEntries/{id}
        app.delete("/api/habitEntries/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            habitEntryManager.delete(id);
            ctx.status(204);
        });

        // GET /api/dailyLogs/{dailyLogId}/habitEntries/pretty
        app.get("/api/dailyLogs/{dailyLogId}/habitEntries/pretty", ctx -> {
            int dailyLogId = Integer.parseInt(ctx.pathParam("dailyLogId"));
            ctx.result(gson.toJson(habitEntryManager.getEntriesPrettyByDailyLog(dailyLogId)));
        });

        app.start(port);
        return app;
    }

    public static void main(String[] args) {
    start();
}

}
