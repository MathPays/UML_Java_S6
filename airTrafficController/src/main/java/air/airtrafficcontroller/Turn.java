package air.airtrafficcontroller;

import air.airtrafficcontroller.options.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Turn implements Serializable {
    private final ArrayList<Request> requestList; //List of requests [1-3] for this turn

    //Constructor
    public Turn(){
        Random random = new Random();
        int nbRequest = 1 + random.nextInt(3); //random [1-3] number of requests
        this.requestList = new ArrayList<>();
        for(int i = 0; i < nbRequest; i++){
            Request request = this.createRequest();
            this.requestList.add(request);
        }
    }

    /**
     * Create one random request and its options
     * @return random request created
     */
    private Request createRequest(){
        Random random = new Random();
        int idRequest = random.nextInt(100); //number from 0 to 99 for percentages of rarity
        if(idRequest < 40){ //plane landing, 40%
            int fuel = 3 + random.nextInt(3); //create random values for plane attributes
            int runwayTime = 1 + random.nextInt(4);
            int passengers = 100 + random.nextInt(401);
            PlaneRequest request = new PlaneRequest("A plane wants to land.", "Plane landing", 40, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new AddToWaitingLineOption("Place on standby", request.getPlane());
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 45){ //emergency landing, 5%
            int fuel = 1 + random.nextInt(3); //create random values for plane attributes
            int runwayTime = 3 + random.nextInt(3);
            int passengers = 100 + random.nextInt(401);
            PlaneRequest request = new PlaneRequest("A plane wants to land in an emergency.", "Emergency landing", 5, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new AddToWaitingLineOption("Place on standby", request.getPlane());
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 60){ //Jumbo jet, 15%
            int fuel = 4 + random.nextInt(3); //create random values for plane attributes
            int runwayTime = 1 + random.nextInt(4);
            int passengers = 300 + random.nextInt(301);
            PlaneRequest request = new PlaneRequest("A large aircraft wants to land.", "Jumbo Jet", 15, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new AddToWaitingLineOption("Place on standby", request.getPlane());
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 70){ //snakes on plane, 10%
            int fuel = 1 + random.nextInt(3); //create random values for plane attributes
            int runwayTime = 7;
            int passengers = 100 + random.nextInt(401);
            PlaneRequest request = new PlaneRequest("A pilot is reporting that their plane is being overrun by snakes. They want to land at your airport.", "Snakes on the plane", 10, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new KillPeopleOption("Refuse them", 50);
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 80){ //Funding event, 10%
            Request request = new Request("Increase in funding allows for one of the following bonuses:", "Funding Event", 10, "funding.gif");
            Option opt1 = new EmptyRandomRunwayOption("Empty a random runway");
            Option opt2 = new ReduceDeadPeopleOption("Rescue Team", 100);
            Option opt3 = new AddFuelOption(1, "Air Refueling");
            request.addOption(opt1);
            request.addOption(opt2);
            request.addOption(opt3);
            return request;
        }
        else if(idRequest < 90){//Bad Weather, 10%
            Request request = new Request("Select a bad weather event.", "Bad weather", 10);
            Option opt1 = new RemoveFuelOption(1, "Cross Winds");
            Option opt2 = new AddWaitTimeOption("Icy runways", 2);
            Option opt3 = new BlockRunwayOption("Ice storm", 24, Runway.State.REMOVED);
            request.addOption(opt1);
            request.addOption(opt2);
            request.addOption(opt3);
            return request;
        }
        else if(idRequest < 95){ //protests, 5%
            Request request = new Request("A group of protestors are blocking the runway claiming that your airplanes are dropping chemtrails which has caused the following health issues: “makes their toothpaste taste like mint.”", "Protests", 5, "protest.gif");
            Option opt1 = new BlockRunwayOption("Allow them space to protest.", 10, Runway.State.PROTEST);
            Option opt2 = new BlockRunwayOption("Get police involved", 4, Runway.State.RIOT);Option opt3 = new KillPeopleOption("Let the plane land anyways", 100);
            request.addOption(opt1);
            request.addOption(opt2);
            request.addOption(opt3);
            return request;
        }
        else { //john mcclain, 5%
            Request request = new Request("Security reports of a man hijacking a plane on the runway. He claims he needs to stop the terrorists from stealing christmas. Do you lock down the runway or let them go?", "John McClain", 5, "johnmcclane.gif");
            Option opt1 = new BlockRunwayOption("Lockdown the runway", 8, Runway.State.OCCUPIED);
            Option opt2 = new CrashPlaneOption("Let the man go", 100);
            request.addOption(opt2);
            request.addOption(opt1);
            return request;
        }
    }

    /**
     * Checks if runwayTime of runways is 0 and removes planes that can go (called at end of every turn)
     */
    public void allTakeOff() {
        for (Runway runway : Runways.getRunways()) {
            if(runway.getRunwayTime() == 0) {
                runway.removePlane();
            }
        }
    }
    /**
     * Checks fuel left in waiting planes and crashed those that are out (called at the end of every turn)
     * @return true if there is no planes to crash
     */
    public boolean crashWaitingPlanes() {
        ArrayList<Plane> planesToCrash = new ArrayList<>();
        for (Plane plane : WaitingLine.getWaitingLine()) {
            if (plane.getHoursFuel() == 0) {
                planesToCrash.add(plane);
            }
        }
        if (!planesToCrash.isEmpty()) {
            Application.displayCrash(planesToCrash);
            return true;
        }
        return false;
    }

    /**
     * Gets next request if there is one, returns null if no requests left
     * @return next request in the list
     */
    public Request getNextRequest() {
        for(Request request : requestList){
            if(request != null){
                return request;
            }
        }
        return null;
    }
    /**
     * Removes a request (once request is completed)
     */
    public void removeRequest() {
        this.requestList.remove(0);
    }
}
