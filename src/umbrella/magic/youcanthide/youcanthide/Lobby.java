/*
 * First draft of the lobby class
 */
package umbrella.magic.youcanthide.youcanthide;

/**
 *
 * @author angie
 */
public class Lobby {
    //private ArrayList<Observer> observers; // These are just the potential players (i think?)
    
    public Lobby() {
        //this.observers = new ArrayList();
    }
    
    // Notify all observers of a change:
    /*private void notifyObservers() {
        // Give them an updated list of who is in the lobby?
        for (int i=0; i<this.observers.size(); i++) {
            this.observers.get(i).notify(this.observers);
        }
    }
    
    // Add someone to the lobby:
    public void addObserver(Observer newObserver) {
        this.observers.add(newObserver);
        addObserver(notifyObservers);
    }
    
    // Remove someone from the lobby:
    public void removeObserver(Observer newObserver) {
        this.observers.remove(newObserver);
        addObserver(notifyObservers);
    }
    
    public void startGame() {
        Game game = new Game(1234);
        for (int i=0; i<this.observers.size(); i++) {
            game.addPlayer(observers.get(i));
            // Also prompt users to leave lobby (will be garbage collected)
        }
    }*/
}
