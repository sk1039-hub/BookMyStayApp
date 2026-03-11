
    /**
     * UseCase2RoomInitialization
     *
     * Demonstrates room initialization and static availability
     * in the Book My Stay Hotel Booking System.
     *
     * @author Student
     * @version 2.1
     */

// Abstract Room class
    abstract class Room {

        protected int beds;
        protected int size;
        protected double price;

        public Room(int beds, int size, double price) {
            this.beds = beds;
            this.size = size;
            this.price = price;
        }

        public void displayDetails() {
            System.out.println("Beds: " + beds);
            System.out.println("Room Size: " + size + " sq.ft");
            System.out.println("Price: $" + price);
        }

        public abstract String getRoomType();
    }

    // Single Room class
    class SingleRoom extends Room {

        public SingleRoom() {
            super(1, 200, 80.0);
        }

        public String getRoomType() {
            return "Single Room";
        }
    }

    // Double Room class
    class DoubleRoom extends Room {

        public DoubleRoom() {
            super(2, 350, 120.0);
        }

        public String getRoomType() {
            return "Double Room";
        }
    }

    // Suite Room class
    class SuiteRoom extends Room {

        public SuiteRoom() {
            super(3, 500, 250.0);
        }

        public String getRoomType() {
            return "Suite Room";
        }
    }

    // Main Application Class
    public class BasicRoom {

        public static void main(String[] args) {

            System.out.println("==================================");
            System.out.println("   Book My Stay App");
            System.out.println("   Hotel Booking System v2.1");
            System.out.println("==================================");

            // Creating room objects (Polymorphism)
            Room singleRoom = new SingleRoom();
            Room doubleRoom = new DoubleRoom();
            Room suiteRoom = new SuiteRoom();

            // Static availability variables
            int singleAvailable = 10;
            int doubleAvailable = 5;
            int suiteAvailable = 2;

            System.out.println("\nRoom Details:\n");

            System.out.println(singleRoom.getRoomType());
            singleRoom.displayDetails();
            System.out.println("Available: " + singleAvailable);
            System.out.println("-----------------------");

            System.out.println(doubleRoom.getRoomType());
            doubleRoom.displayDetails();
            System.out.println("Available: " + doubleAvailable);
            System.out.println("-----------------------");

            System.out.println(suiteRoom.getRoomType());
            suiteRoom.displayDetails();
            System.out.println("Available: " + suiteAvailable);
        }
    }

