package coralreef2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.apache.commons.collections15.Predicate;

import coralreef2.InputData.CycloneData;
import coralreef2.InputData.Disturbance_bleaching;
import coralreef2.InputData.Disturbance_cyclone;
import coralreef2.InputData.Disturbance_grazing;
import coralreef2.InputData.Disturbances_priority;
import coralreef2.InputData.FunctionalTraitData;
import coralreef2.InputData.Sand_cover;
import coralreef2.agent.Agent;
import coralreef2.common.Constants;
import coralreef2.common.SMUtils;
import Disturbances.Bleaching;
import Disturbances.Cyclone;
import OutputData.ColonyPlanarAreaDistribution;
import OutputData.NumberCoralRecruits;
import OutputData.PercentageCover;
import OutputData.RugosityCoverGrazed;
import repast.simphony.context.Context;
import repast.simphony.context.ContextListener;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.projection.Projection;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.valueLayer.ValueLayer;

public class ContextCoralReef2 extends DefaultContext<Object> {
	
//	public static int numberAgentsGrazed ;
	public static int numberMacroalgaeAgentsInvading ;
	public static int numberTurfAgentsInvading ;
	public static int numberCCAAgentsInvading ;
	public static int numberReplicates ;

//	Iterable<Agent> listAllAgents = RunState.getInstance().getMasterContext().getObjects(Agent.class) ;
	public static ArrayList<Agent> listAllAgents = new ArrayList<Agent>();
//	public static ArrayList<PercentageCover> percentageCoverList = new ArrayList<PercentageCover>();
//	public static ArrayList<PercentageCover> numberRecruitsList = new ArrayList<PercentageCover>() ;        // number of recruits data are created under a list of PercentageCover instance
	public static ArrayList <Agent> listAgentToCheckSizeColony = new ArrayList<Agent>() ;             // this list is cleared and filled every time SMUtils.calculatePercertageCover_and_NumberRecruits() is called
		
	public static double year = 0.0 ;       // timer is one time step ahead of year
	public static double year_event = 0.0 ; // 
	
	// for disturbances:
//	private static boolean thereIsCyclone = false ;
//	private static boolean thereIsBleaching = false ;
	private static double cyclone_DMT = 0.0 ;
	private static double bleaching_DHW = 0.0 ;
	private static double percentage_reef_grazed = 0.0 ;
	private static String priority_1 = "na" ;
	private static String priority_2 = "na" ;
	private static String priority_3 = "na" ;
	public static String season = "na" ;
	private static double sand_cover = 0 ;
	
	/* #####################################
	 * ######## Schedule period 1 ########## Setup all the global variables and the agents-own variables with appropriate values 
	 * #####################################
	 */
	/**
	 * Set the year and the timer.
	 * Timer is a time step ahead of year until we reach the end of the time step
	 */
	@ScheduledMethod(start = 1, interval = 4, priority = 2)
	public void RunTimer(){		
		year = Math.round(CoralReef2Builder.timer * 100.0) / 100.0 ;
		CoralReef2Builder.timer = CoralReef2Builder.timer + 1 / (double)Constants.yearDivision ;
		// this list is made only once per year in order to reduce the time of implementation
//		listAllAgents = SMUtils.getAllAgentList();		
		Constants.counter = 0 ;   // TODO: find what this is for
		double i = RunEnvironment.getInstance().getCurrentSchedule().getTickCount() ;
		
		if(Constants.outprint_data){
			System.out.printf("\n");
			System.out.printf("BEGINING NEW SEASON: year: %.1f timer: %.1f TICK: %.1f \n", year,CoralReef2Builder.timer,i) ;
		}
		
		if (year == 0 ){
			Constants.eventName = Constants.eventName_initial ;

		}else {
			Constants.eventName = Constants.eventName_beginingSeason ;
		}
		
		endSimulation() ; // ends the simulation at Constants.tick_max + 1
		
	}
	
	/**
	 * Get the intensity of a cyclone (DMT) and bleaching event (DHW) are grazing pressure (%).
	 * Get the priority of the events between cyclone, bleaching and coral reproduction.
	 * 
	 *  The percentage_reef_grazed is eventually updated if rugosity influences grazing pressure
	 */
	@ScheduledMethod(start = 1 , interval = 4 , priority = 1.6)
	public void checkDisturbance(){
//		thereIsCyclone = false ;
//		thereIsBleaching = false ;
		for(Disturbances_priority distPrio : Constants.disturbancesPriorityList){
			boolean sameYear = distPrio.getYear() == year ;
			if(sameYear){
//				System.out.printf("year: %.1f, cylcone: %.1f, bleaching: %.1f, priority_1: %s, priority_2: %s, priority_3: %s \n", distData.getYear(),distData.getCyclone_DMT(),distData.getBleaching_DHW(),distData.getPriority_1(),distData.getPriority_2(),distData.getPriority_3()) ;				
//				cyclone_DMT = distData.getCyclone_DMT() ;
//				bleaching_DHW = distData.getBleaching_DHW() ;
//				percentage_reef_grazed = distData.getPercent_reef_grazed() ;
				priority_1 = distPrio.getPriority_1() ;
				priority_2 = distPrio.getPriority_2() ;
				priority_3 = distPrio.getPriority_3() ;	
				season = distPrio.getSeason() ;
//				boolean cycloneHappen = cyclone_DMT != 0.0 ;
//				boolean bleachingHappen = bleaching_DHW != 0.0 ;
//				boolean disturbanceHappen =  cycloneHappen || bleachingHappen ;
//				if(disturbanceHappen){
//					if(cycloneHappen){
//						thereIsCyclone = true ;
//					}
//					if(bleachingHappen){
//						thereIsBleaching = true ;
//					}
//				}
				break ;
			}
		}
		for(Disturbance_cyclone dc : Constants.cycloneDMTDataList) {
			boolean sameYear = dc.getYear() == year ;
			if(sameYear) {
				cyclone_DMT = dc.getCyclone_DMT() ;
				break ;
			}
		}
		for(Disturbance_bleaching db : Constants.bleachingDHWDataList) {
			boolean sameYear = db.getYear() == year ;
			if(sameYear) {
				bleaching_DHW = db.getBleaching_DHW() ;
				break ;
			}
		}
		for(Disturbance_grazing dg : Constants.grazingDataList) {
			boolean sameYear = dg.getYear() == year ;
			if(sameYear) {
				percentage_reef_grazed = dg.getCoverGrazed() ;
				break ;
			}
		}
	}
	
