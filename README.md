# JBus-Android

JBus-Android is the mobile client for the JBus bus booking system, providing a user-friendly interface for booking buses, managing schedules, and handling payments.

## Features

- Interactive UI for booking buses
- Schedule management for renters and users
- Payment integration
- User account management

## Activities Overview

The app includes several activities for different functions:

- `MainActivity.java`: The main interface for users.
- `LoginActivity.java`: Handles user login.
- `RegisterActivity.java`: Allows new users to register.
- `BookingActivity.java`: Interface for booking a bus.
- `ManageBusActivity.java`: Interface for a renter to manage his bus.

## Models

- `Account.java`: Represents user accounts within the system. 
- `BaseResponse.java`: A generic class used to structure the response returned from the server. 
- `Bus.java`: Encapsulates all details related to a bus, such as its name, type, capacity, and associated facilities
- `BusType.java`: An enumeration that defines the various types of buses available.
- `City.java`: An enumeration of cities or a class that represents a city where the buses operate.
- `Facility.java`: An enumeration or a class that represents the different amenities or services provided on the bus.
- `Invoice.java`: Represents an invoice generated for a transaction, possibly containing details like invoice ID, buyer and renter information, status, and timestamp.
- `Payment.java`: Describes a payment transaction, including details such as the amount, payment status, and associated booking details.
- `Predicate.java`: An interface that represents a condition or expression that can be true or false based on the provided object. Used for filtering or matching objects.
- `Price.java`: Manages pricing information.
- `Rating.java`: Manages ratings given by users or customers.
- `Renter.java`: Represents entities (users or companies) that rent out buses, including their details such as name, contact information, and a list of buses they offer.
- `Review.java`: Represents customer reviews, including the date of the review, its content, and the associated rating.
- `Schedule.java`: Details of a bus's schedule, including departure times and seat availability.
- `Station.java`: Represents a physical bus station, with properties like its name, location, and the city it's associated with.
- `Type.java`: An enumeration that categorizes different voucher types.
- `Validate.java`: A utility class containing methods to validate various inputs or data against predefined rules or patterns.
- `Voucher.java`: Represents vouchers that may be used in the system.

## API Integration

- `BaseApiService.java`: Interfaces with the backend API.
- `UtilsApi.java`: Utility class for API operations.

## Setup

The Android app can be installed and run on an Android device to interact with the system.

## Links

- Backend Repository: [JBus Backend](https://github.com/NargaFRZ/JBus)

---

This README provides an overview of the Android client. For more detailed information, please refer to the source code and comments within.
