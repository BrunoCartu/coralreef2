package coralreef2.common;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jscience.physics.amount.Amount;
import org.renjin.script.RenjinScriptEngineFactory;

import OutputData.ColonyPlanarAreaDistribution;
import OutputData.NumberCoralRecruits;
import OutputData.PercentageCover;
import OutputData.RugosityCoverGrazed;
import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import coralreef2.ContextCoralReef2;
import coralreef2.CoralReef2Builder;
import coralreef2.InputData.BiodiversityData;
import coralreef2.InputData.CycloneData;
import coralreef2.InputData.Disturbance_bleaching;
import coralreef2.InputData.Disturbance_cyclone;
import coralreef2.InputData.Disturbance_grazing;
import coralreef2.InputData.Disturbance_larvalConnectivity;
import coralreef2.InputData.Disturbances_priority;
import coralreef2.InputData.FunctionalTraitData;
import coralreef2.InputData.Sand_cover;
import coralreef2.agent.Agent;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.freezedry.datasource.JDBCDataSourceTest.Foo;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;


public class SMUtils {
	
	final static public Random RANDOM = new Random(System.currentTimeMillis());  

    static public double nextSkewedBoundedDouble(double min, double max, double skew, double bias) {
        double range = max - min;
        double mid = min + range / 2.0;
        double unitGaussian = RANDOM.nextGaussian();
        double biasFactor = Math.exp(bias);
        double retval = mid+(range*(biasFactor/(biasFactor+Math.exp(-unitGaussian/skew))-0.5));
        return retval;
    }
	
    static public double getRandomRadiusFromSkewedDistribution(String nameSpecies,double skew, double bias){
    	double maxColonyDiameter = SMUtils.getDoubleTraitFromFTTable(nameSpecies, Constants.colony_max_diameter) ;
    	double radius = nextSkewedBoundedDouble(0.0, maxColonyDiameter,skew,bias) / 2.0 ;
    	return radius ;
    }
	 
	/**
	 * return the grid of the object concerned
	 */
	public static Grid<Object> getGrid(final Object o){
		@SuppressWarnings("unchecked")
		final Grid<Object> grid = (Grid<Object>) ContextUtils.getContext(o).getProjection(Constants.GRID_ID);
		return grid;
	}
	
	/**
	 * generalized method that makes a list (ret) of neighboring cells that are empty
	 */
	public static List<GridCell<Agent>> getFreeGridCells(final List <GridCell<Agent>> neighborhood){  
		if (null == neighborhood){
			throw new IllegalArgumentException("Parameter neighborhood cannot be null.");
		}
		final ArrayList<GridCell<Agent>> ret = new ArrayList<GridCell<Agent>>() ;
		
		for (final GridCell<Agent> act : neighborhood){                                               // act is one of the cell of the list neighborhood
			if (0 == act.size()){                                                                 // if act is empty, 
				ret.add(act); 																	  // act is added to the list ret
			}
		}
		return ret;
	}
	
	/**
	 * take a random element in a list (based on uniform distribution)
	 * @param list
	 * @return
	 */
	public static <T> T randomElementOf(final List<T> list){             
		if (null == list){
			throw new IllegalArgumentException("Cannot return a random element from an empty list.");
		}
		return list.get(RandomHelper.nextIntFromTo(0, list.size() - 1));
	}
	
	/**
	 * Returns a List<String> with the same strings but ordered in increasing order accordingly to the list of
	 * double[] numtraitName (name with higher number goes 1st)
	 * @param listNAmes
	 * @param numtraitName
	 * @return
	 */
	public static List<String> orderStringsAccordingNumVal(List<String> listNames, String numtraitName){
		double[] traitValueList = getListValuesTraitDouble(listNames,numtraitName) ;		
//		System.out.printf("in SMUtils.orderStringsAccordingNumVal() \n");
//		for(int i = 0 ; i < traitValueList.length ; i++){
//			System.out.printf("%s : %.1f \n",listNames.get(i),traitValueList[i]) ;
//		}	
//		int k = 0 ;
//		for(String name : listNames){
//			System.out.printf("%s %s \n",name,listNames.get(k)) ;
//			k++;
//		}	
		
		List<String>  speciesnameListOrdered = new ArrayList<String>();
		while(speciesnameListOrdered.size() < listNames.size()){
			speciesnameListOrdered.add("na");
		}
		int[] positionSp = new int[listNames.size()] ;
		for(int i= 0 ; i < listNames.size() ; i++){
			positionSp[i] = i ;
		}
		boolean ordered = false ;
		while(!ordered){
			boolean changed = false ; 
			for(int i = 0 ; i < listNames.size() - 1 ; i++){
				for(int j = i + 1 ; j < listNames.size() ; j++){
					boolean ismallerNeedChangePosition = traitValueList[i] < traitValueList[j] & positionSp[i] < positionSp[j] ;
					boolean ibiggerNeedChangePosition = traitValueList[i] > traitValueList[j] & positionSp[i] > positionSp[j] ;
					if(ismallerNeedChangePosition || ibiggerNeedChangePosition){ // reverse position number
						int valTemp = positionSp[j] ;
						positionSp[j] = positionSp[i] ;
						positionSp[i] = valTemp ;
						changed = true ;
//						System.out.printf("CALLED \n");
					}
				}
			}
			if(!changed){
				ordered = true ;
			}
		}
//		System.out.printf("in SMUtils.orderStringsAccordingNumVal() \n");
		for(int i = 0 ; i < listNames.size() ; i++){
//			System.out.printf("%s: %.2f %d \n",listNames.get(i),traitValueList[i],positionSp[i]) ;
			speciesnameListOrdered.set(positionSp[i] ,listNames.get(i)) ;
		}
//		for(String name : speciesnameListOrdered){
//			System.out.printf("%s \n",name) ;
//
//		}
//		System.out.printf("\n") ;
		return speciesnameListOrdered ;
	}
	
	

	
// returns a list of coral and macroalgae agents
// to be changed in the future in order to select only the agents that are on the edge of their colony or patch 
	/**
	 * 
	 * @return
	 */
	public static ArrayList<Agent> getActiveAgentList(){    
		final Iterable<Agent> agents = RunState.getInstance().getMasterContext().getObjects(Agent.class); // " get a reference to the current master context (since we have only that context) from the RunState instance assigned to the simulation, and query all of the Bug agents by specifying their class instance to the getObjects() function" ??
		final ArrayList<Agent> activeAgentList = new ArrayList<Agent>();
		for (final Agent agent : agents){
			if (agent.getSubstrateCategory() == "Algae" || agent.getSubstrateCategory() == "Coral"){
				activeAgentList.add(agent);
			}
		}
		return activeAgentList;
	}

	/**
	 * return a list of all the agents
	 */
	public static ArrayList<Agent> getAllAgentList(){    
		final Iterable<Agent> agents = RunState.getInstance().getMasterContext().getObjects(Agent.class); // " get a reference to the current master context (since we have only that context) from the RunState instance assigned to the simulation, and query all of the Bug agents by specifying their class instance to the getObjects() function" ??
		
//		do not make a list, use directly the iterable agents !!!
		
		final ArrayList<Agent> allAgentList = new ArrayList<Agent>();
		for (final Agent agent : agents){
			allAgentList.add(agent);
			}
		return allAgentList;
	}
	
	/*************************************************************************************************
	 *  NOTE: for the following functions, I could reduce the implementation time by making not 
	 *  ArrayList<Agent> listAllAgents = SMUtils.getAllAgentList(); every time but doing it instead 
	 *  in the class where the function is called and the modify the function as follow:
	 *  
	 *  public static ArrayList<Agent> getAllCoralAgentList(ArrayList<Agent> listAllAgents){
	 *************************************************************************************************
	 */
	
	/**
	 * return a list of all the coral agents, alive, dead or bleached, but not covered by algae
	 */
	public static ArrayList<Agent> getAllCoralAgentList(){    
		ArrayList<Agent> listAllAgents = SMUtils.getAllAgentList();
		ArrayList<Agent> listAllCoralAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
			   listAllCoralAgents.add(agent);
		   }
	    }
	    return listAllCoralAgents;
	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 * Offers the possibility to include or not bleached and dead corals
	 */
	public static ArrayList<Agent> getAllCoralAgentListFromList(ArrayList<Agent> listAllAgents, boolean evenBleached, boolean evenDead){    
		ArrayList<Agent> listAllCoralAgents = new ArrayList<Agent>();
		
		if(evenBleached && evenDead){  // include bleached and dead corals
		    for (Agent agent : listAllAgents){
				   if(agent.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
					   listAllCoralAgents.add(agent);
				   }
			 }
		}else if(evenBleached && !evenDead){ // include bleached but not dead corals
		    for (Agent agent : listAllAgents){
				   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL) || agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL)){
					   listAllCoralAgents.add(agent);
				   }
			 }		
		}else if(!evenBleached && evenDead){ // include dead but not bleached corals
		    for (Agent agent : listAllAgents){
				   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL) || agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL)){
					   listAllCoralAgents.add(agent);
				   }
			 }
		}else if(!evenBleached && !evenDead){ // does not include dead and bleached corals
		    for (Agent agent : listAllAgents){
				   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
					   listAllCoralAgents.add(agent);
				   }
			 }
		}
	    return listAllCoralAgents;
	}
	/**
	 * return a list of all the living coral agents
	 */
	public static ArrayList<Agent> getLivingCoralAgentList(){
		ArrayList<Agent> listAllAgents = SMUtils.getAllAgentList();      // TODO use ContextCoralReef2.listAllAgents instead
		ArrayList<Agent> listLivingCoralAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
			   listLivingCoralAgents.add(agent);
		   }
	    }
	    return listLivingCoralAgents;
	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating an extra additional list of agents when not necessary.
	 */
	public static ArrayList<Agent> getLivingCoralAgentListFromList(ArrayList<Agent> listAllAgents){
		ArrayList<Agent> listLivingCoralAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){		    	
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
			   listLivingCoralAgents.add(agent);
		   }
	    }
	    return listLivingCoralAgents;
	}
	
	/**
	 * returns a list of all the agents that are on a colony: corals and algae covering a colony
	 */
	public static  ArrayList<Agent> getAllColonyAgentList(){
		ArrayList<Agent> listAllAgents = SMUtils.getAllAgentList();
		ArrayList<Agent> listAllColonyAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
	    	int a = agent.getIDNumber();
		    if(a != 0){
			   listAllColonyAgents.add(agent);
		   }
	    }
		return listAllColonyAgents;
	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 * It also planar_area_colony = 0 is IDnumber == 0 and planar_area_colony > 0
	 */
	public static  ArrayList<Agent> getAllColonyAgentListFromList(ArrayList<Agent> listAllAgents){
		ArrayList<Agent> listAllColonyAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
	    	int a = agent.getIDNumber();
		    if(a != 0){
			   listAllColonyAgents.add(agent);
		   }else if(agent.getPlanarAreaColony() > 0){
			   agent.setPlanarAreaColony(0) ;       // agent not on a colony should have a planar_area_colony == 0
		   }
	    }
		return listAllColonyAgents;
	}
	
	/**
	 * return a list of all the Macroalgae agents, including those covering a coral community
	 */
	public static ArrayList<Agent> getAllMacroalgaeAgentList(){    
		ArrayList<Agent> listAllAgents = SMUtils.getAllAgentList();
		ArrayList<Agent> listAllMacroalgaeAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)){
			   listAllMacroalgaeAgents.add(agent);
		   }
	    }
	    return listAllMacroalgaeAgents;
	}
	
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 */
	public static ArrayList<Agent> getAllMacroalgaeAgentListFromList(ArrayList<Agent> listAllAgents){    
		ArrayList<Agent> listAllMacroalgaeAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)){
			   listAllMacroalgaeAgents.add(agent);
		   }
	    }
	    return listAllMacroalgaeAgents;
	}
	
//		/**
//		 * Met
//		 * Same a previous but agent are taken from an existing list instead of creating it.
//		 * This is done to reduce implementation time by not creating extra agent list when not necessary.
//		 * The agents are removed from the list. 
//		 */
//		public static List<ArrayList<Agent>> getAllMacroalgaeAgentListFromListRemove(ArrayList<Agent> listAllAgents){     
//			List<ArrayList<Agent>> listMacroAgents_RestAgents = new ArrayList<ArrayList<Agent>>();
//			ArrayList<Agent> listAllMacroalgaeAgents = new ArrayList<Agent>();
//			ArrayList<Agent> listAllAgents_Bis = (ArrayList) listAllAgents.clone();
//		    for (Agent agent : listAllAgents){
//			   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)){
//				   listAllMacroalgaeAgents.add(agent);
//				   listAllAgents_Bis.remove(agent) ;
//			   }
//		    }
//		    listMacroAgents_RestAgents.add(listAllMacroalgaeAgents) ;
//		    listMacroAgents_RestAgents.add(listAllAgents_Bis) ;
////		    System.out.printf("in getAllMacroalgaeAgentListFromListRemov, MAlist: %d AllAgentList: %d \n",listMacroAgents_RestAgents.get(0).size(),listMacroAgents_RestAgents.get(1).size());
////		    System.out.printf("in getAllMacroalgaeAgentListFromListRemov, MAlist: %d AllAgentList: %d \n",listAllMacroalgaeAgents.size(),listAllAgents_Bis.size());
//		    return listMacroAgents_RestAgents;
//		}
	
	/**
	 * Method that returns a list of 2 lists of agents: 1st, the list of algae agents of a certain substratSubactegory, and second a list of the remaining agents not put in the 1st list
	 * It take (1) the name of the substrateSubcategory targeted and (2) a list of agents from which the 2 returned lists are made (to reduce time of implementation).
	 * @param subCategoryAlgae, which is also the species name (species name is entered as a parameter)
	 * @param listAllAgents
	 * @return List<ArrayList<Agent>> listAlgaeAgents_RestAgents, at 0: listAlgaeAgents, at 1: 
	 */
	public static List<ArrayList<Agent>> getAlgaeAgentListFromListRemove(String subCategoryAlgae, ArrayList<Agent> listAllAgents){   
		List<ArrayList<Agent>> listAlgaeAgents_RestAgents = new ArrayList<ArrayList<Agent>>() ;  // list of 2 lists of agents: (1) the list of the algae agents of one type and (2) the list of rest of agents
		ArrayList<Agent> listAlgaeAgents = new ArrayList<Agent>() ;
		ArrayList<Agent> lisRestAgents = (ArrayList) listAllAgents.clone() ;	
		boolean agentIsAlgae = subCategoryAlgae.equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE) || subCategoryAlgae.equals(Constants.SUBSTRATE_SUBCATEGORY_TURF) || subCategoryAlgae.equals(Constants.SUBSTRATE_SUBCATEGORY_CCA) || 
						       subCategoryAlgae.equals(Constants.SUBSTRATE_SUBCATEGORY_AMA) || subCategoryAlgae.equals(Constants.SUBSTRATE_SUBCATEGORY_ACA) || subCategoryAlgae.equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA) ;			
		if(agentIsAlgae){
		    for (Agent agent : listAllAgents){
				   if(agent.getSubstrateSubCategory().equals(subCategoryAlgae)){
					   listAlgaeAgents.add(agent) ;
					   lisRestAgents.remove(agent) ;
				   }
		    }
		}else{
			System.out.printf("In SMUtils.getAlgaeAgentListFromListRemove(): wrong alage substratSubCategory entered %s vs. %s \n",Constants.SUBSTRATE_SUBCATEGORY_CCA,subCategoryAlgae);
		}
		listAlgaeAgents_RestAgents.add(listAlgaeAgents) ;
		listAlgaeAgents_RestAgents.add(lisRestAgents) ;
//			System.out.printf("in getAllMacroalgaeAgentListFromListRemov, MAlist: %d AllAgentList: %d \n",listMacroAgents_RestAgents.get(0).size(),listMacroAgents_RestAgents.get(1).size());
//			System.out.printf("in getAllMacroalgaeAgentListFromListRemov, MAlist: %d AllAgentList: %d \n",listAllMacroalgaeAgents.size(),listAllAgents_Bis.size());
	    return listAlgaeAgents_RestAgents ;
	}
	
	/**
	 * return a list of all the turf agents, including those covering a coral community
	 */
	public static ArrayList<Agent> getAllTurfAgentList(){    
		ArrayList<Agent> listAllAgents = SMUtils.getAllAgentList();
		ArrayList<Agent> listAllTurfAgents = new ArrayList<Agent>();		
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)){
			   listAllTurfAgents.add(agent);
		   }
	    }
	    return listAllTurfAgents;
	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 */
	public static ArrayList<Agent> getAllTurfAgentListFromList(ArrayList<Agent> listAllAgents){    
		ArrayList<Agent> listAllTurfAgents = new ArrayList<Agent>();		
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)){
			   listAllTurfAgents.add(agent);
		   }
	    }
	    return listAllTurfAgents;
	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 * The agents are removed from the list. 
	 */
	public static ArrayList<Agent> getAllTurfAgentListFromListRemove(ArrayList<Agent> listAllAgents){    
		ArrayList<Agent> listAllTurfAgents = new ArrayList<Agent>();	
		ArrayList<Agent> listAllTurfAgents_Bis = (ArrayList) listAllAgents.clone();
	    for (Agent agent : listAllTurfAgents_Bis){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)){
			   listAllTurfAgents.add(agent);
			   listAllAgents.remove(agent) ;
		   }
	    }
	    return listAllTurfAgents;
	}
	
	/**
	 * returns a list of all the CCA agents, including those covering a coral community
	 */
	public static ArrayList<Agent> getAllCCAAgentList(){    
		ArrayList<Agent> listAllAgents = SMUtils.getAllAgentList();
		ArrayList<Agent> listAllCCAAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
			   listAllCCAAgents.add(agent);
		   }
	    }
	    return listAllCCAAgents;
	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 */
	public static ArrayList<Agent> getAllCCAAgentListFromList(ArrayList<Agent> listAllAgents){    
		ArrayList<Agent> listAllCCAAgents = new ArrayList<Agent>();
	    for (Agent agent : listAllAgents){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
			   listAllCCAAgents.add(agent);
		   }
	    }
	    return listAllCCAAgents;
	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 * The agents are removed from the list.
	 */
	public static ArrayList<Agent> getAllCCAAgentListFromListRemove(ArrayList<Agent> listAllAgents){    
		ArrayList<Agent> listAllCCAAgents = new ArrayList<Agent>();
		ArrayList<Agent> listAllCCAAgents_Bis = (ArrayList) listAllAgents.clone();
	    for (Agent agent : listAllCCAAgents_Bis){
		   if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
			   listAllCCAAgents.add(agent);
			   listAllAgents.remove(agent) ;
		   }
	    }
	    return listAllCCAAgents;
	}
	
	/**
	 * Returns a list of 2 lists of barrengrounds agents, the 1st with barrenground agent, the 2nd with sand agents.
	 * Gives the option to convert solitary sand agent into barren ground to avoid grains of sand here and there.
	 * @param listAgent
	 * @return
	 */
	public static List<ArrayList<Agent>> getBarrenGroundAgentFromList(ArrayList<Agent> listAgent, boolean removeSolitarySand){
		List<ArrayList<Agent>> listBarrenGroundAgents = new ArrayList<ArrayList<Agent>>();
		ArrayList<Agent> sandAgents = new ArrayList<Agent>();
		ArrayList<Agent> barrenGroundAgents = new ArrayList<Agent>();
		for(Agent agent : listAgent){
			if(agent.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_BARREN_GROUND)){
				if(agent.getSpecies().equals(Constants.SPECIES_BARREN_GROUND)){
					barrenGroundAgents.add(agent);
				}else if(agent.getSpecies().equals(Constants.SPECIES_SAND)){
					if(removeSolitarySand) { // a solitary sand agent is converted to barren ground to avoid having sand grains everywhere
						if(agent.amIAloneFromListCondition(3,"sameSpecies")){ // if the 4 Von Newmann neighbohors are not sand --> conversion to barren ground
							agent.conversionToBarrenGround() ;
							barrenGroundAgents.add(agent) ;
						}else{
							sandAgents.add(agent) ;
						}
					}else {
						sandAgents.add(agent) ;
					}
				}else{
					System.out.printf("weird species name for substratum... in SMUtils.getBarrenGroundAgentFromList() \n");
				}
			}
		}	
		listBarrenGroundAgents.add(barrenGroundAgents);
		listBarrenGroundAgents.add(sandAgents);
		return listBarrenGroundAgents ;
	}
	
	/**
	 * return a list of all the agents that are ON the edge of a coral colony.
	 * They can be alive or dead coral agents, or algae agents sitting on a 
	 * coral colony.
	 */