	/** CHECKED
	 * Generate the 1st PercentageCover instance for year = 0 and add it to percentageCoverList
	 */
	@ScheduledMethod(start = 1 , interval = 4 , priority = 1.5)
	public void initialPercentageCover(){
		// ONLY TAKE THE INITIAL PERCENTAGE COVER !!!!
		if (year == 0 ){
			listAllAgents = SMUtils.getAllAgentList() ;		
			
//			System.out.printf("ContextCoralReef2.initialPercentageCover() \n");
//			for(int i = 0 ; i < 10 ; i ++){
//				System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
//			}
			
//			System.out.printf("in Context: %s %d %.1f %s %d %.1f \n",listAllAgents.get(1).getSpecies(),listAllAgents.get(1).getAgentX(),listAllAgents.get(1).getRed(),
//								   listAllAgents.get(100).getSpecies(),listAllAgents.get(100).getAgentX(),listAllAgents.get(100).getRed()) ;
			
			SMUtils.calculatePercentageCover_and_NumberRecruits(year,listAllAgents,"initial_count") ; // also upate listAgentToCheckSizeColony
			
//			PercentageCover coverMeasure = SMUtils.calculatePercertageCover_and_NumberRecruits(year,listAllAgents,"initial_count").get(0) ;
//			percentageCoverList.add(coverMeasure) ;
//			for(PercentageCover pc : percentageCoverList){
//				System.out.printf("In initialPercentageCover(): year: %.1f sp1: %.1f sp2: %.1f sp3: %.1f sp4: %.1f sp5: %.1f sp6: %.1f sp7: %.1f sp8: %.1f sp9: %.1f sp10: %.1f sp11: %.1f sp12: %.1f MA: %.1f Turf: %.1f CCA %.1f AMA %.1f ACA %.1f Hali %.1f \n", 
//						pc.getYear(),pc.getSp1Cover(),pc.getSp2Cover(),pc.getSp3Cover(),pc.getSp4Cover(),pc.getSp5Cover(),pc.getSp6Cover(),
//						pc.getSp7Cover(),pc.getSp8Cover(),pc.getSp9Cover(),pc.getSp10Cover(),pc.getSp11Cover(),pc.getSp12Cover(),
//						pc.getMacroalgaeCover(), pc.getTurfCover(), pc.getCCACover(),pc.getAMACover(),pc.getACACover(),pc.getHalimeda()) ; 
//			}
		}
	}

//	@ScheduledMethod(start = 1, interval = 4, priority = 1)
//	public void setInvasionGrazingToZero(){
//		numberAgentsGrazed = 0 ;
//		numberMacroalgaeAgentsInvading = 0 ;
//		numberTurfAgentsInvading = 0 ;
//		numberCCAAgentsInvading = 0 ;
//		System.out.printf("%s %d \n", "numberAgentsGrazed :",numberAgentsGrazed);
//		System.out.printf("%s %d \n", "numberMacroalgaeAgentsInvading : ",numberMacroalgaeAgentsInvading);
//		System.out.printf("%s %d \n", "numberTurfAgentsInvading : ",numberTurfAgentsInvading);
//		System.out.printf("%s %d \n", "numberCCAAgentsInvading : ",numberCCAAgentsInvading);	
//	}
	
	/************************************************************************************** 
	 * THERE IS A METHOD CALLED setDefaultParameters() IN CLASS AGENT with priority = 0.5 *
	 **************************************************************************************/
	
