package controllers;

import play.libs.Json;

import models.Task;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import akka.actor.ActorSystem;
//import akka.stream.javadsl.Flow;
import play.mvc.Controller;
import play.mvc.WebSocket;

public class TaskController extends Controller {

    // In-memory task storage
    private static final List<Task> tasks = new ArrayList<>();
    private static long idCounter = 1;

    // Get all tasks
    public Result getAllTasks() {
        return ok(Json.toJson(tasks));
    }

    // Get task by ID
    public Result getTask(Long id) {
        Task task = tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        if (task == null) {
            return notFound("Task not found");
        }
        return ok(Json.toJson(task));
    }

    // Create a new task
    public Result createTask(Http.Request request) {
        JsonNode json = request.body().asJson();
        String name = json.get("name").asText();
        String description = json.get("description").asText();
        Task task = new Task(idCounter++, name, description);
        tasks.add(task);
        return created(Json.toJson(task));
    }

    // Update an existing task
    public Result updateTask(Http.Request request, Long id) {
        JsonNode json = request.body().asJson();
        String name = json.get("name").asText();
        String description = json.get("description").asText();
        Task task = tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        if (task == null) {
            return notFound("Task not found");
        }
        task.setName(name);
        task.setDescription(description);
        return ok(Json.toJson(task));
    }

    // Delete a task
    public Result deleteTask(Long id) {
        Task task = tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        if (task == null) {
            return notFound("Task not found");
        }
        tasks.remove(task);
        return noContent();
    }

//    // WebSocket handler
//    public WebSocket socket() {
//        return WebSocket.Text.accept(request -> {
//            // Create a WebSocket flow
//            return Flow.fromSinkAndSource(
//                    akka.stream.javadsl.Sink.ignore(),  // Sink to handle incoming messages
//                    akka.stream.javadsl.Source.tick(
//                            java.time.Duration.ZERO,
//                            java.time.Duration.ofSeconds(1),
//                            "Task Update: " + java.time.Instant.now())  // Source for outgoing messages
//            );
//        });
//    }
}
