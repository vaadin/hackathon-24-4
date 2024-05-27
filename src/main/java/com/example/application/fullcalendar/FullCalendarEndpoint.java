package com.example.application.fullcalendar;

import com.example.application.fullcalendar.entryprovider.EntryQuery;
import com.example.application.fullcalendar.model.Entry;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import java.util.Optional;

@NpmPackage(value = "@fullcalendar/core", version = "6.1.9")
@NpmPackage(value = "@fullcalendar/interaction", version = "6.1.9")
@NpmPackage(value = "@fullcalendar/daygrid", version = "6.1.9")
@NpmPackage(value = "@fullcalendar/timegrid", version = "6.1.9")
@NpmPackage(value = "@fullcalendar/list", version = "6.1.9")
@NpmPackage(value = "@fullcalendar/multimonth", version = "6.1.9")
@NpmPackage(value = "moment", version = "2.29.4")
@NpmPackage(value = "moment-timezone", version = "0.5.40")
@NpmPackage(value = "@fullcalendar/moment", version = "6.1.9")
@NpmPackage(value = "@fullcalendar/moment-timezone", version = "6.1.9")
@NpmPackage(value = "@fullcalendar/react", version = "6.1.9")
@BrowserCallable
@AnonymousAllowed
public class FullCalendarEndpoint {

    private final FullCalendarConnector connector;

    public FullCalendarEndpoint(FullCalendarConnector connector) {
        this.connector = connector;
    }

//    public void onTimeslotClicked(String dateTime, boolean allDay) {
//        connector.onTimeslotClicked(new TimeslotClickedEvent(fullCalendar, true, dateTime, allDay));
//    }

    /**
     * @param entryQuery
     * @return
     */
    public Entry[] fetchEntries(EntryQuery entryQuery) {
        // debug - simulate slow database
        //        try {
        //            Thread.sleep(500);
        //        } catch (InterruptedException e) {
        //            throw new RuntimeException(e);
        //        }

        return connector
                .fetchEntries(entryQuery)
                .toArray(Entry[]::new);
    }

    public Entry fetchFullEntry(String id) {
        return connector.fetchEntry(id).orElse(null);
    }

    public boolean removeEntry(String id) {
        Optional<Entry> fetched = connector.fetchEntry(id);
        fetched.ifPresent(connector::removeEntry);
        return fetched.isPresent();
    }

}