package com.creditsuisse.service;

import com.creditsuisse.model.Order;
import com.creditsuisse.model.OrderType;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.creditsuisse.model.OrderType.BUY;
import static com.creditsuisse.model.OrderType.SELL;

public class LiveOrderBoard implements OrderBoard {
    private List<Order> orders = new CopyOnWriteArrayList<>();

    @Override
    public void register(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Can't register a null order");
        }

        orders.add(order);
    }

    @Override
    public void cancel(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Can't cancel a null order");
        }

        orders.remove(order);
    }

    @Override
    public Map<Double, Double> summary() {
        Map<Double, Double> result = new LinkedHashMap<>();

        result.putAll(getSummary(SELL));
        result.putAll(getSummary(BUY));

        return result;
    }

    private Map<Double, Double> getSummary(OrderType orderType) {
        final Comparator<Double> comparator;

        switch (orderType) {
            case BUY:
                comparator = Comparator.comparingDouble((Double p) -> p).reversed();
                break;
            case SELL:
                comparator = Comparator.comparingDouble((Double p) -> p);
                break;
            default:
                throw new IllegalArgumentException("Order should be SELL or BUY");
        }

        return orders.stream()
                .filter(o -> o.getType().equals(orderType))
                .collect(Collectors.groupingBy(
                        Order::getPrice,
                        () -> new TreeMap<>(comparator),
                        Collectors.summingDouble(Order::getQuantity)
                ));
    }
}
