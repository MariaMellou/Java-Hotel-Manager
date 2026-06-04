import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private static int idCounter = 1;

    private final int reservationId;
    private final Customer customer;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private boolean active;

    public Reservation(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null)   throw new IllegalArgumentException("Dates cannot be null.");
        if (!checkOut.isAfter(checkIn))            throw new IllegalArgumentException("Check-out must be after check-in.");
        this.reservationId = idCounter++;
        this.customer  = customer;
        this.room      = room;
        this.checkIn   = checkIn;
        this.checkOut  = checkOut;
        this.active    = true;
    }

    public int getReservationId()   { return reservationId; }
    public Customer getCustomer()   { return customer; }
    public Room getRoom()           { return room; }
    public LocalDate getCheckIn()   { return checkIn; }
    public LocalDate getCheckOut()  { return checkOut; }
    public boolean isActive()       { return active; }

    public void cancel() {
        this.active = false;
    }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    /** Delegates cost calculation to the room via the Bookable interface. */
    public double getTotalCost() {
        return room.calculateCost(checkIn, checkOut);
    }

    /**
     * Returns true if this reservation overlaps with the given date range.
     * Overlap rule: the reservation starts before the range ends AND ends after the range starts.
     */
    public boolean overlaps(LocalDate start, LocalDate end) {
        return active && checkIn.isBefore(end) && checkOut.isAfter(start);
    }

    @Override
    public String toString() {
        return String.format(
                "Reservation #%d | %s | Room %d (%s) | %s -> %s | %d night(s) | $%.2f | %s",
                reservationId, customer.getFullName(), room.getRoomNumber(),
                room.getDescription(), checkIn, checkOut, getNights(),
                getTotalCost(), active ? "ACTIVE" : "CANCELLED");
    }
}
