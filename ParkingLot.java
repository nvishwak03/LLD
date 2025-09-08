// Vehicle → has license plate, type.
// ParkingSpot → has id, availability and Vehicle if occupied.
// Ticket → has id, created time, spotId.
// ParkingLot → has list of spots.

//Vehicle - create vehicle ith plate and type(bike, car, truck)
//ParkingSpot - is availabilty there, park vehicle, leave vehicle
//Ticket - create ticket, close ticket
//ParkingLot - park vehicle, unpark vehicle, get count of free spots



import java.util.*;

// Vehicle
class Vehicle {
    String plate;
    String type; // "CAR", "BIKE", "TRUCK"

    public Vehicle(String plate, String type) {
        this.plate = plate;
        this.type = type;
    }
}

// Parking Spot
class ParkingSpot {
    int id;
    boolean free;
    Vehicle vehicle;

    public ParkingSpot(int id) {
        this.id = id;
        this.free = true;
    }

    public boolean isFree() { return free; }

    public void park(Vehicle v) {
        this.vehicle = v;
        this.free = false;
    }

    public void leave() {
        this.vehicle = null;
        this.free = true;
    }
}

// Ticket
class Ticket {
    String id;
    Vehicle vehicle;
    int spotId;
    Date createdAt;
    boolean active;

    public Ticket(Vehicle v, int spotId) {
        this.id = UUID.randomUUID().toString();
        this.vehicle = v;
        this.spotId = spotId;
        this.createdAt = new Date();
        this.active = true;
    }

    public void close() { active = false; }
}

// Parking Lot
class ParkingLot {
    List<ParkingSpot> spots;
    Map<String, Ticket> tickets;

    public ParkingLot(int capacity) {
        spots = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            spots.add(new ParkingSpot(i));
        }
        tickets = new HashMap<>();
    }

    // Park vehicle
    public Ticket parkVehicle(Vehicle v) {
        for (ParkingSpot s : spots) {
            if (s.isFree()) {
                s.park(v);
                Ticket t = new Ticket(v, s.id);
                tickets.put(t.id, t);
                System.out.println("Parked " + v.plate + " (" + v.type + ") at Spot " + s.id);
                return t;
            }
        }
        System.out.println("No free spot available!");
        return null;
    }

    // Unpark vehicle
    public void unparkVehicle(String ticketId) {
        Ticket t = tickets.get(ticketId);
        if (t != null && t.active) {
            for (ParkingSpot s : spots) {
                if (s.id == t.spotId) {
                    s.leave();
                    t.close();
                    System.out.println("Vehicle " + t.vehicle.plate + " left Spot " + s.id);
                    return;
                }
            }
        }
        System.out.println("Invalid ticket!");
    }

    // Get count of free spots
    public int availableSpots() {
        int count = 0;
        for (ParkingSpot s : spots) if (s.isFree()) count++;
        return count;
    }
}

// Demo
public class Main {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(3);

        Ticket t1 = lot.parkVehicle(new Vehicle("CAR123", "CAR"));
        Ticket t2 = lot.parkVehicle(new Vehicle("BIKE9", "BIKE"));
        System.out.println("Available spots: " + lot.availableSpots());

        lot.unparkVehicle(t1.id);
        System.out.println("Available spots: " + lot.availableSpots());

        Ticket t3 = lot.parkVehicle(new Vehicle("TRK77", "TRUCK"));
    }
}
