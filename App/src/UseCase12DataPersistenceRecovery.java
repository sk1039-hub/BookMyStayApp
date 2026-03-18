import java.io.*;
import java.util.*;

// Reservation Model (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return reservationId + " - " + customerName + " (" + roomType + ")";
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (Exception e) {
            System.out.println("Error loading state. Starting with clean state.");
            return null;
        }
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // Try loading existing state
        SystemState state = persistenceService.load();

        Map<String, Integer> inventory;
        List<Reservation> history;

        if (state != null) {
            // Restore state
            inventory = state.inventory;
            history = state.bookingHistory;
        } else {
            // Initialize fresh state
            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 1);

            history = new ArrayList<>();
        }

        // Display current state
        System.out.println("\nCurrent Inventory: " + inventory);
        System.out.println("Booking History: " + history);

        // Simulate new booking
        Reservation newReservation =
                new Reservation("RES401", "Alice", "Single");


        history.add(newReservation);
        inventory.put("Single", inventory.get("Single") - 1);

        System.out.println("\nNew booking added: " + newReservation);

        // Save updated state before shutdown
        SystemState newState = new SystemState(inventory, history);
        persistenceService.save(newState);

        System.out.println("\nFinal Inventory: " + inventory);
        System.out.println("Final Booking History: " + history);
    }
}