	/** CHECKED
	 * Method that updates the size of the colony of all the agents (living, bleached or dead coral or algae covering a coral)
	 * It 1st select only the agents that have an IDNumber != 0 from listAgentToCheckSizeColony,
	 * then attributes to each of them the size of their colony (so even algae agent on a colony have the information about the size of the colony).
	 * In addition, the method record the colony species name, the colony size and the colony IDNumber and update CoralReef2Builder.colonyPlanarAreaDistList.
	 * The arrayList listAgentToCheckSizeColony is updated just before either at initialPercentageCover() or at the end at substrateCompositionCSV() (which will serve as the beginning of the next time step)
	 */
	@ScheduledMethod(start = 1, interval = 4, priority = 0.2)
	public static void updateAgentColonySize(){
		
		if(!Constants.eventName.equals(Constants.eventName_beginingSeason)){  // the size class of colonies is not collected at the beginning of a season but at the end of the previous season, except for the initial count:== Constants.eventName_initial
			ColonyPlanarAreaDistribution.upDateColonyPlanarAreaDistList_onlyYear_and_Event(Constants.eventName) ;
		}
		
//		ArrayList <Agent> listAgentToCheckSizeColony = SMUtils.getAllColonyAgentListFromList(listAllAgents) ;     // listAgentToCheckSizeColony is updated via SMUtils.calculatePercentageCover_and_NumberRecruits()
//		List<Integer> IDNumberColoniesLists = new ArrayList<Integer>() ;
		// listAgentToCheckSizeColony is emptied
		while (listAgentToCheckSizeColony.size() > 0){   // listAgentToCheckSizeColony is via SMUtils.calculatePercertageCover()
			Agent selectedAgent = SMUtils.randomElementOf(listAgentToCheckSizeColony) ;
			ArrayList<Agent> listAgentsSameColony = SMUtils.getAgentsFromSameColonyFromList(selectedAgent,listAgentToCheckSizeColony) ; // returns agents based on IDNumber, regardless if the agent is alive or not
			int areaColony = listAgentsSameColony.size() ;
			int colonyIDNumber = selectedAgent.getIDNumber() ;
			
			if(!Constants.eventName.equals(Constants.eventName_beginingSeason)){ // the size class of colonies is not collected at the beginning of a season but at the end of the previous season, except for the initial count:== Constants.eventName_initial
				ColonyPlanarAreaDistribution.upDateColonyPlanarAreaDistList(selectedAgent.getSpecies(),(double)areaColony,colonyIDNumber) ;
			}
			for(Agent agentSameCollony : listAgentsSameColony){
				agentSameCollony.setPlanarAreaColony(areaColony) ;
				agentSameCollony.setSizeUpDated(true) ;
				listAgentToCheckSizeColony.remove(agentSameCollony) ;
				if (selectedAgent.getIDNumber() == 0){ // just to check
					System.out.printf("Context.updateAgentColonySize(), the agent is not on a colony %s \n", selectedAgent.getSpecies()) ;
					break ;
				}
			}
//			IDNumberColoniesLists.add(colonyIDNumber);
		}
//		System.out.println("******************* DUPLICATES in COntext");
//		Collections.sort(IDNumberColoniesLists) ;
//		for(int i = 0 ; i < IDNumberColoniesLists.size() - 1 ; i ++) {
//			System.out.printf("%d ",IDNumberColoniesLists.get(i)) ;
//			if(IDNumberColoniesLists.get(i) == IDNumberColoniesLists.get(i + 1)) {
//				System.out.printf("\n") ;
//				System.out.println("duplicate item "+IDNumberColoniesLists.get(i + 1)+" at Location"+(i+1) );
//			}
//		}
//		System.out.printf("\n") ;
//		
//		for(ColonyPlanarAreaDistribution cpad :  CoralReef2Builder.colonyPlanarAreaDistList) {
////			System.out.printf("HHHEYEYEYEYYYYY year: %.1f %s %s : \n", cpad.getYear(),cpad.getSpeciesName(),cpad.getEvent());
//			for(int i = 0 ; i < cpad.getColonyPlanarAreaList().size() ; i++) {
//				System.out.printf("%.1f ",cpad.getColonyPlanarAreaList().get(i)) ;
//			}
//			System.out.printf("\n") ;
//		}
		

		/*
		 * If rugosity is considered to determine the grazing pressure due to fish.
		 * A deep copy of CoralReef2Builder.colonyPlanarAreaDistList is created because the latter is erased every time the csv file is updated.
		 * The copy is only created once at the very beginning and then at the end of each period ?! --> this is the same then
		 */
		boolean bool = Constants.eventName.equals(Constants.eventName_initial) | Constants.eventName.equals(Constants.eventName_endSeason) ;
		if(Constants.RugosityGrazingDo & bool) {
						
			// Do a deep copy of CoralReef2Builder.colonyPlanarAreaDistList
			CoralReef2Builder.colonyPlanarAreaDistListCopy = new ArrayList<ColonyPlanarAreaDistribution>() ;
			for(ColonyPlanarAreaDistribution cpad : CoralReef2Builder.colonyPlanarAreaDistList) {
				CoralReef2Builder.colonyPlanarAreaDistListCopy.add(cpad.cloneDeepCopy()) ;
			}
		}
		
//		System.out.printf("IN UPDATE COLONY AREA \n") ;
//		System.out.printf("colonyPlanarAreaDistList\n") ;
//		for(ColonyPlanarAreaDistribution cpa : CoralReef2Builder.colonyPlanarAreaDistList) {
//			
//			System.out.printf("%s year: %.1f ",cpa.getSpeciesName(), cpa.getYear()) ;
//			for(int i = 0 ; i < cpa.getColonyPlanarAreaList().size() ; i++) {
//				System.out.printf("%.1f ",cpa.getColonyPlanarAreaList().get(i)) ;
//			}
//			System.out.printf("\n") ;
//		}
//		System.out.printf("          *******************\n") ;
//		System.out.printf("colonyPlanarAreaDistListCopy \n") ;
//		for(ColonyPlanarAreaDistribution cpa : CoralReef2Builder.colonyPlanarAreaDistListCopy) {
//			System.out.printf("%s year: %.1f ",cpa.getSpeciesName(), cpa.getYear()) ;
//			for(int i = 0 ; i < cpa.getColonyPlanarAreaList().size() ; i++) {
//				System.out.printf("%.1f ",cpa.getColonyPlanarAreaList().get(i)) ;
//			}
//			System.out.printf("\n") ;
//		}
//		System.out.printf("          *******************\n") ;
		
		if(!Constants.eventName.equals(Constants.eventName_beginingSeason)){
			ColonyPlanarAreaDistribution.writeCSVColonyPlanarAreaList_EraseList() ; // the csv is updated only (1) when year = 0.0, and (2) after a cyclone (year = year + 0.1) and eventually after a bleaching event (yeary = year + 0.2) and (4) at the end (year = year + 0.5), which is also the beginning of the next time step 
		}
	}
	
