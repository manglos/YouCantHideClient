package umbrella.magic.youcanthide.youcanthide;

public class Field extends Zone{ 
  
	
	public Field(int size){
		super(size);
	}
  
	public boolean nearEdgeWarning(){ 
		//use Listener Pings to return whether a player is near 
		//the edge 
    	return false;
	} 
}
