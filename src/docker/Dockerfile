FROM alpine:edge
RUN apk add --no-cache openjdk11
COPY TodoList-1.0-SNAPSHOT.jar /opt/todo-list/bin/
COPY app_starter.sh /opt/todo-list/bin/
RUN chmod u+x /opt/todo-list/bin/app_starter.sh
ENTRYPOINT ["/bin/sh"]
CMD ["/opt/todo-list/bin/app_starter.sh"]