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

// Thread-safe Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);

        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.offer(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Runnable)
class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private InventoryService inventoryService;

    public BookingProcessor(BookingQueue queue, InventoryService inventoryService) {
        this.queue = queue;
        this.inventoryService = inventoryService;
    }

    @Override
    public void run() {
        while (true) {

            BookingRequest request;

            // synchronized access to queue
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break;
            }

            processBooking(request);
        }
    }

    private void processBooking(BookingRequest request) {

        System.out.println(Thread.currentThread().getName() +
                " processing: " + request.customerName);

        // critical section for allocation
        boolean success = inventoryService.allocateRoom(request.roomType);

        if (success) {
            System.out.println("Booking CONFIRMED for " + request.customerName +
                    " (" + request.roomType + ")");
        } else {
            System.out.println("Booking FAILED for " + request.customerName +
                    " (" + request.roomType + ")");
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate multiple requests
        bookingQueue.addRequest(new BookingRequest("Alice", "Single"));
        bookingQueue.addRequest(new BookingRequest("Bob", "Single"));
        bookingQueue.addRequest(new BookingRequest("Charlie", "Single")); // should fail
        bookingQueue.addRequest(new BookingRequest("David", "Double"));
        bookingQueue.addRequest(new BookingRequest("Eve", "Double")); // should fail

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventoryService), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventoryService), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventoryService), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventoryService.displayInventory();
    }
}