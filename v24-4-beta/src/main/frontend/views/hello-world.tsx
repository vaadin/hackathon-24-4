import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useSignal } from '@vaadin/hilla-react-signals';
import { Button } from '@vaadin/react-components/Button.js';
import { Notification } from '@vaadin/react-components/Notification.js';
import { TextField } from '@vaadin/react-components/TextField.js';
import { HelloWorldService } from 'Frontend/generated/endpoints.js';
import {reactElement} from "Frontend/generated/flow/Flow";

export const config: ViewConfig = {
  menu: { order: 1, icon: 'line-awesome/svg/globe-solid.svg' },
  title: 'React hello world',
};

function MyCustomGrid() {
    return reactElement("custom-grid", {}, () => {
        document.getElementById("loading")?.remove();
    }, (err: any) => { console.error(err); })
}
export default function ReacthelloworldView() {
  const name = useSignal('');

  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextField
          label="Your name"
          onValueChanged={(e) => {
            name.value = e.detail.value;
          }}
        />
        <Button
          onClick={async () => {
            const serverResponse = await HelloWorldService.sayHello(name.value);
            Notification.show(serverResponse);
          }}
        >
          Say hello
        </Button>
          <MyCustomGrid></MyCustomGrid>
      </section>
    </>
  );
}
