package model;

import java.util.*;
import java.util.stream.*;

public final class UserDao {
    private UserDao() {}
    private static final List<User> DATA = new ArrayList<>();

    public static void add(User user) {
        DATA.add(user);
    }

    public static User find(String id) {
        return DATA.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public static void update(String id, String title) {
        find(id).setName(title);
    }

    public static void remove(String id) {
        DATA.remove(find(id));
    }

    public static List<User> all() {
        return DATA;
    }
}
