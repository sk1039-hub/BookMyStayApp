import java.util.*;

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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType;
    }
}

// Booking History (stores confirmed reservations)
class BookingHistory {

    // List preserves insertion order
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation.getReservationId());
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history); // return copy (immutability)
    }
}

// Booking Report Service
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report (count by room type)
    public void generateSummaryReport(List<Reservation> reservations) {
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary Report ---");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomTypeCount.get(type));
        }
    }
}

// Main Class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Booking history instance
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from previous use cases)
        Reservation r1 = new Reservation("RES101", "Alice", "Single");
        Reservation r2 = new Reservation("RES102", "Bob", "Double");
        Reservation r3 = new Reservation("RES103", "Charlie", "Single");
        Reservation r4 = new Reservation("RES104", "David", "Suite");

        // Add to history (in order of confirmation)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Admin retrieves data
        List<Reservation> allReservations = history.getAllReservations();

        // Reporting service
        BookingReportService reportService = new BookingReportService();

        // Display full history
        reportService.displayAllBookings(allReservations);

        // Generate summary
        reportService.generateSummaryReport(allReservations);
    }
}