//	public static ArrayList<Agent> getEdgeColonyAgentList(){    
//		ArrayList<Agent> EdgeColonyAgentList = new ArrayList<Agent>();
//		final ArrayList<Agent> listAllAgent =  getAllAgentList();
//		for (final Agent colonyAgent : listAllAgent){
//			int a = colonyAgent.IDNumber;
//			if (a != 0){   
//				ArrayList<Agent> neighborhood = getAgentsInRadiusFromList(colonyAgent,1,listAllAgent);
//				for (Agent neighborAgent : neighborhood){
//					int b = neighborAgent.getIDNumber();
//					if (a != b){    								// if the neighboring agent does not belong to the same colony or is not on a colony
//						EdgeColonyAgentList.add(colonyAgent);		// so colonyAgent is added only once in the process
//					}
//				}
//			}
//		}
//		return EdgeColonyAgentList;  
//	}
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 */
//	public static ArrayList<Agent> getEdgeColonyAgentListFromList(ArrayList<Agent>listAllAgent){    
//		ArrayList<Agent> EdgeColonyAgentList = new ArrayList<Agent>();
//		for (final Agent colonyAgent : listAllAgent){
//			int a = colonyAgent.IDNumber;
//			if (a != 0){   
//				ArrayList<Agent> neighborhood = getAgentsInRadiusFromList(colonyAgent,1,listAllAgent); 
//				for (Agent neighborAgent : neighborhood){
//					int b = neighborAgent.getIDNumber();
//					if (a != b){    								// if the neighboring agent does not belong to the same colony or is not on a colony
//						EdgeColonyAgentList.add(colonyAgent);		// so colonyAgent is added only once in the process
//					}
//				}
//			}
//		}
//		return EdgeColonyAgentList;  
//	}
	
	/**
	 * return a list of all the CORAL agents that are ON the edge of a coral colony.
	 * They can be alive or dead or bleached coral agents.
	 * In addition, if includeAlgaeCoveringColony = True, algae agents covering a colony are also included
	 * in the list.
	 */
	public static ArrayList<Agent> getEdgeColonyCoralAgentList(boolean includeAlgaeCoveringColony){  
		final ArrayList<Agent> listAllAgent = getAllAgentList();
		ArrayList<Agent> listAllCoralAgent = new ArrayList<Agent>();;
		if(includeAlgaeCoveringColony){
			listAllCoralAgent =  getAllColonyAgentListFromList(listAllAgent);	
		}else {
			listAllCoralAgent =  getAllCoralAgentListFromList(listAllAgent,true,true);	
		}
		ArrayList<Agent> EdgeColonyCoralAgentList = new ArrayList<Agent>();
		for (final Agent edgeCoralAgent : listAllCoralAgent){
			final int a = edgeCoralAgent.getIDNumber();				
			ArrayList<Agent> neighborhood = getAgentsInRadiusFromList(edgeCoralAgent,1,listAllAgent);
			for (Agent neighborAgent : neighborhood){
				final int b = neighborAgent.getIDNumber();		
				if (a != b){    // if the neighboring agent does not belong to the same colony or does not have a colony
					EdgeColonyCoralAgentList.add(edgeCoralAgent);
					break;
				}
			}
		}
		return EdgeColonyCoralAgentList;  
	}	
	
	/**
	 * Same a previous but agent are taken from an existing list instead of creating it.
	 * This is done to reduce implementation time by not creating extra agent list when not necessary.
	 */
	public static ArrayList<Agent> getEdgeColonyCoralAgentListFromList(ArrayList<Agent> listAllAgent, boolean includeAlgaeCoveringColony){  
		ArrayList<Agent> listAllColonyAgent = new ArrayList<Agent>();;
		if(includeAlgaeCoveringColony){
			listAllColonyAgent =  getAllColonyAgentListFromList(listAllAgent);	
		}else {
			listAllColonyAgent =  getAllCoralAgentListFromList(listAllAgent,true,true);	
		}
		ArrayList<Agent> EdgeColonyCoralAgentList = new ArrayList<Agent>();
		for (final Agent edgeCoralAgent : listAllColonyAgent){
			final int a = edgeCoralAgent.getIDNumber();				
			ArrayList<Agent> neighborhood = getAgentsInRadiusFromList(edgeCoralAgent,1,listAllAgent);
			for (Agent neighborAgent : neighborhood){
				final int b = neighborAgent.getIDNumber();		
				if (a != b){    // if the neighboring agent does not belong to the same colony or does not have a colony
					EdgeColonyCoralAgentList.add(edgeCoralAgent);						
					break;
				}
			}
		}
		return EdgeColonyCoralAgentList;  
	}
	/**
	 * function that returns a list of agents that have the same IDNumber (belong to / are on the same colony  
	 * that the agent used as a parameter by the function, the latter being also included in the list.
	 * The agents can be alive, dead or bleached coral, or algae covering a colony.
	 */
//		public static ArrayList<Agent> getAgentsFromSameColony(Agent chosenAgent){
//			ArrayList<Agent> listAgentSameColony = new ArrayList<Agent>();
//			final ArrayList<Agent> listAllAgents = getAllAgentList();
//			//listAgentSameColony.add(chosenAgent);  --> no need to do that, otherwise the agent is added twice
//			int A = chosenAgent.getIDNumber();
//			if(A == 0){
//				System.out.printf("ATTENTION, AGENT IS NOT ON A COLONY \n") ;
//			}	
//			for (Agent agent : listAllAgents){
//				int B = agent.getIDNumber();
//				if (A == B){
//					listAgentSameColony.add(agent);
//				}
//			}
//			return listAgentSameColony;
//		}
	
	/**
	 * Same as above except the agents are chosen from a list that is also used as a parameter 
	 * so as to reduce computational time in the case the list is already made.
	 */
	public static ArrayList<Agent> getAgentsFromSameColonyFromList(Agent chosenAgent,ArrayList<Agent> listAgents){
		ArrayList<Agent> listAgentSameColony = new ArrayList<Agent>();
		//listAgentSameColony.add(chosenAgent);  --> no need to do that, other the agent is added twice
		int A = chosenAgent.getIDNumber();
		if(A == 0){
			System.out.printf("SMUtils.getAgentsFromSameColonyFromList(): ATTENTION, AGENT IS NOT ON A COLONY \n") ;
		}
		for (Agent agent : listAgents){
			int B = agent.getIDNumber();
			if (A == B){
				listAgentSameColony.add(agent);
			}
		}
		return listAgentSameColony;
	}
	
	/**
	 * function that returns all the coral agents that belong to the species name entered as a parameter.
	 * it offers the option to include bleached and/or dead coral
	 * but not an algae agent that covers a colony
	 */
	public static ArrayList<Agent> getListCoralAgentFromSameSpecies(String speciesName, boolean evenBleached, boolean evenDead){
		ArrayList<Agent> listCoralAgentSameSpecies = new ArrayList<Agent>();
		final ArrayList<Agent> listAllCoralAgent =  getAllCoralAgentList();
		
		if (evenBleached == false && evenDead == false){			// online alive coral agents
			for (Agent coral : listAllCoralAgent){
				if(coral.getSpecies().equals(speciesName) && coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}else if (evenBleached == true && evenDead == false){		// alive or bleached
			for (Agent coral : listAllCoralAgent){
				boolean a = coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)   || 
						    coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL) ;
				if(coral.getSpecies().equals(speciesName) && a ){
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}else if (evenBleached == false && evenDead == true){		// alive or dead
			for (Agent coral : listAllCoralAgent){
				boolean a = coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)   || 
						    coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL) ;
				if(coral.getSpecies().equals(speciesName) && a ){
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}else if (evenBleached == true && evenDead == true){		// alive or bleach or dead but not algae
			for (Agent coral : listAllCoralAgent){
				if(coral.getSpecies().equals(speciesName) && coral.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_CORAL)){     
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}
		return listCoralAgentSameSpecies;
	}
	/**
	 * Same as before but the agents are taken from a pre-made list that need to be enter as a parameter
	 */
	public static ArrayList<Agent> getListCoralAgentFromSameSpeciesFromList(String speciesName, ArrayList<Agent>listAllCoralAgent, boolean evenBleached, boolean evenDead){
		ArrayList<Agent> listCoralAgentSameSpecies = new ArrayList<Agent>();
		
		if (evenBleached == false && evenDead == false){			// online alive coral agents
			for (Agent coral : listAllCoralAgent){
				if(coral.getSpecies().equals(speciesName) && coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}else if (evenBleached == true && evenDead == false){		// alive or bleached
			for (Agent coral : listAllCoralAgent){
				boolean a = coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)   || 
						    coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL) ;
				if(coral.getSpecies().equals(speciesName) && a ){
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}else if (evenBleached == false && evenDead == true){		// alive or dead
			for (Agent coral : listAllCoralAgent){
				boolean a = coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)   || 
						    coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL) ;
				if(coral.getSpecies().equals(speciesName) && a ){
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}else if (evenBleached == true && evenDead == true){		// alive or bleach or dead but not algae
			for (Agent coral : listAllCoralAgent){
				if(coral.getSpecies().equals(speciesName) && coral.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_CORAL)){     
					listCoralAgentSameSpecies.add(coral);
				}
			}
		}
		return listCoralAgentSameSpecies;
	}
	/**
	 * Same as before but the agents are taken from a pre-made list that need to be enter as a parameter.
	 * Plus the agents are removed from the list listAllCoralAgent.
	 */
	
	/**
	 * Returns a list of 2 lists: (1) of coral agents from a certain species (name entered as a parameter) and (2) a list of remaining agents that were not put in the first list
	 * Gives the option to include coral agents that are dead and/or bleached
	 * @param speciesName
	 * @param listAllAgent
	 * @param evenBleached
	 * @param evenDead
	 * @return
	 */
	public static List<ArrayList<Agent>> getListCoralAgentFromSameSpeciesFromListRemove(String speciesName,ArrayList<Agent>listAllAgent,boolean evenBleached,boolean evenDead){	
		List<ArrayList<Agent>> listCoralAgents_RestAgents = new ArrayList<ArrayList<Agent>>() ;
		ArrayList<Agent> listCoralAgentSameSpecies = new ArrayList<Agent>();
		ArrayList<Agent> listAllAgent_rest = (ArrayList) listAllAgent.clone();		
		if (evenBleached == false && evenDead == false){			// online alive coral agents
			for (Agent coral : listAllAgent){
				if(coral.getSpecies().equals(speciesName) && coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
					listCoralAgentSameSpecies.add(coral);
					listAllAgent_rest.remove(coral) ;
				}
			}
		}else if (evenBleached == true && evenDead == false){		// alive or bleached
			for (Agent coral : listAllAgent){
				boolean a = coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)   || 
						    coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL) ;
				if(coral.getSpecies().equals(speciesName) && a ){
					listCoralAgentSameSpecies.add(coral);
					listAllAgent_rest.remove(coral) ;
				}
			}
		}else if (evenBleached == false && evenDead == true){		// alive or dead
			for (Agent coral : listAllAgent){
				boolean a = coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)   || 
						    coral.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL) ;
				if(coral.getSpecies().equals(speciesName) && a ){
					listCoralAgentSameSpecies.add(coral);
					listAllAgent_rest.remove(coral) ;
				}
			}
		}else if (evenBleached == true && evenDead == true){		// alive or bleach or dead but not algae
			for (Agent coral : listAllAgent){
				if(coral.getSpecies().equals(speciesName) && coral.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_CORAL)){     
					listCoralAgentSameSpecies.add(coral);
					listAllAgent_rest.remove(coral) ;
				}
			}
		}
		listCoralAgents_RestAgents.add(listCoralAgentSameSpecies);
		listCoralAgents_RestAgents.add(listAllAgent_rest);
		return listCoralAgents_RestAgents ;
	}
	
	/**
	 * Methods that iterate through listAllAgent and classify agent agents into different arrayLiat of agent.
	 * The arrayList of agents are then place into a list which is returned.
	 * @param listAllAgent
	 * @return
	 */
	public static List<ArrayList<Agent>> getListArrayListAgentCategories(ArrayList<Agent> listAllAgent){
		List<ArrayList<Agent>> listArrayListAgentCategorie = new ArrayList<ArrayList<Agent>>() ;
		ArrayList<Agent> listCoralAgentAlive = new ArrayList<Agent>() ;
		ArrayList<Agent> listMacrolagaeAgent = new ArrayList<Agent>() ;
		ArrayList<Agent> listTurfAgent = new ArrayList<Agent>() ;
		ArrayList<Agent> listCCAAgent = new ArrayList<Agent>() ;
		ArrayList<Agent> listAMAAgent = new ArrayList<Agent>() ;
		ArrayList<Agent> listACAAgent = new ArrayList<Agent>() ;
		ArrayList<Agent> listHalimedaAgent = new ArrayList<Agent>() ;
		ArrayList<Agent> listBarrenGroundAgent = new ArrayList<Agent>() ;
		ArrayList<Agent> listSandAgent = new ArrayList<Agent>() ;

		ArrayList<Agent> listColonyAgent = new ArrayList<Agent>() ;
		
		for(Agent ag : listAllAgent) {
			String substratSubCat = ag.getSubstrateSubCategory() ;
			if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)) {
				listCoralAgentAlive.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)) {
				listMacrolagaeAgent.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)) {
				listTurfAgent.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)) {
				listCCAAgent.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_AMA)) {
				listAMAAgent.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_ACA)) {
				listACAAgent.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA)) {
				listHalimedaAgent.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_BARREN_GROUND)) {
				String speciesAgent = ag.getSpecies() ;
				if(speciesAgent.equals(Constants.SPECIES_BARREN_GROUND)) {
					listBarrenGroundAgent.add(ag) ;
				}else if(speciesAgent.equals(Constants.SPECIES_SAND)) {
					listSandAgent.add(ag) ;
				}
			}
			if(ag.getIDNumber() != 0) {
				listColonyAgent.add(ag) ;
			}else if(substratSubCat.equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
				System.out.printf("SMUtils.getListArrayListAgentCategories(): a living coral agent is not on a colony !!! \n") ;
			}
		}
		listArrayListAgentCategorie.add(listCoralAgentAlive) ;
		listArrayListAgentCategorie.add(listMacrolagaeAgent) ;
		listArrayListAgentCategorie.add(listTurfAgent) ;
		listArrayListAgentCategorie.add(listCCAAgent) ;
		listArrayListAgentCategorie.add(listAMAAgent) ;
		listArrayListAgentCategorie.add(listACAAgent) ;
		listArrayListAgentCategorie.add(listHalimedaAgent) ;
		listArrayListAgentCategorie.add(listBarrenGroundAgent) ;
		listArrayListAgentCategorie.add(listSandAgent) ;
		listArrayListAgentCategorie.add(listColonyAgent) ;

		return listArrayListAgentCategorie ;
	}
	
	
	/** NOT USED 
	 * Methods that creates an identical list of agents as the one put as a parameter
	 */
	public static ArrayList<Agent> duplicateListeAgent(ArrayList<Agent> listAgent){
		ArrayList<Agent> duplicateList = new ArrayList<Agent>();
		for(Agent agent : listAgent){
			duplicateList.add(agent);
		}
		return duplicateList;
	}
		
	/**
	 * method that returns a list of gridCells within a certain radius around a central gridCell but from a list of gridCells
	 * the central gridcell IS included in the list
	 * @param gridCell
	 * @param radius
	 * @param listGridCells
	 */
	public static <T> List<GridCell<Agent>> getCellsInRadiusFromList (GridCell<Agent> gridCell,double radius,List<GridCell<Agent>> listGridCells){
		List<GridCell<Agent>> inRadiusListFromList = new ArrayList<GridCell<Agent>>();
		final GridPoint center = gridCell.getPoint();
		// convert the double radius into the inferior higher integer
//		final int radiusRounded = (int)radius;
		for (final GridCell<Agent> gc : listGridCells){
			final GridPoint point = gc.getPoint();
			final double distance = getDistance(center, point);
			if (distance <= radius){			
				inRadiusListFromList.add(gc);

		//		listGridCells.remove(gc);
			}
		}
		return inRadiusListFromList;
	}
	
