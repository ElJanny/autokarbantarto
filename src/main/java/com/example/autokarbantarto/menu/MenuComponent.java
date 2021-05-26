package com.example.autokarbantarto.menu;

import com.example.autokarbantarto.security.SecurityUtils;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class MenuComponent extends HorizontalLayout {
    public MenuComponent(){
        Anchor main = new Anchor();
        main.setText("main");
        main.setHref("/");
        add(main);

        Anchor cars = new Anchor();
        cars.setText("cars");
        cars.setHref("/car");
        add(cars);

        Anchor manufacturers = new Anchor();
        manufacturers.setText("manufacturers");
        manufacturers.setHref("/manufacturer");
        add(manufacturers);

        if(SecurityUtils.isAdmin()) {
            Anchor users = new Anchor();
            users.setText("users");
            users.setHref("/user");
            add(users);
        }
    }


}
