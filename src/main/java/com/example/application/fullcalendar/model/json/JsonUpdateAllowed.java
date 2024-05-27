package com.example.application.fullcalendar.model.json;

import elemental.json.JsonObject;
import elemental.json.JsonValue;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonUpdateAllowed {
}
