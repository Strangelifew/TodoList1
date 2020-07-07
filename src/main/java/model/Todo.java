package model;

import java.util.Optional;
import java.util.UUID;

public class Todo {

    private String title;
    private String id;
    private Status status;
    private User assignedUser;

    public Todo(String title, String id, Status status) {
        this.title = title;
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void toggleStatus() {
        this.status = isComplete() ? Status.ACTIVE : Status.COMPLETE;
    }

    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

    public String getAssignee() {
        return getAssignedUser().map(User::getName).orElse("");
    }

    public Optional<User> getAssignedUser() {
        return Optional.ofNullable(assignedUser);
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public static Todo create(String title) {
        return new Todo(title, UUID.randomUUID().toString(), Status.ACTIVE);
    }
}
