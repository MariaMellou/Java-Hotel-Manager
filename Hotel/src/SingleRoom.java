public class SingleRoom extends Room {
    public SingleRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    @Override
    public String getDescription() {
        return "Single (max 1 guest)";
    }
}
