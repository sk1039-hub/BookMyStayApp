import java.util.HashMap;

/**
 * RoomInventory class manages centralized room availability
 * using a HashMap data structure.
 *
 * @author Student
 * @version 3.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes room availability
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        System.out.println("---------------------------");

        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType));
        }
    }
}

/**
 * UseCase3InventorySetup
 *
 * Demonstrates centralized inventory management using HashMap.
 *
 * @author Student
 * @version 3.1
 */
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v3.1");
        System.out.println("==================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Retrieve specific availability
        System.out.println("\nChecking availability for Single Room:");
        System.out.println("Available: " + inventory.getAvailability("Single Room"));

        // Update inventory
        System.out.println("\nUpdating Suite Room availability...");
        inventory.updateAvailability("Suite Room", 3);

        // Display updated inventory
        inventory.displayInventory();
    }
}