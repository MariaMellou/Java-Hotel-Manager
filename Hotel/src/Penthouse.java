public class Penthouse extends Room {
    public Penthouse(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    @Override
    public String getDescription() {
        return "Penthouse (max 6 guests)";
    }
}
