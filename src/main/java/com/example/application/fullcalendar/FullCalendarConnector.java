package com.example.application.fullcalendar;

import com.example.application.fullcalendar.entryprovider.EntryProvider;
import com.example.application.fullcalendar.entryprovider.EntryQuery;
import com.example.application.fullcalendar.model.Entry;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This interface is the main entry point for the developer using the Hilla integration of the FC. The FC Endpoint
 * will inject an instance of this interface and use its methods to interact with the client, e.g. by providing
 * calendar items to show.
 * <p/>
 * It is recommended to use an EntryProvider, but you may also override the fetch methods, if preferred.
 */
public interface FullCalendarConnector {
    /**
     * Returns the entry provider to be used.
     * @return entry provider
     */
    EntryProvider<Entry> getEntryProvider();

    /**
     * Returns the entries matching the given entry query. Delegates the call to the entry provider by default
     * @return entries
     */
    default Stream<Entry> fetchEntries(EntryQuery entryQuery) {
        return getEntryProvider().fetch(entryQuery);
    }

    /**
     * Returns the entry matching the given id or empty. Delegates the call to the entry provider by default
     * @return entry or empty
     */
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
