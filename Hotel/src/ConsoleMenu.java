import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final HotelManager hotel;
    private final Scanner scanner;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ConsoleMenu(HotelManager hotel) {
        this.hotel   = hotel;
        this.scanner = new Scanner(System.in);
    }

    // ─────────────────────────────────────────────────────────────
    // ENTRY POINT
    // ─────────────────────────────────────────────────────────────
    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            String input = prompt("Select an option");
            switch (input) {
                case "1": createRoom();                  break;
                case "2": viewAvailableRooms();          break;
                case "3": createReservation();           break;
                case "4": viewRoomsForDateRange();       break;
                case "5": cancelReservation();           break;
                case "6": searchReservation();           break;
                case "0":
                    System.out.println("\n  Goodbye! See you next time.\n");
                    running = false;
                    break;
                default:
                    System.out.println("  ! Invalid option. Please choose 0–6.");
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN MENU PRINT
    // ─────────────────────────────────────────────────────────────
    private void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║     GRAND JAVA HOTEL  — SYSTEM       ║");
        System.out.println("  ╚══════════════════════════════════════╝");
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("  ┌─────────────────────────────────────┐");
        System.out.println("  │              MAIN MENU              │");
        System.out.println("  ├─────────────────────────────────────┤");
        System.out.println("  │  1.  Create a room                  │");
        System.out.println("  │  2.  View available rooms           │");
        System.out.println("  │  3.  Create a reservation           │");
        System.out.println("  │  4.  Show rooms for date range      │");
        System.out.println("  │  5.  Cancel a reservation           │");
        System.out.println("  │  6.  Search reservations            │");
        System.out.println("  │  0.  Exit                           │");
        System.out.println("  └─────────────────────────────────────┘");
    }

    // ─────────────────────────────────────────────────────────────
    // OPTION 1 — CREATE A ROOM
    // ─────────────────────────────────────────────────────────────
    private void createRoom() {
        printHeader("CREATE A ROOM");

        // Room number
        int roomNumber;
        while (true) {
            String input = prompt("  Room number (or 'back' to return)");
            if (isBack(input)) return;
            try {
                roomNumber = Integer.parseInt(input);
                if (roomNumber <= 0) { System.out.println("  ! Room number must be positive."); continue; }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a valid number.");
            }
        }

        // Room type
        System.out.println();
        System.out.println("  Room types:");
        System.out.println("    1. Single     (max 1 guest)");
        System.out.println("    2. Double     (max 2 guests)");
        System.out.println("    3. Suite      (max 4 guests)");
        System.out.println("    4. Penthouse  (max 6 guests)");

        int typeChoice;
        while (true) {
            String input = prompt("  Select room type (1-4, or 'back')");
            if (isBack(input)) return;
            try {
                typeChoice = Integer.parseInt(input);
                if (typeChoice < 1 || typeChoice > 4) { System.out.println("  ! Choose between 1 and 4."); continue; }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a number between 1 and 4.");
            }
        }

        // Price
        double price;
        while (true) {
            String input = prompt("  Price per night in $ (or 'back')");
            if (isBack(input)) return;
            try {
                price = Double.parseDouble(input);
                if (price < 0) { System.out.println("  ! Price cannot be negative."); continue; }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a valid price.");
            }
        }

        // Build room object for confirmation preview
        String typeName;
        switch (typeChoice) {
            case 1: typeName = "Single";    break;
            case 2: typeName = "Double";    break;
            case 3: typeName = "Suite";     break;
            default: typeName = "Penthouse"; break;
        }

        // Confirmation
        System.out.println();
        System.out.println("  ┌── Confirm new room ───────────────────┐");
        System.out.printf ("  │  Room number : %-24d │%n", roomNumber);
        System.out.printf ("  │  Type        : %-24s │%n", typeName);
        System.out.printf ("  │  Price/night : $%-23.2f │%n", price);
        System.out.println("  └────────────────────────────────────────┘");

        String confirm = prompt("  Add this room? (yes / no)");
        if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
            System.out.println("  Room creation cancelled. Returning to main menu.");
            return;
        }

        try {
            Room room;
            switch (typeChoice) {
                case 1: room = new SingleRoom(roomNumber, price);  break;
                case 2: room = new DoubleRoom(roomNumber, price);  break;
                case 3: room = new Suite(roomNumber, price);       break;
                default: room = new Penthouse(roomNumber, price);  break;
            }
            hotel.addRoom(room);
            System.out.println("  ✔ Room " + roomNumber + " added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("  ! Error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // OPTION 2 — VIEW AVAILABLE ROOMS
    // ─────────────────────────────────────────────────────────────
    private void viewAvailableRooms() {
        printHeader("AVAILABLE ROOMS");
        List<Room> rooms = hotel.getAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("  No rooms are currently available.");
        } else {
            printRoomTable(rooms);
        }
        waitForEnter();
    }

    // ─────────────────────────────────────────────────────────────
    // OPTION 3 — CREATE A RESERVATION
    // ─────────────────────────────────────────────────────────────
    private void createReservation() {
        printHeader("CREATE A RESERVATION");

        // Step 1: pick a room
        System.out.println("  Step 1 of 3 — Choose a room");
        List<Room> available = hotel.getAvailableRooms();
        if (available.isEmpty()) {
            System.out.println("  No rooms are currently available.");
            waitForEnter();
            return;
        }
        printRoomTable(available);

        int roomNumber;
        while (true) {
            String input = prompt("  Enter room number (or 'back')");
            if (isBack(input)) return;
            try {
                roomNumber = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a valid room number.");
            }
        }

        Room room = hotel.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("  ! Room " + roomNumber + " does not exist.");
            return;
        }
        if (!hotel.isRoomAvailable(room)) {
            System.out.println("  ! Room " + roomNumber + " is not available.");
            return;
        }

        // Step 2: dates
        System.out.println();
        System.out.println("  Step 2 of 3 — Reservation dates  (format: dd/MM/yyyy, e.g. 24/12/2026)");
        LocalDate checkIn  = readDate("  Check-in  date");
        if (checkIn == null) return;
        LocalDate checkOut = readDate("  Check-out date");
        if (checkOut == null) return;

        if (!checkOut.isAfter(checkIn)) {
            System.out.println("  ! Check-out must be after check-in.");
            return;
        }

        if (!hotel.isRoomAvailable(room, checkIn, checkOut)) {
            System.out.println("  ! Room " + roomNumber + " is already booked for those dates.");
            return;
        }

        // Step 3: customer info
        System.out.println();
        System.out.println("  Step 3 of 3 — Customer information");
        String firstName = promptNonEmpty("  First name");
        if (firstName == null) return;
        String lastName  = promptNonEmpty("  Last name");
        if (lastName == null) return;
        String email     = promptNonEmpty("  Email");
        if (email == null) return;
        String phone     = prompt("  Phone");
        if (isBack(phone)) return;

        // Confirmation
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double total = room.calculateCost(checkIn, checkOut);

        System.out.println();
        System.out.println("  ┌── Confirm reservation ────────────────────────┐");
        System.out.printf ("  │  Customer    : %-33s │%n", firstName + " " + lastName);
        System.out.printf ("  │  Email       : %-33s │%n", email);
        System.out.printf ("  │  Phone       : %-33s │%n", phone);
        System.out.printf ("  │  Room        : %-33s │%n", roomNumber + " — " + room.getDescription());
        System.out.printf ("  │  Check-in    : %-33s │%n", checkIn);
        System.out.printf ("  │  Check-out   : %-33s │%n", checkOut);
        System.out.printf ("  │  Nights      : %-33d │%n", nights);
        System.out.printf ("  │  Total cost  : $%-32.2f │%n", total);
        System.out.println("  └───────────────────────────────────────────────┘");

        String confirm = prompt("  Confirm reservation? (yes / no)");
        if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
            System.out.println("  Reservation cancelled. Returning to main menu.");
            return;
        }

        try {
            Customer customer = hotel.registerCustomer(firstName, lastName, email, phone);
            Reservation res   = hotel.createReservation(customer, room, checkIn, checkOut);
            System.out.println("  ✔ Reservation #" + res.getReservationId() + " created successfully.");
        } catch (Exception e) {
            System.out.println("  ! Error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // OPTION 4 — AVAILABLE ROOMS FOR DATE RANGE
    // ─────────────────────────────────────────────────────────────
    private void viewRoomsForDateRange() {
        printHeader("AVAILABLE ROOMS FOR DATE RANGE");
        System.out.println("  Date format: dd/MM/yyyy  (e.g. 01/06/2026)");
        System.out.println();

        LocalDate from = readDate("  From date");
        if (from == null) return;
        LocalDate to   = readDate("  To   date");
        if (to == null) return;

        if (!to.isAfter(from)) {
            System.out.println("  ! End date must be after start date.");
            return;
        }

        List<Room> rooms = hotel.getAvailableRooms(from, to);
        System.out.println();
        System.out.println("  Available rooms from " + from + " to " + to + ":");
        if (rooms.isEmpty()) {
            System.out.println("  No rooms available for that period.");
        } else {
            printRoomTable(rooms);
        }
        waitForEnter();
    }

    // ─────────────────────────────────────────────────────────────
    // OPTION 5 — CANCEL A RESERVATION
    // ─────────────────────────────────────────────────────────────
    private void cancelReservation() {
        printHeader("CANCEL A RESERVATION");

        List<Reservation> all = hotel.getAllReservations();
        List<Reservation> active = new java.util.ArrayList<>();
        for (Reservation r : all) {
            if (r.isActive()) active.add(r);
        }

        if (active.isEmpty()) {
            System.out.println("  There are no active reservations.");
            waitForEnter();
            return;
        }

        printReservationTable(active);

        while (true) {
            String input = prompt("  Enter reservation ID to cancel (or 'back')");
            if (isBack(input)) return;

            int id;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a valid ID.");
                continue;
            }

            Reservation target = hotel.findReservationById(id);
            if (target == null) {
                System.out.println("  ! Reservation #" + id + " not found.");
                continue;
            }
            if (!target.isActive()) {
                System.out.println("  ! Reservation #" + id + " is already cancelled.");
                continue;
            }

            // Confirm
            System.out.println();
            System.out.println("  Cancelling: " + target);
            String confirm = prompt("  Are you sure? (yes / no)");
            if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
                System.out.println("  Cancellation aborted.");
                return;
            }

            hotel.cancelReservation(id);
            System.out.println("  ✔ Reservation #" + id + " has been cancelled.");
            return;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // OPTION 6 — SEARCH RESERVATIONS
    // ─────────────────────────────────────────────────────────────
    private void searchReservation() {
        printHeader("SEARCH RESERVATIONS");
        System.out.println("  Search by:");
        System.out.println("    1. Customer name");
        System.out.println("    2. Date range");

        String choice = prompt("  Select search type (or 'back')");
        if (isBack(choice)) return;

        switch (choice) {
            case "1": {
                String name = prompt("  Enter customer name (full or partial)");
                if (isBack(name)) return;
                List<Reservation> results = hotel.searchByCustomerName(name);
                printSearchResults(results, "customer name \"" + name + "\"");
                break;
            }
            case "2": {
                System.out.println("  Date format: dd/MM/yyyy  (e.g. 01/06/2026)");
                LocalDate from = readDate("  From date");
                if (from == null) return;
                LocalDate to   = readDate("  To   date");
                if (to == null) return;
                List<Reservation> results = hotel.searchByDateRange(from, to);
                printSearchResults(results, "date range " + from + " → " + to);
                break;
            }
            default:
                System.out.println("  ! Invalid choice.");
                return;
        }
        waitForEnter();
    }

    // ─────────────────────────────────────────────────────────────
    // PRINT HELPERS
    // ─────────────────────────────────────────────────────────────
    private void printHeader(String title) {
        System.out.println();
        System.out.println("  ══════════════════════════════════════");
        System.out.println("    " + title);
        System.out.println("  ══════════════════════════════════════");
    }

    private void printRoomTable(List<Room> rooms) {
        System.out.println();
        System.out.printf("  %-6s %-26s %-12s %-12s%n", "Room#", "Type", "Price/Night", "Status");
        System.out.println("  " + "─".repeat(58));
        for (Room r : rooms) {
            System.out.printf("  %-6d %-26s $%-11.2f %-12s%n",
                    r.getRoomNumber(),
                    r.getDescription(),
                    r.getPricePerNight(),
                    r.isAvailable() ? "Available" : "Unavailable");
        }
        System.out.println();
    }

    private void printReservationTable(List<Reservation> reservations) {
        System.out.println();
        System.out.printf("  %-4s %-20s %-6s %-12s %-12s %-10s%n",
                "ID", "Customer", "Room", "Check-in", "Check-out", "Total");
        System.out.println("  " + "─".repeat(68));
        for (Reservation r : reservations) {
            System.out.printf("  %-4d %-20s %-6d %-12s %-12s $%-9.2f%n",
                    r.getReservationId(),
                    r.getCustomer().getFullName(),
                    r.getRoom().getRoomNumber(),
                    r.getCheckIn(),
                    r.getCheckOut(),
                    r.getTotalCost());
        }
        System.out.println();
    }

    private void printSearchResults(List<Reservation> results, String criteria) {
        System.out.println();
        if (results.isEmpty()) {
            System.out.println("  No reservations found for " + criteria + ".");
        } else {
            System.out.println("  Found " + results.size() + " reservation(s) for " + criteria + ":");
            printReservationTable(results);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // INPUT HELPERS
    // ─────────────────────────────────────────────────────────────
    private String prompt(String message) {
        System.out.print("  > " + message + ": ");
        return scanner.nextLine().trim();
    }

    /** Keeps asking until the user types something non-empty, or 'back'. Returns null on back. */
    private String promptNonEmpty(String message) {
        while (true) {
            String value = prompt(message + " (or 'back')");
            if (isBack(value)) return null;
            if (!value.isEmpty()) return value;
            System.out.println("  ! This field cannot be empty.");
        }
    }

    /** Reads a date in dd/MM/yyyy format. Returns null on bad format or 'back'. */
    private LocalDate readDate(String message) {
        while (true) {
            String input = prompt(message + " (dd/MM/yyyy, or 'back')");
            if (isBack(input)) return null;
            try {
                return LocalDate.parse(input, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("  ! Invalid date. Please use the format dd/MM/yyyy  (e.g. 24/12/2026).");
            }
        }
    }

    private boolean isBack(String input) {
        return input.equalsIgnoreCase("back") || input.equalsIgnoreCase("b");
    }

    private void waitForEnter() {
        System.out.print("  Press Enter to return to the main menu...");
        scanner.nextLine();
    }
}
