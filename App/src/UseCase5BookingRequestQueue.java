import java.util.*;

// Reservation class representing a booking request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // Display queued requests
    public void showQueue() {
        System.out.println("\nCurrent Booking Request Queue:");

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }
}

// Main Program
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Deluxe");
        Reservation r3 = new Reservation("Charlie", "Suite");
        Reservation r4 = new Reservation("David", "Single");

        // Add requests to queue (FIFO)
        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);
        queue.addRequest(r4);

        // Display queue order
        queue.showQueue();
    }
}