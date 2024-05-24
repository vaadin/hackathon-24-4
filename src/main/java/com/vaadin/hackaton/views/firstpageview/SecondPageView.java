package com.vaadin.hackaton.views.firstpageview;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.hackaton.views.MainLayout;

@PageTitle("Second page")
@Route(value = "second", layout = MainLayout.class)
public class SecondPageView extends Composite<VerticalLayout> {

    public SecondPageView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
    }
}
