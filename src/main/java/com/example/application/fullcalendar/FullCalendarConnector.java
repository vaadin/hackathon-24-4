package com.example.application.fullcalendar;

import com.example.application.fullcalendar.entryprovider.EntryProvider;
import com.example.application.fullcalendar.entryprovider.EntryQuery;
import com.example.application.fullcalendar.model.Entry;

import java.util.Optional;
import java.util.stream.Stream;

public interface FullCalendarConnector {
    EntryProvider<Entry> getEntryProvider();

    default Stream<Entry> fetchEntries(EntryQuery entryQuery) {
        return getEntryProvider().fetch(entryQuery);
    }

    default Optional<Entry> fetchEntry(String id) {
        return getEntryProvider().fetchById(id);
    }

    default Entry createNewEntry() { // TODO extend with create info parameter
        Entry entry = new Entry();
        return entry;
    }

    void saveEntry(Entry entry);

    void removeEntry(Entry entry);
}
