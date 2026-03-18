import java.util.*;

// Booking Request Model
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public synchronized boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public synchronized void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();

    // Map room type -> allocated room IDs
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Global set to ensure uniqueness
    private Set<String> allRoomIds = new HashSet<>();

    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Add booking request (FIFO)
    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 4);
        } while (allRoomIds.contains(roomId));

        return roomId;
    }

    // Process booking requests
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();

            System.out.println("\nProcessing booking for: " + request.customerName);

            synchronized (this) {
                if (inventoryService.isAvailable(request.roomType)) {

                    // Generate unique room ID
                    String roomId = generateRoomId(request.roomType);

                    // Add to global set
                    allRoomIds.add(roomId);

                    // Map room type to allocated IDs
                    allocatedRooms
                            .computeIfAbsent(request.roomType, k -> new HashSet<>())
                            .add(roomId);

                    // Decrement inventory immediately
                    inventoryService.decrement(request.roomType);

                    // Confirm booking
                    System.out.println("Booking CONFIRMED");
                    System.out.println("Room Type: " + request.roomType);
                    System.out.println("Assigned Room ID: " + roomId);

                } else {
                    System.out.println("Booking FAILED - No rooms available for " + request.roomType);
                }
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + " -> " + allocatedRooms.get(type));
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Add booking requests (FIFO)
        bookingService.addRequest(new BookingRequest("Alice", "Single"));
        bookingService.addRequest(new BookingRequest("Bob", "Double"));
        bookingService.addRequest(new BookingRequest("Charlie", "Single"));
        bookingService.addRequest(new BookingRequest("David", "Suite"));
        bookingService.addRequest(new BookingRequest("Eve", "Suite")); // should fail

        // Process all bookings
        bookingService.processBookings();

        // Display results
        bookingService.displayAllocations();
        inventoryService.displayInventory();
    }
}