import java.util.*;

// Reservation Model
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String customerName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking History
class BookingHistory {

    // Active bookings
    private Map<String, Reservation> activeBookings = new HashMap<>();

    // Cancelled bookings (audit)
    private List<Reservation> cancelledBookings = new ArrayList<>();

    public void addBooking(Reservation r) {
        activeBookings.put(r.getReservationId(), r);
    }

    public Reservation getBooking(String reservationId) {
        return activeBookings.get(reservationId);
    }

    public void removeBooking(String reservationId) {
        Reservation r = activeBookings.remove(reservationId);
        if (r != null) {
            cancelledBookings.add(r);
        }
    }

    public boolean exists(String reservationId) {
        return activeBookings.containsKey(reservationId);
    }

    public void displayActiveBookings() {
        System.out.println("\nActive Bookings:");
        for (Reservation r : activeBookings.values()) {
            System.out.println(r.getReservationId() + " -> " + r.getRoomType() + " (" + r.getRoomId() + ")");
        }
    }

    public void displayCancelledBookings() {
        System.out.println("\nCancelled Bookings:");
        for (Reservation r : cancelledBookings) {
            System.out.println(r.getReservationId() + " -> " + r.getRoomType() + " (" + r.getRoomId() + ")");
        }
    }
}

// Cancellation Service
class CancellationService {

    private InventoryService inventoryService;
    private BookingHistory bookingHistory;

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomIds = new Stack<>();

    public CancellationService(InventoryService inventoryService, BookingHistory bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate existence
        if (!bookingHistory.exists(reservationId)) {
            System.out.println("Cancellation FAILED: Reservation does not exist or already cancelled");
            return;
        }

        // Fetch reservation
        Reservation r = bookingHistory.getBooking(reservationId);

        // Step 1: Push room ID to rollback stack
        releasedRoomIds.push(r.getRoomId());

        // Step 2: Restore inventory
        inventoryService.increment(r.getRoomType());

        // Step 3: Remove from active & move to cancelled
        bookingHistory.removeBooking(reservationId);

        // Success message
        System.out.println("Cancellation SUCCESSFUL");
        System.out.println("Released Room ID: " + r.getRoomId());
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + releasedRoomIds);
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES301", "Alice", "Single", "S-101");
        Reservation r2 = new Reservation("RES302", "Bob", "Double", "D-201");

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);

        // Decrement inventory as if allocated earlier
        inventoryService.decrement("Single");
        inventoryService.decrement("Double");

        // Display initial state
        bookingHistory.displayActiveBookings();
        inventoryService.displayInventory();

        // Cancellation Service
        CancellationService cancellationService =
                new CancellationService(inventoryService, bookingHistory);

        // Perform cancellations
        cancellationService.cancelBooking("RES301"); // valid
        cancellationService.cancelBooking("RES999"); // invalid
        cancellationService.cancelBooking("RES301"); // duplicate cancel

        // Final state
        bookingHistory.displayActiveBookings();
        bookingHistory.displayCancelledBookings();
        inventoryService.displayInventory();
        cancellationService.displayRollbackStack();
    }
}