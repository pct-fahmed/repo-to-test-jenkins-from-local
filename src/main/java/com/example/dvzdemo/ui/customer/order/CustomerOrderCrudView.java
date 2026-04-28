package com.example.dvzdemo.ui.customer.order;

import com.example.dvzdemo.commerce.customerorder.OrderStatus;
import com.example.dvzdemo.commerce.customerorder.v1.CustomerOrderRequest;
import com.example.dvzdemo.commerce.customerorder.v1.CustomerOrderResponse;
import com.example.dvzdemo.ui.RefreshableView;
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

    private final CustomerOrderApiClient customerOrderApiClient;
    private final Grid<CustomerOrderResponse> grid = new Grid<>(CustomerOrderResponse.class, false);
    private final TextField orderNumber = new TextField("Order Number");
    private final TextField customerName = new TextField("Customer Name");
    private final ComboBox<OrderStatus> status = new ComboBox<>("Status");
    private final DateTimePicker orderedAt = new DateTimePicker("Ordered At");
    private CustomerOrderResponse selectedOrder;

    public CustomerOrderCrudView(CustomerOrderApiClient customerOrderApiClient) {
        this.customerOrderApiClient = customerOrderApiClient;

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
        grid.addColumn(CustomerOrderResponse::id).setHeader("ID").setAutoWidth(true);
        grid.addColumn(CustomerOrderResponse::orderNumber).setHeader("Order Number").setAutoWidth(true);
        grid.addColumn(CustomerOrderResponse::customerName).setHeader("Customer").setAutoWidth(true);
        grid.addColumn(CustomerOrderResponse::status).setHeader("Status").setAutoWidth(true);
        grid.addColumn(CustomerOrderResponse::orderedAt).setHeader("Ordered At").setAutoWidth(true);
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
        CustomerOrderRequest request = new CustomerOrderRequest(
            orderNumber.getValue(),
            customerName.getValue(),
            status.getValue(),
            orderedAt.getValue()
        );
        try {
            if (selectedOrder == null) {
                customerOrderApiClient.create(request);
                Notification.show("Customer order created");
            } else {
                customerOrderApiClient.update(selectedOrder.id(), request);
                Notification.show("Customer order updated");
            }
            refreshView();
            clearForm();
        } catch (RuntimeException exception) {
            Notification.show("Customer order operation failed: " + exception.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void deleteSelected() {
        if (selectedOrder == null) {
            Notification.show("Select a customer order first");
            return;
        }
        try {
            customerOrderApiClient.delete(selectedOrder.id());
            Notification.show("Customer order deleted");
            refreshView();
            clearForm();
        } catch (RuntimeException exception) {
            Notification.show("Customer order delete failed: " + exception.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void populateForm(CustomerOrderResponse order) {
        selectedOrder = order;
        if (order == null) {
            clearForm();
            return;
        }
        orderNumber.setValue(order.orderNumber());
        customerName.setValue(order.customerName());
        status.setValue(order.status());
        orderedAt.setValue(order.orderedAt());
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
        grid.setItems(customerOrderApiClient.findAll());
    }
}
