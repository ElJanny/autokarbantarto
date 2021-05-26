package com.example.autokarbantarto.user.View;

import com.example.autokarbantarto.menu.MenuComponent;
import com.example.autokarbantarto.user.entity.RoleEntity;
import com.example.autokarbantarto.user.entity.UserEntity;
import com.example.autokarbantarto.user.service.RoleService;
import com.example.autokarbantarto.user.service.UserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Route
public class UserView extends VerticalLayout {

    //Keresési mezők

    private TextField filterUsername;
    private TextField filterLastname;
    private TextField filterFirstname;

    private String filterUsernameVar;
    private String filterLastnameVar;
    private String filterFirstnameVar;

    //Keresési mezők vége

    //Rendezési gombok

    private Button orderUsername;
    private Button orderFirstname;
    private Button orderLastname;
    private Integer orderBy;

    //Rendezési gombok vége
    private VerticalLayout form;
    private UserEntity selectedUser;
    private Binder<UserEntity> binder;
    private TextField username;
    private TextField lastname;
    private TextField firstname;
    private PasswordField password;
    private ComboBox<RoleEntity> comboBox;
    private Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());

    @Autowired
    private UserService service;
    @Autowired
    private RoleService roleService;

    @PostConstruct
    public void init() {
        add(new MenuComponent());
        add(new Text("A felhasználók oldala"));
        Grid<UserEntity> grid = new Grid<>();
        grid.setItems(service.getAll());
        grid.addColumn(UserEntity::getId).setHeader("Id");
        grid.addColumn(UserEntity::getUsername).setHeader("Username");
        grid.addColumn(userEntity -> {
                    if (userEntity.getAuthorities() != null) {
                        StringBuilder builder = new StringBuilder();
                        userEntity.getAuthorities().forEach(roleEntity -> {
                            builder.append(roleEntity.getAuthority()).append(",");
                        });
                        return builder.toString();
                    }
                    return "";
                }
        ).setHeader("Author");
        grid.addColumn(UserEntity::getFirstName).setHeader("First name");
        grid.addColumn(UserEntity::getLastName).setHeader("Last name");
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedUser = event.getValue();
            binder.setBean(selectedUser);
            form.setVisible(selectedUser != null);
            deleteBtn.setEnabled(selectedUser != null);

        });
        initFilterRow(grid);
        addButtonBar(grid);
        add(grid);
        addForm(grid);
    }

    private void initFilterRow(Grid<UserEntity> grid){
        HorizontalLayout filtRow = new HorizontalLayout();
        filterUsername = new TextField("Search for Username");
        filterFirstname = new TextField("Search for Firstname");
        filterLastname = new TextField("Search for Lastname");
        orderBy=0;
        filterUsernameVar = "";
        filterFirstnameVar = "";
        filterLastnameVar = "";

        filterUsername.addValueChangeListener(event ->{
            filterUsernameVar = event.getValue();
            grid.setItems(service.getAll(filterUsernameVar,filterFirstnameVar, filterLastnameVar));
        });

        filterFirstname.addValueChangeListener(event ->{
            filterFirstnameVar = event.getValue();
            grid.setItems(service.getAll(filterUsernameVar,filterFirstnameVar, filterLastnameVar));
        });

        filterLastname.addValueChangeListener(event ->{
            filterLastnameVar = event.getValue();
            grid.setItems(service.getAll(filterUsernameVar,filterFirstnameVar, filterLastnameVar));
        });

        orderUsername = new Button("Order by Username");
        orderFirstname = new Button("Order by Firstname");
        orderLastname = new Button("Order by Lastname");

        orderUsername.addClickListener(event->{
            if(orderBy == 1 || orderBy == (-1)){
                orderBy *= (-1);
            }else{
                orderBy= 1;
            }
            orderData(orderBy,grid);
        });

        orderFirstname.addClickListener(event->{
            if(orderBy == 2 || orderBy == (-2)){
                orderBy *= (-1);
            }else{
                orderBy= 2;
            }
            orderData(orderBy,grid);
        });

        orderLastname.addClickListener(event->{
            if(orderBy == 3 || orderBy == (-3)){
                orderBy *= (-1);
            }else{
                orderBy= 3;
            }
            orderData(orderBy,grid);
        });
        filtRow.add(filterUsername,filterFirstname,filterLastname,orderUsername,orderFirstname,orderLastname);
        add(filtRow);
    }

    private void orderData(Integer orderBy, Grid<UserEntity> grid){
        List<UserEntity> list = service.getAll(filterUsernameVar,filterFirstnameVar, filterLastnameVar);
        list.sort(new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity a, UserEntity b) {
               switch (orderBy)
               {
                   case 1:
                       return  a.getUsername().compareTo(b.getUsername());
                   case -1:
                       return  b.getUsername().compareTo(a.getUsername());
                   case 2:
                       return  a.getFirstName().compareTo(b.getFirstName());
                   case -2:
                       return  b.getFirstName().compareTo(a.getFirstName());
                   case 3:
                       return  a.getLastName().compareTo(b.getLastName());
                   case -3:
                       return  b.getLastName().compareTo(a.getLastName());
               }
                return  a.getUsername().compareTo(b.getUsername());
            }
        });
        grid.setItems(list);
    }

    private void addForm(Grid<UserEntity> grid) {
        form = new VerticalLayout();
        binder = new Binder<>(UserEntity.class);

        HorizontalLayout nameField = new HorizontalLayout();
        username = new TextField();
        firstname = new TextField();
        lastname = new TextField();
        nameField.add(new Text("Username"), username,new Text(" Fisrt Name"), firstname, new Text(" Last Name"),lastname);



        HorizontalLayout passwordField = new HorizontalLayout();
        password = new PasswordField();
        passwordField.add(new Text("Password"), password);

        HorizontalLayout authorField = new HorizontalLayout();
        comboBox = new ComboBox<>();
        comboBox.setItems(roleService.getAll());
        comboBox.setItemLabelGenerator(authorEntity -> authorEntity.getAuthority());
        authorField.add(new Text("Authorities"), comboBox);


        form.add(nameField, authorField, passwordField, addSaveBtn(grid));
        add(form);
        form.setVisible(false);
        binder.bindInstanceFields(this);
    }

    private Button addSaveBtn(Grid<UserEntity> grid) {
        Button saveBtn = new Button("Save", VaadinIcon.SAFE.create());
        saveBtn.addClickListener(buttonClickEvent -> {
           if(!(selectedUser.getUsername().equals("") && selectedUser.getFirstName().equals("")
            && selectedUser.getLastName().equals(""))) {
               if (selectedUser.getId() == null) {
                   UserEntity userEntity = new UserEntity();
                   userEntity.setUsername(selectedUser.getUsername());
                   userEntity.setAuthorities(Collections.singletonList(comboBox.getValue()));
                   userEntity.setPassword(generatePassword());
                   userEntity.setFirstName(selectedUser.getFirstName());
                   userEntity.setLastName(selectedUser.getLastName());
                   service.add(userEntity);
                   grid.setItems(service.getAll());
                   selectedUser = null;
                   Notification.show("Sikeres mentés");
               } else {
                   if(!selectedUser.getPassword().equals("")) {
                       service.update(selectedUser);
                       grid.setItems(service.getAll());
                       Notification.show("Sikeres módosítás");
                   }
               }
               form.setVisible(false);
           }
        });
        return saveBtn;

    }

    private String generatePassword(){
        Random rand = new Random();
        int length = rand.nextInt(5)+6;
        RandomString s = new RandomString();
        String password = s.make(length);
        Notification.show("A felhasználó jelszava: "+password);
        return new BCryptPasswordEncoder().encode(password);
    }
    private void addButtonBar(Grid<UserEntity> grid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteBtn.addClickListener(buttonClickEvent -> {
            service.remove(selectedUser);
            Notification.show("Sikeres törlés");
            selectedUser = null;
            grid.setItems(service.getAll());
            form.setVisible(false);

        });
        deleteBtn.setEnabled(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            selectedUser = new UserEntity();
            binder.setBean(selectedUser);
            form.setVisible(true);

        });
        horizontalLayout.add(deleteBtn);
        horizontalLayout.add(addBtn);
        add(horizontalLayout);
    }
}