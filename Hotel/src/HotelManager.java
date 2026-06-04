import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelManager {

    private final String hotelName;
    private final List<Room>        rooms        = new ArrayList<>();
    private final List<Customer>    customers    = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    public HotelManager(String hotelName) {
        this.hotelName = hotelName;
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Room creation
    // ─────────────────────────────────────────────────────────────
    public void addRoom(Room room) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == room.getRoomNumber()) {
                throw new IllegalArgumentException("Room " + room.getRoomNumber() + " already exists.");
            }
        }
        rooms.add(room);
        log("Room added: " + room);
    }

    // ─────────────────────────────────────────────────────────────
    // 2. View all available rooms (no date filter)
    // ─────────────────────────────────────────────────────────────
    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (isRoomAvailable(r)) {
                available.add(r);
            }
        }
        return available;
    }

    // ─────────────────────────────────────────────────────────────
    // 4. Room availability for a specific date range
    // ─────────────────────────────────────────────────────────────
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (isRoomAvailable(r, checkIn, checkOut)) {
                available.add(r);
            }
        }
        return available;
    }

    /** Checks if a room has no active reservations at all. */
    public boolean isRoomAvailable(Room room) {
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber() == room.getRoomNumber() && res.isActive()) {
                return false;
            }
        }
        return true;
    }

    /** Checks if a room is free for the given date range. */
    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber() == room.getRoomNumber()) {
                if (res.overlaps(checkIn, checkOut)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────
    // Customer registration
    // ─────────────────────────────────────────────────────────────
    public Customer registerCustomer(String firstName, String lastName, String email, String phone) {
        Customer customer = new Customer(firstName, lastName, email, phone);
        customers.add(customer);
        log("Customer registered: " + customer);
        return customer;
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Reservation creation
    // ─────────────────────────────────────────────────────────────
    public Reservation createReservation(Customer customer, Room room,
                                         LocalDate checkIn, LocalDate checkOut) {
        if (!isRoomAvailable(room, checkIn, checkOut)) {
            throw new IllegalStateException(
                    "Room " + room.getRoomNumber() + " is not available for the requested dates.");
        }
        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        reservations.add(reservation);
        log("Reservation created: " + reservation);
        return reservation;
    }

    // ─────────────────────────────────────────────────────────────
    // 5. Reservation cancellation
    // ─────────────────────────────────────────────────────────────
    public void cancelReservation(int reservationId) {
        Reservation res = findReservationById(reservationId);
        if (res == null) {
            throw new IllegalArgumentException("Reservation #" + reservationId + " not found.");
        }
        if (!res.isActive()) {
            throw new IllegalStateException("Reservation #" + reservationId + " is already cancelled.");
        }
        res.cancel();
        log("Reservation cancelled: #" + reservationId);
    }

    // ─────────────────────────────────────────────────────────────
    // 6. Search reservations – by customer name or date range
    // ─────────────────────────────────────────────────────────────
    public List<Reservation> searchByCustomerName(String name) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getCustomer().getFullName().toLowerCase().contains(name.toLowerCase())) {
                results.add(res);
            }
        }
        return results;
    }

    public List<Reservation> searchByCustomerId(int customerId) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getCustomer().getCustomerId() == customerId) {
                results.add(res);
            }
        }
        return results;
    }

    public List<Reservation> searchByDateRange(LocalDate from, LocalDate to) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.overlaps(from, to)) {
                results.add(res);
            }
        }
        return results;
    }

    // ─────────────────────────────────────────────────────────────
    // Utility helpers
    // ─────────────────────────────────────────────────────────────
    public Reservation findReservationById(int id) {
        for (Reservation res : reservations) {
            if (res.getReservationId() == id) {
                return res;
            }
        }
        return null;
    }

    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public List<Room>        getAllRooms()        { return new ArrayList<>(rooms); }
    public List<Customer>    getAllCustomers()    { return new ArrayList<>(customers); }
    public List<Reservation> getAllReservations() { return new ArrayList<>(reservations); }

    public void log(String message) {
        System.out.println("[" + hotelName + " LOG] " + message);
    }
}
