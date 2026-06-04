public class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    @Override
    public String getDescription() {
        return "Double (max 2 guests)";
    }
}
