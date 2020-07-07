package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TodoDao {
    private TodoDao() {
    }

    private static final List<Todo> DATA = new ArrayList<>();

    public static void add(Todo todo) {
        DATA.add(todo);
    }

    public static Optional<Todo> find(String id) {
        return DATA.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public static void update(String id, String title) {
        find(id).ifPresent(todo -> todo.setTitle(title));
    }

    public static List<Todo> ofStatus(String statusString) {
        return (statusString == null || statusString.isEmpty()) ? DATA : ofStatus(Status.valueOf(statusString.toUpperCase()));
    }

    public static List<Todo> ofStatus(Status status) {
        return DATA.stream().filter(t -> t.getStatus().equals(status)).collect(Collectors.toList());
    }

    public static void remove(String id) {
        find(id).ifPresent(DATA::remove);
    }

    public static void removeCompleted() {
        ofStatus(Status.COMPLETE).forEach(t -> remove(t.getId()));
    }

    public static void toggleStatus(String id) {
        find(id).ifPresent(Todo::toggleStatus);
    }

    public static void toggleAll(boolean complete) {
        TodoDao.all().forEach(t -> t.setStatus(complete ? Status.COMPLETE : Status.ACTIVE));
    }

    public static List<Todo> all() {
        return DATA;
    }

    public static List<Todo> getAssigned(User user) {
        return DATA.stream()
                .filter(t -> t.getAssignedUser().map(u -> u.getId().equals(user.getId())).orElse(false))
                .collect(Collectors.toList());
    }
}