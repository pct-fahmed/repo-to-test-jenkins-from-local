package com.example.dvzdemo.ui;

import com.example.dvzdemo.commerce.product.Product;
import com.example.dvzdemo.commerce.product.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ProductCrudView extends VerticalLayout implements RefreshableView {

    private final ProductService productService;
    private final Grid<Product> grid = new Grid<>(Product.class, false);
    private final TextField sku = new TextField("SKU");
    private final TextField name = new TextField("Name");
    private final TextArea description = new TextArea("Description");
    private final BigDecimalField price = new BigDecimalField("Price");
    private Product selectedProduct;

    public ProductCrudView(ProductService productService) {
        this.productService = productService;

        setSizeFull();
        configureGrid();
        FormLayout form = buildForm();
        VerticalLayout formColumn = new VerticalLayout(new H3("Product CRUD"), form);
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
        grid.addColumn(Product::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Product::getSku).setHeader("SKU").setAutoWidth(true);
        grid.addColumn(Product::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Product::getDescription).setHeader("Description").setFlexGrow(1);
        grid.addColumn(product -> product.getPrice().toPlainString()).setHeader("Price").setAutoWidth(true);
        grid.setHeight("420px");
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));
    }

    private FormLayout buildForm() {
        FormLayout formLayout = new FormLayout();
        description.setMaxLength(500);

        Button save = new Button("Save", event -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button clear = new Button("Clear", event -> clearForm());
        Button delete = new Button("Delete", event -> deleteSelected());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout actions = new HorizontalLayout(save, clear, delete);
        formLayout.add(sku, name, description, price, actions);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        return formLayout;
    }

    private void save() {
        if (selectedProduct == null) {
            productService.create(sku.getValue(), name.getValue(), description.getValue(), price.getValue());
            Notification.show("Product created");
        } else {
            productService.update(selectedProduct.getId(), sku.getValue(), name.getValue(), description.getValue(), price.getValue());
            Notification.show("Product updated");
        }
        refreshView();
        clearForm();
    }

    private void deleteSelected() {
        if (selectedProduct == null) {
            Notification.show("Select a product first");
            return;
        }
        productService.delete(selectedProduct.getId());
        Notification.show("Product deleted");
        refreshView();
        clearForm();
    }

    private void populateForm(Product product) {
        selectedProduct = product;
        if (product == null) {
            clearForm();
            return;
        }
        sku.setValue(product.getSku());
        name.setValue(product.getName());
        description.setValue(product.getDescription() == null ? "" : product.getDescription());
        price.setValue(product.getPrice());
    }

    private void clearForm() {
        selectedProduct = null;
        sku.clear();
        name.clear();
        description.clear();
        price.clear();
        grid.deselectAll();
    }

    @Override
    public void refreshView() {
        grid.setItems(productService.findAll());
    }
}