	/* #####################################
	 * ######## Schedule period 2 ##########
	 * #####################################
	 */
	
	/** CHECKED
	 *  Random clusters of agents are potentially grazed until the % cover grazed reaches percentage_reef_grazed values from Disturbance_grazing.csv
	 *  All the patches grazed have their own-variable haveIBeenGrazed = T, BUT grazed agents are not necessarily algae.
	 *  Agents with haveIBeenGrazed = T cannot be covered by algae during the growing part of the model.
	 */
	@ScheduledMethod(start = 2, interval = 4, priority = 0.1)
	public void grazing(){
//		listAllAgents = SMUtils.getAllAgentList() ; // no need to update the list, the agents can be updated 
		if(Constants.outprint_data){
			System.out.printf("\n") ;
			System.out.printf("GRAZING: ") ;
		}
		
		// if rugosity determines the % of reef grazed by fish:
		if(Constants.RugosityGrazingDo) {
			
//			System.out.printf("IN GRAZING \n") ;
//			System.out.printf("colonyPlanarAreaDistList\n") ;
//			for(ColonyPlanarAreaDistribution cpa : CoralReef2Builder.colonyPlanarAreaDistList) {
//				
//				System.out.printf("%s year: %.1f ",cpa.getSpeciesName(), cpa.getYear()) ;
//				for(int i = 0 ; i < cpa.getColonyPlanarAreaList().size() ; i++) {
//					System.out.printf("%.1f ",cpa.getColonyPlanarAreaList().get(i)) ;
//				}
//				System.out.printf("\n") ;
//			}
//			System.out.printf("          *******************\n") ;
//			System.out.printf("colonyPlanarAreaDistListCopy \n") ;
//			for(ColonyPlanarAreaDistribution cpa : CoralReef2Builder.colonyPlanarAreaDistListCopy) {
//				System.out.printf("%s year: %.1f ",cpa.getSpeciesName(), cpa.getYear()) ;
//				for(int i = 0 ; i < cpa.getColonyPlanarAreaList().size() ; i++) {
//					System.out.printf("%.1f ",cpa.getColonyPlanarAreaList().get(i)) ;
//				}
//				System.out.printf("\n") ;
//			}
//			System.out.printf("          *******************\n") ;

			// determine the rugosity created by coral colonies and convert it into % reef grazed			
			// using colonyPlanarAreaDistListCopy because colonyPlanarAreaDistList was erased when the csv was updated (in updateAgentColonySize())
			CoralReef2Builder.rugosityCoverGrazedList.add(SMUtils.rugosityToGrazing(CoralReef2Builder.colonyPlanarAreaDistListCopy)) ;
			
			percentage_reef_grazed = percentage_reef_grazed + CoralReef2Builder.rugosityCoverGrazedList.get(0).getGrazingFish() ; // percentage_reef_grazed contains grazing by urchins from the csv and by fish which is determined by rugosity
			
			if(percentage_reef_grazed > 100) {
				percentage_reef_grazed = 100 ;
			}
			
//			if(Constants.outprint_data) {
//				System.out.printf("****************** RUGOSITY: %.1f PERCENTAGE GRAZED: %.1f",
//						CoralReef2Builder.rugosityCoverGrazedList.get(0).getRugosity(),percentage_reef_grazed) ;
//			}
			
			RugosityCoverGrazed.writeCSVRugosityCoverGrazedList_EraseList(percentage_reef_grazed) ;
			
		}
		
//		for(int i = 0 ; i < 10 ; i ++){
//			System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
//		}
				
//		ListIterator<Agent> agentsNotGrazed = listAllAgents.listIterator() ;     // returns a reference of listAllAgents   
		ArrayList<Agent> agentsNotGrazed = new ArrayList<Agent>(listAllAgents)  ;    // shallow copy			 				https://dzone.com/articles/java-copy-shallow-vs-deep-in-which-you-will-swim
//		ArrayList<Agent> agentsNotGrazed = (ArrayList)listAllAgents.clone() ;    	   // shallow copy								https://programmingmitra.blogspot.ca/2016/11/Java-Cloning-Types-of-Cloning-Shallow-Deep-in-Details-with-Example.html
		
		ArrayList<Agent> agentsGrazed = new ArrayList<Agent>() ;
		double proportionReefGrazed = 0.0 ;
		while(proportionReefGrazed < percentage_reef_grazed && agentsNotGrazed.size() != 0){
			ArrayList<Agent> inRadiusAgents = new ArrayList<Agent>() ;
			if(agentsNotGrazed.size() > 1){
				Agent selectedAgent = SMUtils.randomElementOf(agentsNotGrazed) ;
				inRadiusAgents = SMUtils.getRadius3NeighborsCondition(selectedAgent, true,"haveIBeenGrazed") ;  // selects neighboring agents up to 3 cells away that have not been grazed and includes selectedAgent in the list
				
				int numberUnGrazedAgent = inRadiusAgents.size() ;
				if(numberUnGrazedAgent < 29) { // 29 is the max number of agents in inRadiusAgents
					Agent selectedAgent2 = SMUtils.randomElementOf(inRadiusAgents) ;
					ArrayList<Agent> inRadiusAgents2 = SMUtils.getRadius3NeighborsCondition(selectedAgent2, false,"haveIBeenGrazed") ;  
					for(Agent ag : inRadiusAgents2) {
						if(!inRadiusAgents.contains(ag)){
							inRadiusAgents.add(ag) ;
						}
					}
					numberUnGrazedAgent = inRadiusAgents.size() ;
				}
				
			}else if(agentsNotGrazed.size() == 1){				
				for(Agent ag : agentsNotGrazed){ // there is only one agent in agentsNotGrazed
					inRadiusAgents.add(ag) ;
				}
			}
			
			double random = RandomHelper.nextDoubleFromTo(0, 1) ;
			for (Agent agentGrazed : inRadiusAgents){			
				// if agentGrazed is an algae agent
				if(!agentGrazed.haveIBeenGrazed) {  // just to be more sure
					if(agentGrazed.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_ALGAE)){
						boolean agent_is_Grazed = (agentGrazed.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE) && random < Constants.proba_grazing_MA) ||
												  (agentGrazed.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_TURF) && random < Constants.proba_grazing_Turf) ||
												  (agentGrazed.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA) && random < Constants.proba_grazing_CCA) ||
												  (agentGrazed.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_ACA) && random < Constants.proba_grazing_ACA) ||
												  (agentGrazed.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_AMA) && random < Constants.proba_grazing_AMA) ||
												  (agentGrazed.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA) && random < Constants.proba_grazing_Halimeda) ;
						if(agent_is_Grazed){                // agent is turf or not an algae
							agentGrazed.beGrazed();
							agentsNotGrazed.remove(agentGrazed);
							agentsGrazed.add(agentGrazed) ;
						}
					}else{
						agentGrazed.set_HaveIBeenGrazed(true) ;     // non-algae agent can also be grazed but are not converted into substratum 
						agentsNotGrazed.remove(agentGrazed);
						agentsGrazed.add(agentGrazed) ;
					}
				}
