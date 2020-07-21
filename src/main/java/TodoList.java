import model.*;
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static spark.Spark.*;


public class TodoList {

    public static void main(String[] args) {

        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        staticFiles.location("/public");
        port(9999);

        // Render main UI
        get("/", (req, res) -> renderTodos(req));

        // Render Users
        get("/users", (req, res) -> renderUsers(req, true));

        // Add new
        post("/todos", (req, res) -> {
            TodoDao.add(Todo.create(req.queryParams("todo-title")));
            return renderTodos(req);
        });

        // Set Max Todos
        put("/users/maxTodos", (req, res) -> {
            UserDao.setMaxTodos(Integer.parseInt(req.queryParams("maxTodos")));
            return renderTodos(req);
        });

        // Add new User
        post("/users/add", (req, res) -> renderUsers(req, UserDao.add(User.create(req.queryParams("user-name")))));

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
            TodoDao.remove(UUID.fromString(req.params("id")));
            return renderTodos(req);
        });

        // Remove User by id
        delete("/users/:id", (req, res) -> {
            UserDao.remove(UUID.fromString(req.params("id")));
            return renderUsers(req, true);
        });

        // Update by id
        put("/todos/:id", (req, res) -> {
            String id = req.params("id");
            String userName = req.queryParams("selected-user");
            TodoDao.update(UUID.fromString(id), req.queryParams("todo-title"));
            TodoDao.assign(userName, UUID.fromString(id));
            return renderTodos(req);
        });

        // Update User by id
        put("/users/:id", (req, res) ->
                renderUsers(req, UserDao.update(UUID.fromString(req.params("id")), req.queryParams("user-name"))));

        // Toggle status by id
        put("/todos/:id/toggle_status", (req, res) -> {
            TodoDao.toggleStatus(UUID.fromString(req.params("id")));
            return renderTodos(req);
        });

        // Edit by id
        get("/todos/:id/edit", (req, res) -> renderEditTodo(req));

        // Edit user by id
        get("/users/:id/edit", (req, res) -> renderEditUser(req));

    }

    private static String renderUsers(Request req, boolean isSuccess) {
        Map<String, Object> model = new HashMap<>();
        model.put("users", UserDao.all());
        model.put("isFailure", !isSuccess);
        if ("true".equals(req.queryParams("ic-request"))) {
            return renderTemplate("velocity/UserList.vm", model);
        }
        return renderTemplate("velocity/userIndex.vm", model);
    }

    private static String renderEditTodo(Request req) {
        Map<String, Object> model = new HashMap<>();
        String id = req.params("id");
        Todo todo = TodoDao.find(UUID.fromString(id)).orElseThrow(() -> new IllegalStateException("Failed to find todo by id = " + id));
        model.put("todo", todo);
        model.put("users", UserDao.all());
        model.put("free", UserDao.freeUsers());
        model.put("user-todo", todo.getAssignee());
        if ("true".equals(req.queryParams("ic-request"))) {
            return renderTemplate("velocity/editTodo.vm", model);
        }
        return renderTemplate("velocity/editTodoIndex.vm", model);
    }

    private static String renderEditUser(Request req) {
        return renderTemplate("velocity/editUser.vm", new HashMap<>() {{
            String id = req.params("id");
            put("user", UserDao.find(UUID.fromString(id))
                    .orElseThrow(() -> new IllegalStateException("Can't find user by id = " + id)));
        }});
    }

    private static String renderTodos(Request req) {
        String statusStr = req.queryParams("status");
        int maxTodos = UserDao.getMaxTodos();
        Map<String, Object> model = new HashMap<>();
        model.put("todos", TodoDao.ofStatus(statusStr));
        model.put("maxTodos", maxTodos);
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

