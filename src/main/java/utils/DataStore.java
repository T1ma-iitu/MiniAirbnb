package utils;

import models.User;
import models.Listing;
import models.Booking;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static List<User> users = new ArrayList<>();
    public static List<Listing> listings = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();
}
