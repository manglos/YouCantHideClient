package umbrella.magic.youcanthide;


import java.util.ArrayList;
import umbrella.magic.youcanthide.youcanthide.*;

	/**
	 *
	 * @author angie
	 */
public class MyGame {
    static private int id;
    static String location, size;
    static int numIt;
    static private ArrayList<Player> players; // possibly redundant?
    //private ArrayList<Observer> observers;
    static private Stats statistics;
    static private double time;
    
    public MyGame(int i) {
        id = i; // I don't know how you guys want to handle game identification...
        players = new ArrayList<Player>();
        statistics = new Stats();
    }
    public MyGame(int i, String l, String s, int n, int t){
    	id = i;
    	location = l;
    	size = s;
    	numIt = n;
    	time = t;
    }
    // A Player notifies us of a change:
    public void notifyChange(GameEvent e) {
        // Do something with this information?
        //notifyObservers();
    }
    
    // Notify all observers of a change:
    /*private void notifyObservers() {
        for (int i=0; i<this.observers.size(); i++) {
            this.observers.get(i);
        }
    }*/
    
    public void setTimeLimit(Double limit) {
        time = limit;
    }
    
    public double getTimeLimit() {
        return time;
    }

}
