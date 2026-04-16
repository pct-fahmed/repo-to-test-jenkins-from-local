package com.example.dvzdemo.ui;

import com.example.dvzdemo.commerce.customerorder.CustomerOrder;
import com.example.dvzdemo.commerce.customerorder.CustomerOrderService;
import com.example.dvzdemo.commerce.orderitem.OrderItem;
import com.example.dvzdemo.commerce.orderitem.OrderItemService;
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
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class OrderItemCrudView extends VerticalLayout implements RefreshableView {

    private final OrderItemService orderItemService;
    private final CustomerOrderService customerOrderService;
    private final ProductService productService;
    private final Grid<OrderItem> grid = new Grid<>(OrderItem.class, false);
    private final ComboBox<CustomerOrder> order = new ComboBox<>("Customer Order");
    private final ComboBox<Product> product = new ComboBox<>("Product");
    private final IntegerField quantity = new IntegerField("Quantity");
    private final BigDecimalField unitPrice = new BigDecimalField("Unit Price");
    private OrderItem selectedOrderItem;

    public OrderItemCrudView(
        OrderItemService orderItemService,
        CustomerOrderService customerOrderService,
        ProductService productService
    ) {
        this.orderItemService = orderItemService;
        this.customerOrderService = customerOrderService;
        this.productService = productService;

        setSizeFull();
        configureGrid();
        FormLayout form = buildForm();
        VerticalLayout formColumn = new VerticalLayout(new H3("Order Item CRUD"), form);
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
        grid.addColumn(OrderItem::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(item -> item.getOrder().getOrderNumber()).setHeader("Order").setAutoWidth(true);
        grid.addColumn(item -> item.getProduct().getSku()).setHeader("Product").setAutoWidth(true);
        grid.addColumn(OrderItem::getQuantity).setHeader("Quantity").setAutoWidth(true);
        grid.addColumn(item -> item.getUnitPrice().toPlainString()).setHeader("Unit Price").setAutoWidth(true);
        grid.setHeight("420px");
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));
    }

    private FormLayout buildForm() {
        FormLayout formLayout = new FormLayout();
        order.setItemLabelGenerator(item -> item.getOrderNumber() + " - " + item.getCustomerName());
        product.setItemLabelGenerator(item -> item.getSku() + " - " + item.getName());

        Button save = new Button("Save", event -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button clear = new Button("Clear", event -> clearForm());
        Button delete = new Button("Delete", event -> deleteSelected());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        formLayout.add(order, product, quantity, unitPrice, new HorizontalLayout(save, clear, delete));
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("960px", 2));
        return formLayout;
    }

    private void save() {
        if (selectedOrderItem == null) {
            orderItemService.create(
                order.getValue().getId(),
                product.getValue().getId(),
                quantity.getValue(),
                unitPrice.getValue()
            );
            Notification.show("Order item created");
        } else {
            orderItemService.update(
                selectedOrderItem.getId(),
                order.getValue().getId(),
                product.getValue().getId(),
                quantity.getValue(),
                unitPrice.getValue()
            );
            Notification.show("Order item updated");
        }
        refreshView();
        clearForm();
    }

    private void deleteSelected() {
        if (selectedOrderItem == null) {
            Notification.show("Select an order item first");
            return;
        }
        orderItemService.delete(selectedOrderItem.getId());
        Notification.show("Order item deleted");
        refreshView();
        clearForm();
    }

    private void populateForm(OrderItem item) {
        selectedOrderItem = item;
        if (item == null) {
            clearForm();
            return;
        }
        order.setValue(item.getOrder());
        product.setValue(item.getProduct());
        quantity.setValue(item.getQuantity());
        unitPrice.setValue(item.getUnitPrice());
    }

    private void clearForm() {
        selectedOrderItem = null;
        order.clear();
        product.clear();
        quantity.clear();
        unitPrice.clear();
        grid.deselectAll();
    }

    @Override
    public void refreshView() {
        order.setItems(customerOrderService.findAll());
        product.setItems(productService.findAll());
        grid.setItems(orderItemService.findAll());
    }
}