//	method that return a list of gridCells within a certain radius around a central gridCell
// central gridcell is not included in the list
// the method consider the possibility of being on the edge of the reef
//	@param the centrat gridCell
//	@param the length of the radius
	public static <T> List<GridCell<Agent>> getCellsInRadius (final GridCell<Agent> centerCell, final double radius){
		List<GridCell<Agent>> inRadiusList = new ArrayList<GridCell<Agent>>();
		final GridPoint center = centerCell.getPoint();
		// convert the double radius into the inferior higher integer
		final int radiusRounded = (int)(radius+0.5) ; // 0.5 so rounded properly because (int) always rounds to the smaller interger
		
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// use Math.min so the circle does not go out of the grid		
		
		for (int i = Math.max(center.getX() - radiusRounded, 0) ; i <= Math.min(center.getX() + radiusRounded, borderRight-1) ; i++){	
			for (int j = Math.max(center.getY() - radiusRounded, 0) ; j <= Math.min(center.getY() + radiusRounded, borderTop - 1) ; j++){
				final GridPoint point = new GridPoint(i,j);
				final double distance = getDistance(center, point);
				if (distance <= radiusRounded && distance != 0){		// 'distance != 0' so that 'gridCell' is not included in the list
					final GridCell<Agent> gridCellij = new GridCell(point, Agent.class);
					inRadiusList.add(gridCellij);
				}
			}
		}
		return inRadiusList ;
	}
	/** CHECKED for radius = 1
	 *  methods that returns a list of agents that are in a certain perimeter around 
	 *  the agent of focus T
	 *  The agent of focus is included in the list.
	 */
	public static <T> ArrayList<Agent> getAgentsInRadius (Agent agent, double radius){    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		final ArrayList<Agent> listAllAgents = getAllAgentList();
		ArrayList<Agent> inRadiusAgentsList = new ArrayList<Agent>();
		double areaCircle = 0 ;
		if(radius == 1){
			areaCircle = 5 ;
		}else{
			areaCircle = Math.PI * Math.pow(radius,2) ;
		}
		for (final Agent ag : listAllAgents){
			final double distance = getDistanceBetween2Agents(agent, ag);
			if (distance <= radius){			
				inRadiusAgentsList.add(ag);
			}
			if(inRadiusAgentsList.size() >= areaCircle){
				break ;
			}
		}
		return inRadiusAgentsList ;
	}
	
	/**
	 *  methods that returns a list of agents that are in a certain perimeter around 
	 *  the agent of focus T
	 *  The agent of focus is included in the list.
	 *  Here, the agents come from a list.
	 */
	public static <T> ArrayList<Agent> getAgentsInRadiusFromList (Agent agent, double radius, List<Agent> listAgents){
		ArrayList<Agent> inRadiusAgentsListFromList = new ArrayList<Agent>();
		double areaCircle = 0 ;
		if(radius == 1){
			areaCircle = 5 ;
		}else{
			areaCircle = Math.PI * Math.pow(radius,2) ;
		}
		for (final Agent ag : listAgents){
			final double distance = getDistanceBetween2Agents(agent, ag);
			if (distance <= radius){			
				inRadiusAgentsListFromList.add(ag);
				if(inRadiusAgentsListFromList.size() >= areaCircle){
					break ;
				}
			}
		}
		return inRadiusAgentsListFromList;
	}
	   
	/**
	 *  Methods that returns a list of agents that are in a certain perimeter around 
	 *  the agent of focus T
	 *  The agent of focus is included in the list.
	 *  Way faster than getAgentsInRadius() because we don't have to go through all the agents. Instead the method
	 *  1st focus on the cell grid on the agent of focus and then select the neighboring gridcells within the specified radius
	 *  length. Finally, it returns the agents within these gridcells.
	 * @param agent
	 * @param radius
	 * @return
	 */
	public static <T> ArrayList<Agent> getAgentsInRadiusBis (Agent agent, double radius){
		
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
//	System.out.printf("radius: %.1f x : %d y : %d  \n", radius,agent.getAgentX(),agent.getAgentY());
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
		
		Object object = grid.getObjectAt(X,Y) ;

		final int radiusRounded = (int)(radius+0.5) ; // 0.5 so rounded properly because (int) always rounds to the smaller interger
		int minX =  X - radiusRounded ;
		int maxX =  X +  radiusRounded ;
		int minY =  Y - radiusRounded ;
		int maxY =  Y +  radiusRounded ;
				
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// use Math.min so the circle does not go out of the grid		
//		System.out.printf("in list objects: \n");

		for (int i = minX ; i <= maxX ; i++){
			for(int j = minY ; j <= maxY ; j++){
				boolean bool = i < 0 || i > borderRight - 1 || j < 0 || j > borderTop - 1 ;
				if(! bool){
//				System.out.printf("i : %d j : %d \n", i,j);
					for(Object obj :  grid.getObjectsAt(i,j)){
						if(obj instanceof Agent){
							if(getDistanceBetween2Agents(agent,(Agent)obj) <= radius){
								listAgents.add((Agent) obj );
							}
						}
					}
				}
			}
		}
		return  listAgents ;
	}
	
	/**
	 *  Same as getAgentsInRadiusBis() but only take the agents that have not been grazed yet
	 *  the agent of focus T
	 *  The agent of focus is included in the list.
	 *  Way faster than getAgentsInRadius() because we don;t have to go through all the agents
	 */   
	public static <T> ArrayList<Agent> getAgentsInRadiusBisOnlyNotGrazed (Agent agent, double radius){
		
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
//		System.out.printf("radius: %.1f x : %d y : %d  \n", radius,agent.getAgentX(),agent.getAgentY());
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
		
//		Object object = grid.getObjectAt(X,Y) ;

		final int radiusRounded = (int)(radius+0.5) ; // 0.5 so rounded properly because (int) always rounds to the smaller interger
		int minX =  X - radiusRounded ;
		int maxX =  X +  radiusRounded ;
		int minY =  Y - radiusRounded ;
		int maxY =  Y +  radiusRounded ;
				
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// use Math.min so the circle does not go out of the grid		
//		System.out.printf("in list objects: \n");
		for (int i = minX ; i <= maxX ; i++){
			for(int j = minY ; j <= maxY ; j++){
				boolean bool = i < 0 || i > borderRight - 1 || j < 0 || j > borderTop - 1 ; // so we don't go out of the grid
				if(! bool){
//					System.out.printf("i : %d j : %d \n", i,j);
					for(Object obj :  grid.getObjectsAt(i,j)){
						if(obj instanceof Agent){
							if(getDistanceBetween2Agents(agent,(Agent)obj) <= radius){
								if( ! ((Agent) obj).getHaveIBeenGrazed() ){   // only of the agent has not been grazed
									listAgents.add((Agent) obj );
								}								
							}
						}
					}
				}
			}
		}
		return  listAgents ;
	}
	
	/**
	 *  methods that returns a list of agents that are in a certain perimeter around 
	 *  the agent of focus T
	 *  The agent of focus is included in the list.
	 *  The agent is added to the list returned only if it is present on in the InputList.
	 */   
	public static <T> ArrayList<Agent> getAgentsInRadiusBisFromList (Agent agent, double radius, List<Agent> InputList){
		
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
//		System.out.printf("radius: %.1f x : %d y : %d  \n", radius,agent.getAgentX(),agent.getAgentY());
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
		
		Object object = grid.getObjectAt(X,Y) ;

		final int radiusRounded = (int)(radius+0.5) ; // 0.5 so rounded properly because (int) always rounds to the smaller interger
		int minX =  X - radiusRounded ;
		int maxX =  X +  radiusRounded ;
		int minY =  Y - radiusRounded ;
		int maxY =  Y +  radiusRounded ;
				
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// use Math.min so the circle does not go out of the grid		
//		System.out.printf("in list objects: \n");

		for (int i = minX ; i <= maxX ; i++){
			for(int j = minY ; j <= maxY ; j++){
				boolean bool = i < 0 || i > borderRight - 1 || j < 0 || j > borderTop - 1 ;
				if(! bool){
//					System.out.printf("i : %d j : %d \n", i,j);
					for(Object obj :  grid.getObjectsAt(i,j)){
						if(obj instanceof Agent){
							for(Agent ag : InputList){
								if(ag.getAgentX() == i && ag.getAgentY() == j && getDistanceBetween2Agents(agent,ag) <= radius){  // this take time because if the Math.sqrt command
									listAgents.add((Agent) obj) ;
								}
							}
						}
					}
				}
			}
		}	
		return  listAgents ;
	}

	/**
	 * return a list of agents present in the Von Neumann neigborhood 
	 * @param agent
	 * @param includeCenterAgent
	 * @return
	 */
	public static ArrayList<Agent> getVonNeumannNeighbors(Agent agent, boolean includeCenterAgent){
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
//		System.out.printf("centerAgent: X: %d Y: %d ", X,Y);
		int minX =  X - 1 ;
		int maxX =  X +  1 ;
		int minY =  Y - 1 ;
		int maxY =  Y +  1 ;
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// in case agent is on the edge of the reef
		if(minX < 0) {
			minX = 0 ;
		}
		if(maxX > borderRight) {
			maxX = borderRight ;
		}
		if(minY < 0) {
			minY = 0 ;
		}
		if(maxY > borderTop) {
			maxY = borderRight ;
		}
//		System.out.printf("in list objects: \n");
		for (int x = minX ; x <= maxX ; x++){
			for(int y = minY ; y <= maxY ; y++){
				for(Object obj :  grid.getObjectsAt(x,y)) {
					if(obj instanceof Agent) {
						boolean centerAgent = x == X && y == Y ;
						boolean VNN = (x == X && y == minY) || (x == X && y == maxY) || (x == minX && y == Y) || (x == maxX && y == Y) ;
						boolean includeAgent = (centerAgent && includeCenterAgent) || VNN ;
						if(includeAgent) {
							listAgents.add((Agent)obj) ;
//							System.out.printf("VNN agent: X: %d Y: %d ", ((Agent)obj).getAgentX(), ((Agent)obj).getAgentY());
						}
					}	
				}
			}
		}	
//		System.out.printf("\n") ;
		return  listAgents ;
	}
	
	/**
	 * return a list of agents present in the Moore neigborhood 
	 * @param agent
	 * @param includeCenterAgent
	 * @return
	 */
	public static ArrayList<Agent> getMooreNeighbors(Agent agent, boolean includeCenterAgent){
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
//		System.out.printf("centerAgent: X: %d Y: %d ", X,Y);
		int minX =  X - 1 ;
		int maxX =  X +  1 ;
		int minY =  Y - 1 ;
		int maxY =  Y +  1 ;
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// in case agent is on the edge of the reef
		if(minX < 0) {
			minX = 0 ;
		}
		if(maxX > borderRight) {
			maxX = borderRight ;
		}
		if(minY < 0) {
			minY = 0 ;
		}
		if(maxY > borderTop) {
			maxY = borderRight ;
		}
//		System.out.printf("in list objects: \n");
		for (int x = minX ; x <= maxX ; x++){
			for(int y = minY ; y <= maxY ; y++){
				for(Object obj :  grid.getObjectsAt(x,y)) {
					if(obj instanceof Agent) {
						boolean centerAgent = x == X && y == Y ;
						boolean includeAgent = (centerAgent && includeCenterAgent) || !centerAgent ;
						if(includeAgent) {
							listAgents.add((Agent)obj) ;
//							System.out.printf("VNN agent: X: %d Y: %d ", ((Agent)obj).getAgentX(), ((Agent)obj).getAgentY());
						}
					}	
				}
			}
		}	
//		System.out.printf("\n") ;
		return  listAgents ;
	}
	
	public static ArrayList<Agent> getRadius2Neighbors(Agent agent, boolean includeCenterAgent){
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
//		System.out.printf("centerAgent: X: %d Y: %d ", X,Y);
		int minX1 =  X - 1 ;
		int maxX1 =  X + 1 ;
		int minX2 =  X - 2 ;
		int maxX2 =  X + 2 ;
		int minY1 =  Y - 1 ;
		int maxY1 =  Y + 1 ;
		int minY2 =  Y - 2 ;
		int maxY2 =  Y + 2 ;
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// in case agent is on the edge of the reef
		if(minX1 < 0) {
			minX2 = 0 ;
			minX1 = 0 ;
		}else if(minX2 < 0) {
			minX2 = minX1 ;
		}
		if(maxX1 > borderRight) {
			maxX1 = borderRight ;
			maxX2 = borderRight ;
		}else if(maxX2 > borderRight) {
			maxX2 = maxX1 ;
		}
		if(minY1 < 0) {
			minY2 = 0 ;
			minY1 = 0 ;
		}else if(minY2 < 0) {
			minY2 = minY1 ;
		}
		if(maxY1 > borderTop) {
			maxY1 = borderTop ;
			maxY2 = borderTop ;
		}else if(maxY2 > borderTop) {
			maxY2 = maxY1 ;
		}
//		System.out.printf("in list objects: \n");
		for (int x = minX2 ; x <= maxX2 ; x++){
			for(int y = minY2 ; y <= maxY2 ; y++){
				for(Object obj :  grid.getObjectsAt(x,y)) {
					if(obj instanceof Agent) {
						boolean centerAgent = x == X && y == Y ;
						boolean forbiddenExtreams = ((x == maxX2 || x == minX2) && y != Y) || (x != X && (y == minY2 || y == maxY2)) ;
						boolean includeAgent = ((centerAgent && includeCenterAgent) || !centerAgent) && !forbiddenExtreams ;
						if(includeAgent) {
							listAgents.add((Agent)obj) ;
//							System.out.printf("VNN agent: X: %d Y: %d ", ((Agent)obj).getAgentX(), ((Agent)obj).getAgentY());
						}
					}	
				}
			}
		}	
//		System.out.printf("\n") ;
		return  listAgents ;
	}
	
	
	public static ArrayList<Agent> getRadius2NeighborsNotGrazed(Agent agent, boolean includeCenterAgent){
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
//		System.out.printf("centerAgent: X: %d Y: %d ", X,Y);
		int minX1 =  X - 1 ;
		int maxX1 =  X + 1 ;
		int minX2 =  X - 2 ;
		int maxX2 =  X + 2 ;
		int minY1 =  Y - 1 ;
		int maxY1 =  Y + 1 ;
		int minY2 =  Y - 2 ;
		int maxY2 =  Y + 2 ;
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// in case agent is on the edge of the reef
		if(minX1 < 0) {
			minX2 = 0 ;
			minX1 = 0 ;
		}else if(minX2 < 0) {
			minX2 = minX1 ;
		}
		if(maxX1 > borderRight) {
			maxX1 = borderRight ;
			maxX2 = borderRight ;
		}else if(maxX2 > borderRight) {
			maxX2 = maxX1 ;
		}
		if(minY1 < 0) {
			minY2 = 0 ;
			minY1 = 0 ;
		}else if(minY2 < 0) {
			minY2 = minY1 ;
		}
		if(maxY1 > borderTop) {
			maxY1 = borderTop ;
			maxY2 = borderTop ;
		}else if(maxY2 > borderTop) {
			maxY2 = maxY1 ;
		}
//		System.out.printf("in list objects: \n");
		for (int x = minX2 ; x <= maxX2 ; x++){
			for(int y = minY2 ; y <= maxY2 ; y++){
				for(Object obj :  grid.getObjectsAt(x,y)) {
					if(obj instanceof Agent) {
						boolean centerAgent = x == X && y == Y ;
						boolean forbiddenExtreams = ((x == maxX2 || x == minX2) && y != Y) || (x != X && (y == minY2 || y == maxY2)) ;
						boolean includeAgent = ((centerAgent && includeCenterAgent) || !centerAgent) && !forbiddenExtreams ;
						boolean isGrazedAlready = ((Agent)obj).getHaveIBeenGrazed() ;
						
						if(includeAgent && !isGrazedAlready) {
							listAgents.add((Agent)obj) ;
//							System.out.printf("VNN agent: X: %d Y: %d ", ((Agent)obj).getAgentX(), ((Agent)obj).getAgentY());
						}
					}	
				}
			}
		}	
//		System.out.printf("\n") ;
		return  listAgents ;
	}
	
	public static ArrayList<Agent> getRadius3Neighbors(Agent agent, boolean includeCenterAgent){
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
//		System.out.printf("centerAgent: X: %d Y: %d ", X,Y);
		int minX1 =  X - 1 ;
		int maxX1 =  X + 1 ;
		int minX2 =  X - 2 ;
		int maxX2 =  X + 2 ;
		int minX3 =  X - 3 ;
		int maxX3 =  X + 3 ;
		int minY1 =  Y - 1 ;
		int maxY1 =  Y + 1 ;
		int minY2 =  Y - 2 ;
		int maxY2 =  Y + 2 ;
		int minY3 =  Y - 3 ;
		int maxY3 =  Y + 3 ;
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// in case agent is on the edge of the reef
		if(minX1 < 0) {
			minX3 = 0 ;
			minX2 = 0 ;
			minX1 = 0 ;
		}else if(minX2 < 0) {
			minX3 = minX1 ;
			minX2 = minX1 ;
		}else if(minX3 < 0) {
			minX3 = minX2 ;
		}
		if(maxX1 > borderRight) {
			maxX1 = borderRight ;
			maxX2 = borderRight ;
			maxX3 = borderRight ;
		}else if(maxX2 > borderRight) {
			maxX2 = maxX1 ;
			maxX3 = maxX2 ;
		}else if(maxX3 > borderRight) {
			maxX3 = maxX2 ;
		}
		if(minY1 < 0) {
			minY2 = 0 ;
			minY1 = 0 ;
			minY3 = 0 ;
		}else if(minY2 < 0) {
			minY2 = minY1 ;
			minY3 = minY1 ;
		}else if(minY3 < 0) {
			minY3 = minY2 ;
		}
		if(maxY1 > borderTop) {
			maxY1 = borderTop ;
			maxY2 = borderTop ;
			maxY3 = borderTop ;
		}else if(maxY2 > borderTop) {
			maxY2 = maxY1 ;
			maxY3 = maxY1 ;
		}else if(maxY3 > borderTop) {
			maxY3 = maxY2 ;
		}
//		System.out.printf("in list objects: \n");
		for (int x = minX3 ; x <= maxX3 ; x++){
			for(int y = minY3 ; y <= maxY3 ; y++){
				for(Object obj :  grid.getObjectsAt(x,y)) {
					if(obj instanceof Agent) {
						boolean centerAgent = x == X && y == Y ;
						boolean forbiddenExtreams = ((y == minY3 || y == maxY3) && x != X) ||  ((x == minX3 || x == maxX3) && y != Y ) ;
						boolean includeAgent = ((centerAgent && includeCenterAgent) || !centerAgent) && !forbiddenExtreams ;
						if(includeAgent) {
							listAgents.add((Agent)obj) ;
//							System.out.printf("VNN agent: X: %d Y: %d ", ((Agent)obj).getAgentX(), ((Agent)obj).getAgentY());
						}
					}	
				}
			}
		}	
//		System.out.printf("\n") ;
		return  listAgents ;
	}
	
	/**
	 * 
	 * @param agent
	 * @param includeCenterAgent
	 * @param condition
	 * @return
	 */
	public static ArrayList<Agent> getRadius3NeighborsCondition(Agent agent, boolean includeCenterAgent, String condition){
		ArrayList<Agent> listAgents = new ArrayList<Agent>();
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
//		System.out.printf("centerAgent: X: %d Y: %d ", X,Y);
		int minX1 =  X - 1 ;
		int maxX1 =  X + 1 ;
		int minX2 =  X - 2 ;
		int maxX2 =  X + 2 ;
		int minX3 =  X - 3 ;
		int maxX3 =  X + 3 ;
		int minY1 =  Y - 1 ;
		int maxY1 =  Y + 1 ;
		int minY2 =  Y - 2 ;
		int maxY2 =  Y + 2 ;
		int minY3 =  Y - 3 ;
		int maxY3 =  Y + 3 ;
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// in case agent is on the edge of the reef
		if(minX1 < 0) {
			minX3 = 0 ;
			minX2 = 0 ;
			minX1 = 0 ;
		}else if(minX2 < 0) {
			minX3 = minX1 ;
			minX2 = minX1 ;
		}else if(minX3 < 0) {
			minX3 = minX2 ;
		}
		if(maxX1 > borderRight) {
			maxX1 = borderRight ;
			maxX2 = borderRight ;
			maxX3 = borderRight ;
		}else if(maxX2 > borderRight) {
			maxX2 = maxX1 ;
			maxX3 = maxX2 ;
		}else if(maxX3 > borderRight) {
			maxX3 = maxX2 ;
		}
		if(minY1 < 0) {
			minY2 = 0 ;
			minY1 = 0 ;
			minY3 = 0 ;
		}else if(minY2 < 0) {
			minY2 = minY1 ;
			minY3 = minY1 ;
		}else if(minY3 < 0) {
			minY3 = minY2 ;
		}
		if(maxY1 > borderTop) {
			maxY1 = borderTop ;
			maxY2 = borderTop ;
			maxY3 = borderTop ;
		}else if(maxY2 > borderTop) {
			maxY2 = maxY1 ;
			maxY3 = maxY1 ;
		}else if(maxY3 > borderTop) {
			maxY3 = maxY2 ;
		}
//		System.out.printf("in list objects: \n");
		for (int x = minX3 ; x <= maxX3 ; x++){
			for(int y = minY3 ; y <= maxY3 ; y++){
				for(Object obj :  grid.getObjectsAt(x,y)) {
					if(obj instanceof Agent) {
						boolean centerAgent = x == X && y == Y ;
						boolean forbiddenExtreams = ((y == minY3 || y == maxY3) && x != X) ||  ((x == minX3 || x == maxX3) && y != Y ) ;
						boolean includeAgent = ((centerAgent && includeCenterAgent) || !centerAgent) && !forbiddenExtreams ;
						boolean conditionMet = false ;
						if(condition.equals("haveIBeenGrazed")) {
							conditionMet = !((Agent)obj).getHaveIBeenGrazed() ;
						}else if(condition.equals("isBarrenGround")){
							conditionMet = ((Agent)obj).getSpecies().equals(Constants.SPECIES_BARREN_GROUND) ;
						}else if(condition.equals("isSand")){
							conditionMet = ((Agent)obj).getSpecies().equals(Constants.SPECIES_SAND) ;
						}else {
							System.out.printf("!!!!! In SMUtils.getRadius3NeighborsCondition(): condition does not exist \n") ;
						}
						if(includeAgent && conditionMet) {
							listAgents.add((Agent)obj) ;
//							System.out.printf("VNN agent: X: %d Y: %d ", ((Agent)obj).getAgentX(), ((Agent)obj).getAgentY());
						}
					}	
				}
			}
		}	
//		System.out.printf("\n") ;
		return  listAgents ;
	}
	
	/**
	 * returns true if the agent has at least one Von Neumann neigbohour 
	 * @param agent
	 * @return
	 */
	public static boolean IsAgentOnEdgeColony(Agent agent){
		int IDNbAgent = agent.getIDNumber() ;
		boolean agentEdgeColony = false ;
		final Grid<Object> grid = SMUtils.getGrid(agent)  ;
		int X = agent.getAgentX() ;
		int Y = agent.getAgentY() ;
//		System.out.printf("centerAgent: X: %d Y: %d ", X,Y);
		int minX =  X - 1 ;
		int maxX =  X +  1 ;
		int minY =  Y - 1 ;
		int maxY =  Y +  1 ;
		int borderRight = Constants.width;
		int borderTop = Constants.height;
		// in case agent is on the edge of the reef
		if(minX < 0) {
			minX = 0 ;
		}
		if(maxX > borderRight) {
			maxX = borderRight ;
		}
		if(minY < 0) {
			minY = 0 ;
		}
		if(maxY > borderTop) {
			maxY = borderRight ;
		}
//		System.out.printf("in list objects: \n");
		for (int x = minX ; x <= maxX ; x++){
			for(int y = minY ; y <= maxY ; y++){
				for(Object obj :  grid.getObjectsAt(x,y)) {
					if(obj instanceof Agent) {
						boolean VNN = (x == X && y == minY) || (x == X && y == maxY) || (x == minX && y == Y) || (x == maxX && y == Y) ; // only consider the 4 Van Neumann neighbours 
						if(VNN) {
							if(((Agent)obj).getIDNumber() != IDNbAgent) {
								agentEdgeColony = true ;
//								System.out.printf("IDNumber: %d %d agentEdgeColony: %b \n",IDNbAgent,((Agent)obj).getIDNumber(),agentEdgeColony) ;
								break ;
							}
						}
					}	
				}
			}
		}	
//		System.out.printf("\n") ;
		return  agentEdgeColony ;
	}
	
	
	/**
	 * ********************************** TO CHECK ************************************************
	 * method that returns a list of a certain number of agents from a list of agents
	 * The agents are randomly selected from the list;
	 * If replace = true, the selected agents are not removed from the listAgent and can consequently be selected several times.
	 */
	public static ArrayList<Agent> getGivenNumberAgentsFromList(int numberAgents, ArrayList<Agent> listAgent, boolean replace){
		ArrayList<Agent> listNumberAgentsFromList = new ArrayList<Agent>();
		ArrayList<Agent> listAgent_copie = (ArrayList) listAgent.clone();   // so I don't touch listAgent
		for (int i = 0; i < numberAgents; i++){
			Agent agent = randomElementOf(listAgent_copie);
			listNumberAgentsFromList.add(agent);
			if(!replace){
				listAgent_copie.remove(agent);
			}
		}
		return listNumberAgentsFromList;
	}

	
	/**
	 * returns the euclidian distance between 2 agents
	 */
	public static double getDistanceBetween2Agents(Agent A, Agent B){
		final double distance = Math.sqrt((A.getAgentX() - B.getAgentX()) * (A.getAgentX() - B.getAgentX()) +
											(A.getAgentY() - B.getAgentY()) * (A.getAgentY() - B.getAgentY()));
		return distance; 
	}
	
    /**
     *  returns the euclidian distance between 2 gridPoints 
     */
	public static double getDistance(GridPoint A, GridPoint B) {
		final double distance = Math.sqrt((A.getX() - B.getX()) * (A.getX() - B.getX()) +
											(A.getY() - B.getY()) * (A.getY() - B.getY()));
		return distance;
	}	
	
	/**
	 * Method that returns the value of one categorical trait the agent (growth_form, mode_larval_development, coloniality).
	 * reduce implementation time. 
	 * @param nameSpecies
	 * @param agentOwnStringVariable
	 * @return
	 */
	public static String getStringTraitFromFTTable(String nameSpecies, String agentOwnStringVariable){
		String stringValue = null;
		for(FunctionalTraitData ftd : Constants.functionalTraitsTable){
			if(ftd.getSpecies().equals(nameSpecies)){		
				if(agentOwnStringVariable.equals(Constants.growth_form)){
					stringValue = ftd.getGrowthForm();					
				}else if(agentOwnStringVariable.equals(Constants.mode_larval_development)){
					stringValue = ftd.getModeLarvalDevelopment();	
				}else if(agentOwnStringVariable.equals(Constants.coloniality)){
					stringValue = ftd.getColoniality();	
				}else if(agentOwnStringVariable.equals(Constants.sexual_system)) {
					stringValue = ftd.getSexualSystem() ;
				}else{
					System.out.printf("wrong spelling of variable \n");
				}
				break;
			}
		}		
		return stringValue;
	}
	
	/**
	 * Method that returns the value of a numerical trait of the agent (growth_form, mode_larval_development, coloniality).
	 * @param nameSpecies
	 * @param agentOwnDoubleVariable
	 * @return
	 */
	public static double getDoubleTraitFromFTTable(String nameSpecies,String agentOwnDoubleVariable){ 
		double traitValue = 0.0 ;
		for(FunctionalTraitData ft : Constants.functionalTraitsTable){
			if(ft.getSpecies().equals(nameSpecies)){
				if(agentOwnDoubleVariable.equals(Constants.aggressiveness)){
					traitValue = ft.getAggressiveness();
				}else if(agentOwnDoubleVariable.equals(Constants.colony_max_diameter)){
					traitValue = ft.getColonyMaxDiameter() ;
				}else if(agentOwnDoubleVariable.equals(Constants.corallite_area)){
					traitValue = ft.getCoralliteArea();
				}else if(agentOwnDoubleVariable.equals(Constants.egg_diameter)){
					traitValue = ft.getEggDiameter() ;
				}else if(agentOwnDoubleVariable.equals(Constants.fecundity_polyp)){
					traitValue = ft.getFecundityPolyp() ;
				}else if(agentOwnDoubleVariable.equals(Constants.growth_rate)){
					traitValue = ft.getGrowthRate() ;
				}else if(agentOwnDoubleVariable.equals(Constants.reduced_scattering_coefficient)){
					traitValue = ft.getReducedScatteringCoefficient() ;
				}
//				else if(agentOwnDoubleVariable.equals(Constants.response_bleaching_index)){
//					traitValue = ft.getResponseBleachingIndex() ;
//				}else if(agentOwnDoubleVariable.equals(Constants.skeletal_density)){
//					traitValue = ft.getSkeletalDensity();
//				}
				else if(agentOwnDoubleVariable.equals(Constants.size_maturity)){
					traitValue = ft.getSizeMaturity();
				}else if(agentOwnDoubleVariable.equals(Constants.age_maturity)){
					traitValue = ft.getAgeMaturity();
				}else if(agentOwnDoubleVariable.equals(Constants.red)){
					traitValue = ft.getRed() ;
				}else if(agentOwnDoubleVariable.equals(Constants.green)){
					traitValue = ft.getGreen() ;
				}else if(agentOwnDoubleVariable.equals(Constants.blue)){
					traitValue = ft.getBlue() ;
				}else if(agentOwnDoubleVariable.equals(Constants.bleaching_probability)){
					traitValue = ft.getBleachingProbability() ;
				}else if(agentOwnDoubleVariable.equals(Constants.correction_coeff_polypFecundity)) {
					traitValue = ft.getCorrectionCoeffPolypFecundity() ;
				}
//				else if(agentOwnDoubleVariable.equals(Constants.x0_logitBleaching)){
//					traitValue = ft.getX0LogitBleaching() ;
//				}else if(agentOwnDoubleVariable.equals(Constants.r_logitBleaching)){
//					traitValue = ft.getRLogitBleaching() ;
//				}
			}
		}
		return traitValue ;
	}
	
	/**
	 * Returns a double[] of the values of the traits for the species desired
	 * @param listNames
	 * @param numTraitName
	 * @return
	 */
	public static double[] getListValuesTraitDouble(List<String> listNames, String numTraitName){
		double[] traitValuesList = new double[listNames.size()] ;
		int i = 0 ;
		for(String nameSp : listNames){
			traitValuesList[i] = getDoubleTraitFromFTTable(nameSp,numTraitName) ;			
//			System.out.printf("%s ; %.1f \n", nameSp, getDoubleTraitFromFTTable(nameSp,numTraitName)) ;
			i++ ;
		}
		return traitValuesList ;
	}
	
