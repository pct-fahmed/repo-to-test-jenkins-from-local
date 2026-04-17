package com.example.dvzdemo.ui.product;

import com.example.dvzdemo.commerce.product.v1.ProductRequest;
import com.example.dvzdemo.commerce.product.v1.ProductResponse;
import com.example.dvzdemo.ui.RefreshableView;
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

    private final ProductApiClient productApiClient;
    private final Grid<ProductResponse> grid = new Grid<>(ProductResponse.class, false);
    private final TextField sku = new TextField("SKU");
    private final TextField name = new TextField("Name");
    private final TextArea description = new TextArea("Description");
    private final BigDecimalField price = new BigDecimalField("Price");
    private ProductResponse selectedProduct;

    public ProductCrudView(ProductApiClient productApiClient) {
        this.productApiClient = productApiClient;

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
        grid.addColumn(ProductResponse::id).setHeader("ID").setAutoWidth(true);
        grid.addColumn(ProductResponse::sku).setHeader("SKU").setAutoWidth(true);
        grid.addColumn(ProductResponse::name).setHeader("Name").setAutoWidth(true);
        grid.addColumn(ProductResponse::description).setHeader("Description").setFlexGrow(1);
        grid.addColumn(product -> product.price().toPlainString()).setHeader("Price").setAutoWidth(true);
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
        ProductRequest request = new ProductRequest(
            sku.getValue(),
            name.getValue(),
            description.getValue(),
            price.getValue()
        );
        try {
            if (selectedProduct == null) {
                productApiClient.create(request);
                Notification.show("Product created");
            } else {
                productApiClient.update(selectedProduct.id(), request);
                Notification.show("Product updated");
            }
            refreshView();
            clearForm();
        } catch (RuntimeException exception) {
            Notification.show("Product operation failed: " + exception.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void deleteSelected() {
        if (selectedProduct == null) {
            Notification.show("Select a product first");
            return;
        }
        try {
            productApiClient.delete(selectedProduct.id());
            Notification.show("Product deleted");
            refreshView();
            clearForm();
        } catch (RuntimeException exception) {
            Notification.show(exception.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void populateForm(ProductResponse product) {
        selectedProduct = product;
        if (product == null) {
            clearForm();
            return;
        }
        sku.setValue(product.sku());
        name.setValue(product.name());
        description.setValue(product.description() == null ? "" : product.description());
        price.setValue(product.price());
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
        grid.setItems(productApiClient.findAll());
    }
}
