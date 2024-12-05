package services;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import models.Task;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import actors.NotificationActor;
//import akka.stream.javadsl.Source;

public class TaskService {
    private final MongoCollection<Document> collection;
    private final ActorRef notificationActor;

    public TaskService() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        collection = mongoClient.getDatabase("taskdb").getCollection("tasks");

        ActorSystem system = ActorSystem.create("NotificationSystem");
        notificationActor = system.actorOf(Props.create(NotificationActor.class));
    }

    public CompletableFuture<Void> createTask(Task task) {
        return CompletableFuture.runAsync(() -> {
            Document doc = new Document("title", task.getName())
                    .append("description", task.getDescription())
                    .append("status", task.status)
                    .append("createdAt", new Date());
            collection.insertOne(doc);
        });
    }

    public CompletableFuture<Task> getTask(long id) {
        return CompletableFuture.supplyAsync(() -> {
            Document doc = collection.find(new Document("_id", id)).first();
            if (doc == null) {
                return null;
            }
            return new Task(doc.getLong("_id"), doc.getString("title"), doc.getString("description"));
        });
    }

    public CompletableFuture<Void> updateTask(Task task) {
        return CompletableFuture.runAsync(() -> {
            Document doc = new Document("title", task.getName())
                    .append("description", task.getDescription())
                    .append("status", task.status);
            collection.updateOne(new Document("_id", task.getId()), new Document("$set", doc));
        });
    }

    public CompletableFuture<Void> deleteTask(long id) {
        return CompletableFuture.runAsync(() -> {
            collection.deleteOne(new Document("_id", id));
        });
    }

//    public CompletableFuture<Void> notify(String message) {
//        return CompletableFuture.runAsync(() -> {
//            notificationActor.tell(message, ActorRef.noSender());
//        });
//    }
//
//    public Source<String, ?> getTaskUpdates() {
//        return Source.tick(
//                Duration.ofSeconds(1),
//                Duration.ofSeconds(5),
//                "Task update at " + Instant.now()
//        );
//    }
}