package com.example.dvzdemo.ui.appUser;

import java.util.List;

import com.example.dvzdemo.appUser.v1.AppUserRequest;
import com.example.dvzdemo.appUser.v1.AppUserResponse;
import com.example.dvzdemo.ui.RefreshableView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class AppUserCrudView extends VerticalLayout implements RefreshableView {

    private final AppUserApiClient appUserApiClient;
    private final Grid<AppUserResponse> grid = new Grid<>(AppUserResponse.class, false);
    private final Binder<AppUserResponse> binder = new Binder<>(AppUserResponse.class);
    private final TextField userName = new TextField("Username");
    private final TextField givenName = new TextField("Given Name");
    private final TextField surname = new TextField("Surname");
    private final TextField email = new TextField("Email");
    private final PasswordField password = new PasswordField("Password");
    private AppUserResponse selectedUser;

    public AppUserCrudView(AppUserApiClient appUserApiClient) {
        this.appUserApiClient = appUserApiClient;

        setSizeFull(); // view auf full setzen (hight & width)
        configureGrid(); // tabelle konfigurieren (Spalten + Klick-Handler)
        FormLayout form = buildForm();
        configureBinder();
        VerticalLayout formColumn = new VerticalLayout(new H3("AppUser CRUD"), form); // Vertikal Layout erzeugen mit
                                                                                      // Überschrift und Formular aus
                                                                                      // voriger Zeile
        formColumn.setWidth("400px");
        formColumn.setFlexShrink(0);
        formColumn.setPadding(false);
        formColumn.setSpacing(true);

        grid.setSizeFull(); // Tabelle auf volle Breite und Höhe setzen

        HorizontalLayout layout = new HorizontalLayout(formColumn, grid); // horizontales Layout besteht aus formColumn,
                                                                          // grid)
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.expand(grid);

        add(layout); // horizontal layout zu CustomerOrderCrudView hinzufügen
        expand(layout); // Layout soll den gesamten Platz in View ausfüllen
    }

    // Formular links erzeugen
    private FormLayout buildForm() {
        FormLayout formLayout = new FormLayout();

        Button save = new Button("Save", event -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button clear = new Button("Clear", event -> clearForm());
        Button delete = new Button("Delete", event -> deleteSelected());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // deklarierte Textfelder werden für Erzeugung des Formulars genutzt
        formLayout.add(userName, givenName, surname, email, password, new HorizontalLayout(save, clear, delete));
        password.setRevealButtonVisible(false);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("900px", 2));
        return formLayout;
    }

    private void configureBinder() {
        binder.forField(userName)
                .asRequired("Username required")
                .withValidator(name -> !userNameExists(name), "Username exists already")
                .withValidator(new StringLengthValidator("Username must be between 5 and 64 characters", 5, 64))
                .bind(AppUserResponse::username, null);

        binder.forField(givenName)
                .asRequired("Given Name required")
                .bind(AppUserResponse::givenName, null);

        binder.forField(surname)
                .asRequired("Surname required")
                .bind(AppUserResponse::surname, null);

        binder.forField(email)
                .asRequired("Email required")
                .withValidator(mailAddress -> !emailExists(mailAddress), "Email exists already")
                .withValidator(email -> !email.matches(".*[äöüÄÖÜ].*"), "Umlauts are not allowed")
                .withValidator(new EmailValidator("Invalid Email"))
                .bind(AppUserResponse::email, null);

        binder.forField(password)
                .asRequired("Password required")
                .withValidator(new StringLengthValidator("Password must be at least 8 characters", 8, null))
                .bind(AppUserResponse::password, null);
    }

    private void configureGrid() {
        grid.addColumn(AppUserResponse::id).setHeader("ID").setAutoWidth(true);
        grid.addColumn(AppUserResponse::username).setHeader("Username").setAutoWidth(true);
        grid.addColumn(AppUserResponse::givenName).setHeader("Given Name").setAutoWidth(true);
        grid.addColumn(AppUserResponse::surname).setHeader("Surname").setAutoWidth(true);
        grid.addColumn(AppUserResponse::email).setHeader("Email").setAutoWidth(true);
        grid.addColumn(user -> "••••••••••••••").setHeader("Password").setAutoWidth(true);
        grid.setHeight("420px");

        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));
        // es darf nur ein Eintrag ausgewählt sein
        // Listener wird bei jeder Änderung ausgeführt
        // Lambda implementiert was je Änderung ausgeführt wird
        // populateForm befüllt form mit Werten
        // event = Änderungsereignis
        // event.getValue() = ResponseObjekt
    }

    private void deleteSelected() {
        if (selectedUser == null) {
            Notification.show("Please select a user");
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete " + selectedUser.username() + " ?");
        dialog.setText("Are you sure you want to delete this user?");

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            performDelete();
        });

        dialog.setCancelText("Cancel");
        dialog.setCancelable(true);

        dialog.open();
    }

    private void performDelete() {
        try {
            appUserApiClient.delete(selectedUser.id());
            Notification.show("User deleted");
            refreshView();
            clearForm();
        } catch (RuntimeException e) {
            Notification.show("Delete user failed: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void clearForm() {
        selectedUser = null;
        userName.clear();
        givenName.clear();
        surname.clear();
        email.clear();
        password.clear();
        grid.deselectAll();
        binder.readBean(null);
    }

    private boolean userNameExists(String name) {
        name = userName.getValue();

        if (selectedUser != null) {
            return false;
        } else {
            List<String> allUserNames = appUserApiClient.findAll()
                    .stream()
                    .map(u -> u.username())
                    .toList();

            return allUserNames.contains(name);
        }
    }

    private boolean emailExists(String mailAddress) {
        mailAddress = email.getValue();

        if (selectedUser != null) {
            return false;
        } else {
            List<String> allEmails = appUserApiClient.findAll()
                    .stream()
                    .map(u -> u.email())
                    .toList();

            return allEmails.contains(mailAddress);
        }
    }

    private void save() {
        if (binder.validate().isOk()) {

            AppUserRequest request = new AppUserRequest(
                    userName.getValue(),
                    givenName.getValue(),
                    surname.getValue(),
                    email.getValue(),
                    password.getValue());

            try {
                if (selectedUser == null) {
                    appUserApiClient.create(request);
                    Notification.show("User created");
                } else {
                    appUserApiClient.update(selectedUser.id(), request);
                    Notification.show("User updated");
                }
                refreshView();
                clearForm();
            } catch (RuntimeException exception) {
                Notification.show("Save user failed: " + exception.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        }

    }

    private void populateForm(AppUserResponse user) {
        selectedUser = user;
        if (user == null) {
            userName.setReadOnly(false);
            clearForm();
            return;
        }

        userName.setValue(user.username());
        userName.setReadOnly(true);
        givenName.setValue(user.givenName());
        surname.setValue(user.surname());
        email.setValue(user.email());
        password.setValue(user.password());
    }

    @Override
    public void refreshView() {
        grid.setItems(appUserApiClient.findAll());
    }
}
