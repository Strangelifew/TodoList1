#!/bin/sh
echo "###Starting app###" >> /opt/todo-list/bin/app.out
/usr/bin/java -jar /opt/todo-list/bin/TodoList-1.0-SNAPSHOT.jar >> /opt/todo-list/bin/app.out 2>&1