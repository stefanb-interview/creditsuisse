package com.creditsuisse.model;

import java.util.Objects;

public class Order {
    private String userid;

    private double quantity;

    private double price;

    private OrderType type;

    public Order(String userid, double quantity, double price, OrderType type) {
        this.userid = userid;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.quantity, quantity) == 0 &&
                Double.compare(order.price, price) == 0 &&
                Objects.equals(userid, order.userid) &&
                type == order.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, quantity, price, type);
    }
}
