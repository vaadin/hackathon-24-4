package com.vaadin.hackaton.views.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

@NpmPackage(value = "@mui/base", version = "5.0.0-beta.40")
@NpmPackage(value = "@mui/system", version = "5.15.15")
@JsModule("./ReactMuiSliderElement.tsx")
@Tag("mui-slider")
public class ReactMuiSlider extends ReactAdapterComponent {

   public ReactMuiSlider(int defaultValue) {
       setDefaultValue(defaultValue);
       getStyle().setWidth("100%");
   }

   public int getDefaultValue() {
       return getState("defaultValue", Integer.class);
   }

   public void setDefaultValue(int color) {
       setState("defaultValue", color);
   }

}
