package models;

public class TaskEvent {
    private final long id;
    private String name;
    private String description;
    public String status;
    public String createdAt;

    // Constructor
    public TaskEvent(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
