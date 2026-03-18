import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Model
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;

    public Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
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
}

// Inventory Service with validation
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check availability
    public void validateAvailability(String roomType) throws InvalidBookingException {
        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }

    // Safe decrement
    public void decrement(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("Cannot decrement. Inventory already zero for: " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory State: " + inventory);
    }
}

// Booking Validator (Fail-Fast)
class BookingValidator {

    public static void validate(String customerName, String roomType, InventoryService inventoryService)
            throws InvalidBookingException {

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        // Validate against inventory
        inventoryService.validateRoomType(roomType);
        inventoryService.validateAvailability(roomType);
    }
}

// Booking Service
class BookingService {

    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void confirmBooking(String reservationId, String customerName, String roomType) {
        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(customerName, roomType, inventoryService);

            // Step 2: Proceed with booking
            inventoryService.decrement(roomType);

            Reservation reservation = new Reservation(reservationId, customerName, roomType);

            System.out.println("Booking CONFIRMED:");
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Customer: " + reservation.getCustomerName());
            System.out.println("Room Type: " + reservation.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Test cases

        // ✅ Valid booking
        bookingService.confirmBooking("RES201", "Alice", "Single");

        // ❌ Invalid room type
        bookingService.confirmBooking("RES202", "Bob", "Deluxe");

        // ❌ Empty customer name
        bookingService.confirmBooking("RES203", "", "Double");

        // ❌ Valid but inventory runs out
        bookingService.confirmBooking("RES204", "Charlie", "Suite");
        bookingService.confirmBooking("RES205", "David", "Suite"); // should fail

        // Display final inventory
        inventoryService.displayInventory();
    }
}