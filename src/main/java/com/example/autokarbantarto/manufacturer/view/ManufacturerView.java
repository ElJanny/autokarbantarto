package com.example.autokarbantarto.manufacturer.view;


import com.example.autokarbantarto.car.entity.CarEntity;
import com.example.autokarbantarto.manufacturer.entity.ManufacturerEntity;
import com.example.autokarbantarto.manufacturer.service.Impl.ManufacturerServiceImpl;
import com.example.autokarbantarto.menu.MenuComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

@Route
public class ManufacturerView extends VerticalLayout {
    private TextField filterName;
    private Button sortName;
    private String filter;
    private boolean order;
    private VerticalLayout form;
    private ManufacturerEntity selectedManufacturer;
    private Binder<ManufacturerEntity> binder;
    private TextField name;
    private Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());
    @Autowired
    private ManufacturerServiceImpl manufacturerService;

    @PostConstruct
    public void init(){
        add(new MenuComponent());
        Grid<ManufacturerEntity> grid = new Grid<>();
        grid.setItems((manufacturerService.getAll()));
        grid.addColumn(ManufacturerEntity::getId).setHeader("Id");
        grid.addColumn(ManufacturerEntity::getName).setHeader("Name");
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedManufacturer = event.getValue();
            binder.setBean(selectedManufacturer);
            form.setVisible(selectedManufacturer != null);
            deleteBtn.setEnabled(selectedManufacturer != null);

        });
        initFilter(grid);
        addButtonBar(grid);
        add(grid);
        addForm(grid);
    }

    private void addForm(Grid<ManufacturerEntity> grid) {
        form = new VerticalLayout();
        binder = new Binder<>(ManufacturerEntity.class);

        HorizontalLayout nameField = new HorizontalLayout();
        name = new TextField("name");
        nameField.add(name,addSaveBtn(grid));

        form.add(nameField);
        add(form);
        form.setVisible(false);
        binder.bindInstanceFields(this);

    }

    private void initFilter(Grid<ManufacturerEntity> grid){
        HorizontalLayout filterField = new HorizontalLayout();
        filter = "";
        filterName = new TextField("Search by name");
        sortName = new Button("Order by name");
        filterName.addValueChangeListener(event -> {
            filter = event.getValue();
           grid.setItems(manufacturerService.getAll(filter));
        });
        sortName.addClickListener(event ->{
            List<ManufacturerEntity> list = (manufacturerService.getAll(filter));
            list.sort(new Comparator<ManufacturerEntity>() {
                @Override
                public int compare(ManufacturerEntity a, ManufacturerEntity b) {
                    if(order){
                        return a.getName().compareTo(b.getName());
                    }else{
                        return b.getName().compareTo(a.getName());
                    }

                }

            });
            order = !order;
            grid.setItems(list);
    });
        add(filterName,sortName);
    }

    private Button addSaveBtn(Grid<ManufacturerEntity> grid) {
        Button saveBtn = new Button("Save", VaadinIcon.SAFE.create());
        saveBtn.addClickListener(buttonClickEvent -> {



            if(!selectedManufacturer.getName().equals("")) {
                if (selectedManufacturer.getId() == null) {
                    ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
                    manufacturerEntity.setName(selectedManufacturer.getName());


                    manufacturerService.add(manufacturerEntity);
                    grid.setItems(manufacturerService.getAll());
                    selectedManufacturer = null;
                    Notification.show("Sikeres mentés");
                } else {
                    manufacturerService.update(selectedManufacturer);
                    grid.setItems(manufacturerService.getAll());
                    Notification.show("Sikeres módosítás");
                }
                form.setVisible(false);
            }
        });
        return saveBtn;

    }

    private void addButtonBar(Grid<ManufacturerEntity> grid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteBtn.addClickListener(buttonClickEvent -> {
            manufacturerService.remove(selectedManufacturer);
            Notification.show("Sikeres törlés");
            selectedManufacturer = null;
            grid.setItems(manufacturerService.getAll());
            form.setVisible(false);

        });
        deleteBtn.setEnabled(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            selectedManufacturer = new ManufacturerEntity();
            binder.setBean(selectedManufacturer);
            form.setVisible(true);

        });
        horizontalLayout.add(deleteBtn);
        horizontalLayout.add(addBtn);
        add(horizontalLayout);
    }

}
