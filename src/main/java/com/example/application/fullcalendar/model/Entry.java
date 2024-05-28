/*
 * Copyright 2020, Stefan Uebe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.application.fullcalendar.model;

import com.example.application.fullcalendar.model.json.JsonUpdateAllowed;
import com.example.application.fullcalendar.model.serializer.LocalDateTimeUTCSerializer;
import com.example.application.fullcalendar.model.serializer.TrimToNullSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.*;
import java.util.*;

@Getter
@Setter // prevent conflicts with Vaadin Setter
@EqualsAndHashCode(of = "id")
@FieldNameConstants
public class Entry {

    private static final Set<BeanProperties<Entry>> PROPERTIES = BeanProperties.read(Entry.class);

    @NotNull
    private final String id;

    @JsonSerialize(using = TrimToNullSerializer.class)
    private String groupId;

    @NotNull
    private String title;

    @JsonSerialize(using = LocalDateTimeUTCSerializer.class) // FIXME not working currently?
    private LocalDateTime start;

    @JsonSerialize(using = LocalDateTimeUTCSerializer.class) // FIXME not working currently?
    private LocalDateTime end;

    @JsonUpdateAllowed
    private boolean allDay;

    private boolean editable = true;
    private boolean startEditable = true;
    private boolean durationEditable = true;
    private boolean overlap = true;

    @JsonSerialize(using = TrimToNullSerializer.class)
    private String color;

    @JsonSerialize(using = TrimToNullSerializer.class)
    private String constraint;

    @JsonSerialize(using = TrimToNullSerializer.class)
    private String backgroundColor;

    @JsonSerialize(using = TrimToNullSerializer.class)
    private String borderColor;

    @JsonSerialize(using = TrimToNullSerializer.class)
    private String textColor;

    @JsonProperty("display")
    @NotNull
    private DisplayMode displayMode = DisplayMode.AUTO;

    @JsonProperty("startRecur")
//    @JsonConverter(LocalDateConverter.class)
    private LocalDate recurringStartDate;

    @JsonProperty("endRecur")
//    @JsonConverter(LocalDateConverter.class)
    private LocalDate recurringEndDate;

    @JsonProperty("startTime")
//    @JsonConverter(RecurringTimeConverter.class)
    private RecurringTime recurringStartTime; // see #139

    @JsonProperty("endTime")
//    @JsonConverter(RecurringTimeConverter.class)
    private RecurringTime recurringEndTime; // see #139

    @JsonProperty("daysOfWeek")
//    @JsonConverter(DayOfWeekItemConverter.class)
    private Set<DayOfWeek> recurringDaysOfWeek;

//    private Set<String> classNames;

//    private Map<String, Object> customProperties;

    /**
     * Creates a new editable instance with a generated id.
     */
    public Entry() {
        this(null);
    }

    /**
     * Creates a new entry with the given id. Null will lead to a generated id.
     * <br><br>
     * Please be aware, that the ID needs to be unique in the calendar instance. Otherwise it can lead to
     * unpredictable results.
     *
     * @param id id
     */
    public Entry(String id) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
    }

