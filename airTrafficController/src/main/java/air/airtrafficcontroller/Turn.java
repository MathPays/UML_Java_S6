package air.airtrafficcontroller;

import java.util.Random;

public class Turn {
    private Request[] requestList;

    public Turn(){
        Random random = new Random();
        int nbRequest = 1 + random.nextInt(3);
        this.requestList = new Request[nbRequest];
        for(int i = 0; i < nbRequest; i++){
            Request request = this.createRequest();
            this.requestList[i] = request;
        }
    }

    public void answerRequest(int requestIndex, int optionIndex) {
        this.requestList[requestIndex].choose(optionIndex);
    }

    private Request createRequest(){
        Random random = new Random();
        int idRequest = random.nextInt(100);
        if(idRequest < 40){ //plane landing
            int fuel = 3 + random.nextInt(3);
            int runwayTime = 1 + random.nextInt(4);
            int passengers = 100 + random.nextInt(401);
            PlaneRequest request = new PlaneRequest("A plane wants to land.", "Plane landing", 40, 2, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new AddToWaitingLineOption("Place on standby", request.getPlane());
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 45){ //emergency landing
            int fuel = 1 + random.nextInt(3);
            int runwayTime = 3 + random.nextInt(3);
            int passengers = 100 + random.nextInt(401);
            PlaneRequest request = new PlaneRequest("A plane wants to land in an emergency.", "Emergency landing", 5, 2, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new AddToWaitingLineOption("Place on standby", request.getPlane());
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 60){ //Jumbo jet
            int fuel = 4 + random.nextInt(3);
            int runwayTime = 1 + random.nextInt(3);
            int passengers = 300 + random.nextInt(301);
            PlaneRequest request = new PlaneRequest("A large aircraft wants to land.", "Emergency landing", 15, 2, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new AddToWaitingLineOption("Place on standby", request.getPlane());
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 70){ //snakes on plane
            int fuel = 1 + random.nextInt(3);
            int runwayTime = 7;
            int passengers = 100 + random.nextInt(401);
            PlaneRequest request = new PlaneRequest("A pilot is reporting that their plane is being overrun by snakes. They want to land at your airport.", "Snakes on the plane", 10, 2, fuel, runwayTime, passengers);
            Option opt1 = new LandPlaneOption("Allow landing", request.getPlane());
            Option opt2 = new KillPeopleOption("Refuse them", 50);
            request.addOption(opt1);
            request.addOption(opt2);
            return request;
        }
        else if(idRequest < 80){ //Funding event
            Request request = new Request("Increase in funding allows for one of the following bonuses:", "Funding Event", 10, 3);
            Option opt1 = new EmptyRandomRunwayOption("Empty a random runway");
            Option opt2 = new ReduceDeadPeopleOption("Rescue Team", 100);
            Option opt3 = new AddFuelOption(1, "Air Refueling");
            request.addOption(opt1);
            request.addOption(opt2);
            request.addOption(opt3);
            return request;
        }
        else if(idRequest < 90){//Bad Weather
            Request request = new Request("Select a bad weather event.", "Bad weather", 10, 3);
            Option opt1 = new RemoveFuelOption(1, "Cross Winds");
            Option opt2 = new AddWaitTimeOption("Icy runways", 2);
            Option opt3 = new BlockRunwayOption("Ice storm", 5, Runway.State.FROZEN);
            request.addOption(opt1);
            request.addOption(opt2);
            request.addOption(opt3);
            return request;
        }
        else if(idRequest < 95){ //protests
            Request request = new Request("A group of protestors are blocking the runway claiming that your airplanes are dropping chemtrails which has caused the following health issues: “makes their toothpaste taste like mint.”", "Protests", 5, 3);
            Option opt1 = new BlockRunwayOption("Allow them space to protest.", 10, Runway.State.PROTEST);
            // TODO: IMPORTANT MISSING OPTION BLOCK 2 RUNWAYS
            Option opt3 = new KillPeopleOption("Let the plane land anyways", 100);
            request.addOption(opt1);
            request.addOption(opt3);
            return request;
        }
        else { //john mcclain
            Request request = new Request("Security reports of a man hijacking a plane on the runway. He claims he needs to stop the terrorists from stealing christmas. Do you lock down the runway or let them go?", "John McClain", 5, 2);
            Option opt1 = new BlockRunwayOption("Lockdown the runway", 8, Runway.State.OCCUPIED);
            // TODO: MISSIONG OPTIONS
            request.addOption(opt1);
            return request;
        }
    }

    public void planeWaitingToRunway(Plane plane) {
        WaitingLine.removePlane(plane);
        Runways.addPlane(plane);
    }

    public void allTakeOff() {
        for (Runway runway : Runways.getRunways()) {
            if(runway.getRunwayTime() == 0) {
                runway.removePlane();
            }
        }
    }

    public void crashWaitingPlanes() {
        for (Plane plane : WaitingLine.getWaitingLine()) {
            if (plane.getHoursFuel() == 0) {
                Game.killMorePeople(plane.getPassengers());
                WaitingLine.removePlane(plane);
            }
        }
    }



}
