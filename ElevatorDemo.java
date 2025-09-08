// Elevator : currentFloor, direction("UP/DOWN/IDLE"), doorsOpen, pending(TreeSet<Integer>), minFloor, maxFloor,
// constructor(), request(floor), nextTarget(), step(), openDoors(), closeDoors(), status()
// ElevatorController : elevator, constructor(minFloor,maxFloor,startFloor), requestPickup(floor), run(steps), print()

import java.util.*;

// -------- Elevator --------
class Elevator {
    int currentFloor;
    String direction;         // "UP", "DOWN", "IDLE"
    boolean doorsOpen;
    int minFloor, maxFloor;

    // Keep all requests (unique) sorted
    TreeSet<Integer> pending = new TreeSet<>();

    public Elevator(int minFloor, int maxFloor, int startFloor) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.currentFloor = startFloor;
        this.direction = "IDLE";
        this.doorsOpen = false;
    }

    // Add a floor request (inside or hall call routed here)
    public void request(int floor) {
        if (floor < minFloor || floor > maxFloor) {
            System.out.println("Invalid floor " + floor);
            return;
        }
        pending.add(floor);
        // If idle, set a direction toward the nearest target
        if (direction.equals("IDLE")) setDirectionToward(nextTarget());
    }

    // Decide the nearest target from current floor
    private Integer nextTarget() {
        if (pending.isEmpty()) return null;
        Integer atOrAbove = pending.ceiling(currentFloor);
        Integer below      = pending.floor(currentFloor);
        if (atOrAbove == null) return below;
        if (below == null) return atOrAbove;
        // pick the closer one
        int dUp = Math.abs(atOrAbove - currentFloor);
        int dDown = Math.abs(currentFloor - below);
        return (dUp <= dDown) ? atOrAbove : below;
    }

    // Set direction toward a given floor
    private void setDirectionToward(Integer target) {
        if (target == null) { direction = "IDLE"; return; }
        if (target > currentFloor) direction = "UP";
        else if (target < currentFloor) direction = "DOWN";
        else direction = "IDLE";
    }

    // Open/close doors
    private void openDoors()  { doorsOpen = true;  System.out.println("Doors OPEN at " + currentFloor); }
    private void closeDoors() { doorsOpen = false; System.out.println("Doors CLOSE at " + currentFloor); }

    // One simulation step:
    // - if doors open, close them (simple timing)
    // - else, move one floor toward target; if arrive, open doors and clear request
    public void step() {
        if (doorsOpen) { // close and wait next tick
            closeDoors();
            if (pending.isEmpty()) direction = "IDLE";
            return;
        }

        Integer target = nextTarget();
        if (target == null) {
            direction = "IDLE";
            System.out.println(status());
            return;
        }

        if (currentFloor == target) {
            // Serve this floor
            pending.remove(target);
            openDoors();
            // Direction may change next tick based on remaining requests
            if (pending.isEmpty()) direction = "IDLE";
            else setDirectionToward(nextTarget());
            return;
        }

        // Move one floor toward target
        if (target > currentFloor) { currentFloor++; direction = "UP"; }
        else if (target < currentFloor) { currentFloor--; direction = "DOWN"; }

        System.out.println(status());
    }

    public String status() {
        return "Elevator at " + currentFloor + " [" + direction + "] " +
               (doorsOpen ? "DOORS:OPEN" : "DOORS:CLOSED") +
               " Pending=" + pending;
    }
}

// -------- Controller --------
class ElevatorController {
    Elevator elevator;

    public ElevatorController(int minFloor, int maxFloor, int startFloor) {
        elevator = new Elevator(minFloor, maxFloor, startFloor);
    }

    // External pickup or internal selection all go here
    public void requestPickup(int floor) {
        System.out.println("Request for floor " + floor);
        elevator.request(floor);
    }

    // Run N steps of simulation
    public void run(int steps) {
        for (int i = 0; i < steps; i++) elevator.step();
    }

    public void print() {
        System.out.println(elevator.status());
    }
}

// -------- Demo --------
public class ElevatorDemo {
    public static void main(String[] args) {
        ElevatorController ctrl = new ElevatorController(0, 10, 0);
        ctrl.print();

        // People request floors
        ctrl.requestPickup(5);
        ctrl.requestPickup(2);
        ctrl.requestPickup(8);

        // Run steps
        ctrl.run(5);      // move toward nearest (2), maybe arrive and open
        ctrl.requestPickup(3);
        ctrl.run(8);      // continue serving remaining
        ctrl.run(5);      // idle at end
    }
}
