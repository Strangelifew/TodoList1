package model;

import java.util.*;

public class Todo {

    private String title;
    private String id;
    private Status status;
    private List<User> users = new ArrayList<>();;

    public Todo(String title, String id, Status status, List<User> users) {
        this.title = title;
        this.id = id;
        this.status = status;
        this.users = users;
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

    public List<User>  getUsers() {
        return users;
    }

    public void setUsers(List<User> users){
        this.users=users;
    }

    public static Todo create(String title) {
        return new Todo(title, UUID.randomUUID().toString(), Status.ACTIVE, null);
    }

}
