import {type ReactElement} from 'react';
import { Slider } from '@mui/base';
import {ReactAdapterElement, type RenderHooks} from
        "Frontend/generated/flow/ReactAdapter";
import {VerticalLayout} from "@vaadin/react-components";

class ReactMuiSliderElement extends ReactAdapterElement {

    protected override render(hooks: RenderHooks): ReactElement | null {
        const [defaultValue, setDefaultValue] = hooks.useState<number>("defaultValue");

        return (
            <VerticalLayout>
                <Slider
                    defaultValue={defaultValue}
                />
            </VerticalLayout>
        )
    }
}

customElements.define("mui-slider", ReactMuiSliderElement);