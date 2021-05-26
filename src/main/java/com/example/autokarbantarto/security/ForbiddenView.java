package com.example.autokarbantarto.security;

import com.example.autokarbantarto.menu.MenuComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("403")
public class ForbiddenView extends VerticalLayout {

    public ForbiddenView(){
        add(new MenuComponent());
        add("Access Denied");
    }
}