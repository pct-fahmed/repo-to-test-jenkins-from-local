package com.example.dvzdemo.ui;

import com.example.dvzdemo.ui.customer.order.CustomerOrderCrudView;
import com.example.dvzdemo.ui.inventory.InventoryCrudView;
import com.example.dvzdemo.ui.inventory.item.InventoryItemCrudView;
import com.example.dvzdemo.ui.order.item.OrderItemCrudView;
import com.example.dvzdemo.ui.product.ProductCrudView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedHashMap;
import java.util.Map;

@Route("")
@PageTitle("Commerce Admin")
public class MainView extends VerticalLayout {

    private final VerticalLayout content = new VerticalLayout();
    private final Map<Tab, Component> views = new LinkedHashMap<>();

    public MainView(
        ProductCrudView productCrudView,
        InventoryCrudView inventoryCrudView,
        CustomerOrderCrudView customerOrderCrudView,
        InventoryItemCrudView inventoryItemCrudView,
        OrderItemCrudView orderItemCrudView
    ) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Commerce Admin");

        Tab products = new Tab("Products");
        Tab inventories = new Tab("Inventories");
        Tab customerOrders = new Tab("Customer Orders");
        Tab inventoryItems = new Tab("Inventory Items");
        Tab orderItems = new Tab("Order Items");

        views.put(products, productCrudView);
        views.put(inventories, inventoryCrudView);
        views.put(customerOrders, customerOrderCrudView);
        views.put(inventoryItems, inventoryItemCrudView);
        views.put(orderItems, orderItemCrudView);

        Tabs tabs = new Tabs(products, inventories, customerOrders, inventoryItems, orderItems);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(event -> showView(event.getSelectedTab()));

        content.setPadding(false);
        content.setSpacing(false);
        content.setSizeFull();

        add(title, tabs, content);
        expand(content);

        showView(products);
    }

    private void showView(Tab tab) {
        content.removeAll();
        Component component = views.get(tab);
        if (component instanceof RefreshableView refreshableView) {
            refreshableView.refreshView();
        }
        content.add(component);
        content.expand(component);
    }
}
