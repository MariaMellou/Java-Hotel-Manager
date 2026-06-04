/**
 * Any entity that can be booked for a date range
 * must implement this interface.
 */
import java.time.LocalDate;

public interface Bookable {
    double calculateCost(LocalDate checkIn, LocalDate checkOut);
    double getPricePerNight();
}