//    /**
//     * Returns the set of class names or creates a new, empty one, if none exists yet. The returned set is
//     * the same as used internally, therefore any changes to it will be reflected to the client side on the
//     * next refresh.
//     *
//     * @return class names set
//     * @see #getClassNames()
//     */
//    @JsonIgnore
//    public Set<String> getOrCreateClassNames() {
//        Set<String> classNames = getClassNames();
//        if (classNames == null) {
//            classNames = new LinkedHashSet<>();
//            setClassNames(classNames);
//        }
//
//        return classNames;
//    }

    /**
     * Returns the description of this entry. Since the description is a <b>custom property</b>, it will
     * not automatically be shown on the entry.
     *
     * @return description
     */
    public String getDescription() {
        return getCustomProperty(EntryCustomProperties.DESCRIPTION);
    }

    /**
     * Sets the description of this entry. Since the description is a <b>custom property</b>, it will
     * not automatically be shown on the entry.
     *
     * @param description description
     */
    public void setDescription(String description) {
        setCustomProperty(EntryCustomProperties.DESCRIPTION, description);
    }

    /**
     * Sets the display mode for this entry. Passing null will reset it to the default.
     *
     * @param displayMode how to display the entry
     */
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode != null ? displayMode : DisplayMode.AUTO;
    }

    /**
     * Indicates, if this entry is recurring. This is indicated by having any "recurring" property set (e.g.
     * {@link #getRecurringDaysOfWeek()}).
     *
     * @return is a recurring event
     */
    @JsonIgnore
    public boolean isRecurring() {
        Set<DayOfWeek> daysOfWeek = getRecurringDaysOfWeek();
        return (daysOfWeek != null && !daysOfWeek.isEmpty())
               || getRecurringEndDate() != null
               || getRecurringStartDate() != null
               || getRecurringStartTime() != null
               || getRecurringEndTime() != null;
    }

    /**
     * Sets custom properties.
     * <p></p>
     * You can access custom properties on the client side when customizing the event rendering via the property
     * <code>event.getCustomProperty('key')</code>, for instance inside the entry content callback.
     *
     * @param customProperties custom properties
     * @see FullCalendarBuilder#withEntryContent(String)
     */
    public void setCustomProperties(Map<String, Object> customProperties) {
//        this.customProperties = customProperties;
    }

    /**
     * Sets custom property for this entry. An existing property will be overwritten.
     * <p></p>
     * You can access custom properties on the client side when customizing the event rendering via the property
     * <code>event.getCustomProperty('key')</code>, for instance inside the entry content callback.
     *
     * @param key   the name of the property to set
     * @param value value to set
     * @see FullCalendarBuilder#withEntryContent(String)
     */
    public void setCustomProperty(@NotNull String key, Object value) {
        Objects.requireNonNull(key);
        getOrCreateCustomProperties().put(key, value);
    }

    /**
     * Returns a custom property (or null if not defined).
     * <p></p>
     * You can access custom properties on the client side when customizing the event rendering via the property
     * <code>event.getCustomProperty('key')</code>, for instance inside the entry content callback.
     *
     * @param key name of the custom property
     * @param <T> return type
     * @return custom property value or null
     * @see FullCalendarBuilder#withEntryContent(String)
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public <T> T getCustomProperty(@NotNull String key) {
        return (T) getCustomPropertiesOrEmpty().get(key);
    }

    /**
     * Remove the custom property based on the name.
     *
     * @param key the name of the property to remove
     */
    public void removeCustomProperty(@NotNull String key) {
        Map<String, Object> customProperties = getCustomProperties();
        if (customProperties != null) {
            // FIXME this will currently not remove the custom property from the client side!
            customProperties.remove(Objects.requireNonNull(key));
        }
    }

    /**
     * Remove specific custom property where the name and value match.
     *
     * @param key   the name of the property to remove
     * @param value the object to remove
     */
    public void removeCustomProperty(@NotNull String key, @NotNull Object value) {
        Map<String, Object> customProperties = getCustomProperties();
        if (customProperties != null) {
            // FIXME this will currently not remove the custom property from the client side!
            customProperties.remove(Objects.requireNonNull(key), Objects.requireNonNull(value));
        }
    }

    /**
     * Returns the map of the custom properties of this instance. This map is editable and any changes
     * will be sent to the client when entries are refreshed.
     * <p></p>
     * Might be null.
     * <p></p>
     * You can access custom properties on the client side when customizing the event rendering via the property
     * <code>event.getCustomProperty('key')</code>, for instance inside the entry content callback.
     *
     * @return Map
     * @see FullCalendarBuilder#withEntryContent(String)
     * @see #getCustomPropertiesOrEmpty()
     * @see #getOrCreateCustomProperties()
     */
    public Map<String, Object> getCustomProperties() {
//        return customProperties;
        return new HashMap<>();
    }

    /**
     * Returns the custom properties map or an empty one, if none has yet been created. The map is not writable.
     *
     * @return map
     * @see #getCustomProperties()
     * @see #getOrCreateCustomProperties()
     */
    @JsonIgnore
    public Map<String, Object> getCustomPropertiesOrEmpty() {
//        return customProperties != null ? Collections.unmodifiableMap(customProperties) : Collections.emptyMap();
        return new HashMap<>();
    }

    /**
     * Returns the map of the custom properties of this instance. This map is editable and any changes
     * will be sent to the client when the entry provider is refreshed.
     * <p></p>
     * Creates and registers a new map, if none is there yet.
     * <p></p>
     * Be aware, that any non standard property you
     * set via "set(..., ...)" is not automatically put into this map, but this is done by the client later.
     *
     * @return Map
     * @see #getCustomPropertiesOrEmpty()
     * @see #getCustomProperties()
     */
    @JsonIgnore
    public Map<String, Object> getOrCreateCustomProperties() {
//        if (customProperties == null) {
//            customProperties = new HashMap<>();
//        }
//        return customProperties;
        return new HashMap<>();
    }

    /**
     * Defines known custom properties, for instance since they are widely used.
     */
    public static final class EntryCustomProperties {
        /**
         * Key for an entry's description.
         */
        public static final String DESCRIPTION = "description";
    }


}
