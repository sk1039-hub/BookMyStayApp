import java.util.*;

// Add-On Service Model
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + cost + ")";
    }
}

// Reservation Model (from previous use case)
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

// Add-On Service Manager
class AddOnServiceManager {

    // Map reservation ID -> list of services
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: " + service.getServiceName() +
                " to Reservation ID: " + reservationId);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0;

        for (AddOnService service : services) {
            total += service.getCost();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation ID: " + reservationId);
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        System.out.println("Total Add-On Cost: $" + calculateTotalCost(reservationId));
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Sample reservations (already created from Use Case 6)
        Reservation r1 = new Reservation("RES101", "Alice", "Single");
        Reservation r2 = new Reservation("RES102", "Bob", "Double");

        // Add-On Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Define available services
        AddOnService breakfast = new AddOnService("Breakfast", 15.0);
        AddOnService spa = new AddOnService("Spa Access", 40.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 25.0);

        // Guest selects services
        manager.addService(r1.getReservationId(), breakfast);
        manager.addService(r1.getReservationId(), spa);

        manager.addService(r2.getReservationId(), airportPickup);
        manager.addService(r2.getReservationId(), breakfast);

        // Display services and cost
        manager.displayServices(r1.getReservationId());
        manager.displayServices(r2.getReservationId());
    }
}