package com.creditsuisse;

import com.creditsuisse.model.Order;
import com.creditsuisse.service.LiveOrderBoard;
import com.creditsuisse.service.OrderBoard;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.creditsuisse.model.OrderType.BUY;
import static com.creditsuisse.model.OrderType.SELL;
import static org.junit.jupiter.api.Assertions.*;

class LiveOrderBoardTest {

    private final String ONE_USER = "oneUser";

    private final double A_PRICE = 1;

    private final double ANOTHER_PRICE = 1.5;

    private final double YET_ANOTHER_PRICE = 2;

    private final double A_QUANTITY = 2;

    private final double ANOTHER_QUANTITY = 2.5;


    @Test
    void whenRegisteringOneOrderThenOrderIsPresentInSummary() {
        OrderBoard orderBoard = new LiveOrderBoard();
        Order order = new Order(ONE_USER, A_QUANTITY, A_PRICE, BUY);

        orderBoard.register(order);

        assertEquals(1, orderBoard.summary().entrySet().size());
        assertTrue(orderBoard.summary().containsKey(order.getPrice()));
        assertTrue(orderBoard.summary().containsValue(order.getQuantity()));
    }

    @Test
    void whenRegisteringAnInvalidOrderWeGetAnException() {
        OrderBoard orderBoard = new LiveOrderBoard();
        assertThrows(IllegalArgumentException.class, () -> orderBoard.register(null));
    }

    @Test
    void whenTwoOrdersHavingSamePriceThenSummaryContainsJustOneRecord() {
        OrderBoard orderBoard = new LiveOrderBoard();
        Order anOrder = new Order(ONE_USER, A_QUANTITY, A_PRICE, BUY);
        Order anotherOrder = new Order(ONE_USER, ANOTHER_QUANTITY, A_PRICE, BUY);

        orderBoard.register(anOrder);
        orderBoard.register(anotherOrder);

        assertEquals(1, orderBoard.summary().entrySet().size());
        assertTrue(orderBoard.summary().containsKey(anOrder.getPrice()));
    }

    @Test
    void whenTwoOrdersHavingSamePriceThenSummaryContainsSumOfTheirQuantities() {
        OrderBoard orderBoard = new LiveOrderBoard();
        Order anOrder = new Order(ONE_USER, A_QUANTITY, A_PRICE, BUY);
        Order anotherOrderHavingSamePrice = new Order(ONE_USER, ANOTHER_QUANTITY, A_PRICE, BUY);

        orderBoard.register(anOrder);
        orderBoard.register(anotherOrderHavingSamePrice);

        assertEquals(1, orderBoard.summary().entrySet().size());
        assertTrue(orderBoard.summary().containsValue(anOrder.getQuantity() + anotherOrderHavingSamePrice.getQuantity()));
    }

    @Test
    void whenHavingTwoDifferentOrdersAsPriceThenSummaryContainsEach() {
        OrderBoard orderBoard = new LiveOrderBoard();
        Order anOrder = new Order(ONE_USER, A_QUANTITY, A_PRICE, BUY);
        Order anotherOrder = new Order(ONE_USER, ANOTHER_QUANTITY, ANOTHER_PRICE, BUY);

        orderBoard.register(anOrder);
        orderBoard.register(anotherOrder);

        assertEquals(2, orderBoard.summary().entrySet().size());

        assertEquals(anOrder.getQuantity(), orderBoard.summary().get(anOrder.getPrice()));
        assertEquals(anotherOrder.getQuantity(), orderBoard.summary().get(anotherOrder.getPrice()));
    }

    @Test
    void whenMultipleOrdersSummarySortsSellOrdersAscendingByPrice() {
        OrderBoard orderBoard = new LiveOrderBoard();

        Order firstOrder = new Order(ONE_USER, A_QUANTITY, YET_ANOTHER_PRICE, SELL);
        Order secondOrder = new Order(ONE_USER, ANOTHER_QUANTITY, ANOTHER_PRICE, SELL);
        Order thirdOrder = new Order(ONE_USER, ANOTHER_QUANTITY, A_PRICE, SELL);

        orderBoard.register(firstOrder);
        orderBoard.register(secondOrder);
        orderBoard.register(thirdOrder);

        Map<Double, Double> summary = orderBoard.summary();

        assertArrayEquals(summary.keySet().toArray(), new Double[]{A_PRICE, ANOTHER_PRICE, YET_ANOTHER_PRICE});
    }

    @Test
    void whenMultipleOrdersSummarySortsBuyOrdersDescendingByPrice() {
        OrderBoard orderBoard = new LiveOrderBoard();

        Order firstOrder = new Order(ONE_USER, A_QUANTITY, A_PRICE, BUY);
        Order secondOrder = new Order(ONE_USER, ANOTHER_QUANTITY, ANOTHER_PRICE, BUY);
        Order thirdOrder = new Order(ONE_USER, ANOTHER_QUANTITY, YET_ANOTHER_PRICE, BUY);

        orderBoard.register(firstOrder);
        orderBoard.register(secondOrder);
        orderBoard.register(thirdOrder);

        Map<Double, Double> summary = orderBoard.summary();

        assertArrayEquals(summary.keySet().toArray(), new Double[]{YET_ANOTHER_PRICE, ANOTHER_PRICE, A_PRICE});
    }

    @Test
    void whenOrderCanceledThenOrderNotPresentInSummary() {
        OrderBoard orderBoard = new LiveOrderBoard();
        Order order = new Order(ONE_USER, A_QUANTITY, A_PRICE, BUY);

        orderBoard.register(order);
        orderBoard.cancel(order);

        assertEquals(0, orderBoard.summary().entrySet().size());
    }

    @Test
    void whenInvalidOrderCanceledThenIllegalArgumentExceptionIsThrown() {
        OrderBoard orderBoard = new LiveOrderBoard();
        assertThrows(IllegalArgumentException.class, () -> orderBoard.cancel(null));
    }
}
