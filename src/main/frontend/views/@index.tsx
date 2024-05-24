import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useSignal } from '@vaadin/hilla-react-signals';
import { Button, Checkbox, TextField, VerticalLayout } from '@vaadin/react-components';
import Todo from 'Frontend/generated/com/cromoteca/TodoService/Todo';
import { TodoService } from 'Frontend/generated/endpoints';
import { useEffect } from 'react';

export const config: ViewConfig = {
  title: 'Home',
  menu: { order: 0, icon: 'line-awesome/svg/file.svg' },
};

export default function HomeView() {
  const todos = useSignal<Todo[]>([]);
  const newTodo = useSignal('');

  useEffect(() => {
    TodoService.getTodos().onNext(result => todos.value = result);
  }, []);

  return (
    <VerticalLayout theme='spacing'>
      {todos.value.map(todo => (
        <div key={todo.index}>
          <Checkbox
            checked={todo.done}
            onChange={(e) => TodoService.update(todo.index, e.target.checked)}
            label={todo.text}
          />
        </div>
      ))}
      <div>
        <TextField
          value={newTodo.value}
          onValueChanged={({ detail: { value } }) => newTodo.value = value} />
        <Button
          disabled={!newTodo.value}
          onClick={() => TodoService.add({ index: 0, text: newTodo.value, done: false })}>
          Add Todo
        </Button>
      </div>
    </VerticalLayout>
  );
}
