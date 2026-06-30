package shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shop.config.AppConfig;


public class Order {

    private static int counter = 1000;

    private final String id;
    private final List<Item> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.NEW;
    private String paymentLabel;
    private String paymentReference;

    public Order() {
        this.id = "ORD-" + (++counter);
    }

    public String getId() {
        return id;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal subtotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Item item : items) {
            sum = sum.add(item.lineTotal());
        }
        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal tax() {
        BigDecimal rate = AppConfig.getInstance().getTaxRate();
        return subtotal().multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal total() {
        return subtotal().add(tax()).setScale(2, RoundingMode.HALF_UP);
    }

    public void markPaid(String label, String reference) {
        this.status = OrderStatus.PAID;
        this.paymentLabel = label;
        this.paymentReference = reference;
    }

    public String getPaymentLabel() {
        return paymentLabel;
    }

    public String getPaymentReference() {
        return paymentReference;
    }
}
