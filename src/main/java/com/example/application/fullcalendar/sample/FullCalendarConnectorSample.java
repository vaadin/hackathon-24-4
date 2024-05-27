package com.example.application.fullcalendar.sample;

import com.example.application.fullcalendar.entryprovider.EntryProvider;
import com.example.application.fullcalendar.entryprovider.EntryQuery;
import com.example.application.fullcalendar.model.Entry;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
// TODO extract interface
public class FullCalendarConnectorSample implements com.example.application.fullcalendar.FullCalendarConnector {

    private final EntryService<Entry> entryService;

    public FullCalendarConnectorSample() {
        entryService = EntryService.createRandomInstance();

    }

    @Override
    public EntryProvider<Entry> getEntryProvider() {
        return EntryProvider.fromCallbacks(entryService::streamEntries, entryService::getEntryOrNull);
    }

    @Override
    public void saveEntry(Entry entry) {
        entryService.addOrUpdateEntries(entry);
    }

    @Override
    public void removeEntry(Entry entry) {
        entryService.removeEntries(entry);
    }

}