import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Abstract base class for all room types.
 * Implements Bookable.
 * Concrete subclasses must provide getDescription().
 */
public abstract class Room implements Bookable {

    private final int roomNumber;
    private final double pricePerNight;
    private boolean available;

    public Room(int roomNumber, double pricePerNight) {
        if (roomNumber <= 0)    throw new IllegalArgumentException("Room number must be positive.");
        if (pricePerNight < 0)  throw new IllegalArgumentException("Price cannot be negative.");
        this.roomNumber     = roomNumber;
        this.pricePerNight  = pricePerNight;
        this.available      = true;
    }

    public int getRoomNumber()          { return roomNumber; }
    public boolean isAvailable()        { return available; }
    public void setAvailable(boolean v) { this.available = v; }

    @Override
    public double getPricePerNight() { return pricePerNight; }

    /** Calculates total cost for a stay. Shared by all subclasses. */
    @Override
    public double calculateCost(LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return nights * pricePerNight;
    }

    /** Each subclass describes what kind of room it is. */
    public abstract String getDescription();


    @Override
    public String toString() {
        return String.format("Room %d | %-10s | $%.2f/night | %s",
                roomNumber, getDescription(), pricePerNight,
                available ? "Available" : "Unavailable");
    }
}
