import model.*;
import spark.*;
import spark.template.velocity.*;
import java.util.*;
import static spark.Spark.*;


public class TodoList {

    public static void main(String[] args) {

        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        staticFiles.location("/public");
        port(9999);

        // Render main UI
        get("/", (req, res) -> renderTodos(req));

        // Render Users
        get("/users", (req, res) -> renderUsers(req));

        // Add new
        post("/todos", (req, res) -> {
            TodoDao.add(Todo.create(req.queryParams("todo-title")));
            return renderTodos(req);
        });

        // Add new User
        post("/users/add", (req, res) -> {
            UserDao.add(User.create(req.queryParams("user-name")));
            return renderUsers(req);
        });

        // Remove all completed
        delete("/todos/completed", (req, res) -> {
            TodoDao.removeCompleted();
            return renderTodos(req);
        });

        // Toggle all status
        put("/todos/toggle_status", (req, res) -> {
            TodoDao.toggleAll(req.queryParams("toggle-all") != null);
            return renderTodos(req);
        });

        // Remove by id
        delete("/todos/:id", (req, res) -> {
            TodoDao.remove(req.params("id"));
            return renderTodos(req);
        });

        // Remove User by id
        delete("/users/:id", (req, res) -> {
            UserDao.remove(req.params("id"));
            return renderUsers(req);
        });

        // Update by id
        put("/todos/:id", (req, res) -> {
            TodoDao.update(req.params("id"), req.queryParams("todo-title"));
            return renderTodos(req);
        });

        // Update User by id
        put("/users/:id", (req, res) -> {
            UserDao.update(req.params("id"), req.queryParams("user-name"));
            return renderUsers(req);
        });

        // Toggle status by id
        put("/todos/:id/toggle_status", (req, res) -> {
            TodoDao.toggleStatus(req.params("id"));
            return renderTodos(req);
        });

        // Edit by id
        get("/todos/:id/edit", (req, res) -> renderEditTodo(req));

        // Edit user by id
        get("/users/:id/edit", (req, res) -> renderEditUser(req));

    }

    private static String renderUsers(Request req) {
        Map<String, Object> model = new HashMap<>();
        model.put("users", UserDao.all());
        if ("true".equals(req.queryParams("ic-request"))) {
            return renderTemplate("velocity/UserList.vm", model);
        }
        return renderTemplate("velocity/userIndex.vm", model);
    }

    private static String renderEditTodo(Request req) {
        return renderTemplate("velocity/editTodo.vm", new HashMap<>(){{ put("todo", TodoDao.find(req.params("id"))); }});
    }

    private static String renderEditUser(Request req) {
        return renderTemplate("velocity/editUser.vm", new HashMap<>(){{ put("user", UserDao.find(req.params("id"))); }});
    }

    private static String renderTodos(Request req) {
        String statusStr = req.queryParams("status");
        Map<String, Object> model = new HashMap<>();
        model.put("todos", TodoDao.ofStatus(statusStr));
        model.put("filter", Optional.ofNullable(statusStr).orElse(""));
        model.put("activeCount", TodoDao.ofStatus(Status.ACTIVE).size());
        model.put("anyCompleteTodos", TodoDao.ofStatus(Status.COMPLETE).size() > 0);
        model.put("allComplete", TodoDao.all().size() == TodoDao.ofStatus(Status.COMPLETE).size());
        model.put("status", Optional.ofNullable(statusStr).orElse(""));
        if ("true".equals(req.queryParams("ic-request"))) {
            return renderTemplate("velocity/todoList.vm", model);
        }
        return renderTemplate("velocity/index.vm", model);
    }

    private static String renderTemplate(String template, Map<String, Object> model) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, template));
    }

}

