package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class UserDao {

    private static int maxTodos = 10;


    private UserDao() {
    }


    public static int getMaxTodos() {
        return maxTodos;
    }

    public static void setMaxTodos(int maxTodos) {
        UserDao.maxTodos = maxTodos;
    }

    private static final List<User> DATA = new ArrayList<>();

    public static boolean add(User user) {
        for (User u : DATA) if (u.getName().equals(user.getName())) return false;
        DATA.add(user);
        return true;
    }

    public static User find(String id) {
        return DATA.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public static Optional<User> findByName(String name) {
        return DATA.stream().filter(u -> u.getName().equals(name)).findFirst();
    }

    public static boolean update(String id, String name) {
        for (User u : DATA) if (u.getName().equals(name) && !u.getId().equals(id)) return false;
        find(id).setName(name);
        return true;
    }

    public static List<User> freeUsers() {
        return DATA.stream().filter(u -> TodoDao.getAssigned(u).size() < getMaxTodos()).collect(Collectors.toList());
    }

    public static void remove(String id) {
        DATA.remove(find(id));
    }

    public static List<User> all() {
        return DATA;
    }

    public static void assignTodo(String userName, String todoId) {
        Todo todo = TodoDao.find(todoId)
                .orElseThrow(() -> new IllegalStateException("Failed to find todo by id = " + todoId));
        User user = UserDao.findByName(userName).orElse(null);
        todo.setAssignedUser(user);
    }

}
