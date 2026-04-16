package com.example.dvzdemo.ui;

import com.example.dvzdemo.commerce.customerorder.CustomerOrder;
import com.example.dvzdemo.commerce.customerorder.CustomerOrderService;
import com.example.dvzdemo.commerce.customerorder.OrderStatus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
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
public class CustomerOrderCrudView extends VerticalLayout implements RefreshableView {

    private final CustomerOrderService customerOrderService;
    private final Grid<CustomerOrder> grid = new Grid<>(CustomerOrder.class, false);
    private final TextField orderNumber = new TextField("Order Number");
    private final TextField customerName = new TextField("Customer Name");
    private final ComboBox<OrderStatus> status = new ComboBox<>("Status");
    private final DateTimePicker orderedAt = new DateTimePicker("Ordered At");
    private CustomerOrder selectedOrder;

    public CustomerOrderCrudView(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;

        setSizeFull();
        configureGrid();
        FormLayout form = buildForm();
        VerticalLayout formColumn = new VerticalLayout(new H3("Customer Order CRUD"), form);
        formColumn.setWidth("400px");
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
        grid.addColumn(CustomerOrder::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(CustomerOrder::getOrderNumber).setHeader("Order Number").setAutoWidth(true);
        grid.addColumn(CustomerOrder::getCustomerName).setHeader("Customer").setAutoWidth(true);
        grid.addColumn(CustomerOrder::getStatus).setHeader("Status").setAutoWidth(true);
        grid.addColumn(CustomerOrder::getOrderedAt).setHeader("Ordered At").setAutoWidth(true);
        grid.setHeight("420px");
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));
    }

    private FormLayout buildForm() {
        FormLayout formLayout = new FormLayout();
        status.setItems(OrderStatus.values());

        Button save = new Button("Save", event -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button clear = new Button("Clear", event -> clearForm());
        Button delete = new Button("Delete", event -> deleteSelected());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        formLayout.add(orderNumber, customerName, status, orderedAt, new HorizontalLayout(save, clear, delete));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("900px", 2));
        return formLayout;
    }

    private void save() {
        if (selectedOrder == null) {
            customerOrderService.create(orderNumber.getValue(), customerName.getValue(), status.getValue(), orderedAt.getValue());
            Notification.show("Customer order created");
        } else {
            customerOrderService.update(selectedOrder.getId(), orderNumber.getValue(), customerName.getValue(), status.getValue(), orderedAt.getValue());
            Notification.show("Customer order updated");
        }
        refreshView();
        clearForm();
    }

    private void deleteSelected() {
        if (selectedOrder == null) {
            Notification.show("Select a customer order first");
            return;
        }
        customerOrderService.delete(selectedOrder.getId());
        Notification.show("Customer order deleted");
        refreshView();
        clearForm();
    }

    private void populateForm(CustomerOrder order) {
        selectedOrder = order;
        if (order == null) {
            clearForm();
            return;
        }
        orderNumber.setValue(order.getOrderNumber());
        customerName.setValue(order.getCustomerName());
        status.setValue(order.getStatus());
        orderedAt.setValue(order.getOrderedAt());
    }

    private void clearForm() {
        selectedOrder = null;
        orderNumber.clear();
        customerName.clear();
        status.clear();
        orderedAt.clear();
        grid.deselectAll();
    }

    @Override
    public void refreshView() {
        grid.setItems(customerOrderService.findAll());
    }
}
