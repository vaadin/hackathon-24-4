import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from "@fullcalendar/daygrid";
import {useRef} from "react";
import interactionPlugin from "@fullcalendar/interaction"
import {FullCalendarEndpoint} from "Frontend/generated/endpoints";
import timeGridPlugin from '@fullcalendar/timegrid'
import {
    formatDate as _formatDate,
    CalendarOptions
} from '@fullcalendar/core';
import Entry from "Frontend/generated/com/example/application/fullcalendar/model/Entry";
import momentTimezonePlugin from '@fullcalendar/moment-timezone';

export interface HillaOptions {
    // /**
    //  * By default the calendar will only fetch items for the current period. With prefetchPerios set, it will
    //  * additionally load the respective amount of adjacend periods in the future and the past. This prevents potential
    //  * flickering, when switching the period (i.e. having for a very short moment an empty calendar and then
    //  * plopping in entries).
    //  * <p/>
    //  * The calendar will try to match the number to the current period. This means, when in a monthly based view
    //  * it will load entries for the adjacent months; when a weekly based view is shown, it will load the adjacent
    //  * weeks and so on.
    //  * <p/>
    //  * Please note that the number should not be too large, otherwise it can have memory impacts on the client.
    //  */
    // TODO implement
    // prefetchPeriods?: number
    className?: string;
}

export type FullCalendarHillaOptions = {[key: string]: any} & HillaOptions & CalendarOptions /* basic FC options */;

function formatDate(date: string | Date, asDay = false) {
    let formatted = new Date(_formatDate(date, {
        timeZone: "utc",
        dateStyle: "medium",
        timeStyle: "medium"
    }));

    return formatted.toISOString();
}

function ignoreOffset(date: string | Date) {
    if(date instanceof Date) {
        date = date.toISOString();
    }
    let indexOf = date.indexOf("+");
    if (indexOf > 0) {
        date = date.substring(0, indexOf);
    }

    return date;
}

function removeUnknownCalendarOptions(props: FullCalendarHillaOptions, key?: string) {
    // TODO is there some way to automate this?
    if (key) {
        if(props[key] !== undefined) {
            props[key] = undefined;
        }
    } else {
        removeUnknownCalendarOptions(props, "className");

    }
}

export default function FullCalendarHilla(props: FullCalendarHillaOptions) {

    let lastFetchInfo: any;

    let calendarOptions = {...props};

    removeUnknownCalendarOptions(calendarOptions);

    return (
        <FullCalendar
            plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin, momentTimezonePlugin]}
            headerToolbar={{
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            }}
            // locale={"german"}
            // timeZone={"Europe/Berlin"}

            height={"100%"}
            initialView='dayGridMonth'
            dateClick={console.warn}
            eventClick={event => console.warn(event)}
            selectable
            select={arg => console.warn(useRef(null))}
            events={(fetchInfo, successCallback, failureCallback) => {

                console.warn(fetchInfo.startStr, formatDate(fetchInfo.start));

                // TODO add prefetch option
                lastFetchInfo = fetchInfo;
                return FullCalendarEndpoint.fetchEntries({
                    start: ignoreOffset(fetchInfo.start),
                    end: ignoreOffset(fetchInfo.end)
                }).then(value => {
                    // check, if there already has been a new fetch before this one
                    // returned. this can be the case, if the user switches fast through periods, while the
                    // server has not yet answered.
                    if (lastFetchInfo === fetchInfo) {
                        if (value) {
                            value = value.map(entry => {
                                // remove this map, when the utc serializer works on the server side

                                if (entry?.start && !entry.start.endsWith("Z")) {
                                    entry.start += "Z";
                                }
                                if (entry?.end && !entry.end.endsWith("Z")) {
                                    entry.end += "Z";
                                }

                                return entry;
                            })
                            successCallback(value as Entry[]);
                        }
                    }
                }).catch(reason => {
                    failureCallback(reason);
                });
            }}
            /*eventClick={eventInfo => {
                FullCalendarEndpoint.fetchFullEntry(eventInfo.event.id);
            }}*/


            {...calendarOptions /*allow overriding any preset option*/}
        />
    );
}



