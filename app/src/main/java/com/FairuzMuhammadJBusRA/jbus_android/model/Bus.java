package com.FairuzMuhammadJBusRA.jbus_android.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bus extends Serializable {
    public int accountId;
    public String name;
    public List<Facility> facilities;
    public Price price;
    public int capacity;
    public BusType busType;
    public Station departure;
    public Station arrival;
    public List<Schedule> schedules;

    public static List<Bus> sampleBusList(int size) {
        List<Bus> busList = new ArrayList<>();

        Random random = new Random();
        City[] cities = City.values();

        for (int i = 1; i <= size; i++) {
            Bus bus = new Bus();
            bus.name = "Bus " + i;

            City departureCity = cities[random.nextInt(cities.length)];
            Station departureStation = new Station();
            departureStation.city = departureCity;
            bus.departure = departureStation;

            City arrivalCity;
            do {
                arrivalCity = cities[random.nextInt(cities.length)];
            } while (arrivalCity == departureCity);
            Station arrivalStation = new Station();
            arrivalStation.city = arrivalCity;
            bus.arrival = arrivalStation;
            busList.add(bus);
        }

        return busList;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
