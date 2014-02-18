package umbrella.magic.youcanthide;

import umbrella.magic.youcanthide.youcanthide.Game;
import umbrella.magic.youcanthide.youcanthide.It;
import umbrella.magic.youcanthide.youcanthide.Player;
import umbrella.magic.youcanthide.youcanthide.Role;
import umbrella.magic.youcanthide.youcanthide.Runner;
import umbrella.magic.youcanthide.youcanthide.Spectator;


public class Me {
	private static String firstName, lastName, username, password, phoneNumber;
    private static int xp=0;
    private static boolean online=false;
    private static int loginCount, numTagged, numBeenTagged;
    private static Role myRole=null;
    private static Game myGame;

    public Me(String fn, String ln, String un, String pw, String pn, int x, boolean on){
        firstName=fn; lastName=ln; username=un; password=pw; phoneNumber = pn; xp=x;
        setOnline(on);
        loginCount=0;
        numTagged=0;
        numBeenTagged=0;
    }
    public Me(String fn, String ln, String un, String pw, String pn, int x, boolean on, Role r){
        firstName=fn; lastName=ln; username=un; password=pw; phoneNumber = pn; xp=x;
        setOnline(on);
        myRole=r;
        loginCount=0;
        numTagged=0;
        numBeenTagged=0;
    }
    public Me(String fn, String ln, String un, String pw, String pn, int x, boolean on, String r){
        firstName=fn; lastName=ln; username=un; password=pw; phoneNumber = pn; xp=x;
        setOnline(on);
        loginCount=0;
        numTagged=0;
        numBeenTagged=0;
        setRole(r);
        
    }
    
    static void setInfoToPlayer(Player p){
    	setName(p.getFirstName(), p.getLastName());
    	setExperience(p.getExperience());
    	setUsername(p.getUsername());
    	setPhonenumber(p.getPhonenumber());
    	setPassword(p.getPassword());
    	setRole(p.getRole());
    }
    static void setGame(Game g){
    	myGame=g;
    }
    static void setName(String fn, String ln){
        firstName=fn; lastName=ln;
    }
    static void setUsername(String un){
        username=un;
    }
    static void setPhonenumber(String pn){
        phoneNumber=pn;
    }
    static void setPassword(String p){
    	password=p;
    }
    static void setExperience(int x){
        xp=x;
    }
    static void setRole(Role r){
		myRole=r;
    }
    static void setRole(String r){
            if(r==null)
                    return;

            if(r.equalsIgnoreCase("IT"))
                    myRole=new It();
            else if(r.equalsIgnoreCase("Runner"))
                    myRole=new Runner();
            else if(r.equalsIgnoreCase("Spectator"))
                    myRole=new Spectator();

    }
    static Role getRole(){	
            return myRole;
    }
    static String getName(){
        return firstName + " " + lastName;
    }
    static Game getGame(){
    	return myGame;
    }
    static String getFirstName(){
        return firstName;
    }
    static String getLastName(){
        return lastName;
    }
    static String getUsername(){
        return username;
    }
    static String getPhonenumber(){
        return phoneNumber;
    }
    static int getExperience(){
        return xp;
    }
    static String getPassword(){
        return password;
    }
    static void addXP(int x){
		xp+=x;
	}
	
	//This is very VERY temporary
    static void addXP(String action){
		if(action.equals("tag")){
			addXP(100);
			return;
		}
		if(action.equals("tagged")){
			addXP(-50);
			return;
		}
		if(action.equals("win")){
			addXP(300);
			return;
		}
		if(action.equals("loss")){
			addXP(100);
			return;
		}
	}
    static int getLoginCount(){
        return loginCount;
    }
    static void addLogin(){
        loginCount++;
    }
    static boolean isOnline(){
        return online;
    }
    static void setOnline(boolean o){
        online=o;
    }
    static void editInfo(String fn, String ln, String un, String pw){
        username=un;
        firstName=fn;
        lastName=ln;
        password=pw;
    }
    @Override
    public String toString(){
        return username + "'s info:\nName: " +getName() + "\nPhone: " + getPhonenumber() + "\nExperience: " + getExperience() + " --" + getRole();
    }
	
}
