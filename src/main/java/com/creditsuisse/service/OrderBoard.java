package com.creditsuisse.service;

import com.creditsuisse.model.Order;

import java.util.Map;

public interface OrderBoard {
    /**
     * Registers an order
     * @param order order to be registered
     */
    void register(Order order);

    /**
     * Cancels an order
     * @param order order to be canceled
     */
    void cancel(Order order);

    /**
     * Gets a summary of (price, quantity) tuples
     * @return a map containing (price,quantity) tuples
     */
    Map<Double, Double> summary();
}
