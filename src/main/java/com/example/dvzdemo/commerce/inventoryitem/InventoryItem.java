package com.example.dvzdemo.commerce.inventoryitem;

import com.example.dvzdemo.commerce.inventory.Inventory;
import com.example.dvzdemo.commerce.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_item")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity_on_hand", nullable = false)
    private Integer quantityOnHand;

    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel;

    protected InventoryItem() {
    }

    public InventoryItem(Inventory inventory, Product product, Integer quantityOnHand, Integer reorderLevel) {
        this.inventory = inventory;
        this.product = product;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;
    }

    public void update(Inventory inventory, Product product, Integer quantityOnHand, Integer reorderLevel) {
        this.inventory = inventory;
        this.product = product;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;
    }

    public Long getId() {
        return id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }
}