//				System.out.printf("reef should be garzed at : %.2f agentsNotGrazed: %d bool: %b %b %b \n",
//						proportionReefGrazed,agentsNotGrazed.size(),proportionReefGrazed < percentage_reef_grazed,agentsNotGrazed.size() != 0,proportionReefGrazed < percentage_reef_grazed && agentsNotGrazed.size() != 0) ;			
			}			
			proportionReefGrazed = (double)agentsGrazed.size() / (double)Constants.sizeReef * 100.0 ;
		}	
//		System.out.printf("ContextCoralReef2.grazing() \n");
		if(Constants.outprint_data){
			System.out.printf("Reef should be grazed at: %.2f and is grazed at: %.2f agentsGrazed.size():%d sizeReef: %.1f listAllAgents.size(): %d \n",percentage_reef_grazed,proportionReefGrazed,agentsGrazed.size(),Constants.sizeReef,listAllAgents.size()) ;
		}
		
//		System.out.printf("listAllAgents after being grazed \n");
//		for(int i = 0 ; i < 10 ; i ++){
//			System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
//		}	
	}
	
	/* #####################################
	 * ######## Schedule period 3 #########   disturbances and reproduction
	 * #####################################
	 */
	/**
	 * Determine the order at which coral reproduction, bleaching event and cyclone happen
	 */
	@ScheduledMethod(start = 3, interval = 4, priority = 0.1)
	public void Disturbance_and_Reproduction(){
		
		year_event = year ;
		
		boolean reproduction_cyclone_bleaching = priority_1.equals(Constants.reproduction) && priority_2.equals(Constants.cyclone) ;
		boolean reproduction_bleaching_cyclone = priority_1.equals(Constants.reproduction) && priority_2.equals(Constants.bleaching) ;
		boolean cyclone_reproduction_bleaching = priority_1.equals(Constants.cyclone) && priority_2.equals(Constants.reproduction) ;
		boolean cyclone_bleaching_reproduction = priority_1.equals(Constants.cyclone) && priority_2.equals(Constants.bleaching) ;
		boolean bleaching_cyclone_reproduction = priority_1.equals(Constants.bleaching) && priority_2.equals(Constants.cyclone) ;
		boolean bleaching_reproduction_cyclone = priority_1.equals(Constants.bleaching) && priority_2.equals(Constants.reproduction) ;

		if(reproduction_cyclone_bleaching){
			CoralReproduction.coralReproduction() ;
			year_event = year_event + 0.1 ;
			Cyclone.cyclone(year_event,cyclone_DMT) ;
			if(bleaching_DHW > 0.0){
				year_event = year_event + 0.1 ;
				Bleaching.bleaching(year_event,bleaching_DHW) ;               // + 0.1 is added in case there 2 disturbances during the same period, so that % cover values are not overlapping 
			}
		}else if(reproduction_bleaching_cyclone){
			CoralReproduction.coralReproduction() ;
			if(bleaching_DHW > 0.0){
				year_event = year_event + 0.1 ;
				Bleaching.bleaching(year_event,bleaching_DHW) ;               // + 0.1 is added in case there 2 disturbances during the same period, so that % cover values are not overlapping 
				year_event = year_event + 0.1 ;
				Cyclone.cyclone(year_event,cyclone_DMT) ;
			}else{
				year_event = year_event + 0.1 ;
				Cyclone.cyclone(year_event,cyclone_DMT) ;
			}
		}else if(cyclone_reproduction_bleaching){
			year_event = year_event + 0.1 ;
			Cyclone.cyclone(year_event,cyclone_DMT) ;
			CoralReproduction.coralReproduction() ;
			if(bleaching_DHW > 0.0){
				year_event = year_event + 0.1 ;
				Bleaching.bleaching(year_event,bleaching_DHW) ;
			}
		}else if(cyclone_bleaching_reproduction){
			year_event = year_event + 0.1 ;
			Cyclone.cyclone(year_event,cyclone_DMT) ;
			if(bleaching_DHW > 0.0){
				year_event = year_event + 0.1 ;
				Bleaching.bleaching(year_event,bleaching_DHW) ;
			}
			CoralReproduction.coralReproduction() ;
		}else if(bleaching_cyclone_reproduction){
			if(bleaching_DHW > 0.0){
				year_event = year_event + 0.1 ;
				Bleaching.bleaching(year_event,bleaching_DHW) ;
				year_event = year_event + 0.1 ;
				Cyclone.cyclone(year_event,cyclone_DMT) ;
			}else{
				year_event = year_event + 0.1 ;
				Cyclone.cyclone(year_event,cyclone_DMT) ;
			}
			CoralReproduction.coralReproduction() ;
		}else if(bleaching_reproduction_cyclone){
			if(bleaching_DHW > 0.0){
				year_event = year_event + 0.1 ;
				Bleaching.bleaching(year_event,bleaching_DHW) ;
				CoralReproduction.coralReproduction() ;
				year_event = year_event + 0.1 ;
				Cyclone.cyclone(year_event,cyclone_DMT) ;
			}else{
				CoralReproduction.coralReproduction() ;
				year_event = year_event + 0.1 ;
				Cyclone.cyclone(year_event,cyclone_DMT) ;
			}
		}
		if(Constants.outprint_data){
			System.out.printf("\n");
			System.out.printf("GROWTH \n") ;
		}
	}
	
