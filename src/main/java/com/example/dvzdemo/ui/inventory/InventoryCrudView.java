package com.example.dvzdemo.ui.inventory;

import com.example.dvzdemo.commerce.inventory.Inventory;
import com.example.dvzdemo.commerce.inventory.InventoryService;
import com.example.dvzdemo.ui.RefreshableView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class InventoryCrudView extends VerticalLayout implements RefreshableView {

    private final InventoryService inventoryService;
    private final Grid<Inventory> grid = new Grid<>(Inventory.class, false);
    private final TextField code = new TextField("Code");
    private final TextField name = new TextField("Name");
    private final TextField location = new TextField("Location");
    private Inventory selectedInventory;

    public InventoryCrudView(InventoryService inventoryService) {
        this.inventoryService = inventoryService;

        setSizeFull();
        configureGrid();
        FormLayout form = buildForm();
        VerticalLayout formColumn = new VerticalLayout(new H3("Inventory CRUD"), form);
        formColumn.setWidth("360px");
        formColumn.setFlexShrink(0);
        formColumn.setPadding(false);
        formColumn.setSpacing(true);

        grid.setSizeFull();

        HorizontalLayout layout = new HorizontalLayout(formColumn, grid);
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.expand(grid);

        add(layout);
        expand(layout);
    }

    private void configureGrid() {
        grid.addColumn(Inventory::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Inventory::getCode).setHeader("Code").setAutoWidth(true);
        grid.addColumn(Inventory::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Inventory::getLocation).setHeader("Location").setAutoWidth(true);
        grid.setHeight("420px");
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));
    }

    private FormLayout buildForm() {
        FormLayout formLayout = new FormLayout();

        Button save = new Button("Save", event -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button clear = new Button("Clear", event -> clearForm());
        Button delete = new Button("Delete", event -> deleteSelected());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        formLayout.add(code, name, location, new HorizontalLayout(save, clear, delete));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        return formLayout;
    }

    private void save() {
        if (selectedInventory == null) {
            inventoryService.create(code.getValue(), name.getValue(), location.getValue());
            Notification.show("Inventory created");
        } else {
            inventoryService.update(selectedInventory.getId(), code.getValue(), name.getValue(), location.getValue());
            Notification.show("Inventory updated");
        }
        refreshView();
        clearForm();
    }

    private void deleteSelected() {
        if (selectedInventory == null) {
            Notification.show("Select an inventory first");
            return;
        }
        inventoryService.delete(selectedInventory.getId());
        Notification.show("Inventory deleted");
        refreshView();
        clearForm();
    }

    private void populateForm(Inventory inventory) {
        selectedInventory = inventory;
        if (inventory == null) {
            clearForm();
            return;
        }
        code.setValue(inventory.getCode());
        name.setValue(inventory.getName());
        location.setValue(inventory.getLocation());
    }

    private void clearForm() {
        selectedInventory = null;
        code.clear();
        name.clear();
        location.clear();
        grid.deselectAll();
    }

    @Override
    public void refreshView() {
        grid.setItems(inventoryService.findAll());
    }
}
