package ru.ylab.hw1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Order {
    public enum OrderStatus {
        PENDING, COMPLETED, CANCELLED
    }

    private static int idCounter = 1;
    private int id;
    private User client;
    private Car car;
    private OrderStatus status;
    private Date orderDate;

    public Order(User client, Car car) {
        this.id = idCounter++;
        this.client = client;
        this.car = car;
        this.status = OrderStatus.PENDING;
        this.orderDate = new Date();
    }
}
