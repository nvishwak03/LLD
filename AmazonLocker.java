// Locker : id, size("S/M/L"), occupied, packageId, constructor(), assign(pkgId), vacate()
// Package : id, size("S/M/L"), constructor()
// ManageLockers : lockers, addLocker(id,size), dropOff(packageId,size)->lockerId, pickup(lockerId), findFreeLocker(size), printState()

import java.util.*;

// Locker
class Locker {
    int id;
    String size;      // "S", "M", "L"
    boolean occupied;
    String packageId; // null if free

    public Locker(int id, String size) {
        this.id = id;
        this.size = size;
        this.occupied = false;
        this.packageId = null;
    }


    public void assign(String pkgId) {
        this.packageId = pkgId;
        this.occupied = true;
    }

    public void vacate() {
        this.packageId = null;
        this.occupied = false;
    }

    @Override
    public String toString() {
        return "Locker#" + id + " [" + size + "] " + (occupied ? ("PKG=" + packageId) : "FREE");
    }
}

// Package
class Package {
    String id;
    String size; // "S","M","L"

    public Package(String id, String size) {
        this.id = id;
        this.size = size;
    }
}

// ManageLockers (manager)
class ManageLockers {
    List<Locker> lockers = new ArrayList<>();

    public ManageLockers addLocker(int id, String size) {
        lockers.add(new Locker(id, size));
        return this;
    }

    // Find first free locker of the SAME size
    private Locker findFreeLocker(String size) {
        for (Locker l : lockers) {
            if (l.size.equalsIgnoreCase(size) && !l.occupied) return l;
        }
        return null;
    }

    // Drop a package; return assigned locker id (or -1 if none)
    public int dropOff(String packageId, String size) {
        Package pkg = new Package(packageId, size);
        Locker l = findFreeLocker(pkg.size);
        if (l == null) {
            System.out.println("No free locker for size " + size);
            return -1;
        }
        l.assign(pkg.id);
        System.out.println("Dropped " + pkg.id + " into Locker#" + l.id);
        return l.id;
    }

    // Pickup by locker id (simple)
    public boolean pickup(int lockerId) {
        for (Locker l : lockers) {
            if (l.id == lockerId) {
                if (!l.occupied) {
                    System.out.println("Locker#" + lockerId + " is already empty");
                    return false;
                }
                System.out.println("Picked up " + l.packageId + " from Locker#" + lockerId);
                l.vacate();
                return true;
            }
        }
        System.out.println("Locker#" + lockerId + " not found");
        return false;
    }

    public void printState() {
        System.out.println("=== LOCKERS ===");
        for (Locker l : lockers) System.out.println(l);
        System.out.println();
    }
}

// Demo
public class AmazonLocker {
    public static void main(String[] args) {
        ManageLockers bank = new ManageLockers()
            .addLocker(1, "S").addLocker(2, "S")
            .addLocker(3, "M").addLocker(4, "L");

        int a = bank.dropOff("PKG-A", "S");
        int b = bank.dropOff("PKG-B", "M");
        int c = bank.dropOff("PKG-C", "S");
        bank.printState();

        bank.pickup(a);
        bank.printState();

        bank.dropOff("PKG-D", "S");
        bank.printState();

        bank.pickup(99); // locker not found
        bank.pickup(b);  // pickup M
        bank.printState();
    }
}
