package coralreef2;

import java.util.ArrayList;
import java.util.List;

import coralreef2.agent.Agent;
import coralreef2.common.Constants;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.space.grid.GridPoint;

public class ListAgents {
	
	private static ArrayList<Agent> listAgents;
	
	public ListAgents(){
		listAgents = new ArrayList<Agent>(); 
	}
	
	public static void fillListAgents (ArrayList<Agent> list){
		for (Agent agent :  list){
			getListAgents().add(agent);
		}
	}
	
	/**
	 * fill up a listAgents with the agents contained in a list 
	 */
	public static ArrayList<Agent> getListAgents(){
		return listAgents;
	}
	
	/**
	 * fill up a list with the agents contained in listAgents
	 */
	public static void fillAnotherListofAgents(ArrayList<Agent> list){
		for (Agent agent : getListAgents()){
			list.add(agent);
		}
		
	}
}