//	/**
//	 * Call Cyclone.totalCycloneDamage(), then update the listAllAgents list and then creates a instance "coverMeasure" of PercentageCover that is
//	 * placed in the list percentageCoverList. Finally the size of the colonies is updated for each agent.
//	 */
//	public void cyclone(double yearCyclone){    // TODO place it in class cyclone
//		System.out.printf("CYCLONE : DMT = %.1f \n",cyclone_DMT);
//		Cyclone.totalCycloneDamage(cyclone_DMT,listAllAgents);	  // 	
////		listAllAgents = SMUtils.getAllAgentList() ;    // the list is updated
////		System.out.printf("ContextCoralReef2.cyclone() \n");
////		for(int i = 0 ; i < 10 ; i ++){
////			System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
////		}
//		PercentageCover coverMeasure = SMUtils.calculatePercertageCover(yearCyclone,listAllAgents) ;
//		percentageCoverList.add(coverMeasure) ;
//		updateAgentColonySize() ;
//	}
	
	/* #####################################
	 * ######## Schedule period 4 ##########
	 * #####################################
	 */
	
	/************************************************************************************** 
	 * THERE IS A METHOD CALLED grow() IN CLASS AGENT with priority = 1 *
	 **************************************************************************************/
	
	/** TO CHECK
	 * This method checks if each agent has 3 or 4 (parameter) immediate neighbors that are not from the same colony
	 * In this case --> conversion to barren ground
	 */
	@ScheduledMethod(start = 4, interval = 4, priority = 0.5)
	public void smoothingCoralColonies(){
		if(Constants.outprint_data){
			System.out.printf("\n");
			System.out.printf("SMOOTHERING CORAL COLONIES \n") ;
		}
		for(int i = 0 ; i < 1 ; i++){
//			ArrayList<Agent> listAllColonyAgents = SMUtils.getAllColonyAgentListFromList(listAllAgents);			
			for(Agent agent : listAllAgents){
				if(agent.IDNumber != 0) {   // agent is on a colony
					if(agent.amIAloneFromListCondition(3,"sameColony") && agent.getAge() >= 0.5){ // the minimum number of different VonNeumannNeighbors to be considered "alone" = 3
						agent.conversionToBarrenGround();
					}
				}
			}
		}
//		listAllAgents = SMUtils.getAllAgentList() ; 		
	}
	
