# Hotel Reservation Management System

A simple Java-based hotel reservation management system built using object-oriented programming principles.

## Description

This project models the basic operations of a hotel management system. It allows the user to manage hotel rooms, customers, and reservations. The system checks room availability for specific date ranges and prevents overlapping reservations.

The project was created to practice Java OOP concepts such as classes, objects, encapsulation, enums, lists, and date handling with `LocalDate`.

## Features

* Create hotel rooms
* Add customers
* Create new reservations
* Check room availability by date range
* Display available rooms
* Cancel reservations
* Search reservations by customer
* Search reservations by date range
* Prevent overlapping bookings

## Technologies Used

* Java
* Object-Oriented Programming
* Java Collections
* Java `LocalDate`
* Enums

## Project Structure

```text
HotelApp.java
├── Room
├── Customer
├── Reservation
├── HotelManager
├── RoomType
├── RoomStatus
└── ReservationStatus
```

## Main Classes

### Room

Represents a hotel room with information such as:

* room number
* room type
* price per night
* room status

### Customer

Represents a hotel customer with basic contact information.

### Reservation

Connects a customer with a room for a specific date range. Each reservation has a status, such as active or cancelled.

### HotelManager

Handles the main business logic of the system, including room creation, reservation creation, availability checking, cancellation, and search operations.

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/your-username/hotel-reservation-system.git
```

2. Open the project in an IDE such as IntelliJ IDEA, Eclipse, or VS Code.

3. Compile and run the file:

```bash
HotelApp.java
```

## Example Functionality

The system can create rooms and customers, make reservations, check whether a room is available for selected dates, cancel an existing reservation, and search reservations based on customer ID or date range.

## What I Learned

Through this project, I practiced:

* designing classes and relationships
* using encapsulation
* working with lists and enums
* handling dates in Java
* implementing validation logic
* preventing overlapping reservations
* organizing business logic inside a manager class

## Future Improvements

Possible future improvements include:

* adding a graphical user interface
* connecting the system to a database
* adding payment functionality
* adding user authentication
* calculating total reservation cost
* supporting different hotel branches

## Author

Created by Maria Mellou.
