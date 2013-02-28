package webEditor.dst.client;

import java.util.ArrayList;

import webEditor.client.Proxy;

import com.google.gwt.user.client.rpc.IsSerializable;
public class Evaluation_Hashing extends Evaluation implements IsSerializable {
	public String evaluate(String problemName, String[] arguments,
			ArrayList<Node> nodes, ArrayList<EdgeParent> edges) {
		
		// Setup location Arrays for user and solution locations
		ArrayList<Location> userLocations = getUserLocations(nodes);
		ArrayList<Location> solutionLocations = getSolutionLocations(getRightArguments(arguments));
		
		// Check to make sure they moved all given values down from the starting locations.
		if(!allMovedDown(nodes)){
			Proxy.submitDST(problemName, 0);
			return("Feedback: Please make sure that you have placed all of your items, at least one is still above the line.");
		}
		
		// Check to see if they tried to put more than one item in a location.
		if(anyDoubledUp(userLocations)){
			Proxy.submitDST(problemName, 0);
			return("Feedback: You have two items on top of each other. You can only have one item per location.");
		}
		
		// Check to see if they are in the correct positions
		return checkOrder(userLocations, solutionLocations, problemName);
	}
	
	
	//Laughably inefficeint but hey, we're dealing with small data sets right?
	public String checkOrder(ArrayList<Location> userLocations, ArrayList<Location> solutionLocations, String problemName){
		String incorrectNodes = "";
		boolean correct = true;
		int f = -7777;
		for(int i=0;i<20;i++){
			int userValue=f;
			int solutionValue= f;
			for(int j=0;j<userLocations.size();j++){
				if(userLocations.get(j).getLocation()==i){
					userValue = userLocations.get(j).getValue();
				}
			}
			for(int h=0;h<solutionLocations.size();h++){
				if(solutionLocations.get(h).getLocation()==i){
					solutionValue = solutionLocations.get(h).getValue();
				}
			}
			if(userValue!=solutionValue){
				correct = false;
				if(userValue!=f)
					incorrectNodes+=userValue+" ";
			}
		}
        if(correct){
        	Proxy.submitDST(problemName, 1);
			return "Correct!";
        }else{
        	Proxy.submitDST(problemName,0);
        	return "Feedback: Incorrect. The following data items were not in their correct locations: "+incorrectNodes.trim()+".";
        }
		
	}
	public boolean allMovedDown(ArrayList<Node> nodes){
		for(Node n: nodes){
			if(n.getTop()<200)
				return false;
		}
		return true;
	}
	public boolean anyDoubledUp(ArrayList<Location> locations){
		ArrayList<Integer> usedLocations = new ArrayList<Integer>();
		for(Location l: locations){
			if(usedLocations.contains(l.getLocation())){
				return true;
			}else{
				usedLocations.add(l.getLocation());
			}
		}
		
		return false;
	}
	// Stupid stupid rounding
	public int getPosition(Node n){
		int left = n.getLeft();
		if(n.getTop()>270 && n.getTop() < 280){
			if(left>10 && left < 15)
				return 0;
			if(left>70 && left < 75)
				return 1;
			if(left>130 && left < 135)
				return 2;
			if(left>190 && left < 195)
				return 3;
			if(left>250 && left < 255)
				return 4;
			if(left>310 && left < 315)
				return 5;
			if(left>370 && left < 375)
				return 6;
			if(left>430 && left < 435)
				return 7;
			if(left>490 && left < 495)
				return 8;
			if(left>550 && left < 555)
				return 9;	
		}else if(n.getTop()>380 && n.getTop()<390){
			if(left>10 && left < 15)
				return 10;
			if(left>70 && left < 75)
				return 11;
			if(left>130 && left < 135)
				return 12;
			if(left>190 && left < 195)
				return 13;
			if(left>250 && left < 255)
				return 14;
			if(left>310 && left < 315)
				return 15;
			if(left>370 && left < 375)
				return 16;
			if(left>430 && left < 435)
				return 17;
			if(left>490 && left < 495)
				return 18;
			if(left>550 && left < 555)
				return 19;
		}else{
			return -1;
		}
		return -1;
	}
	public class Location{
		public int value;
		public int location;
		
		public Location(int value, int location){
			this.value = value;
			this.location = location;
		}
		public Location(String s){
			String[] split = s.split(" ");
			value = Integer.parseInt(split[0]);
			location = Integer.parseInt(split[1]);
		}
		
		public int getValue(){
			return value;
		}
		
		public int getLocation(){
			return location;
		}
		
		public String toString(){
			return value + " "+ location;
		}
		
	}
	public ArrayList<Location> getUserLocations(ArrayList<Node> nodes){
		ArrayList<Location> userLocations = new ArrayList<Location>();
		for(Node n: nodes){
			userLocations.add(new Location(Integer.parseInt(n.getValue()), getPosition(n)));
		}
		
		/*
		 * Looks like debug code
		 *
		String solutions="";
		for(Location l: userLocations){
			solutions+=">"+l.getValue()+" "+l.getLocation()+"<";
		}
		Window.alert("User: "+solutions);
		*/
		return userLocations;
	}
	public ArrayList<Location> getSolutionLocations(String[] nodes){
		ArrayList<Location> solutionLocations = new ArrayList<Location>();
		String[] split;
		for(String s: nodes){
			split = s.split(" ");
			solutionLocations.add(new Location(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
		}
		/*
		 * Looks like debug code
		 * 
		String solutions="";
		for(Location l: solutionLocations){
			solutions+=">"+l.getValue()+" "+l.getLocation()+"<";
		}
		Window.alert("Solution "+solutions);
		*/
		return solutionLocations;
	}
	
	public String[] getRightArguments(String[] args){
		String[] newArgs = new String[args.length-1];
		for(int i=1;i<args.length;i++){
			newArgs[i-1] = args[i];
		}
		return newArgs;
	}


	@Override
	public int returnKeyValue() {
		return DSTConstants.HASHING_KEY;
	}
}
