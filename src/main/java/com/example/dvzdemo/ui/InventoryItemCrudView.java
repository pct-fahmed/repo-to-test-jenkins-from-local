package com.example.dvzdemo.ui;

import com.example.dvzdemo.commerce.inventory.Inventory;
import com.example.dvzdemo.commerce.inventory.InventoryService;
import com.example.dvzdemo.commerce.inventoryitem.InventoryItem;
import com.example.dvzdemo.commerce.inventoryitem.InventoryItemService;
import com.example.dvzdemo.commerce.product.Product;
import com.example.dvzdemo.commerce.product.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class InventoryItemCrudView extends VerticalLayout implements RefreshableView {

    private final InventoryItemService inventoryItemService;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final Grid<InventoryItem> grid = new Grid<>(InventoryItem.class, false);
    private final ComboBox<Inventory> inventory = new ComboBox<>("Inventory");
    private final ComboBox<Product> product = new ComboBox<>("Product");
    private final IntegerField quantityOnHand = new IntegerField("Quantity On Hand");
    private final IntegerField reorderLevel = new IntegerField("Reorder Level");
    private InventoryItem selectedInventoryItem;

    public InventoryItemCrudView(
        InventoryItemService inventoryItemService,
        InventoryService inventoryService,
        ProductService productService
    ) {
        this.inventoryItemService = inventoryItemService;
        this.inventoryService = inventoryService;
        this.productService = productService;

        setSizeFull();
        configureGrid();
        FormLayout form = buildForm();
        VerticalLayout formColumn = new VerticalLayout(new H3("Inventory Item CRUD"), form);
        formColumn.setWidth("420px");
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
        grid.addColumn(InventoryItem::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(item -> item.getInventory().getCode()).setHeader("Inventory").setAutoWidth(true);
        grid.addColumn(item -> item.getProduct().getSku()).setHeader("Product").setAutoWidth(true);
        grid.addColumn(InventoryItem::getQuantityOnHand).setHeader("On Hand").setAutoWidth(true);
        grid.addColumn(InventoryItem::getReorderLevel).setHeader("Reorder Level").setAutoWidth(true);
        grid.setHeight("420px");
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));
    }

    private FormLayout buildForm() {
        FormLayout formLayout = new FormLayout();
        inventory.setItemLabelGenerator(item -> item.getCode() + " - " + item.getName());
        product.setItemLabelGenerator(item -> item.getSku() + " - " + item.getName());

        Button save = new Button("Save", event -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button clear = new Button("Clear", event -> clearForm());
        Button delete = new Button("Delete", event -> deleteSelected());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        formLayout.add(inventory, product, quantityOnHand, reorderLevel, new HorizontalLayout(save, clear, delete));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("960px", 2));
        return formLayout;
    }

    private void save() {
        if (selectedInventoryItem == null) {
            inventoryItemService.create(
                inventory.getValue().getId(),
                product.getValue().getId(),
                quantityOnHand.getValue(),
                reorderLevel.getValue()
            );
            Notification.show("Inventory item created");
        } else {
            inventoryItemService.update(
                selectedInventoryItem.getId(),
                inventory.getValue().getId(),
                product.getValue().getId(),
                quantityOnHand.getValue(),
                reorderLevel.getValue()
            );
            Notification.show("Inventory item updated");
        }
        refreshView();
        clearForm();
    }

    private void deleteSelected() {
        if (selectedInventoryItem == null) {
            Notification.show("Select an inventory item first");
            return;
        }
        inventoryItemService.delete(selectedInventoryItem.getId());
        Notification.show("Inventory item deleted");
        refreshView();
        clearForm();
    }

    private void populateForm(InventoryItem item) {
        selectedInventoryItem = item;
        if (item == null) {
            clearForm();
            return;
        }
        inventory.setValue(item.getInventory());
        product.setValue(item.getProduct());
        quantityOnHand.setValue(item.getQuantityOnHand());
        reorderLevel.setValue(item.getReorderLevel());
    }

    private void clearForm() {
        selectedInventoryItem = null;
        inventory.clear();
        product.clear();
        quantityOnHand.clear();
        reorderLevel.clear();
        grid.deselectAll();
    }

    @Override
    public void refreshView() {
        inventory.setItems(inventoryService.findAll());
        product.setItems(productService.findAll());
        grid.setItems(inventoryItemService.findAll());
    }
}
