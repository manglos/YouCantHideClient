package umbrella.magic.youcanthide.youcanthide;

public class Stats { 
    private int numPeopleTagged = 0;
    private int numTimesTagged = 0;
    private double fastestSpeed = 0.0;
    private int gamesPlayed = 0;
    private int numTimesHome = 0;
    private int numGamesHosted = 0;
    
    //in minutes?
    private double timeSpentHiding = 0.0;
    private int numItSuccess = 0;
    private int numRunSuccess = 0;
    private int abandoned = 0;
    private int numBanned = 0;
    private double distTrav = 0;
    private int timesDisputed = 0;
    
    //false tags
    private int numLies= 0;
    
    //number of times tagged everyone while it
    private int beenABoss= 0;
    
    //number of times where the player hasn't been found for the full duration of the game
    private int numNinja = 0;
    private int experience = 0;

    public void taggedSomeone() {
        numPeopleTagged++;
    }

    public void gameAbandoned() {
        abandoned++;
    }

    public void gotBanned() {
        numBanned++;
    }
    
    public void gotTagged() {
        numTimesTagged++;
    }
    
    public void gamePlayed() {
        gamesPlayed++;
    }

    public void runSuccess() {
        numRunSuccess++;
    }

    public void itSuccess() {
        numItSuccess++;
    }
    
    public void changeFastestSpeed(double newSpeed) {
        if (newSpeed > fastestSpeed) {
            fastestSpeed = newSpeed;
        }
    }
    public void gameHosted() {
        numGamesHosted++;
    }

    public void gotHome() {
        numTimesHome++;
    }

    public void pantsOnFire() {
        //numlies++;
    }
    
    public void addHidingTime(double ht) {
        if (ht > 0.0) {
            timeSpentHiding+=ht;
        }
    }
    
    public void travelledDist(double dist) {
        if (dist> 0.0) {
            distTrav += dist;
        }
    }
    public void wasANinja() {
        numNinja++;
    }
    
    public void wasABoss() {
        beenABoss++;
    }
    
    //this one will need work
    public void addExperience(int expGained) {
        experience+= expGained;
    }

    public void tagDisputed() {
        timesDisputed++;
    }
    

}
