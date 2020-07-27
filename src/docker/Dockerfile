FROM alpine:edge
RUN apk add --no-cache openjdk11
COPY TodoList-1.0-SNAPSHOT.jar /opt/todo-list/bin/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/todo-list/bin/TodoList-1.0-SNAPSHOT.jar"]