package shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Item {

    private final String name;
    private final BigDecimal unitPrice;
    private final int quantity;

    public Item(String name, BigDecimal unitPrice, int quantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