//	/**
//	 * Methods that returns the morphology coefficient value corresponding to the shape of the coral species
//	 * @param coralShape
//	 * @return coefficentMorphology
//	 */
//	public static double determineCoefficientMorphology(String coralGrowthForm){		
//		double coefficentMorphology = 1;
//		if(coralGrowthForm.equals(Constants.growthFormBranching)){
//			coefficentMorphology = Constants.coefficientMorphologyBranching;
//		}else if (coralGrowthForm.equals(Constants.growthFormTables_or_plates)){
//			coefficentMorphology = Constants.coefficientMorphologyTables_or_plates ;
//		}else if (coralGrowthForm.equals(Constants.growthFormCorymbose)){
//			coefficentMorphology = Constants.coefficientMorphologyCorymbose;
//		}else if (coralGrowthForm.equals(Constants.growthFormLaminar)){
//			coefficentMorphology = Constants.coefficientMorphologyLaminar ;	
//		}else if (coralGrowthForm.equals(Constants.growthFormDigitate)){
//			coefficentMorphology = Constants.coefficientMorphologyDigitate;
//		}else if (coralGrowthForm.equals(Constants.growthFormColumnar)){
//			coefficentMorphology = Constants.coefficientMorphologyColumnar ;
//		}else if (coralGrowthForm.equals(Constants.growthFormMassive)){
//			coefficentMorphology = Constants.coefficientMorphologyMassive;
//		}else if (coralGrowthForm.equals(Constants.growthFormEncrusting_long_upright)){
//			coefficentMorphology = Constants.coefficientMorphologyEncrusting_long_upright;
//		}else if (coralGrowthForm.equals(Constants.growthFormEncrusting)){
//			coefficentMorphology = Constants.coefficientMorphologyEncrusting;
//		}else {
//			System.out.printf("the growthForm entered does not have a match: %s \n",coralGrowthForm);
//		}
//		return coefficentMorphology;
//	}
	
	/**
	 * Method returning the 3D surface area of a colony depending on its planar surface area and growth form.
	 * 3D geometric models are from McWilliam et al., 2018.
	 * Colony planar surface area is assumed to be circular.
	 * @param polanarSurfaceArea
	 * @param growthForm
	 * @return SurfaceAreaColony
	 */
	public static double return3DSurfaceAreaColony(double polanarSurfaceArea, String growthForm){
		double SurfaceAreaColony = 0.0 ;
		double radius = Math.sqrt(polanarSurfaceArea/Math.PI) ;    // the colony is assumed to be circular
		boolean massive = growthForm.equals(Constants.growthFormMassive) ;
		boolean laminar = growthForm.equals(Constants.growthFormLaminar) ;
		boolean encrusting = growthForm.equals(Constants.growthFormEncrusting) ;
		boolean encrustingLU = growthForm.equals(Constants.growthFormEncrusting_long_upright) ;
		if(massive) {
			SurfaceAreaColony = 2 * Math.PI * Math.pow(radius, 2) ;  // TODO: replace Math.power(radius,2) by radius*radius 
		}else if(laminar){
			SurfaceAreaColony = 2 * Math.PI * radius * Math.sqrt(radius * radius + Constants.hb_laminar * Constants.hb_laminar) ; // corrected from personal communication Mike McWilliam (July 2019)
		}else if(encrusting) {
			SurfaceAreaColony = Math.PI * Math.pow(radius, 2) ;
		}else if(encrustingLU) {
			SurfaceAreaColony = Math.PI * Math.pow(radius, 2) +
							    Math.PI * Math.pow(radius,2) * (Constants.Nb_encrusting_long_upright * (2 * Math.PI * Constants.rb_encrusting_long_upright * Constants.hb_encrusting_long_upright + Math.PI * Math.pow(Constants.rb_encrusting_long_upright,2))) ;
		}else {
			double rb = Constants.rb_branching ;
			double hb = Constants.hb_branching ;
			double Nb = Constants.Nb_branching ;
			if(growthForm.equals(Constants.growthFormColumnar)){
				rb = Constants.rb_columnar ;
				hb = Constants.hb_columnar ;
				Nb = Constants.Nb_columnar ;
			}else if (growthForm.equals(Constants.growthFormCorymbose)) {
				rb = Constants.rb_corymbose ;
				hb = Constants.hb_corymbose ;
				Nb = Constants.Nb_corymbose ;
			}else if(growthForm.equals(Constants.growthFormDigitate)) {
				rb = Constants.rb_digitate ;
				hb = Constants.hb_digitate ;
				Nb = Constants.Nb_digitate ;
			}else if(growthForm.equals(Constants.growthFormTables_or_plates)) {
				rb = Constants.rb_tables_or_plates ;
				hb = Constants.hb_tables_or_plates ;
				Nb = Constants.Nb_tables_or_plates;
			}
			SurfaceAreaColony = Math.PI * Math.pow(radius,2) * (Nb * (2 * Math.PI * rb * hb + Math.PI * Math.pow(rb,2))) ;
		}
		return SurfaceAreaColony ;
	}
	
	/**
	 * Function returning the probability of a polyp to be fecund depending of the planar surface arae of the colony.
	 * Parameter of the model used are averaged from Alvarez-Noriega et al., 2016
	 * @param planarSurfacearea (in cm2)
	 * @param coeffCorrectionPolypfecundity
	 * @return
	 */
	public static double proportionPolypFecund(double planarSurfacearea,double coeffCorrectionPolypfecundity) {
		double planarSurfacearea_m2 = planarSurfacearea / 10000 ;
		double pm = logistInverseFun(Constants.intercept_polypMaturity + Constants.slope_polypMaturity * (Math.log(planarSurfacearea_m2) + coeffCorrectionPolypfecundity)) ;
		return pm ;		
	}
	
	/**
	 * 
	 * @param bleachingProbability
	 * @param DHW
	 * @return
	 */
	public static double logisticBleachingResponse(double bleachingProbability,double DHW){  // cf. Appendix 4 for meaning of gamma
		double gamma = 0.0 ;
		if(Constants.Bleaching_diff_response == 0) {
			gamma = (bleachingProbability-Constants.meanBleachingSusceptilityCaribbeanSp) / 
					  (Constants.maxBleachingSusceptilityCaribbeanSp - Constants.minBleachingSusceptilityCaribbeanSp) ;
		}else {
			gamma = (bleachingProbability-Constants.meanBleachingSusceptilityCaribbeanSp) / 
					  (Constants.maxBleachingSusceptilityCaribbeanSp - Constants.minBleachingSusceptilityCaribbeanSp) *
					  DHW / Constants.Bleaching_diff_response ;  // Constants.Bleaching_diff_response = phi in Appendix 4
		}
		double x = Constants.interceptLogistBleachingResponse + DHW * Constants.slopeLogistBleachingResponse + gamma ;
		double y = Math.exp(x) / (1 + Math.exp(x)) ;
		return y ;
	}
	
	public static double logisticBleachingMortality(double DHW){
		double x0 = 11.6 ;        // see end of Bleaching_logistic_response_DHW_Eakin_2010.R
		double r = 0.4 ;
		double L = 1.0 ;
		double y = L / (1 + Math.exp(-r*(DHW - x0))) ;
		return y ;
	}
	
	/**
	 * logit function 
	 * @param proportion
	 * @return
	 */
	public static double logistFun(double proportion) {
		double result = - 9999.0  ;
		if(proportion >= 1 || proportion <= 0) {
			System.out.printf("In SMUtils.logistFun(): proportion is not a proportion") ;
		}else {
			result = Math.log(proportion / (1 - proportion)) ;
		}
		return result ;
	}
	
	/**
	 * inverse of the logit function
	 * @param number
	 * @return
	 */
	public static double logistInverseFun(double number) {
		double result = Math.exp(number) / (1 + Math.exp(number)) ;
		return result ;
	}
	
	
	public static void setUUID() {
		UUID uuid = UUID.randomUUID() ;
		Constants.UUID_here = uuid.toString() ;
	}
	
	/**
	 * 
	 * @param cpadlist
	 * @return
	 */
	public static RugosityCoverGrazed rugosityToGrazing(final ArrayList<ColonyPlanarAreaDistribution> cpadlist ){
		
		RugosityCoverGrazed rcg = RugosityCoverGrazed.createRugosityCoverGrazed() ;
		rcg.setYear(cpadlist.get(0).getYear())  ;
		
		// find the total surface area (in cm2) of all the colonies (with radius < 5.64 cm)
		double surfaceAreaTotal = 0 ;
		double planarSurfaceTotal = 0 ;
		for(ColonyPlanarAreaDistribution cpad : cpadlist){
			String species = cpad.getSpeciesName() ;
			String growthForm = SMUtils.getStringTraitFromFTTable(species,Constants.growth_form) ;
			// for each colonies planar area in of the species determine the 
			ArrayList<Double> colonyPlanarAreaListHere = cpad.getColonyPlanarAreaList() ;
			
//			System.out.printf("%s size colonyPlanarAreaListHere: %d \n",species,colonyPlanarAreaListHere.size()) ;
	
			for(Double planarSurfacearea : colonyPlanarAreaListHere){
								
//				System.out.printf("planarSurfacearea: %.1f \n",planarSurfacearea) ;

				// small colonies (radius < 5.64 cm) are not included because we assumed they do not contribute to rugosity
				if(planarSurfacearea > 100){
					surfaceAreaTotal = surfaceAreaTotal + SMUtils.return3DSurfaceAreaColony(planarSurfacearea, growthForm) ;
					planarSurfaceTotal = planarSurfaceTotal + planarSurfacearea ;
				}
			}
		}
		
//		System.out.printf("Total surface area: %.1f total colony surface area: %.1f \n",Constants.sizeReef,planarSurfaceTotal);

		// determine the rugosity using Kubicek et al., 2016 estimation of rugosity
		double R = Math.sqrt((Constants.sizeReef - planarSurfaceTotal + surfaceAreaTotal)/Constants.sizeReef) ;
		rcg.setRugosity(R) ;
		
//		System.out.printf("Rugosity: %.1f \n",R) ;

		// Determine the corresponding number of individual fish per 120 m2
		double indiFish120m2 = 19.74 * (R - 1) ; // model 1 for all fish genera confounded in Bozec et al., 2013 and Froese et al., 2014's species specific coefficients of the length-weight relationship
		
//		System.out.printf("indiFish120m2: %.1f \n",indiFish120m2) ;

		// convert indiFish120m2 into g per m2 using Bozec et al., 2013 empirical dataset of fish community  (Table 1 in Appendix)
		double gFishm2 = indiFish120m2 * (0.512 *   LWR( 9.8 ,  0.01096 ,  3.02) + // Scarus iserti 
										 0.288 *   LWR( 13.4 ,  0.01072 , 3.12) + // Sparisoma aurofrenatum
										 0.134 *   LWR( 24.0 ,  0.01380 , 3.04) + // Sparisoma viride
										 0.044 *   LWR( 26.7 ,  0.01072 , 3.09) + // Sparisoma chrysopterum
										 0.014 *   LWR( 28.4 ,  0.00933 , 3.04) + // Sparisoma rubripinne
										 0.005 *   LWR( 16.7 ,  0.01096 , 3.02) + // Scarus taeniopterus --> I put Scarus iserti's a and b values because they are not available
										 0.002 *   LWR( 27.8 ,  0.01445 , 3.04) + // Scarus vetula
										 0.0006 *  LWR( 40.0 ,  0.01622 , 3.05) // Scarus coelestinus 
				) / 120 ; // conversion from 120 m2 to 1 m2
		
//		System.out.printf("gFishm2: %.1f \n",gFishm2);

		// conversion of gFishm2 into percentage of reef grazed using model 2 in Carturan et al., 2020, Appendix S3
		double surfaceGrazedFish = asymptoteFun(gFishm2, 70, 0, 1, 90) ;
		rcg.setGrazingFish(surfaceGrazedFish);
				
//		System.out.printf("gFishm2: %.1f \n",surfaceGrazedFish);
		
		return(rcg) ;
	}
	
	/**
	 * Length Weight relationship for fish, with the species-specific coefficient esstimated by Froese et al., 2014
	 * and available at fishbase.org.
	 * @param L length of the fish, a and b are coefficients
	 * @param a
	 * @param b
	 * @return
	 */
	public static double LWR(double L, double a, double b) {
		double W = a * Math.pow(L, b) ;
		return(W) ;
	}
	
	/**
	 * Simple asymptote function.
	 * @param x : the variable
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static double asymptoteFun(double x, double a, double b, double c, double d) {
		double y = (a * Math.pow(x, 2) - b) / (c*Math.pow(x, 2) + d) ;
		return(y) ;
	}
	
	
	/********************************************************************************************************
	 ******************* Commands that read CSV files *******************************************************
	 ********************************************************************************************************/
	 
	/**
	 * Returns a list of DisturbanceData instances that each contains information about disturbance of a specific period (variable year) taken from csv file.
	 * @param disturbanceDataFileName
	 * @return listDisturbanceData
	 */
	public static List<Sand_cover> readSandCoverFile(final String sandCoverFileName, final int communityNumberGUI) {
        final ArrayList<Sand_cover> listSandCoverData = new ArrayList<Sand_cover>();
		CSVReader br = null;
        try {
                br = new CSVReader (new FileReader(sandCoverFileName),',','\'',0); // Constants.DISTURBANCE_FILE_HEADER_LINES
                String[] line = null;
                line = br.readNext() ;
                boolean rightScenario = false ;

                while(!rightScenario){                	
                	int idx = 0 ;
                	int scenarioNumbHere = Integer.parseInt(line[idx]) ;
//                	System.out.printf("scenarioNumbHere: %d scenarioNumberGUI: %d \n",scenarioNumbHere,communityNumberGUI);
                	if(scenarioNumbHere == communityNumberGUI){
                        String[] line1 = line ;
                        String[] line2 = br.readNext() ;
                        idx++;  
                        idx++;  // skip the second columns which correspond to to site name
                        idx++; // skip the third column that corresponds to row names
                		while (!line[idx].equals("END")) {  			
                			final double year = Double.parseDouble(line1[idx]) ;                   
                         final double sand_cover = Double.parseDouble(line2[idx]) ;

                        	final Sand_cover sc = new Sand_cover(year,sand_cover) ;
                        	listSandCoverData.add(sc);
                        	idx++;      	
                    }
                		rightScenario = true ;
                	}else {
                		while(scenarioNumbHere != communityNumberGUI){
                			line = br.readNext() ;
                        	if(line == null){
                        		System.out.printf("In SMUtils.readSandCoverFile(): The community number entered is not in the csv file :( \n") ;
                          	break;
                        	}else {
                    			scenarioNumbHere = Integer.parseInt(line[idx]) ;
                        	}
                		}	
                	}
             }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        return Collections.unmodifiableList(listSandCoverData);
	}
	
	/**
	 * 
	 * @param cycloneDMTFileName
	 * @param scenarioNumberGUI
	 * @param numberCycloneDMTModel
	 * @return
	 */
	public static List<Disturbance_cyclone> readCycloneDMTFile(final String cycloneDMTFileName, final int scenarioNumberGUI, final int numberCycloneDMTModel) {
        final ArrayList<Disturbance_cyclone> cycloneDMTData = new ArrayList<Disturbance_cyclone>();
		CSVReader br = null;
		String modelNumber = "cyclone_DMT.model" + numberCycloneDMTModel ;
//		System.out.printf("modelNumber: %s \n", modelNumber) ;
		
        try {
                br = new CSVReader (new FileReader(cycloneDMTFileName),',','\'',0); // Constants.CYCLONE_DMT_DATA_FILE
                String[] line = null;
                line = br.readNext() ;
                boolean rightScenario = false ;

                while(!rightScenario){    
	                	int idx = 0 ;
	                	int scenarioNumbHere = Integer.parseInt(line[idx]) ;
//	                	System.out.printf("scenarioNumbHere: %d \n",scenarioNumbHere);
	                	if(scenarioNumbHere == scenarioNumberGUI){
	                    String[] line1 = line ;
	                    String[] lineNext = br.readNext() ;  // need to chose the line corresponding to the right 
	                    idx++;  		// skip the second columns which correspond to to site name
	                    idx++;      // column with year, cyclone_DMT.model1, model2, model3
//                			System.out.printf("compared to %s \n",lineNext[idx]) ;
	                    while(!lineNext[idx].equals(modelNumber)) {		// to find the right model cyclone_DMT.model1, cyclone_DMT.model2, etc.
	                    		lineNext = br.readNext() ;
	                    }
//	                		System.out.printf("compared to %d \n",lineNext[idx]) ;
	                    idx++; // skip the third column that corresponds to row names
	                		while (!line[idx].equals("END")) {  			
	                			final double year = Double.parseDouble(line1[idx]) ;                   
	                         final double DMT = Double.parseDouble(lineNext[idx]) ;
	
	                        	final Disturbance_cyclone dc = new Disturbance_cyclone(year,DMT) ;
	                        	cycloneDMTData.add(dc);
	                        	idx++;      	
	                    }
	                		rightScenario = true ;
	                	}else {
	                		while(scenarioNumbHere != scenarioNumberGUI){
	                			line = br.readNext() ;
	                        	if(line == null){
	                        		System.out.printf("The scenario number entered is not in the csv file :( \n") ;
	                          	break;
	                        	}else {
	                    			scenarioNumbHere = Integer.parseInt(line[idx]) ;
	                        	}
	                		}
	                }
            }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        return Collections.unmodifiableList(cycloneDMTData);
	}
	
	/**
	 * 
	 * @param grazingFileName
	 * @param scenarioNumberGUI
	 * @param numberGrazingModel
	 * @return
	 */
	public static List<Disturbance_grazing> readGrazingFile(final String grazingFileName, final int scenarioNumberGUI, final int numberGrazingModel) {
			
        final ArrayList<Disturbance_grazing> grazingData = new ArrayList<Disturbance_grazing>();
		CSVReader br = null;
		String modelNumber = "grazing.model" + numberGrazingModel ;
//		System.out.printf("modelNumber: %s \n", modelNumber) ;
        try {
                br = new CSVReader (new FileReader(grazingFileName),',','\'',0); // Constants.GRAZING_DATA_FILE
                String[] line = null;
                line = br.readNext() ;
                boolean rightScenario = false ;

                while(!rightScenario){   
	                	int idx = 0 ;
	                	int scenarioNumbHere = Integer.parseInt(line[idx]) ;
//	                	System.out.printf("scenarioNumbHere: %d \n",scenarioNumbHere);
	                	if(scenarioNumbHere == scenarioNumberGUI){
	                    String[] line1 = line ;
	                    String[] lineNext = br.readNext() ;  // need to chose the line corresponding to the right 
	                    idx++;  		// skip the second columns which correspond to to site name
	                    idx++;      // column with year, grazing.model1, model2, model3
//                			System.out.printf("compared to %s \n",lineNext[idx]) ;
	                    while(!lineNext[idx].equals(modelNumber)) {		// to find the right model cyclone_DMT.model1, cyclone_DMT.model2, etc.
	                    		lineNext = br.readNext() ;
	                    }
//	                		System.out.printf("compared to %s \n",lineNext[idx]) ;
	                    idx++; // skip the third column that corresponds to row names
	                		while (!line[idx].equals("END")) {  			
	                			final double year = Double.parseDouble(line1[idx]) ;                   
	                         final double coverGrazed = Double.parseDouble(lineNext[idx]) ;
	
	                        	final Disturbance_grazing dg = new Disturbance_grazing(year,coverGrazed) ;
	                        	grazingData.add(dg) ;
	                        	idx++ ;      	
	                    }
	                		rightScenario = true ;
	                	}else {
	                		while(scenarioNumbHere != scenarioNumberGUI){
	                			line = br.readNext() ;
	                        	if(line == null){
	                        		System.out.printf("The scenario number entered is not in the csv file :( \n") ;
	                          	break;
	                        	}else {
	                    			scenarioNumbHere = Integer.parseInt(line[idx]) ;
	                        	}
	                		}
	                }
                }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        return Collections.unmodifiableList(grazingData);
	}
	
	/**
	 * 
	 * @param bleachingDHWFileName
	 * @param scenarioNumberGUI
	 * @return
	 */
	public static List<Disturbance_bleaching> readBleachingDHWFile(final String bleachingDHWFileName, final int scenarioNumberGUI) {
        final ArrayList<Disturbance_bleaching> listDHWCoverData = new ArrayList<Disturbance_bleaching>();
		CSVReader br = null;
        try {
                br = new CSVReader (new FileReader(bleachingDHWFileName),',','\'',0); // Constants.BLEACHING_DATA_FILE_HEADER_LINES
                String[] line = null ;
                line = br.readNext() ;
                boolean rightScenario = false ;

                while(!rightScenario){                	
                	int idx = 0 ;
                	int scenarioNumbHere = Integer.parseInt(line[idx]) ;
//              System.out.printf("scenarioNumbHere: %d \n",scenarioNumbHere);
                	if(scenarioNumbHere == scenarioNumberGUI){
                        String[] line1 = line ;
                        String[] line2 = br.readNext() ;
                        idx++;  
                        idx++;  // skip the second columns which correspond to to site name
                        idx++; // skip the third column that corresponds to row names
                		while (!line[idx].equals("END")) {  			
                			final double year = Double.parseDouble(line1[idx]) ;                   
                         final double DHW = Double.parseDouble(line2[idx]) ;

                        	final Disturbance_bleaching db = new Disturbance_bleaching(year,DHW) ;
                        	listDHWCoverData.add(db);
                        	idx++;      	
                    }
                		rightScenario = true ;
                	}else {
                		while(scenarioNumbHere != scenarioNumberGUI){
                			line = br.readNext() ;
                        	if(line == null){
                        		System.out.printf("The scenario number entered is not in the csv file :( \n") ;
                          	break;
                        	}else {
                    			scenarioNumbHere = Integer.parseInt(line[idx]) ;
                        	}
                		}	
                	}
             }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        return Collections.unmodifiableList(listDHWCoverData);
	}
	
	public static List<Disturbance_larvalConnectivity> readlarvalInputFile(final String larvalConnectivityFileName, final int scenarioNumberGUI) {
        final ArrayList<Disturbance_larvalConnectivity> listlarvalInputData = new ArrayList<Disturbance_larvalConnectivity>();
		CSVReader br = null;
        try {
//        		    System.out.printf("in SMUtils readlarvalInputFile() : %s  \n", larvalConnectivityFileName) ;
                br = new CSVReader (new FileReader(larvalConnectivityFileName),',','\'',0); // Constants.LARVAL_CONNECTIVITY_DATA_FILE_HEADER_LINES

                String[] line = null ;
                line = br.readNext() ;
                boolean rightScenario = false ;

                while(!rightScenario){                	
                	int idx = 0 ;
                	int scenarioNumbHere = Integer.parseInt(line[idx]) ;
//              System.out.printf("scenarioNumbHere: %d \n",scenarioNumbHere);
                	if(scenarioNumbHere == scenarioNumberGUI){
                        String[] line1 = line ;
                        String[] line2 = br.readNext() ;
                        idx++;  
                        idx++;  // skip the second columns which correspond to to site name
                        idx++; // skip the third column that corresponds to row names
                		while (!line[idx].equals("END")) {  			
                			final double year = Double.parseDouble(line1[idx]) ;                   
                         final double nbLarvae = Double.parseDouble(line2[idx]) ;

                        	final Disturbance_larvalConnectivity dlc = new Disturbance_larvalConnectivity(year,nbLarvae) ;
                        	listlarvalInputData.add(dlc);
                        	idx++;      	
                    }
                		rightScenario = true ;
                	}else {
                		while(scenarioNumbHere != scenarioNumberGUI){
                			line = br.readNext() ;
                        	if(line == null){
                        		System.out.printf("The scenario number entered is not in the csv file in SMUtils.readlarvalInputFile() :( \n") ;
                          	break;
                        	}else {
                    			scenarioNumbHere = Integer.parseInt(line[idx]) ;
                        	}
                		}	
                	}
             }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        return Collections.unmodifiableList(listlarvalInputData);
	}
	
	
	/**
	 * Returns a list of DisturbanceData instances that each contains information about disturbance of a specific period (variable year) taken from csv file.
	 * @param disturbanceDataFileName
	 * @return listDisturbanceData
	 */
	public static List<Disturbances_priority> readDisturbancePriorityFile(final String disturbancesPriorityDataFileName, final int scenarioNumberGUI) {
        final ArrayList<Disturbances_priority> listDisturbancesPriorityData = new ArrayList<Disturbances_priority>();
		CSVReader br = null;
        try {
                br = new CSVReader (new FileReader(disturbancesPriorityDataFileName),',','\'',0); // Constants.DISTURBANCE_FILE_HEADER_LINES
                String[] line = null;
                line = br.readNext() ;
                boolean rightScenario = false ;

                while(!rightScenario){                	
                	int idx = 0 ;
                	int scenarioNumbHere = Integer.parseInt(line[idx]) ;
//                	System.out.printf("scenarioNumbHere: %d \n",scenarioNumbHere);
                	if(scenarioNumbHere == scenarioNumberGUI){
                        String[] line1 = line ;             	// year
                        String[] line2 = br.readNext() ;		// priority_1
                        String[] line3 = br.readNext() ;		// priority_2
                        String[] line4 = br.readNext() ;		// priority_3
                        String[] line5 = br.readNext() ;		// season
                        idx++;  	// skip 1st column corresponding to simulation number
                        idx++; 	// skip the 2nd column corresponding to sites names
                        idx++;	// skip the 3rd column that corresponds to rownames
                		while (!line[idx].equals("END")) {  		
                			final double year = Double.parseDouble(line1[idx]) ;                   
                        	final String priority_1 = line2[idx] ;
                        	final String priority_2 = line3[idx] ;
                        	final String priority_3 = line4[idx] ;
                        	final String season = line5[idx] ;

                        	final Disturbances_priority dd = new Disturbances_priority(year,
                        												  		priority_1,
                        												  		priority_2,
                        												  		priority_3,
                        												  		season) ;                        	
                        	listDisturbancesPriorityData.add(dd);
                        	idx++;      	
                    }
                		rightScenario = true ;
                	}else {
                		
                		while(scenarioNumbHere != scenarioNumberGUI){
                			line = br.readNext() ;
                        	if(line == null){
                        		System.out.printf("The scenario number entered is not in the csv file :( \n") ;
                          	break;
                        	}else {
                    			scenarioNumbHere = Integer.parseInt(line[idx]) ;
                        	}
                		}			
                	}
            }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        
        return Collections.unmodifiableList(listDisturbancesPriorityData);
	}
	
	/**
	 * Method made to read a cvs file and return a list of FunctionalTraitData instances that contains the functional trait values for each type of agent
	 */
	public static List<FunctionalTraitData> readFunctionalTraitDataFile(final String functionalTraitFileName) {
        final ArrayList<FunctionalTraitData> listFunctionalTraitData = new ArrayList<FunctionalTraitData>();
		CSVReader br = null;
        try {
                br = new CSVReader (new FileReader(functionalTraitFileName),',','\'',CSVParser.DEFAULT_QUOTE_CHARACTER,Constants.FUNCTIONAL_TRAIT_HEADER_LINES); // CSVParser.DEFAULT_QUOTE_CHARACTER: quotes are removed for strings
                String[] line = null;
                while ((line = br.readNext()) != null){
                        int idx = 0 ;
                        final String substrate_category = line[idx++] ;
                        final String substrate_subcategory = line[idx++];
                        final String species = line[idx++];
                        final double aggressiveness = Double.parseDouble(line[idx++]);
                        final String coloniality = line[idx++];
                        final double colony_max_diameter = Double.parseDouble(line[idx++]);
                        final double corallite_area = Double.parseDouble(line[idx++]);
                        final double egg_diameter = Double.parseDouble(line[idx++]);
                        final double fecundity_polyp = Double.parseDouble(line[idx++]);
                        final String growth_form = line[idx++];
                        final double growth_rate = Double.parseDouble(line[idx++]);
	                    	String mode_larval_development = line[idx++];
	                    	if(mode_larval_development.equals("both")) {       // "Pocillopora_ankeli" and "Pocillopora_damicornis" show both spawning and brooding traits; here they are considered as "brooder".
	                    		mode_larval_development = Constants.brooder ;
	                    	}
	                    	final double reduced_scattering_coefficient = Double.parseDouble(line[idx++]);
	                    	//final double response_bleaching_index = Double.parseDouble(line[idx++]);
	                    	//final double skeletal_density = Double.parseDouble(line[idx++]);
	                    	final double size_maturity = Double.parseDouble(line[idx++]);
	                    	final double age_maturity = Double.parseDouble(line[idx++]);
	                    	final int red = Integer.parseInt(line[idx++]);
	                    	final int green = Integer.parseInt(line[idx++]);
	                    	final int blue = Integer.parseInt(line[idx++]);
	                    	final double bleaching_probability = Double.parseDouble(line[idx++]);
	                    	//final double x0_logitBleaching = Double.parseDouble(line[idx++]);
	                    	//final double r_logitBleaching = Double.parseDouble(line[idx++]);
	                    //final double x0_logitBRI = Double.parseDouble(line[idx++]);
	                    	//final double r_logitBRI = Double.parseDouble(line[idx++]);
	                    	final String sexual_system = line[idx++] ;
	                    	final double correctionCoeffFecundityPolyp = Double.parseDouble(line[idx++]) ;
                    	
                    	final FunctionalTraitData ftd = new FunctionalTraitData(substrate_category,
                    															substrate_subcategory,
                    															species,
                    															aggressiveness,
                    															coloniality,
                    															colony_max_diameter,
                    															corallite_area,
                    															egg_diameter,
                    															fecundity_polyp,
                    															growth_form,
                    															growth_rate,
                    															mode_larval_development,
                    															reduced_scattering_coefficient,
                    															//response_bleaching_index,
                    															//skeletal_density,
                    															size_maturity,
                    															age_maturity,
                    															red,
                    															green,
                    															blue,
                    															bleaching_probability,
                    															sexual_system,
                    															correctionCoeffFecundityPolyp);
                    															//x0_logitBleaching,
                    															//r_logitBleaching,
                    															//x0_logitBRI,
                    															//r_logitBRI	);
                    	listFunctionalTraitData.add(ftd);
                }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        return Collections.unmodifiableList(listFunctionalTraitData);
	}
	
	/**
	 * Returns a list that contains the composition of the coral communities that correspond the communityNumber_asked 
	 * The list contains 3 BiodiversityData instances, one for alive, one for bleached and one for dead
	 */
	public static List<BiodiversityData> readBiodiversityDataFile(final String biodiversityFileName,final int communityNumber_asked) {
        final ArrayList<BiodiversityData> listBiodiversityData = new ArrayList<BiodiversityData>();
		CSVReader br = null;
        try {
                br = new CSVReader (new FileReader(biodiversityFileName),',','\'',Constants.BIODIVERSITY_HEADER_LINES); // skip the 1st line
                String[] line = null;
                line = br.readNext();
                boolean right_community = false ;
                while (right_community == false) {    	// select the right community number
                        int idx = 0;
                        final int communityNumber = Integer.parseInt(line[idx]);
//                      System.out.printf("%d \n",communityNumber);
                        if(communityNumber == communityNumber_asked){
                        	for(int i = 0 ; i < 3 ; i++){  // create 3 BiodiversityData instancies, one for alive, one for bleached and one for dead
                                line = br.readNext();
                                idx = 2 ; // skip the second columns that corresonds to name site
                        			final double percentageCoralSp1 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp2 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp3 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp4 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp5 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp6 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp7 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp8 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp9 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp10 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp11 = Double.parseDouble(line[idx++]);
                             	final double percentageCoralSp12 = Double.parseDouble(line[idx++]);
                             	final double percentageMacroalgae = Double.parseDouble(line[idx++]);
                             	final double percentageTurf = Double.parseDouble(line[idx++]);
                             	final double percentageCCA = Double.parseDouble(line[idx++]);
                             	final double percentageAMA = Double.parseDouble(line[idx++]);
                             	final double percentageACA = Double.parseDouble(line[idx++]);
                             	final double percentageHalimeda = Double.parseDouble(line[idx++]);
                             	final double percentageSand= Double.parseDouble(line[idx++]);

                             	final BiodiversityData ftd = new BiodiversityData(communityNumber,
                             													  percentageCoralSp1,
                             													  percentageCoralSp2,
                             													  percentageCoralSp3,
                             													  percentageCoralSp4,
                             													  percentageCoralSp5,
                             													  percentageCoralSp6,
                             													  percentageCoralSp7,
                             													  percentageCoralSp8,
                             													  percentageCoralSp9,
                             													  percentageCoralSp10,
                             													  percentageCoralSp11,
                             													  percentageCoralSp12,
                             													  percentageMacroalgae,
                             													  percentageTurf,
                             													  percentageCCA,
                             													  percentageAMA,
                             													  percentageACA,
                             													  percentageHalimeda,
                             													  percentageSand);
                             	listBiodiversityData.add(ftd);
                        	}
                        	right_community = true ;	
                 		}else{
                        	for(int i = 0 ; i < 4 ; i++){        // skip 4 lines to go to the next community
                        		line = br.readNext() ;
                        	}
                        }
                }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
       
//       System.out.printf("readBiodiversityDataFile \n");
//       for(BiodiversityData bioDivd : listBiodiversityData){
//    	   System.out.printf("%.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f \n",bioDivd.getPercentageCoralSp1(),
//    			   												bioDivd.getPercentageCoralSp2(),
//    			   												bioDivd.getPercentageCoralSp3(),
//    			   												bioDivd.getPercentageCoralSp4(),
//    			   												bioDivd.getPercentageCoralSp5(),
//    			   												bioDivd.getPercentageCoralSp6(),
//    			   												bioDivd.getPercentageCoralSp7(),
//    			   												bioDivd.getPercentageCoralSp8(),
//    			   												bioDivd.getPercentageCoralSp9());
//       }
        return Collections.unmodifiableList(listBiodiversityData);
	}
	
	/** NOT USED
	 * Read the R_path that is written in the datasets_inputs > R_path.csv
	 * @param functionalTraitFileName
	 * @return
	 */
	public static void read_R_path() {
		CSVReader br = null ;
        try {
                br = new CSVReader (new FileReader(Constants.R_PATH_FILE),',','\'',CSVParser.DEFAULT_QUOTE_CHARACTER,Constants.R_PATH_HEADER_LINES); // CSVParser.DEFAULT_QUOTE_CHARACTER: quotes are removed for strings
                String[] line = null;
                while ((line = br.readNext()) != null) {
                        int idx = 0 ;
                        Constants.R_path = line[idx++] ;
                }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
//        System.out.printf("*************** %s \n", Constants.R_PATH_FILE);
//        System.out.printf("*************** %s \n", Constants.R_path);
       
	}
	
	/**	
	 * Methods that returns a List<String> which contains the names of each column in a CSV file (the 1st line)
	 * Note that the name of each column has to be a string.
	 */
	public static List<String> getNameColumnsInCSVFile (final String nameCSVFile, final int communityNumber_asked){
        final List<String> listNameColumnsInCSVFile = new ArrayList<String>();
		CSVReader br = null;
        try {        	
                br = new CSVReader (new FileReader(nameCSVFile),',','\'',1); // do skip the 1st row
                String[] line = null;

                boolean right_community = false ;
                line = br.readNext() ;
                int communityNumber = 0 ;
                while (right_community == false){
//                	   System.out.printf("getNameColumnsInCSVFile community number: %s \n", line[0]);
                    communityNumber = Integer.parseInt(line[0]); // gets the community number in csv file
                    if(communityNumber == communityNumber_asked){ 
                    	int numberTypesAgents = line.length ;
//                    	System.out.printf("%d \n",numberTypesAgents);
                    	for(int j = 1 ; j < numberTypesAgents ; j++){
                    		final String nameColumn = line[j];                   		
                        	listNameColumnsInCSVFile.add(nameColumn);
//                        	System.out.printf("%s \n",nameColumn) ;
                    	}
//                      for(String st : listNameColumnsInCSVFile){
//                          System.out.printf(" %s \n", st);
//                      }
                    right_community = true ;
                	}else{
                    for(int i = 0 ; i < 4 ; i++) {
                    		line = br.readNext() ;
                    }
                	communityNumber = Integer.parseInt(line[0]) ;
                	}            	
             }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                              br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        return listNameColumnsInCSVFile ;
	}
	
	/**
	 * Function that creates agents of a certain type (used only during the context builder)
	 * It takes 6 arguments, the 1st is the species name of the agent, the 2nd is the IDNumber, 
	 * the 3rd and 4th for the grid and context respectively, 
	 * the 5th and 6th are the x and y coordinates respectively. 
	 * It does not creates bleached or dead corals.
	 */
	public static Agent createAgent(String speciesName, int IDNumber, Grid<Object> grid, Context context, int x, int y){
						
		Agent newAgent = new Agent (grid, context, x, y,
									"temp",               // substrateCategory       
									"temp",               // substrateSubCategory
									speciesName,               // species
									999,                  // aggressiveness 
									"temp",				  // coloniality
									999,				  // max radius colony_max_diameter
									999,                  // corallite_area
									999,                  // egg_diameter
									999,                  // fecundity_polyp
									"temp",               // growth_form
									999,                  // growth_rate 
									"temp",               // mode_larval_development
									999,                  // reduced_scattering_coefficient
//									999,                  // response_bleaching_index
//									999,                  // skeletalDensity
									999,                  // sizeAtMaturity
									999,                  // ageAtMaturity
									0,					  // age
									0,					  // timeRecoveryBleaching
									IDNumber,
									255,0,187,			  // red, green, blue
									false,				  // canIGrow
									false, 				  // haveIBeenConverted
									false,				  // haveIBeenGrazed
									0,					  // area
									false,				  // sizeUpDated
									0,					  // bleaching_probability
									false,				  // newRecruit
//									0,					  // x0_logitBleaching
//									0					 // r_logitBleaching
									"temp",				  // sexual_system
									0.0					  // correction_coeff_polypFecundity
									);				  	 
		
		for (FunctionalTraitData FTD : Constants.functionalTraitsTable){
					
//			System.out.printf("%s %s \n",newAgent.getSpecies(),FTD.getSpecies());
			
			if (FTD.getSpecies().equals(newAgent.getSpecies())){	
//				System.out.printf("%s %s \n",newAgent.getSpecies(),FTD.getSpecies());

				newAgent.setSubstrateCategory(FTD.getSubstrateCategory());
				final String agentSubtrateSubCategory = FTD.getSubstrateSubCategory();
				newAgent.setSubstrateSubCategory(agentSubtrateSubCategory);
				newAgent.setSpecies(FTD.getSpecies());
				newAgent.setAggressiveness(FTD.getAggressiveness());
				newAgent.setColoniality(FTD.getColoniality());
				newAgent.setColonyMaxDiameter(FTD.getColonyMaxDiameter());
				newAgent.setCoralliteArea(FTD.getCoralliteArea());
				newAgent.setEggDiameter(FTD.getEggDiameter());
				newAgent.setFecundityPolyp(FTD.getFecundityPolyp());
				newAgent.setGrowthForm(FTD.getGrowthForm());
				double growthRate = FTD.getGrowthRate() ; // growth rate depend on how many parts the year is divided
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				newAgent.setGrowthRate(growthRate) ;
				newAgent.setModeLarvalDevelopment(FTD.getModeLarvalDevelopment()) ;
				newAgent.setReducedScatteringCoefficient(FTD.getReducedScatteringCoefficient()) ;
//				newAgent.setResponseBleachingIndex(FTD.getResponseBleachingIndex());
//				newAgent.setSkeletalDensity(FTD.getSkeletalDensity());
				newAgent.setAgeMaturity(FTD.getAgeMaturity());
				newAgent.setSizeMaturity(FTD.getSizeMaturity());

				if(agentSubtrateSubCategory.equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){ // this is done in order to creates small color variation between colony of a same coral species
					newAgent.setRed(FTD.getRed() - RandomHelper.nextIntFromTo(0,10));
					newAgent.setGreen(FTD.getGreen()- RandomHelper.nextIntFromTo(0,10));
					newAgent.setBlue(FTD.getBlue()- RandomHelper.nextIntFromTo(0,10));
				}else{
					newAgent.setRed(FTD.getRed());
					newAgent.setGreen(FTD.getGreen());
					newAgent.setBlue(FTD.getBlue());
				}
//				newAgent.setAge(RandomHelper.nextDoubleFromTo(1, FTD.getAgeMaturity()*3)); // the age is randomly given between 1 and 3*ageAtMaturity
				newAgent.setAge(3) ;    // 5.5 years is the median of the 3 values available in the coraltrait.org database 
				newAgent.setBleaching_probability(FTD.getBleachingProbability());
//				newAgent.setX0_logitBleaching(FTD.getX0LogitBleaching());
//				newAgent.setR_logitBleaching(FTD.getRLogitBleaching());
				newAgent.setSexualSystem(FTD.getSexualSystem());
				newAgent.setCoeffCorrectionPolypfecundity(FTD.getCorrectionCoeffPolypFecundity());
	//			context.add(newAgent);        // already done in class Agent
//				grid.moveTo(newAgent, x, y);
				break;
			}
		}
		
//		System.out.printf("%s , %d \n",newAgent.getSpecies(),newAgent.getIDNumber());

		return newAgent;
	}
	
	/** CHECKED - Used in the contextBuilder
	 * Creates a matrix of initial % cover
	 * Row 1: alive coral sp1, sp2, ... sp12, MA, Turf and CCA, AMA, ACA, Halimeda, sand 
	 * Row 2: bleached coral sp1, sp2, ... sp12, MA, Turf and CCA, AMA, ACA, Halimeda, sand
	 * Row 3: dead coral sp1, sp2, ... sp12, MA, Turf and CCA, AMA, ACA, Halimeda, sand
	 * @param filename
	 * @param NumberColumnToSkipFromLeft
	 * @return matrix of initial % cover 
	 */
	public static double[][] createBiodiversityMatrixFromCSVFile(String filename, int NumberColumnToSkipFromLeft, int communityNumber_asked){		
		
//		System.out.printf("In createBiodiversityMatrixFromCSVFile() \n");
//		for(BiodiversityData communityComposition : dataBiodiversityBuilder){
//			System.out.printf("%.1f %.1f %.1f \n",communityComposition.getPercentageCoralSp1(),communityComposition.getPercentageCoralSp2(),communityComposition.getPercentageCoralSp3());					
//		}
		List<String> nameColumnsBiodiversityData =  Constants.speciesNamesList ;

		int numberRowsMatrix = Constants.dataBiodiversityList.size();  // should be 3
		int numberColumnsMatrix =  nameColumnsBiodiversityData.size() ;
		double[][] biodiversityMatrix = new double[numberRowsMatrix][numberColumnsMatrix] ;
		int rowNumber = 0;
		for(BiodiversityData communityComposition : Constants.dataBiodiversityList){
			int columnNumber = 0 ;
			for (String nameColumn: nameColumnsBiodiversityData){
				biodiversityMatrix[rowNumber][columnNumber] = communityComposition.getPecentageAgentTypeAsked(nameColumn);
//				System.out.printf("%s: %.1f \n",nameColumn,biodiversityMatrix[rowNumber][columnNumber]);
				columnNumber++;
			}
			rowNumber++;
		}		
		return biodiversityMatrix ;
	}
	
	// readBiodiversityDataFile(final String biodiversityFileName,final int communityNumber_asked, final String biodiversityType_asked) {
	
	/**
	 * Methods that reads the coral_pair_competition_probability_simulation.csv, and returns the corresponding probability.
	 * Values are obtained from Precoda et al., (2017).
	 * @return
	 */
	public static double getProbaOvergrowCoralPair(String sp_growing, String sp_defending){
		double proba = 999 ;
//		String nameSp1 = "temp";
//		String nameSp2 = "temp";
		CSVReader br = null;
		String nameFile = Constants.INPUT_WORKSPACE + "/coral_pair_competition_probability_simulation_community_" + Constants.communityNumber + ".csv" ;

        try {
                br = new CSVReader (new FileReader(nameFile),',','\'',CSVParser.DEFAULT_QUOTE_CHARACTER,Constants.CORAL_PAIR_COMP_PROBA_HEADER_LINES); // CSVParser.DEFAULT_QUOTE_CHARACTER: quotes are removed for strings
                String[] line = null;
//                line = br.readNext();
//                boolean keepWhile = true ;   
                while((line = br.readNext()) != null){
                	boolean rightOrder = line[0].equals(sp_growing) && line[1].equals(sp_defending) ;
                	boolean oppositOrder = line[1].equals(sp_growing) && line[0].equals(sp_defending) ;
                	if(rightOrder || oppositOrder){
                		if(rightOrder){
                    		proba = Double.parseDouble(line[2]); 
                		}else{
                    		proba = 1.0 - Double.parseDouble(line[2]); 
                		}
//                		System.out.printf("************ %s %s %.4f \n",sp_growing,sp_defending,proba);               				
                		break;
                	}
                }
        } catch (final FileNotFoundException e) {
                e.printStackTrace();
        } catch (final IOException e) {
                e.printStackTrace();
        } finally {
                if (br != null) {
                        try {
                                br.close();
                        } catch (final IOException e) {
                                e.printStackTrace();
                        }
                }
        }
		return proba ;
	}
	

	
	/********************************************************************************************************
	 ******************* Commands that write CSV files *******************************************************
	 ********************************************************************************************************/
	
	/**
	 * Called by calculatePercentageCover_and_NumberRecruits()
	 * @param percentageCover_updated_sink
	 */
	public static void writeCSVPercentageCover(List<PercentageCover> percentageCover_updated_sink){
		
		final String COMMA_DELIMITER = "," ;
		final String NEW_LINE_SEPARATOR = "\n" ;
		final String workSpace = Constants.OUTPUT_WORKSPACE + "/PercentageCover" ;
	
		String FILE_HEADER = "year";		
		for(String namesSp : Constants.speciesNamesList){         // 
			FILE_HEADER = FILE_HEADER + COMMA_DELIMITER + namesSp ;
		}
		
		FILE_HEADER = FILE_HEADER + COMMA_DELIMITER + "event" ;
		
//		String nameCSV = "/PercentageCover_ComNumber_"+Constants.communityNumber+"_"+"DistuScenaNumber_"+Constants.disturbanceScenarioNumber+"_"+"Connectivity_"+Constants.connectivity+"_"+ Constants.UUID_here+".csv" ;	
		String nameCSV = "/PercentageCover_" + "UUID_" + Constants.UUID_here + "_SEED_" + Constants.randomSeed + ".csv" ;	

//		if(file.exists()) {  // to make sure the the csv file create is unique
//			while(file.exists()) {
//				SMUtils.setUUID() ; // genrate a new value for UUID_here
//				 nameCSV = "/PercentageCover_ComNumber_"+Constants.communityNumber+"_"+"DistuScenaNumber_"+Constants.disturbanceScenarioNumber+"_"+"Connectivity_"+Constants.connectivity+"_"+ Constants.UUID_here+".csv" ;	
//				 file = new File(workSpace + nameCSV);
//			}
//		}
//		System.out.printf("********coverPathFile: %s \n",workSpace + nameCSV);

		FileWriter fileWriter = null ;
		
		try{

			File file = new File(workSpace + nameCSV);
//			System.out.printf("******************** Does File exist: %b \n",file.exists()); 
			if(!file.exists()&& !file.isDirectory()) {
//				file.createNewFile();
				fileWriter = new FileWriter(file,true);  // append = true --> data is written at the end of the file
				fileWriter.append(FILE_HEADER.toString()) ;
				fileWriter.append(NEW_LINE_SEPARATOR) ;
			}else {
				fileWriter = new FileWriter(file,true);  // append = true --> data is written at the end of the file
			}
			
			//System.out.printf("Absolute path file: %s \n",file.getAbsolutePath());
//			System.out.printf("Is parent directory created: %s \n",file.getParentFile().mkdirs());  // solved the mssing parent directory problem happening during the batch runs
			
			for(PercentageCover coverMeas : percentageCover_updated_sink){
				fileWriter.append(String.valueOf(coverMeas.getYear()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp1Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp2Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp3Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp4Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp5Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp6Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp7Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp8Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp9Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp10Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp11Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getSp12Cover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getMacroalgaeCover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getTurfCover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getCCACover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getAMACover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getACACover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getHalimedaCover()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getCoverSand()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(coverMeas.getEvent()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			
//			 System.out.println("******************* CSV file was created successfully !!!**************");

		}catch (Exception e) {
			System.out.println("Error in CsvFileWriter in writeCSVPercentageCover !!!");
			e.printStackTrace();

		}finally {
			try{
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e){
				System.out.println("Error while flushing/closing fileWriter in writeCSVPercentageCover !!!");
				 e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Called bu calculatePercentageCover_and_NumberRecruits()
	 * @param percentageCoverList
	 */
	public static void writeCSVNumberRecruits(List<NumberCoralRecruits> numberRecruits_updated_list){
		 
		final String COMMA_DELIMITER = "," ;
		final String NEW_LINE_SEPARATOR = "\n" ;
		String FILE_HEADER = "year";		
		String nameCSV = "/NumberRecruits_"+ "UUID_" + Constants.UUID_here + "_SEED_" + Constants.randomSeed + ".csv" ;	
		final String workSpace = Constants.OUTPUT_WORKSPACE + "/NumberRecruits" ;
	
//		System.out.printf("********** Constants.OUTPUT_WORKSPACE: %s \n",Constants.OUTPUT_WORKSPACE);
		
//		System.out.printf("********** numberCoralSpecies: %d \n",numberCoralSpecies);
		
		for(String speciesName : Constants.speciesNamesList) {                  // so we only input the name of the coral species 
			boolean isCoralSpecies = !speciesName.equals(Constants.SPECIES_MACROALGAE) && !speciesName.equals(Constants.SPECIES_TURF) && !speciesName.equals(Constants.SPECIES_CCA) &&
									!speciesName.equals(Constants.SPECIES_AMA) && !speciesName.equals(Constants.SPECIES_ACA) && !speciesName.equals(Constants.SPECIES_HALIMEDA) ;
			if(isCoralSpecies) {
				FILE_HEADER = FILE_HEADER + COMMA_DELIMITER + speciesName ;
			}
		}
		
		FILE_HEADER = FILE_HEADER + COMMA_DELIMITER + "event" ;
		
		FileWriter fileWriter = null ;
		
//		if(file.exists()) {  // to make sure the the csv file create is unique
//			while(file.exists()) {
//				SMUtils.setUUID() ; // genrate a new value for UUID_here
//				 nameCSV = "/PercentageCover_ComNumber_"+Constants.communityNumber+"_"+"DistuScenaNumber_"+Constants.disturbanceScenarioNumber+"_"+"Connectivity_"+Constants.connectivity+"_"+ Constants.UUID_here+".csv" ;	
//				 file = new File(workSpace + nameCSV);
//			}
//		}
		
		try{
			File file = new File(workSpace + nameCSV) ;
			if(!file.exists() && !file.isDirectory()) {
				fileWriter = new FileWriter(file,true);  // true
				fileWriter.append(FILE_HEADER.toString());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}else {
				fileWriter = new FileWriter(file,true);  // true
			}
			
			for(NumberCoralRecruits numberRecruits : numberRecruits_updated_list){
									
				fileWriter.append(String.valueOf(numberRecruits.getYear()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp1numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp2numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp3numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp4numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp5numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp6numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp7numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp8numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp9numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp10numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp11numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getSp12numberRecruits()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(numberRecruits.getEvent()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
//			 System.out.println("CSV file was created successfully !!!");
		}catch (Exception e) {
			System.out.println("Error in CsvFileWriter writeCSVNumberRecruits !!!");
			e.printStackTrace();
		}finally {
			try{
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e){
				System.out.println("Error while flushing/closing fileWriter writeCSVNumberRecruits !!!");
				 e.printStackTrace();
			}	
		}
	}
	
	
	public static void writeCSVParametersSimulation() {
		
		final String COMMA_DELIMITER = "," ;
		final String NEW_LINE_SEPARATOR = "\n" ;
		String FILE_HEADER = "temp";		
		String nameCSV = "/Parameters_values_simulations.csv" ;	
		final String workSpace = Constants.OUTPUT_WORKSPACE + "/Parameters_values";
	
		FileWriter fileWriter = null ;
		
		
		FILE_HEADER = 
				"randomSeed" + COMMA_DELIMITER +
				"UUID_here" + COMMA_DELIMITER +
				
				"sizeReef" + COMMA_DELIMITER +
				
				"communityNumber" + COMMA_DELIMITER +
				"disturbanceScenarioNumber" + COMMA_DELIMITER +
				"yearDivision"  + COMMA_DELIMITER +
				
				"proba_grazing_MA" + COMMA_DELIMITER +
				"proba_grazing_AMA" + COMMA_DELIMITER +
				"proba_grazing_Halimeda" + COMMA_DELIMITER +
				"proba_grazing_ACA" + COMMA_DELIMITER +
				"proba_grazing_Turf" + COMMA_DELIMITER +
				"proba_grazing_CCA"  + COMMA_DELIMITER +
				
				"GR_reduction_bleachedCoral"+ COMMA_DELIMITER +
				"GR_reduction_interaction"+ COMMA_DELIMITER +
				
				"ratioAreaBranchingPlating_OvertopColonies"  + COMMA_DELIMITER +
				"height_BigAlgae"  + COMMA_DELIMITER +
				"height_Turf" + COMMA_DELIMITER +
				"height_CCA_EncrustingCoral"+ COMMA_DELIMITER +
				"areaBranchingPlating_OvertopBigAlgae"+ COMMA_DELIMITER +
				"areaBranchingPlating_OvertopTurf"+ COMMA_DELIMITER +
				"areaBranchingPlating_OvertopFlat"+ COMMA_DELIMITER +
				"proba_MA_winCoral"+ COMMA_DELIMITER +
				"proba_Halimeda_winCoral"+ COMMA_DELIMITER +
				"proba_AMA_winCoral"+ COMMA_DELIMITER +
				"proba_Turf_winCoral"+ COMMA_DELIMITER +
				"proba_ACA_winCoral"+ COMMA_DELIMITER +
				"proba_CCA_winCoral"+ COMMA_DELIMITER +
				"proba_Algae_coverCCA"+ COMMA_DELIMITER +
		
				"rb_tables_or_plates"+ COMMA_DELIMITER +
				"rb_branching"+ COMMA_DELIMITER +
				"rb_corymbose"+ COMMA_DELIMITER +
				"rb_digitate"+ COMMA_DELIMITER +
				"rb_columnar"+ COMMA_DELIMITER +
				"rb_encrusting_long_upright"+ COMMA_DELIMITER +
				"Nb_tables_or_plates"+ COMMA_DELIMITER +
				"Nb_branching"+ COMMA_DELIMITER +
				"Nb_corymbose"+ COMMA_DELIMITER +
				"Nb_digitate"+ COMMA_DELIMITER +
				"Nb_columnar"+ COMMA_DELIMITER +
				"Nb_encrusting_long_upright"+ COMMA_DELIMITER +
				"hb_tables_or_plates"+ COMMA_DELIMITER +
				"hb_branching"+ COMMA_DELIMITER +
				"hb_corymbose"+ COMMA_DELIMITER +
				"hb_digitate"+ COMMA_DELIMITER +
				"hb_laminar"+ COMMA_DELIMITER +
				"hb_columnar"+ COMMA_DELIMITER +
				"hb_encrusting_long_upright"+ COMMA_DELIMITER +
				"intercept_polypMaturity"+ COMMA_DELIMITER +
				"slope_polypMaturity"+ COMMA_DELIMITER +
				"fertilizationRate"+ COMMA_DELIMITER +
				"predationRate"+ COMMA_DELIMITER +
				
				"retentionTime"+ COMMA_DELIMITER +
				"proportion_Male_polyps"+ COMMA_DELIMITER +
				"otherProportions"+ COMMA_DELIMITER +
				"connectivity"+ COMMA_DELIMITER +
				"numberLarvae_leaving_otherReef"+ COMMA_DELIMITER +
				"proportionLarvae_leaving_remoteReef"+ COMMA_DELIMITER +
				"proportion_larvae_from_otherReef_reachingFocalReef_HighConnectivity"+ COMMA_DELIMITER +
				"proportion_larvae_from_otherReef_reachingFocalReef_MediumConnectivity"+ COMMA_DELIMITER +
				"proportion_larvae_from_otherReef_reachingFocalReef_LowConnectivity"+ COMMA_DELIMITER +
				"proportion_larvae_from_otherReef_reachingFocalReef_Isolated100km"+ COMMA_DELIMITER +
				"proportion_larvae_from_otherReef_reachingFocalReef_Isolated200km"+ COMMA_DELIMITER +
				"proportionCompetentAlive_larvae_from_otherReef_HighConnectivity"+ COMMA_DELIMITER +
				"proportionCompetentAlive_larvae_from_otherReef_MediumConnectivity"+ COMMA_DELIMITER +
				"proportionCompetentAlive_larvae_from_otherReef_LowConnectivity"+ COMMA_DELIMITER +
				"proportionCompetentAlive_larvae_from_otherReef_Isolated100km"+ COMMA_DELIMITER +
				"proportionCompetentAlive_larvae_from_otherReef_Isolated200km"+ COMMA_DELIMITER +
				"proba_settle_CCA"+ COMMA_DELIMITER +
				"proba_settle_Barren_ground"+ COMMA_DELIMITER +
				"proba_settle_Dead_coral"+ COMMA_DELIMITER +
				"proba_Xmonth_recruitment"+ COMMA_DELIMITER +
				
				"Bleaching_diff_response"+ COMMA_DELIMITER +
				"numberCycloneDMTmodel"+ COMMA_DELIMITER +
				"numberGrazingModel"+ COMMA_DELIMITER +
		
				"outprint_data"+ COMMA_DELIMITER +
		
				"RugosityGrazingDo" + COMMA_DELIMITER ;

		
		try{
			
			File file = new File(workSpace + nameCSV);
			if(!file.exists() && !file.isDirectory()) {
				fileWriter = new FileWriter(file,true);  // true
				fileWriter.append(FILE_HEADER.toString());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}else {
				fileWriter = new FileWriter(file,true);  // true
			}
			
			fileWriter.append(String.valueOf(Constants.randomSeed));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.UUID_here));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.sizeReef));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.communityNumber));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.disturbanceScenarioNumber));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.yearDivision));
			fileWriter.append(COMMA_DELIMITER);

			fileWriter.append(String.valueOf(Constants.proba_grazing_MA));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_grazing_AMA));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_grazing_Halimeda));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_grazing_ACA));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_grazing_Turf));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_grazing_CCA ));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.GR_reduction_bleachedCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.GR_reduction_interaction));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.ratioAreaBranchingPlating_OvertopColonies ));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.height_BigAlgae ));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.height_Turf));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.height_CCA_EncrustingCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.areaBranchingPlating_OvertopBigAlgae));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.areaBranchingPlating_OvertopTurf));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.areaBranchingPlating_OvertopFlat));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_MA_winCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_Halimeda_winCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_AMA_winCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_Turf_winCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_ACA_winCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_CCA_winCoral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_Algae_coverCCA));
			fileWriter.append(COMMA_DELIMITER);
	
			fileWriter.append(String.valueOf(Constants.rb_tables_or_plates));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.rb_branching));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.rb_corymbose));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.rb_digitate));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.rb_columnar));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.rb_encrusting_long_upright));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.Nb_tables_or_plates));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.Nb_branching));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.Nb_corymbose));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.Nb_digitate));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.Nb_columnar));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.Nb_encrusting_long_upright));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.hb_tables_or_plates));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.hb_branching));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.hb_corymbose));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.hb_digitate));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.hb_laminar));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.hb_columnar));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.hb_encrusting_long_upright));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.intercept_polypMaturity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.slope_polypMaturity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.fertilizationRate));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.predationRate));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.retentionTime));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportion_Male_polyps));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.otherProportions));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.connectivity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.numberLarvae_leaving_otherReef));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportionLarvae_leaving_remoteReef));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportion_larvae_from_otherReef_reachingFocalReef_HighConnectivity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportion_larvae_from_otherReef_reachingFocalReef_MediumConnectivity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportion_larvae_from_otherReef_reachingFocalReef_LowConnectivity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportion_larvae_from_otherReef_reachingFocalReef_Isolated100km));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportion_larvae_from_otherReef_reachingFocalReef_Isolated200km));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportionCompetentAlive_larvae_from_otherReef_HighConnectivity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportionCompetentAlive_larvae_from_otherReef_MediumConnectivity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportionCompetentAlive_larvae_from_otherReef_LowConnectivity));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportionCompetentAlive_larvae_from_otherReef_Isolated100km));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proportionCompetentAlive_larvae_from_otherReef_Isolated200km));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_settle_CCA));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_settle_Barren_ground));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_settle_Dead_coral));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.proba_Xmonth_recruitment));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.Bleaching_diff_response));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.numberCycloneDMTmodel));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(Constants.numberGrazingModel));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.outprint_data));
			fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(String.valueOf(Constants.RugosityGrazingDo));
			// fileWriter.append(COMMA_DELIMITER);
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			
//			 System.out.println("CSV file was created successfully !!!");

		}catch (Exception e) {
			System.out.println("Error in CsvFileWriter writeCSVParametersSimulation() !!!");
			e.printStackTrace();

		}finally {
			try{
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e){
				System.out.println("Error while flushing/closing fileWriter writeCSVParametersSimulation() !!!");
				 e.printStackTrace();
			}	
		}
	
		
	}
	
	
	/**
	 * Calculate the percentage cover of each coral species (alive) and algae FG as well as the number of coral recruits .m^-2 by species.
	 * Update the lists CoralReef2Builder.percentageCover_updated_sink and CoralReef2Builder.numberRecruits_updated_list.
	 * Finally, write the data in corresponding csv files.
	 * Note that CoralReef2Builder.numberRecruits_updated_list is only updated at the end of the time step (i.e., nameEvent.equals("final_count")).
	 * The method also update the  ArrayList<Agent>() ContextCoralReef2.listAgentToCheckSizeColony
	 * @param year
	 * @param listAllAgents
	 * @param community_number_asked
	 * @return PercentageCover instance
	 */
