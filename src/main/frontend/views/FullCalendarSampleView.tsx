import FullCalendarHilla from "Frontend/views/FullCalendarHilla";
import {VerticalLayout} from "@vaadin/react-components/VerticalLayout";

export default function FullCalendarSampleView() {
    return (
        <VerticalLayout theme={"spacing"} className={"items-stretch"}>
            <span>Hint: timeslotted entries are created with 10am (UTC)</span>
            <FullCalendarHilla navLinks={true} className={"flex-grow"} />
        </VerticalLayout>
    )
}