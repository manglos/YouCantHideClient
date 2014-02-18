package umbrella.magic.youcanthide.youcanthide;
public class It extends Role{
	
	/**
	 * 
	 */
	int numTaggedThisGame;
	int numTaggedThisTurn;
	int xpGained;
	
	public It(){
		super();
		setType("IT");
		numTaggedThisTurn=0;
		numTaggedThisTurn=0;
		xpGained=0;
	}
	
	void gameEventHandler(GameEvent e){
		/* Large method with logic for handling any given event
		 * according to player role 'it'
		 */
	}

}
