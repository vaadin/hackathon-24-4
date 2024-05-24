package com.vaadin.hackaton.views.firstpageview;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.hackaton.views.MainLayout;

@PageTitle("Third Page")
@Route(value = "third", layout = MainLayout.class)
public class ThirdPageView extends Composite<VerticalLayout> {

    public ThirdPageView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
    }
}
