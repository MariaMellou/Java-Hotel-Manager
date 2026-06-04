public class Main {
    public static void main(String[] args) {
        HotelManager hotel = new HotelManager("Grand Java Hotel");

        // Seed some rooms using the concrete subclasses
        hotel.addRoom(new SingleRoom(101, 89.99));
        hotel.addRoom(new SingleRoom(102, 89.99));
        hotel.addRoom(new DoubleRoom(201, 149.99));
        hotel.addRoom(new Suite(301, 299.99));
        hotel.addRoom(new Penthouse(401, 599.99));

        // Start interactive console menu
        ConsoleMenu menu = new ConsoleMenu(hotel);
        menu.start();
    }
}