//	public static List<PercentageCover> calculatePercertageCover_and_NumberRecruits(double year, ArrayList<Agent> listAllAgents,String nameEvent){	
	public static void calculatePercentageCover_and_NumberRecruits(double year, ArrayList<Agent> listAllAgents,String nameEvent){	
		
		ContextCoralReef2.listAgentToCheckSizeColony.clear();  // used in the following method ContextCoralReef2.updateAgentColonySize() 

		// for cover of different groups of substrate
		int nbCoralSp1 = 0 ;
		int nbCoralSp2 = 0 ;
		int nbCoralSp3 = 0 ;
		int nbCoralSp4 = 0 ;
		int nbCoralSp5 = 0 ;
		int nbCoralSp6 = 0 ;
		int nbCoralSp7 = 0 ;
		int nbCoralSp8 = 0 ;
		int nbCoralSp9 = 0 ;
		int nbCoralSp10 = 0 ;
		int nbCoralSp11 = 0 ;
		int nbCoralSp12 = 0 ;
		int nbMacrolagae = 0 ;
		int nbTurf = 0 ;
		int nbCCA= 0 ;
		int nbAMA = 0 ;
		int nbACA = 0 ;
		int nbHalimeda = 0 ;
		int nbSand = 0 ;
		
		// for number of coral recruits
		int nbCoralRecruitsSp1 = 0 ;
		int nbCoralRecruitsSp2 = 0 ;
		int nbCoralRecruitsSp3 = 0 ;
		int nbCoralRecruitsSp4 = 0 ;
		int nbCoralRecruitsSp5 = 0 ;
		int nbCoralRecruitsSp6 = 0 ;
		int nbCoralRecruitsSp7 = 0 ;
		int nbCoralRecruitsSp8 = 0 ;
		int nbCoralRecruitsSp9 = 0 ;
		int nbCoralRecruitsSp10 = 0 ;
		int nbCoralRecruitsSp11 = 0 ;
		int nbCoralRecruitsSp12 = 0 ;
//		int nbRecruitsMacrolagae = 0 ;
//		int nbRecruitsTurf = 0 ;
//		int nbRecruitsCCA= 0 ;
//		int nbRecruitsAMA = 0 ;
//		int nbRecruitsACA = 0 ;
//		int nbRecruitsHalimeda = 0 ;
//		int nbRecruitsSand = 0 ;
		
		// for cover
		for(Agent ag : ContextCoralReef2.listAllAgents){
			String speciesAgent = ag.getSpecies() ;
			String subCatAgent = ag.getSubstrateSubCategory() ;
			String catAgent = ag.getSubstrateCategory() ;
			if(subCatAgent.equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){
				boolean coralSp1 = speciesAgent.equals(Constants.SPECIES_CORAL_SP1) ;
				boolean coralSp2 = speciesAgent.equals(Constants.SPECIES_CORAL_SP2) ;
				boolean coralSp3 = speciesAgent.equals(Constants.SPECIES_CORAL_SP3) ;
				boolean coralSp4 = speciesAgent.equals(Constants.SPECIES_CORAL_SP4) ;
				boolean coralSp5 = speciesAgent.equals(Constants.SPECIES_CORAL_SP5) ;
				boolean coralSp6 = speciesAgent.equals(Constants.SPECIES_CORAL_SP6) ;
				boolean coralSp7 = speciesAgent.equals(Constants.SPECIES_CORAL_SP7) ;
				boolean coralSp8 = speciesAgent.equals(Constants.SPECIES_CORAL_SP8) ;
				boolean coralSp9 = speciesAgent.equals(Constants.SPECIES_CORAL_SP9) ;
				boolean coralSp10 = speciesAgent.equals(Constants.SPECIES_CORAL_SP10) ;
				boolean coralSp11 = speciesAgent.equals(Constants.SPECIES_CORAL_SP11) ;
				boolean newRecruit = ag.getNewRecruit() ;
				if(coralSp1) {
					nbCoralSp1++ ;
					if(newRecruit) {
						nbCoralRecruitsSp1 ++ ;
					}
				}else if(coralSp2) {
					nbCoralSp2++ ;
					if(newRecruit) {
						nbCoralRecruitsSp2 ++ ;
					}
				}else if(coralSp3) {
					nbCoralSp3++ ;
					if(newRecruit) {
						nbCoralRecruitsSp3 ++ ;
					}
				}else if(coralSp4) {
					nbCoralSp4++ ;
					if(newRecruit) {
						nbCoralRecruitsSp4 ++ ;
					}
				}else if(coralSp5) {
					nbCoralSp5++ ;
					if(newRecruit) {
						nbCoralRecruitsSp5 ++ ;
					}
				}else if(coralSp6) {
					nbCoralSp6++ ;
					if(newRecruit) {
						nbCoralRecruitsSp6 ++ ;
					}
				}else if(coralSp7) {
					nbCoralSp7++ ;
					if(newRecruit) {
						nbCoralRecruitsSp7 ++ ;
					}
				}else if(coralSp8) {
					nbCoralSp8++ ;
					if(newRecruit) {
						nbCoralRecruitsSp8 ++ ;
					}
				}else if(coralSp9) {
					nbCoralSp9++ ;
					if(newRecruit) {
						nbCoralRecruitsSp9 ++ ;
					}
				}else if(coralSp10) {
					nbCoralSp10++ ;
					if(newRecruit) {
						nbCoralRecruitsSp10 ++ ;
					}
				}else if(coralSp11) {
					nbCoralSp11++ ;
					if(newRecruit) {
						nbCoralRecruitsSp11 ++ ;
					}
				}else{
					nbCoralSp12++ ;
					if(newRecruit) {
						nbCoralRecruitsSp12 ++ ;
					}
				}
			}else if(catAgent.equals(Constants.SUBSTRATE_CATEGORY_ALGAE)){
				boolean subCatMA = subCatAgent.equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE) ;
				boolean subCatTurf = subCatAgent.equals(Constants.SUBSTRATE_SUBCATEGORY_TURF) ;
				boolean subCatCCA = subCatAgent.equals(Constants.SUBSTRATE_SUBCATEGORY_CCA) ;
				boolean subCatAMA = subCatAgent.equals(Constants.SUBSTRATE_SUBCATEGORY_AMA) ;
				boolean subCatACA = subCatAgent.equals(Constants.SUBSTRATE_SUBCATEGORY_ACA) ;
				if(subCatMA){
					nbMacrolagae++ ;
				}else if(subCatTurf){
					nbTurf++ ;
				}else if(subCatCCA){
					nbCCA++ ;
				}else if(subCatAMA){
					nbAMA++ ;
				}else if(subCatACA){
					nbACA++ ;
				}else {
					nbHalimeda++ ;
				}
			}else if(speciesAgent.equals(Constants.SPECIES_SAND)) {
				nbSand++ ;
			}
			/*
			 * Update the ContextCoralReef2.listAgentToCheckSizeColony content to save time 
			 */
			if(ag.getIDNumber() != 0) {     // agents on a colony have an IDnumber !=0
				ContextCoralReef2.listAgentToCheckSizeColony.add(ag) ;
			}else if(ag.getPlanarAreaColony() != 0) {     // just in case, if not on a colony, its planar area should be = 0
				ag.setPlanarAreaColony(0);
			}
		}
		
		// update PercentageCover instance inside ContextCoralReef2.percentageCover_updated_sink 
		CoralReef2Builder.percentageCover_updated_sink.get(0).setYear(year) ;
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp1Cover((double)nbCoralSp1/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp2Cover((double)nbCoralSp2/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp3Cover((double)nbCoralSp3/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp4Cover((double)nbCoralSp4/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp5Cover((double)nbCoralSp5/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp6Cover((double)nbCoralSp6/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp7Cover((double)nbCoralSp7/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp8Cover((double)nbCoralSp8/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp9Cover((double)nbCoralSp9/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp10Cover((double)nbCoralSp10/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp11Cover((double)nbCoralSp11/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSp12Cover((double)nbCoralSp12/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setMacroalgaeCover((double)nbMacrolagae/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setTurfCover((double)nbTurf/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setCCACover((double)nbCCA/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setAMACover((double)nbAMA/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setACACover((double)nbACA/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setHalimedaCover((double)nbHalimeda/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setSandCover((double)nbSand/Constants.sizeReef*100);
		CoralReef2Builder.percentageCover_updated_sink.get(0).setevent(nameEvent);

		// save the percentageCover_updated_sink.get(0) in csv file
		 writeCSVPercentageCover(CoralReef2Builder.percentageCover_updated_sink) ;
	
//		System.out.printf("coralSp1: %.2f %.2f  \n",coverMeasure.getSp1Cover(),nbCoralSp1/Constants.sizeReef*100) ;
//		System.out.printf("coralSp2: %.2f %.2f  \n",coverMeasure.getSp2Cover(),nbCoralSp2/Constants.sizeReef*100) ;
//		System.out.printf("coralSp3: %.2f %.2f  \n",coverMeasure.getSp3Cover(),nbCoralSp3/Constants.sizeReef*100) ;
//		System.out.printf("AMA: %.2f %.2f  \n",coverMeasure.getAMACover(),nbAMA/Constants.sizeReef*100) ;
//		System.out.printf("Turf: %.2f %.2f  \n",coverMeasure.getTurfCover(),nbTurf/Constants.sizeReef*100) ;

		if(nameEvent.equals("end_season")) {  // only assess number of recruit at the end of the time period
						
			// update ContextCoralReef2.numberRecruits_updated
			CoralReef2Builder.numberRecruits_updated_list.get(0).setYear(year) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp1numberRecruits((double)nbCoralRecruitsSp1/Constants.sizeReef*10000) ;  // *10000 because it the output is in .m^-2
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp2numberRecruits((double)nbCoralRecruitsSp2/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp3numberRecruits((double)nbCoralRecruitsSp3/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp4numberRecruits((double)nbCoralRecruitsSp4/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp5numberRecruits((double)nbCoralRecruitsSp5/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp6numberRecruits((double)nbCoralRecruitsSp6/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp7numberRecruits((double)nbCoralRecruitsSp7/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp8numberRecruits((double)nbCoralRecruitsSp8/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp9numberRecruits((double)nbCoralRecruitsSp9/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp10numberRecruits((double)nbCoralRecruitsSp10/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp11numberRecruits((double)nbCoralRecruitsSp11/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setSp12numberRecruits((double)nbCoralRecruitsSp12/Constants.sizeReef*10000) ;
//			CoralReef2Builder.numberRecruits_updated_list.get(0).setMacroalgaenumberRecruits((double)nbRecruitsMacrolagae/Constants.sizeReef*10000) ;			
//			CoralReef2Builder.numberRecruits_updated_list.get(0).setTurfnumberRecruits((double)nbRecruitsTurf/Constants.sizeReef*10000) ;
//			CoralReef2Builder.numberRecruits_updated_list.get(0).setCCAnumberRecruits((double)nbRecruitsCCA/Constants.sizeReef*10000) ;
//			CoralReef2Builder.numberRecruits_updated_list.get(0).setAMAnumberRecruits((double)nbRecruitsAMA/Constants.sizeReef*10000) ;
//			CoralReef2Builder.numberRecruits_updated_list.get(0).setACAnumberRecruits((double)nbRecruitsACA/Constants.sizeReef*10000) ;
//			CoralReef2Builder.numberRecruits_updated_list.get(0).setHalimedanumberRecruits((double)nbRecruitsHalimeda/Constants.sizeReef*10000) ;
//			CoralReef2Builder.numberRecruits_updated_list.get(0).setSandnumberRecruits((double)nbSand/Constants.sizeReef*100/Constants.sizeReef*10000) ;
			CoralReef2Builder.numberRecruits_updated_list.get(0).setevent(nameEvent) ;
			
			// save the percentageCover_updated_sink.get(0) in csv file
			writeCSVNumberRecruits(CoralReef2Builder.numberRecruits_updated_list) ;
			 
//			System.out.printf("******SMUtils. %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f \n",
//			coverMeasure_recruits.getYear(),coverMeasure_recruits.getSp1Cover(),coverMeasure_recruits.getSp2Cover(),coverMeasure_recruits.getSp3Cover(),
//			coverMeasure_recruits.getSp4Cover(),coverMeasure_recruits.getSp5Cover(),coverMeasure_recruits.getSp6Cover(),coverMeasure_recruits.getSp7Cover(),
//			coverMeasure_recruits.getSp8Cover(),coverMeasure_recruits.getSp9Cover(),coverMeasure_recruits.getSp10Cover(),coverMeasure_recruits.getSp11Cover(),
//			coverMeasure_recruits.getSp12Cover(),coverMeasure_recruits.getMacroalgaeCover(),coverMeasure_recruits.getTurfCover(),coverMeasure_recruits.getCCACover()) ;
		}	
	}
	
	/** NOT USED
	 * Methods that executes the coral_pair_competition_probability.R via the command line. 
	 * The coral_pair_competition_probability.R reads the coral_pair_competition_probability.csv in datasets_iputs folder
	 * and returns a subset with only the species present in the simulation. 
	 */
	public static void createCSVCoralPairInteracrtion(){  
		
		SMUtils.writeCSVNameCoralSpeciesPresent(); // creates the Species_present_in_simulation.csv file, that will be read by coral_pair_competition_probability.R
		
		System.out.printf("******* R_path: %s Rscript name: %s \n",Constants.R_path,Constants.COMPETITION_R_SCRIPT);
		
		try {
			Process p = Runtime.getRuntime().exec(Constants.R_path + " " + Constants.COMPETITION_R_SCRIPT) ;	
			
			try {
				p.waitFor() ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// change the R script so that it only create a csv file for the present community (if not created yet)
	/**
	 * Same as before but only if the Species_present_in_simulation_community_#.csv has not been created yet 
	 */
	public static void createCSVCoralPairInteracrtionBIS(){
		// check if the file exist. If not, create it, if yes do nothing
		final String nameCSV = "/Species_present_in_simulation_community_" + Constants.communityNumber + ".csv" ;	
		File file = new File(Constants.INPUT_WORKSPACE + nameCSV) ;
		
//		System.out.printf("**** %s %b \n", file,file.exists());
		
		if(!file.exists()) {
			SMUtils.writeCSVNameCoralSpeciesPresent(); // creates the Species_present_in_simulation.csv file, that will be read by coral_pair_competition_probability.R		
			// http://docs.renjin.org/en/latest/library/evaluating.html
		    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
		    // create a Renjin engine:
		    ScriptEngine engine = factory.getScriptEngine();
		    		    
			try {
//				System.out.printf("****** %s \n", Constants.COMPETITION_R_SCRIPT) ;
				engine.eval(new java.io.FileReader(Constants.COMPETITION_R_SCRIPT));
				String pathWithQuote =  '\'' + Constants.wd + '\'' ;
				String stringToAdd = "workingDirectory <- " + pathWithQuote + " ; communtyNb <- " + Constants.communityNumber ;			
//				System.out.printf("****** %s \n",stringToAdd) ;
				engine.eval(stringToAdd) ;
				engine.eval("createOutcomeCompetition(workingDirectory,communtyNb)");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ScriptException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// other solution: does not work 
//			ScriptEngineManager manager = new ScriptEngineManager(); // https://stackoverflow.com/questions/24640249/if-you-have-created-a-function-in-r-how-do-you-call-it-in-java-using-renjin
//			// create a Renjin engine:
//			ScriptEngine engine = manager.getEngineByName("Renjin");
		}
	}
	



	
	
	/**
	 * Methods called via SMUtils.createCSVCoralPairInteracrtion(), during the CoralReef2Builder execution.
	 * It creates the Species_present_in_simulation.csv that we be will read by coral_pair_competition_probability.R
	 * It only create the csv file if the file is not already present in the directory
	 */
	public static void writeCSVNameCoralSpeciesPresent(){
		
		final String nameCSV = "/Species_present_in_simulation_community_" + Constants.communityNumber + ".csv" ;	
		final String workSpace = Constants.INPUT_WORKSPACE ;

		File f = new File(workSpace + nameCSV);
		
		if(!f.exists()) { 
			final String NEW_LINE_SEPARATOR = "\n" ;
			String FILE_HEADER = "species_names";		
			
			FileWriter fileWriter = null ;
			File file = new File(workSpace + nameCSV);
			try{
				file.createNewFile();
				fileWriter = new FileWriter(file,true);  // 
				fileWriter.append(FILE_HEADER.toString());
				fileWriter.append(NEW_LINE_SEPARATOR);
				for(String name:Constants.coralSpeciesPresentList){
					fileWriter.append(name);
					fileWriter.append(NEW_LINE_SEPARATOR);
				}
				
//				System.out.println("In SMUtils: Species_present_in_simulation.csv was created successfully !!!");

			}catch (Exception e) {
				System.out.println("Error in CsvFileWriter writeCSVNameCoralSpeciesPresent !!!");
				e.printStackTrace();

			}finally {
				try{
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e){
					System.out.println("Error while flushing/closing fileWriter writeCSVNameCoralSpeciesPresent !!!");
					 e.printStackTrace();
				}	
			}
		}
	}
	
	/** NOT USED
	 * Creates a .csv file in datasets_inputs folder with the path of this same folder.
	 * This .csv file is then read by coral_pair_competition_probability.R, called by SMUtils.read_R_path() 
	 * @param Input_Workspace
	 */
	public static void createCSV_Path_for_Rscript(String Input_Workspace) {
		final String nameCSV = "/Input_Dataset_path.csv" ;
		final String workSpace = Constants.INPUT_WORKSPACE ;
		
		File f = new File(workSpace + nameCSV) ;
		
		if(!f.exists()) {
			final String NEW_LINE_SEPARATOR = "\n" ;
			String FILE_HEADER = "Input_path" ;
			
			FileWriter fileWriter = null ;
			File file = new File(workSpace + nameCSV) ;
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file,false);  // false so the new file overwrite the previous one
				fileWriter.append(FILE_HEADER.toString());
				fileWriter.append(NEW_LINE_SEPARATOR);
				fileWriter.append(Input_Workspace);
				fileWriter.append(NEW_LINE_SEPARATOR);
			}catch (Exception e) {
				System.out.println("Error in CsvFileWriter !!!");
				e.printStackTrace();

			}finally {
				try{
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e){
					System.out.println("Error while flushing/closing fileWriter !!!");
					 e.printStackTrace();
				}	
			}
		}
	}

	
	/** NOT USED
	 * 
	 */
	public static void writeCSVRandomSeeds(){
		final String NEW_LINE_SEPARATOR = "\n" ;
		String FILE_HEADER = "randomSeeds";		
		final String nameCSV = "/randomSeeds.csv" ;	
		final String workSpace = Constants.OUTPUT_WORKSPACE ;
		
		FileWriter fileWriter = null ;
		File file = new File(workSpace + nameCSV);
		try{
			file.createNewFile();
			fileWriter = new FileWriter(file,false);  // false so the new file overwrite the previous one
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(String.valueOf(Constants.randomSeed));
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			System.out.println("In SMUtils: randomSeeds.csv was created successfully !!!");

		}catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();

		}finally {
			try{
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e){
				System.out.println("Error while flushing/closing fileWriter !!!");
				 e.printStackTrace();
			}	
		}
	}	
}