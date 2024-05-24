package com.vaadin.hackaton.views.firstpageview;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.hackaton.views.MainLayout;

@PageTitle("FirstPageView")
@Route(value = "", layout = MainLayout.class)
public class FirstPageView extends VerticalLayout {

    public FirstPageView() {
        setWidthFull();
        setHeightFull();
        getStyle().set("flex-grow", "1");
    }
}
