public class Suite extends Room {
    public Suite(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    @Override
    public String getDescription() {
        return "Suite (max 4 guests)";
    }
}
