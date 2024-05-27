package com.example.application.fullcalendar.entryprovider;

import com.example.application.fullcalendar.model.Entry;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A class to provide filter parameters for an {@link EntryProvider} fetch query.
 * @author Stefan Uebe
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EntryQuery {

//    private final FullCalendar source; // needed?
    private LocalDateTime start;
    private LocalDateTime end;

    @NonNull
    private AllDay allDay = AllDay.BOTH;

    public EntryQuery(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

//    public EntryQuery(Instant start, Instant end) {
//        this(start, end, AllDay.BOTH);
//    }
//
//    public EntryQuery(Instant start, Instant end, AllDay allDay) {
//        this(start != null ? LocalDateTime.ofInstant(start, Timezone.ZONE_ID_UTC) : null, end != null ? LocalDateTime.ofInstant(end, Timezone.ZONE_ID_UTC) : null, allDay);
//    }

    /**
     * Convenience implementation to filter a stream based on this query.
     * <p></p>
     * Simply applies the filter to the given stream and returns a stream containing only entries matching it.
     * Entries, that are "crossing" the time range border will be included in the stream.
     * <p></p>
     * Returns the same stream, when this filter is empty.
     *
     * @param stream stream
     * @param <T>    type
     * @return filtered stream
     */
    public <T extends Entry> Stream<T> applyFilter(Stream<T> stream) {
        if (start == null && end == null && allDay == AllDay.BOTH) {
            return stream;
        }

        if (start != null) {
            LocalDate startDate = start.toLocalDate();

            stream = stream.filter(e -> {
                if (e.isRecurring()) {
                    LocalDate recurringEndDate = e.getRecurringEndDate();

                    return recurringEndDate == null || recurringEndDate.isAfter(startDate);

                    /*
                    if (recurringEndDate == null || recurringEndDate.isAfter(start.toLocalDate())) {
                        return true;
                    }

                    // TODO is the filter for recurring time necessary?
                    //  there might be the special case, that a recurring time plus 1 needs to be added to the recurring
                    //  end date, but in all other cases I am not sure, if the time only filter is needed anywhere

                    RecurringTime recurringEndTime = e.getRecurringEndTime();
                    if (recurringEndTime != null && !recurringEndTime.isValidLocalTime()) {
                        return true; // TODO this special case needs to be investigated, not sure yet how to handle
                    }

                    // old impl not working anymore
                    LocalDateTime recurringEnd = e.getRecurringEnd();

                    // recurring events, that have no end may go indefinitely to the future. So we return
                    // them always
                    return recurringEnd == null || recurringEnd.isAfter(start);
                    */
                }

                return e.getEnd() != null && e.getEnd().isAfter(start);
            });
        }

        if (end != null) {
            LocalDate endDate = end.toLocalDate();

            stream = stream.filter(e -> {
                if (e.isRecurring()) {
                    // old impl not working anymore
                    LocalDate recurringStartDate = e.getRecurringStartDate();
                    return recurringStartDate == null || recurringStartDate.isBefore(endDate);

                    // TODO see "end" regarding filtering of recurring times

//                    LocalDateTime recurringStart = e.getRecurringStart();
//
                    // recurring events, that have no start may go indefinitely to the past. So we return
                    // them always
//                    return recurringStart == null || recurringStart.isBefore(end);
                }

                return e.getStart() != null && e.getStart().isBefore(end);
            });
        }


        if (allDay != AllDay.BOTH) {
            Predicate<T> allDayFilter = Entry::isAllDay;
            if (allDay == AllDay.TIMED_ONLY) {
                allDayFilter = allDayFilter.negate();
            }

            stream = stream.filter(allDayFilter);
        }

        return stream;
    }

    public enum AllDay {
        BOTH,
        ALL_DAY_ONLY,
        TIMED_ONLY;
    }
}
