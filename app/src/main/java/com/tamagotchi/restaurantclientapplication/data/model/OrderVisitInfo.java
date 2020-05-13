package com.tamagotchi.restaurantclientapplication.data.model;

import java.util.Date;

/**
 * Класс, содержащий информацию о посещении ресторана.
 */
public class OrderVisitInfo {
    private Date visitTime;
    private int numberOfVisitors;

    /**
     * Конструктор класса.
     * @param visitTime время посещения (во сколько придет клиент).
     * @param numberOfVisitors количество посетителей.
     */
    public OrderVisitInfo(Date visitTime, int numberOfVisitors) {

        this.visitTime = visitTime;
        this.numberOfVisitors = numberOfVisitors;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public int getNumberOfVisitors() {
        return numberOfVisitors;
    }
}
