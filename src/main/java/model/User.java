package model;

import java.util.UUID;

public class User {

    private String name;
    private UUID userId;

    public User() {
    }

    public User(String name, UUID userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getId() {
        return userId.toString();
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public static User create(String name) {
        return new User(name, UUID.randomUUID());
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + "'" +
                ", userId=" + userId +
                '}';
    }
}
