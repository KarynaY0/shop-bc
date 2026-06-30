package shop.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shop.model.Order;

public class OrderRepository {

    private final List<Order> completed = new ArrayList<>();

    public void save(Order order) {
        completed.add(order);
    }

    public List<Order> findAll() {
        return Collections.unmodifiableList(completed);
    }

    public int count() {
        return completed.size();
    }
}
