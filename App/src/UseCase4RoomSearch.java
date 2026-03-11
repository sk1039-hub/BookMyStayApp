import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price per Night: $" + price);
        System.out.println("Amenities: " + amenities);
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String roomType, int count) {
        availability.put(roomType, count);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public Set<String> getRoomTypes() {
        return availability.keySet();
    }
}

// Search Service (Read-only)
class SearchService {

    public void searchAvailableRooms(Inventory inventory, Map<String, Room> rooms) {

        System.out.println("Available Rooms:\n");

        for (String type : inventory.getRoomTypes()) {

            int available = inventory.getAvailability(type);

            // Validation logic (availability > 0)
            if (available > 0) {

                Room room = rooms.get(type);

                if (room != null) {
                    room.displayDetails();
                    System.out.println("Available Count: " + available);
                    System.out.println("---------------------------");
                }
            }
        }
    }
}

// Main Program
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Create room objects (Domain Model)
        Room single = new Room("Single", 100, "WiFi, TV");
        Room deluxe = new Room("Deluxe", 180, "WiFi, TV, Mini Bar");
        Room suite = new Room("Suite", 300, "WiFi, TV, Mini Bar, Ocean View");

        // Store rooms
        Map<String, Room> rooms = new HashMap<>();
        rooms.put("Single", single);
        rooms.put("Deluxe", deluxe);
        rooms.put("Suite", suite);

        // Create inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Deluxe", 0);   // unavailable
        inventory.addRoom("Suite", 2);

        // Guest initiates search
        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, rooms);
    }
}