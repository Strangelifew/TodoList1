package model;

import java.util.Optional;
import java.util.UUID;

public class Todo {

    private String title;
    private UUID todoId;
    private Status status;
    private UUID userId;

    public Todo() {
    }

    public Todo(String title, UUID todoId, Status status) {
        this.title = title;
        this.todoId = todoId;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getTodoId() {
        return todoId;
    }

    public String getId() {
        return todoId.toString();
    }

    public void setTodoId(UUID todoId) {
        this.todoId = todoId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssignee() {
        return getAssignedUser().map(User::getName).orElse("");
    }

    public Optional<User> getAssignedUser() {
        return Optional.ofNullable(userId).flatMap(UserDao::find);
    }

    public static Todo create(String title) {
        return new Todo(title, UUID.randomUUID(), Status.ACTIVE);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "title='" + title + "'" +
                ", todoId=" + todoId +
                '}';
    }

}
