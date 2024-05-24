package com.cromoteca;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.flow.server.auth.AnonymousAllowed;

import com.vaadin.hilla.BrowserCallable;

import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class TodoService {
    public static class Todo {
        private int index;
        private String text;
        private boolean done;

        public Todo() {
        }

        public Todo(int index, String text, boolean done) {
            this.index = index;
            this.text = text;
            this.done = done;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
    }

    private final List<Todo> todos = new ArrayList<>();
    private final Flux<List<Todo>> flux;
    private Consumer<List<Todo>> consumer;

    public TodoService() {
        flux = Flux.<List<Todo>>create(sink -> consumer = sink::next).share();
    }

    public Flux<List<Todo>> getTodos() {
        return Flux.concat(Flux.just(todos), flux);
    }

    public Todo add(Todo todo) {
        todo.setIndex(todos.stream().mapToInt(Todo::getIndex).max().orElse(0) + 1);
        todos.add(todo);

        if (consumer != null) {
            consumer.accept(todos);
        }

        return todo;
    }

    public Todo update(int index, boolean done) {
        Todo todo = todos.stream().filter(t -> t.getIndex() == index).findFirst().orElse(null);

        if (todo != null) {
            todo.setDone(done);

            if (consumer != null) {
                consumer.accept(todos);
            }
        }

        return todo;
    }
}
