package com.example.autokarbantarto.car.view;

import com.example.autokarbantarto.car.entity.CarEntity;
import com.example.autokarbantarto.car.service.Impl.CarServiceImpl;
import com.example.autokarbantarto.manufacturer.entity.ManufacturerEntity;

import com.example.autokarbantarto.manufacturer.service.Impl.ManufacturerServiceImpl;
import com.example.autokarbantarto.menu.MenuComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/*    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    private ManufacturerEntity manufacturer;

    @Column(name = "numberOfDoors")
    private Integer numberOfDoors;

    @Column(name = "year")
    private Integer year;
*/

@Route
public class CarView extends VerticalLayout {
    //Keresés mezői

    private TextField filterType;
    private IntegerField filterNumberOfDoors;
    private IntegerField filterYear;

    private String filterTypeVar = "";
    private Integer filterNumberOfDoorsVar;
    private Integer filterYearVar;

    //Keresés mezői vége
    //Rendezési gombok

    private Button orderType;
    private Button orderYear;

    private Integer orderBy;

    //Rendezési gombok vége
    private VerticalLayout form;
    private CarEntity selectedCar;
    private Binder<CarEntity> binder;
    private TextField type;
    private IntegerField numberOfDoors;
    private  IntegerField year;
    private ComboBox<ManufacturerEntity> comboBox;
    private Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());
    @Autowired
    private CarServiceImpl carService;
    @Autowired
    private ManufacturerServiceImpl manufacturerService;

    @PostConstruct
    public void init(){
        add(new MenuComponent());
        Grid<CarEntity> grid = new Grid<>();
        grid.setItems(carService.getAll());
        grid.addColumn(CarEntity::getId).setHeader("Id");
        grid.addColumn(CarEntity::getType).setHeader("Type");
        grid.addColumn(CarEntity::getNumberOfDoors).setHeader("Number of Doors");
        grid.addColumn(CarEntity::getYear).setHeader("Year");
        grid.addColumn(carEntity -> {
            if (carEntity.getManufacturer() != null){
                return carEntity.getManufacturer().getName();
            }
            return "";
        }).setHeader("Manufacturer");
        initFilterRow(grid);
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedCar = event.getValue();
            binder.setBean(selectedCar);
            form.setVisible(selectedCar != null);
            deleteBtn.setEnabled(selectedCar != null);

        });
        addButtonBar(grid);
        add(grid);
        addForm(grid);
    }

    private void initFilterRow(Grid<CarEntity> grid){
        HorizontalLayout filtrow = new HorizontalLayout();
        filterType = new TextField("Search for type");
        filterNumberOfDoors = new IntegerField("Search for Doors");
        filterYear = new IntegerField("Search for Year");
        filterType.addValueChangeListener(event -> {
            filterTypeVar= event.getValue();
            search(grid);
        });

        filterNumberOfDoors.addValueChangeListener(event ->{
            filterNumberOfDoorsVar = event.getValue();
            search(grid);
        });

        filterYear.addValueChangeListener(event ->{
            filterYearVar = event.getValue();
            search(grid);
        });

        orderType = new Button("Order by Type");
        orderYear = new Button("Order by Year");
        orderBy = 0;
        orderType.addClickListener(event->{
            if(orderBy == 2 || orderBy == (-2)){
                orderBy *= (-1);
            }else{
                orderBy= 2;
            }
            orderData(orderBy,grid);
        });

        orderYear.addClickListener(event->{
            if(orderBy == 1 || orderBy == (-1)){
                orderBy *= (-1);
            }else{
                orderBy= 1;
            }
            orderData(orderBy,grid);
        });
        filtrow.add(filterType,filterNumberOfDoors,filterYear,orderType,orderYear);
        add(filtrow);

    }

    private void orderData (Integer ordeBy,Grid<CarEntity> grid){
        List<CarEntity> list = carService.getAll(filterTypeVar,filterNumberOfDoorsVar,filterYearVar);
        list.sort(new Comparator<CarEntity>() {
            @Override
            public int compare(CarEntity a, CarEntity b) {
                switch (ordeBy)
                {
                    case 1:
                        return a.getYear() - b.getYear();

                    case 2:
                        return a.getType().compareTo(b.getType());

                    case -1:
                        return b.getYear() - a.getYear();


                    case -2:
                        return b.getType().compareTo(a.getType());

                }
                return a.getYear() - b.getYear();
            }
        });
        grid.setItems(list);
    }

    private void search(Grid<CarEntity> grid){
        grid.setItems(carService.getAll(filterTypeVar,filterNumberOfDoorsVar,filterYearVar));
    }

    private void addForm(Grid<CarEntity> grid) {
        form = new VerticalLayout();
        binder = new Binder<>(CarEntity.class);

        HorizontalLayout nameField = new HorizontalLayout();
        type = new TextField("type");
        numberOfDoors = new IntegerField("number of doors");
        year = new IntegerField("year");
        nameField.add(type, numberOfDoors,year);



        comboBox = new ComboBox<>("manufacturer");
        comboBox.setItems(manufacturerService.getAll());
        comboBox.setItemLabelGenerator(ManufacturerEntity -> ManufacturerEntity.getName());
        nameField.add(comboBox,addSaveBtn(grid));


        form.add(nameField);
        add(form);
        form.setVisible(false);
        binder.bindInstanceFields(this);

    }

    private Button addSaveBtn(Grid<CarEntity> grid) {
        Button saveBtn = new Button("Save", VaadinIcon.SAFE.create());
        saveBtn.addClickListener(buttonClickEvent -> {


            if ( !selectedCar.getType().equals( "" ) ){
                if (selectedCar.getId() == null) {
                    CarEntity carEntity = new CarEntity();
                    carEntity.setType(selectedCar.getType());
                    carEntity.setManufacturer(comboBox.getValue());
                    carEntity.setYear(selectedCar.getYear());
                    carEntity.setNumberOfDoors(selectedCar.getNumberOfDoors());
                    carService.add(carEntity);
                    grid.setItems(carService.getAll());
                    selectedCar = null;
                    Notification.show("Sikeres mentés");
                } else {
                    carService.update(selectedCar);
                    grid.setItems(carService.getAll());
                    Notification.show("Sikeres módosítás");
                }
                form.setVisible(false);
            }
        });
        return saveBtn;
    }

    private void addButtonBar(Grid<CarEntity> grid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteBtn.addClickListener(buttonClickEvent -> {
            carService.remove(selectedCar);
            Notification.show("Sikeres törlés");
            selectedCar = null;
            grid.setItems(carService.getAll());
            form.setVisible(false);

        });
        deleteBtn.setEnabled(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            selectedCar = new CarEntity();
            binder.setBean(selectedCar);
            form.setVisible(true);

        });
        horizontalLayout.add(deleteBtn);
        horizontalLayout.add(addBtn);
        add(horizontalLayout);
    }

}
