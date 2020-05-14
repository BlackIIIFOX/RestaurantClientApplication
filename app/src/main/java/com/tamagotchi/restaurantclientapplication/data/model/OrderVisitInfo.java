package com.tamagotchi.restaurantclientapplication.data.model;

import java.util.Calendar;

/**
 * Класс, содержащий информацию о посещении ресторана.
 */
public class OrderVisitInfo {
    private Calendar visitTime;
    private int numberOfVisitors;

    /**
     * Конструктор класса.
     * @param visitTime время посещения (во сколько придет клиент).
     * @param numberOfVisitors количество посетителей.
     */
    public OrderVisitInfo(Calendar visitTime, int numberOfVisitors) {
        this.visitTime = visitTime;
        this.numberOfVisitors = numberOfVisitors;
    }

    public Calendar getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Calendar visitTime) {
        this.visitTime = visitTime;
    }

    public int getNumberOfVisitors() {
        return numberOfVisitors;
    }

    public void setNumberOfVisitors(int numberOfVisitors) {
        this.numberOfVisitors = numberOfVisitors;
    }
}
