/*
 * Game class first draft.
 */
package umbrella.magic.youcanthide.youcanthide; // We also need to discuss how to orginize packages!

import java.util.ArrayList;

/**
 *
 * @author angie
 */
public class Game {
    private int id;
    private ArrayList<Player> players; // possibly redundant?
    //private ArrayList<Observer> observers;
    private Stats statistics;
    private double time;
    
    public Game(int id) {
        this.id = id; // I don't know how you guys want to handle game identification...
        this.players = new ArrayList<Player>();
        this.statistics = new Stats();
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
        this.time = limit;
    }
    
    public double getTimeLimit() {
        return this.time;
    }
    
    public void addPlayer(Player newPlayer) {
        this.players.add(newPlayer);
        //addObserver(newPlayer);
    }
    
    /*private void addObserver(Observer newObserver) {
        this.observers.add(newObserver);
    }*/
}
