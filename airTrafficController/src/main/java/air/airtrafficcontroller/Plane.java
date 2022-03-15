package air.airtrafficcontroller;

public class Plane {
    private static int index = 1;
    private final int id;
    private int hoursFuel, runwayTime, passengers;

    public Plane(int h, int r, int p){
        this.hoursFuel = h;
        this.runwayTime = r;
        this.passengers = p;
        this.id = index;
        index++;
    }

    public void addFuel(int hoursToAdd){
        this.hoursFuel += hoursToAdd;
    }
    public void removeFuel(int hoursToSub){
        this.hoursFuel -= hoursToSub;
    }
    public int getRunwayTime() {return runwayTime; }

    public void setRunwayTime(int runwayTime) {
        this.runwayTime = runwayTime;
    }

    public int getId() { return this.id; }
    public int getHoursFuel() { return this.hoursFuel; }
    public int getPassengers() { return this.passengers; }

    public void passHour() { this.hoursFuel--; }
}