//	updateAgentColonySize
	
	/**
	 * Here sand is added or removed depending if the amount of sand in the coming period is bigger or smaller than the actual amount required
	 * The end is traded against barren ground agents.
	 */
	@ScheduledMethod(start = 4, interval = 4, priority = 0.3)
	public void sandImport(){
		if(Constants.outprint_data){
			System.out.printf("\n");
			System.out.printf("SAND INPUT : ") ;
		}
//		for(Sand_cover sc : Constants.sandCoverDataList){
//			 System.out.printf("********** %.1f %.1f \n", sc.getYear(),sc.getSand_Cover()) ;
//		}	
		// get the % sand cover for the next period --> use timer and not year
		for(Sand_cover sd : Constants.sandCoverDataList){
//			System.out.printf("timer %.1f year %.1f sand cover %.1f : \n",CoralReef2Builder.timer,sd.getYear(),sd.getSand_Cover());
			boolean sameYear = sd.getYear() == CoralReef2Builder.timer ;  // get the sand cover of the coming period, not for the actual one which is "year
			if(sameYear){
				sand_cover = sd.getSand_Cover() ;
				break ;
			}
		}
		// Create sand patches until sand_cover is reached or there is no more barren ground agents
		List<ArrayList<Agent>> barrenGound_sand_agents = SMUtils.getBarrenGroundAgentFromList(listAllAgents,true) ; // true: the solitary sand agents are converted into barren ground
		ArrayList<Agent> bgAgentsList = barrenGound_sand_agents.get(0) ;        
		ArrayList<Agent> sandAgentsList = barrenGound_sand_agents.get(1) ;
//		double bgCoverSoFar = (double) bgAgentsList.size() / Constants.sizeReef * 100 ;
		double sandCoverSoFar = (double) sandAgentsList.size() / Constants.sizeReef * 100.0 ;
//		System.out.printf("sandCoverSoFar: %.1f, sand_cover: %.1f so ",sandCoverSoFar,sand_cover) ;
		// need to add sand
		if(sandCoverSoFar < sand_cover && !bgAgentsList.isEmpty()){
			if(Constants.outprint_data){
				System.out.printf("need to add sand, ");
			}
			while(sandCoverSoFar < sand_cover && !bgAgentsList.isEmpty()){
				Agent chosenAgent = null ;
				ArrayList<Agent> neighAgents = new ArrayList<Agent>() ;
				if(sandAgentsList.isEmpty()){   // in case there is no sand agents in the reef
//					System.out.printf("sandAgentsList is empty ") ;
					chosenAgent = SMUtils.randomElementOf(bgAgentsList);  
					neighAgents = SMUtils.getRadius3NeighborsCondition(chosenAgent,true,"isBarrenGround") ;  	
				}else{
//					System.out.printf("sandAgentsList is NOT empty ") ;
					chosenAgent = SMUtils.randomElementOf(sandAgentsList);   // so that the new sand agents are placed around chosenAgent	
//					System.out.printf("sandAgentsList: %d  bgAgentsList: %d %d %d ",sandAgentsList.size(),bgAgentsList.size(),chosenAgent.getAgentX(),chosenAgent.getAgentY()) ;
					neighAgents = SMUtils.getRadius3NeighborsCondition(chosenAgent,false,"isBarrenGround") ;  
//					System.out.printf("size neighAgents: %d ", neighAgents.size()) ;
					if(neighAgents.isEmpty()) { // the is no barren ground agents to convert --> take a barren ground agent and look around it for more barren ground agents
						chosenAgent = SMUtils.randomElementOf(bgAgentsList);  
						neighAgents = SMUtils.getRadius3NeighborsCondition(chosenAgent,true,"isBarrenGround") ; 
					}
				}	
				// convert the barren grounds agents into sand
				for(Agent ag : neighAgents) {   // note that there is at least 1 barren ground agent in neighAgents no matter what
					ag.conversionToSand() ;
					sandAgentsList.add(ag) ;
					bgAgentsList.remove(ag) ;
				}
				sandCoverSoFar = (double)sandAgentsList.size() / Constants.sizeReef * 100.0 ;
//				System.out.printf("sandCoverSoFar: %.1f vs. %.1f \n",sandCoverSoFar,sand_cover) ;
			}
		}else if(sandCoverSoFar > sand_cover){ // need to remove sand
			if(Constants.outprint_data){
				System.out.printf("need to remove sand, ");
			}
			while(sandCoverSoFar > sand_cover){
				final Agent chosenAgent = SMUtils.randomElementOf(sandAgentsList);         
				ArrayList<Agent> neighAgents = SMUtils.getRadius3NeighborsCondition(chosenAgent,true,"isSand") ;  // true: include chosenAgent, "isSand": only retrieved sand agents
				for(Agent ag : neighAgents){
					ag.conversionToBarrenGround();
					sandAgentsList.remove(ag);
					bgAgentsList.add(ag);
				}
				sandCoverSoFar = (double)sandAgentsList.size() / Constants.sizeReef * 100.0 ;
			}
		}
		if(Constants.outprint_data){
			System.out.printf("final sandCoverSoFar: %.1f, at t = %.1f : \n",sandCoverSoFar,CoralReef2Builder.timer);
		}
	}
	
	/** CHECK (each if statement is called)
	 * The agents that can be covered by algae are converted into 
	 * algae. 
	 * The method 1st makes 1 lists of agents than can be covered, which are:
	 * - the agents have not been grazed
	 * - the agents are either: barren ground species (not sand), dead coral
	 * The probability for algae to settle on barren ground or dead coral = 1.
	 */
	@ScheduledMethod(start = 4, interval = 4, priority = 0)
	public void AlgaeInvasion(){
		if(Constants.algaePresentList.size() > 0) {							// only if there are algae present in the reef
			// first make a list of all the agents that can be covered by algae
			ArrayList<Agent> listAgentsToBeCoveredByAlgae = new ArrayList<Agent>() ;
			for(Agent agent : listAllAgents){
				boolean algaeCanSettle = (agent.getSpecies().equals(Constants.SPECIES_BARREN_GROUND) || agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL)) && !agent.getHaveIBeenGrazed() ; 
				if(algaeCanSettle){
					listAgentsToBeCoveredByAlgae.add(agent) ;		
//					System.out.printf("Agent where algae can settle: %s, %s grqzed: %b \n ", agent.getSubstrateSubCategory(), agent.getSpecies(),agent.getHaveIBeenGrazed()) ;			
				}	
			}
			// second patches of algae are created
			if(listAgentsToBeCoveredByAlgae.size() > 0){
				while(listAgentsToBeCoveredByAlgae.size() > 0){
					for(String algaeFG : Constants.algaePresentList){  // go from one FG of algae to another					
						if(listAgentsToBeCoveredByAlgae.size() > 0){
							Agent chosenAgent = SMUtils.randomElementOf(listAgentsToBeCoveredByAlgae) ;
							
							ArrayList<Agent> neighboringAgentsList = SMUtils.getAgentsInRadiusBisFromList(chosenAgent, 5, listAgentsToBeCoveredByAlgae) ;
							
							//Make that fastere
							
							for(Agent neighboringAgent : neighboringAgentsList){
								neighboringAgent.conversionToAlgaeType(algaeFG);
								listAgentsToBeCoveredByAlgae.remove(neighboringAgent) ;	
							}
						}
					}
				}
			}	
		}
	}
	
	/**
	 *  Method that calculates and adds a new PercentageCover instance to list.
	 *  The list converted into a csv file if we get to the end of the simulation (i.e., timer = numberYearSimulation)
	 */
	@ScheduledMethod(start = 4, interval = 4, priority = -0.5)
	public void substrateCompositionCSV(){
		
//		System.out.printf("\n");
//		System.out.printf("Print CSV \n");
		year = CoralReef2Builder.timer ;
		year_event = year ;
		
		Constants.eventName = Constants.eventName_endSeason ;
		SMUtils.calculatePercentageCover_and_NumberRecruits(year,listAllAgents,Constants.eventName) ; // also update listAgentToCheckSizeColony
		updateAgentColonySize() ;  // to place after the previous method because the latter updates ContextCoralReef2.listAgentToCheckSizeColony
		
//		List<PercentageCover> listCompositionSubstratum = SMUtils.calculatePercertageCover_and_NumberRecruits(year,listAllAgents,"final_count") ;
//		PercentageCover coverMeasure   = listCompositionSubstratum.get(0) ;
//		PercentageCover numberRecruits = listCompositionSubstratum.get(1) ;
		
//		percentageCoverList.add(coverMeasure) ;
////		System.out.printf("coverMeasure.getCCACover() : %.1f \n",coverMeasure.getCCACover());
//		numberRecruitsList.add(numberRecruits) ;
		
//		System.out.printf("ContextCoralReef2.percertageCoverCSV \n");
//		for(int i = 0 ; i < 10 ; i ++){
//			System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
//		}
		if(Constants.outprint_data){
			System.out.printf("\nOUTPUT DATA: timer: %.1f year: %.1f ticks: %.1f \n", CoralReef2Builder.timer , Constants.numberYearSimulation,RunEnvironment.getInstance().getCurrentSchedule().getTickCount()) ;
		}
		
		// If this is the last time step:
		if(CoralReef2Builder.timer == Constants.numberYearSimulation){
//			for(PercentageCover pc : percentageCoverList){
//				System.out.printf("%.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f \n",
//						pc.getYear(),pc.getSp1Cover(),pc.getSp2Cover(),pc.getSp3Cover(),pc.getSp4Cover(),pc.getSp5Cover(),pc.getSp6Cover(),pc.getSp7Cover(),pc.getSp8Cover(),pc.getSp9Cover(),pc.getSp10Cover(),pc.getSp11Cover(),pc.getSp12Cover(),pc.getMacroalgaeCover(),pc.getTurfCover(),pc.getCCACover()) ;
//			}
//			if(Constants.outprint_data){
//				System.out.printf("******* !!!!!! year == timer YEAH !!!!!\n") ;
//			}
			System.out.printf("******* !!!!!! year == timer YEAH !!!!!\n") ;

			// find rugosity and write csv file, only at the end
			if(Constants.RugosityGrazingDo) {
				// determine the rugosity created by coral colonies and convert it into % reef grazed			
				CoralReef2Builder.rugosityCoverGrazedList.add(SMUtils.rugosityToGrazing(CoralReef2Builder.colonyPlanarAreaDistListCopy)) ; // colonyPlanarAreaDistListCopy is updated above in updateAgentColonySize()
				percentage_reef_grazed = percentage_reef_grazed + CoralReef2Builder.rugosityCoverGrazedList.get(0).getGrazingFish() ;
				if(percentage_reef_grazed > 100) {
					percentage_reef_grazed = 100 ;
				}				
				RugosityCoverGrazed.writeCSVRugosityCoverGrazedList_EraseList(percentage_reef_grazed) ;	
			}
		}
	}
	
//	@ScheduledMethod(start = 4, interval = 4, priority = -1)
	public void endSimulation(){
		
//		RunEnvironment.getInstance().endRun();
		RunEnvironment.getInstance().endAt(Constants.tick_max + 1) ;

//		if(CoralReef2Builder.timer == Constants.numberYearSimulation){
//			RunEnvironment.getInstance().endRun();
//			System.out.printf("End simualtion yo \n");
//		}
	}
	
	public static double getTimer(){
		return CoralReef2Builder.timer;
	}
	
//	@ScheduledMethod(start = 1, interval = 4, priority = -2)
//	public void jfjfjf(){
//		
//		System.out.printf("%s \n", "Parameter at the endxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//
//		System.out.printf("%s %d \n", "numberAgentsGrazed :",numberAgentsGrazed);
//		System.out.printf("%s %d \n", "numberMacroalgaeAgentsInvading : ",numberMacroalgaeAgentsInvading);
//		System.out.printf("%s %d \n", "numberTurfAgentsInvading : ",numberTurfAgentsInvading);
//		System.out.printf("%s %d \n", "numberCCAAgentsInvading : ",numberCCAAgentsInvading);
//		
//	}
}
