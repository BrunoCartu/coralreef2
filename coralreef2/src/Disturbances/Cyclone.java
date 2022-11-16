package Disturbances;

import java.util.ArrayList;
import java.util.List;

import OutputData.PercentageCover;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.random.RandomHelper;
import coralreef2.ContextCoralReef2;
import coralreef2.CoralReef2Builder;
import coralreef2.ListAgents;
import coralreef2.InputData.CycloneData;
import coralreef2.agent.Agent;
import coralreef2.common.Constants;
import coralreef2.common.SMUtils;

public class Cyclone {

//		protected final double yearCyclone;
//		protected final double percentageCoralDamaged;
//		protected final double percentageMacroalgaeDamaged;
//		protected final double percentageTurfDamaged;
//		protected final double percentageCCADamaged;
//		protected final String intensity;
//		protected final String habitat;
//	
//		public Cyclone (final CycloneData cycloneData){    // used ???
//			this.yearCyclone = cycloneData.getYearCyclone();
//			this.percentageCoralDamaged = cycloneData.getPercentageCoralDamaged();
//			this.percentageMacroalgaeDamaged = cycloneData.getPercentageMacroalgaeDamaged();
//			this.percentageTurfDamaged = cycloneData.getPercentageTurfDamaged();
//			this.percentageCCADamaged = cycloneData.getPercentageCCADamaged();
//			this.intensity = cycloneData.getIntensity();
//			this.habitat = cycloneData.getHabitat();
//		}
		
		/**
		 * Function that dislodges coral colony, depending on their CSF (colony shape factor, which depend on the shape and 
		 * size of the colony) and the DMT (dislodgement mechanical threshold, which characterizes the exposure of the reef) (cf., Madin et al., 2006).
		 * Once the dislodgement is done, if the reduced % cover is still inferior to the contained in the CSV file, the rest 
		 * of the procedure reduced the coral cover by selecting randomly coral agents that are on the edge of a colony and
		 * convert them and their neighbors into barren ground.
		 * 
		 **NOTE:** might be good to add different proba in the 2nd part so the species are not eroded at the same rate. 
		 *
		 */
		public static void cyclone(double yearCyclone, double cyclone_DMT){
			if(Constants.outprint_data){
				System.out.printf("\n") ;
				System.out.printf("CYCLONE : DMT = %.1f \n",cyclone_DMT);
			}

//			System.out.printf("in Cyclone.totalCycloneDamage: CYCLONE !!! \n") ;
			
//			System.out.printf("Cyclone.totalCycloneDamage() before cyclone \n");
//			for(int i = 0 ; i < 10 ; i ++){
//				System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
//			}
			
//			int numberLivingCoralAgentsDamagedByCyclone = 0;
//			int numberMacroalgaeAgentsDamagedByCyclone = 0;
//			int numberTurfAgentsDamagedByCyclone = 0;
//			int numberCCAAgentsDamagedByCyclone = 0;
//			int numberACAAgentsDamagedByCyclone = 0;
//			int numberAMAAgentsDamagedByCyclone = 0;
//			int numberHalimedaAgentsDamagedByCyclone = 0;

			double percentageLivingCoralAgentsDamagedByCyclone= 0.0;
			double percentageMacroalgaeAgentsDamagedByCyclone = 0.0;
			double percentageTurfAgentsDamagedByCyclone = 0.0;
			double percentageCCAAgentsDamagedByCyclone = 0.0;   
			double percentageACAAgentsDamagedByCyclone = 0.0;   
			double percentageAMAAgentsDamagedByCyclone = 0.0;   
			double percentageHalimedaAgentsDamagedByCyclone = 0.0;   

			List<ArrayList<Agent>> listArrayListAgentCategorie = SMUtils.getListArrayListAgentCategories(ContextCoralReef2.listAllAgents) ; // order list: coralAlive, MA, Truf, CCA, AMA, ACA, Halimeda, , barren ground, sand and then colonyAgent
//			for(ArrayList<Agent> lag : listArrayListAgentCategorie) {
//				if(lag.size() > 0) {
//					System.out.printf("******* In cyclone: %s \n", lag.get(0).getSubstrateSubCategory()) ;
//				}
//			}
			int i = 0 ;
			ArrayList<Agent> listAllLivingCoralAgents =  listArrayListAgentCategorie.get(i++) ; // only living corals agents
			ArrayList<Agent> listAllMacroalgaeAgents =  listArrayListAgentCategorie.get(i++) ;
			ArrayList<Agent> listAllTurfAgents = listArrayListAgentCategorie.get(i++) ;
			ArrayList<Agent> listAllCCAAgents =  listArrayListAgentCategorie.get(i++) ;
			ArrayList<Agent> listAllAMAAgents =  listArrayListAgentCategorie.get(i++) ;
			ArrayList<Agent> listAllACAAgents =  listArrayListAgentCategorie.get(i++) ;
			ArrayList<Agent> listAllHalimedaAgents =  listArrayListAgentCategorie.get(i++) ;
			i++ ; // for barren ground agents
			i++ ; // for sand agents
			ArrayList<Agent> listDislodgeAgents = listArrayListAgentCategorie.get(i++) ; // list containing all agents that are or are on a colony (living, bleached or dead coral and algae covering corals)			
			
			int numberLivingCoralAgentsBeforeCyclone = listAllLivingCoralAgents.size();
			int numberMacroalgaeAgentsBeforeCyclone = listAllMacroalgaeAgents.size();
			int numberTurfAgentsBeforeCyclone = listAllTurfAgents.size();
			int numberCCAAgentsBeforeCyclone = listAllCCAAgents.size();
			int numberAMAAgentsBeforeCyclone = listAllAMAAgents.size();
			int numberACAAgentsBeforeCyclone = listAllACAAgents.size();
			int numberHalimedaAgentsBeforeCyclone = listAllHalimedaAgents.size() ;

//			System.out.printf("macroalage: %d turf: %d CCA: %d \n",numberMacroalgaeAgentsBeforeCyclone,numberTurfAgentsBeforeCyclone,numberCCAAgentsBeforeCyclone);

			//****************************************************************************************************************
			//************************* CORAL dislodgement part *****************************************************
			//****************************************************************************************************************
			/* The while loop is going to go over the coral colonies and see if their CSF < DMT --> the colony is dislodged
			 * The process dislodges also algae agents covering a coral colony. In this case, the amount of alage 
			 * removed is recorded and considered for the "Algae Erosion part".
			 * Same for dead or bleached coral agents. 
			 * The process goes until all the colonies have been checked or the % damaged from csv file has been reached.
			 */					
			while(listDislodgeAgents.size() != 0){
				
				final Agent chosenAgent = SMUtils.randomElementOf(listDislodgeAgents);
				// returns a list of agents that share the same IDNumber (chosenAgent is included in the list)
				// note that in this case the agent can be coral (alive, dead or bleached) or algae covering a coral colony as listDislodgeAgents contains both types
				ArrayList<Agent> agentsFromSameColony = SMUtils.getAgentsFromSameColonyFromList(chosenAgent,listDislodgeAgents); // could be coral dead, alive or bleached or algae covering colony
				
				double CSFcolony = chosenAgent.getCSF(agentsFromSameColony.size()) ;  // return the CSF value of the colony, whihc depend on the growth form and planar area
				
//				System.out.printf("New colony: \n");
//				System.out.printf(" size of colony: %s \n",agentsFromSameColony.size());
//				System.out.printf("CSF: %.1f , DMT: %.1f , ID: %d , shape: %s \n", CSFcolony, DMT, chosenAgent.getIDNumber(),chosenAgent.getGrowthForm());			
				
				for (Agent agent : agentsFromSameColony){  // remove the agent of this colony no matter if they are dislodge or not
					listDislodgeAgents.remove(agent);
				}
				ArrayList<Agent> agentToConvertBarrenGround = new ArrayList<Agent>();
//				boolean branchingFragmentsSurvive = false ;        // to be used in case branching fragments survive 
				if (CSFcolony > cyclone_DMT){   // then dislodgement 				
					if(chosenAgent.getGrowthForm().equals(Constants.growthFormBranching)){  // in Highsmith et al., 1980: proportion of surviving fragments from branching species = 4.44*lengthFragment^0.66
																						   // but I used y = 1 - exp(-x*0.018) instead, because it approximate Highsmith's model but does not exceed 1
						double areaColony = agentsFromSameColony.size() ;  // in cm2
						if(areaColony != 0){ // calculate the probability of surviving 
//							double diameterColony = (double)Math.sqrt(areaColony / Math.PI) * 2.0 ;  
							double proportionColonySurviving = cyclone_DMT * 0.004166667 - 0.004166667 ;  // see Appendix 2: Fig.S11A
							double diameterFragmentColony = (double)Math.sqrt(areaColony * proportionColonySurviving / Math.PI) * 2.0 ; // estimation of diameter of fragment colony, assuming fragment colony has a circular planar area
							double probaSurving = 1 - Math.exp(-diameterFragmentColony*0.018) ;                                         // adapted from Highsmith et al., 1980; see Appendix 2: Fig.S11B
							boolean colonyFragmentSurvive = probaSurving > RandomHelper.nextDoubleFromTo(0,1) ;
							
							if(colonyFragmentSurvive){  // the colony  surface area is reduced until proportion remaining = proportionColonySurviving
								int surfaceToRemove = (int)((1.0 - proportionColonySurviving) * areaColony + 0.5) ;
								while(surfaceToRemove > 0) {
//									System.out.printf("*********** SURFACE TO REMOVE: %d ", surfaceToRemove);
									for(Agent ag : agentsFromSameColony) {
										if(SMUtils.IsAgentOnEdgeColony(ag)){
											agentToConvertBarrenGround.add(ag) ;
											surfaceToRemove = surfaceToRemove - 1 ;
											if(surfaceToRemove <= 0) {
												break ;
											}
										}
									}
								}
							}else { // the whole colony will be dislodged
								for(Agent ag : agentsFromSameColony) {
									agentToConvertBarrenGround.add(ag) ;
								}
							}
						}
					}else { // if not branching the dislodged colony is lost
						for (Agent agentDislodged : agentsFromSameColony){
							agentToConvertBarrenGround.add(agentDislodged) ;
						}
					}
					
//					System.out.printf("Cylone: dislodged !!!!!!!!!!!!!! CSF: %.1f DMT: %.1f \n",CSFcolony,cyclone_DMT);	
					
					for(Agent agentDislodged : agentToConvertBarrenGround) {
						// this part consider the eventuality that the agent is an algae covering a coral
						if (agentDislodged.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){  
//							numberLivingCoralAgentsDamagedByCyclone++;				// only count when a living coral is dislodge
							listAllLivingCoralAgents.remove(agentDislodged);		// ??? needed ???					
						}else if (agentDislodged.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)){
//							numberMacroalgaeAgentsDamagedByCyclone++;
							listAllMacroalgaeAgents.remove(agentDislodged);			// ??? needed ???
						}else if (agentDislodged.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)){
//							numberTurfAgentsDamagedByCyclone++; 
							listAllTurfAgents.remove(agentDislodged);				// ??? needed ???
						}else if (agentDislodged.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
//							numberCCAAgentsDamagedByCyclone++;
							listAllCCAAgents.remove(agentDislodged);				// ??? needed ???
						}else if (agentDislodged.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_AMA)){
							listAllAMAAgents.remove(agentDislodged);				// ??? needed ???
						}else if (agentDislodged.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_ACA)){
							listAllACAAgents.remove(agentDislodged);				// ??? needed ???
						}else if (agentDislodged.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA)){
							listAllHalimedaAgents.remove(agentDislodged);				// ??? needed ???
						}
						agentDislodged.conversionToBarrenGround();
					}
				}
			}
			percentageLivingCoralAgentsDamagedByCyclone = (double)(numberLivingCoralAgentsBeforeCyclone-listAllLivingCoralAgents.size()) / (double)numberLivingCoralAgentsBeforeCyclone*100;
			percentageMacroalgaeAgentsDamagedByCyclone = (double)(numberMacroalgaeAgentsBeforeCyclone-listAllMacroalgaeAgents.size()) / (double)numberMacroalgaeAgentsBeforeCyclone*100;
			percentageTurfAgentsDamagedByCyclone = (double)(numberTurfAgentsBeforeCyclone-listAllTurfAgents.size()) / (double)numberTurfAgentsBeforeCyclone*100;
			percentageCCAAgentsDamagedByCyclone = (double)(numberCCAAgentsBeforeCyclone-listAllCCAAgents.size()) / (double)numberCCAAgentsBeforeCyclone*100;
			percentageAMAAgentsDamagedByCyclone = (double)(numberAMAAgentsBeforeCyclone-listAllAMAAgents.size()) / (double)numberAMAAgentsBeforeCyclone*100;
			percentageACAAgentsDamagedByCyclone = (double)(numberACAAgentsBeforeCyclone-listAllACAAgents.size()) / (double)numberACAAgentsBeforeCyclone*100;
			percentageHalimedaAgentsDamagedByCyclone = (double)(numberHalimedaAgentsBeforeCyclone-listAllHalimedaAgents.size()) / (double)numberHalimedaAgentsBeforeCyclone*100;

//			System.out.printf("percentageCoralAgentsDamagedByCyclone: %.1f \n",percentageLivingCoralAgentsDamagedByCyclone) ;	
//			System.out.printf("\n") ;
			
//			check Highsmith et al., 1980 to estimate the proportion of branching colonies that remain in the reef (fragmentation) + Madin et al., 2014
//			propo pf surviving fragment = 4.44 * fragment length ^ 0.66
//			
//			System.out.printf("Cyclone.totalCycloneDamage() AFTER cyclone \n");
//			for(int i = 0 ; i < 10 ; i ++){
//				System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
//			}
			
			//****************************************************************************************************************
			//******************************** CORAL Erosion part*************************************************************
			//****************************************************************************************************************
			// the % of coral is reduced by selecting the coral agents that are on the edge of their colony
			// the coral agents can be alive, dead or bleached, or covered by algae
			// but the while loop keeps going until the % of LIVING coral 
			// supposed to be damaged (from CSV file) is reached.
			
//			System.out.printf("After dislodgement, before erosion part:\n");
//			System.out.printf("perc living coral doslodged  : %.1f \n",percentageLivingCoralAgentsDamagedByCyclone);
//			System.out.printf("perc Macro doslodged  : %.1f \n",percentageMacroalgaeAgentsDamagedByCyclone);
//			System.out.printf("perc Turf  doslodged  : %.1f \n",percentageTurfAgentsDamagedByCyclone);
//			System.out.printf("perc CCA   doslodged  : %.1f \n",percentageCCAAgentsDamagedByCyclone);
//			System.out.printf("\n");

//			final double percentageLivingCoralAgentsDislodged = percentageLivingCoralAgentsDamagedByCyclone;
			
//			System.out.printf("percentageLivingCoralAgentsDislodged: %.1f,CSV: %.1f  \n", 
//					percentageLivingCoralAgentsDamagedByCyclone,
//					percentageLivingCoralDamagedByCycloneFromCSV );	

			// list contains living, dead or bleached corals and also algae covering a coral.
//			listAllAgents = SMUtils.getAllAgentList();
//			ArrayList<Agent> listAllColonyAgents = SMUtils.getAllColonyAgentListFromList(listAllAgents);			

			//***************************************** REMOVED FOR DEMO *****************************************
			// this part is executed only if there are still colonies (alive, dead or cover by algae in the reef) agents in the reef and if the % of coral damaged from CSV 
			// is not reach yet
//			if (percentageLivingCoralAgentsDamagedByCyclone < percentageLivingCoralDamagedByCycloneFromCSV &&
//					listAllColonyAgents.size() != 0){
//				
//				// this list contains alive, dead or bleached coral agents on the edge of a colony and also algae agents
//				ArrayList<Agent> listAgentOnEdgeColony = SMUtils.getEdgeColonyCoralAgentListFromList(listAllAgents,true);
//
//				// continue to reduce the coral cover in order to reach the % damage found in csv file 
//				while (percentageLivingCoralAgentsDamagedByCyclone < percentageLivingCoralDamagedByCycloneFromCSV &&
//						listAllColonyAgents.size() != 0){
			//********************************************************************************************
			
//			ArrayList<Agent> erodedColonyAgent = new ArrayList<Agent>() ;		
//			for(Agent ag : listAllColonyAgents){				
//				if(ag.amIAloneFromList(2,listAllAgents)){				
//					/* The probability of to be eroded if it is alive dependents on its skeletal density:                                   // see Higsmith 1981
//					 * Max skeletal density: 													2.85 g.cm-3     --> p = 0.05  (= 1 - 2.85/3)   // TODO: lok for better probaba is possible
//					 * Min skeletal density: 													0.15 g.cm-3     --> p = 0.95  (= 1 - 0.15/3)
//					 * (value taken from https://coraltraits.org/)
//					 * If coral agent is dead, p is x 4 and bleached, p x 2. 
//					 * In the case the coral is cover by macoralgae or Turf,the coral is considered dead, p is x 4.
//					 * In the case the coral is covered by CCA, p is / 2 as CCA consolidate reef.
//					 * In case the living coral agent is not eroded, it is not removed from the list listCoralAgentOnEdgeColony
//					 * and can consequently be picked up again.
//					 */							
//					double x = RandomHelper.nextDoubleFromTo(0, 1);
//					double skeletalDensity = ag.getSkeletalDensity();				
//					if (ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)){										
//						if (skeletalDensity / 3 < x){
//							numberLivingCoralAgentsDamagedByCyclone++; 
//							ag.conversionToBarrenGround(); 
//							erodedColonyAgent.add(ag);
//						}
//					}	
//					else if (ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL)){
//						if (skeletalDensity / (3 * 4) < x){
//							ag.conversionToBarrenGround(); 
//							erodedColonyAgent.add(ag);
//						}
//					}
//					else if (ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL)){
//						if (skeletalDensity / (3 * 2) < x){
//							ag.conversionToBarrenGround(); 
//							erodedColonyAgent.add(ag);
//							}
//					}
//					else if (ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)){
//						if (skeletalDensity / (3 * 4) < x){
//							numberMacroalgaeAgentsDamagedByCyclone++;
//							ag.conversionToBarrenGround(); 
//							erodedColonyAgent.add(ag);
//							listAllMacroalgaeAgents.remove(ag);
//						}
//					}
//					else if (ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)){
//						if (skeletalDensity / (3 * 4) < x){
//							numberTurfAgentsDamagedByCyclone++;
//							erodedColonyAgent.add(ag);
//							ag.conversionToBarrenGround(); 
//							listAllTurfAgents.remove(ag);
//						}
//					}
//					else if (ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
//						if (skeletalDensity / 3 * 2 < x){
//							numberCCAAgentsDamagedByCyclone++;
//							erodedColonyAgent.add(ag);
//							ag.conversionToBarrenGround(); 
//							listAllCCAAgents.remove(ag);
//						}
//					}
//				}		
//				percentageLivingCoralAgentsDamagedByCyclone = (double)numberLivingCoralAgentsDamagedByCyclone / (double)numberLivingCoralAgentsBeforeCyclone*100;
//				if(percentageLivingCoralAgentsDamagedByCyclone > percentageLivingCoralDamagedByCycloneFromCSV){
//					break ;
//				}
//		}
	
					
//			for(Agent agent : erodedColonyAgent){
//				listAllColonyAgents.remove(agent) ;
//			}

//			percentageLivingCoralAgentsDamagedByCyclone = (double)numberLivingCoralAgentsDamagedByCyclone / (double)numberLivingCoralAgentsBeforeCyclone*100 ;
			
//			System.out.printf("percentageLivingCoralAgentsDislodged: %.1f,CSV: %.1f, percentageCycloneErosionCoral : %.5f  \n", 
//					percentageLivingCoralAgentsDislodged,
//					percentageLivingCoralDamagedByCycloneFromCSV,
//					percentageLivingCoralAgentsDamagedByCyclone );	
		
//	}			
			
//					System.out.printf("numberLivingCoralAgentsDamagedByCyclone: %d \n", numberLivingCoralAgentsDamagedByCyclone);	
									
//			percentageMacroalgaeAgentsDamagedByCyclone = (double)numberMacroalgaeAgentsDamagedByCyclone / (double)numberMacroalgaeAgentsBeforeCyclone*100;
//			percentageTurfAgentsDamagedByCyclone = (double)numberTurfAgentsDamagedByCyclone / (double)numberTurfAgentsBeforeCyclone*100;
//			percentageCCAAgentsDamagedByCyclone = (double)numberCCAAgentsDamagedByCyclone / (double)numberCCAAgentsBeforeCyclone*100;
			
//			System.out.printf("After coral ersion part: \n");
//			System.out.printf("perc coral damaged CSV: %.1f, perc coral doslodged  : %.1f \n",percentageLivingCoralDamagedByCycloneFromCSV,percentageLivingCoralAgentsDamagedByCyclone);
//			System.out.printf("perc Macro damaged CSV: %.1f, perc Macro doslodged  : %.1f \n",percentageMacroalgaeDamagedByCycloneFromCSV,percentageMacroalgaeAgentsDamagedByCyclone);
//			System.out.printf("perc Truf  damaged CSV: %.1f, perc Turf  doslodged  : %.1f \n",percentageTurfDamagedByCycloneFromCSV,percentageTurfAgentsDamagedByCyclone);
//			System.out.printf("perc CCA   damaged CSV: %.1f, perc CCA   doslodged  : %.1f \n",percentageCCADamagedByCycloneFromCSV,percentageCCAAgentsDamagedByCyclone);
//			System.out.printf("\n");		
				
//			****************************************************************************************************************
			//******************************** Algae Erosion part*************************************************************
			//****************************************************************************************************************
			
			// I have to recalculate the size of the following lists in case all the algae agents have being removed 
			// during the dislodgement part. --> nope as it is updated 
			
			//****************************************************************************************************************
			//******************************** Macroalgae Erosion part *******************************************************
			//****************************************************************************************************************	
			double percentageMacroalgaeToRemove = 0.0 ;  // if DMT > 130, no MA is removed
			if(cyclone_DMT <= 10){
				percentageMacroalgaeToRemove = 100.0 ;
			}else if(cyclone_DMT > 10 && cyclone_DMT < 130){
				percentageMacroalgaeToRemove = -0.83 * cyclone_DMT + 108.33 ;
			}
			while(listAllMacroalgaeAgents.size() != 0 && percentageMacroalgaeAgentsDamagedByCyclone < percentageMacroalgaeToRemove){					
				final Agent chosenAgent = SMUtils.randomElementOf(listAllMacroalgaeAgents);
				final ArrayList<Agent> neighborhood = SMUtils.getRadius3Neighbors(chosenAgent,true);  // chosenAgent is included in the list neighborhood
				for (Agent ag : neighborhood){
					if(ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)){
						listAllMacroalgaeAgents.remove(ag);
						ag.beRemoved();      // equivalent to beGrazed() so that if the agent is on a coral colony it becomes a dead coral, otherwise barren ground, but does not affect "haveIBeenGrazed"  
//						numberMacroalgaeAgentsDamagedByCyclone++ ; 
					}                
				}
				percentageMacroalgaeAgentsDamagedByCyclone = (double)(numberMacroalgaeAgentsBeforeCyclone-listAllMacroalgaeAgents.size()) / (double)numberMacroalgaeAgentsBeforeCyclone*100 ;
			}		
//			System.out.printf("After Macroalgae ersion part: \n") ;
//			System.out.printf("perc Macro damaged CSV: %.1f \n",percentageMacroalgaeAgentsDamagedByCyclone);
//			System.out.printf("\n");
			
			//****************************************************************************************************************
			//******************************** AMA Erosion part *******************************************************
			//****************************************************************************************************************	
			double percentageAMAToRemove = percentageMacroalgaeToRemove ;    // same as for Macroalgae

			while(listAllAMAAgents.size() != 0 && percentageAMAAgentsDamagedByCyclone < percentageAMAToRemove){					
				final Agent chosenAgent = SMUtils.randomElementOf(listAllAMAAgents);
				final ArrayList<Agent> neighborhood = SMUtils.getRadius3Neighbors(chosenAgent,true);  // chosenAgent is included in the list neighborhood
				for (Agent ag : neighborhood){
					if(ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_AMA)){
						listAllAMAAgents.remove(ag);
						ag.beRemoved();      // equivalent to beGrazed() so that if the agent is on a coral colony it becomes a dead coral, otherwise barren ground, but does not affect "haveIBeenGrazed"          
//						numberAMAAgentsDamagedByCyclone++ ; 
					}                
				}
				percentageAMAAgentsDamagedByCyclone = (double)(numberAMAAgentsBeforeCyclone-listAllAMAAgents.size()) / (double)numberAMAAgentsBeforeCyclone*100;
			}	
			//****************************************************************************************************************
			//******************************** ACA Erosion part *******************************************************
			//****************************************************************************************************************	
			double percentageACAToRemove = percentageMacroalgaeToRemove ;    // same as for Macroalgae

			while(listAllAMAAgents.size() != 0 && percentageACAAgentsDamagedByCyclone < percentageACAToRemove){					
				final Agent chosenAgent = SMUtils.randomElementOf(listAllACAAgents);
				final ArrayList<Agent> neighborhood = SMUtils.getRadius3Neighbors(chosenAgent,true);  // chosenAgent is included in the list neighborhood
				for (Agent ag : neighborhood){
					if(ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_ACA)){
						listAllACAAgents.remove(ag);
						ag.beRemoved();      // equivalent to beGrazed() so that if the agent is on a coral colony it becomes a dead coral, otherwise barren ground, but does not affect "haveIBeenGrazed"         
//						numberACAAgentsDamagedByCyclone++ ; 
					}                
				}
				percentageACAAgentsDamagedByCyclone = (double)(numberACAAgentsBeforeCyclone-listAllACAAgents.size()) / (double)numberACAAgentsBeforeCyclone*100;
			}	
			//****************************************************************************************************************
			//******************************** Halimeda Erosion part *******************************************************
			//****************************************************************************************************************	
			double percentageHalimedaToRemove = 0.0 ;  // if DMT > 130, no MA is removed
			if(cyclone_DMT <= 10){
				percentageHalimedaToRemove = 100.0 ;
			}else if(cyclone_DMT > 10 && cyclone_DMT < 130){
				percentageHalimedaToRemove = -0.83 * cyclone_DMT + 108.33 ;
			}
			while(listAllHalimedaAgents.size() != 0 && percentageHalimedaAgentsDamagedByCyclone < percentageHalimedaToRemove){					
				final Agent chosenAgent = SMUtils.randomElementOf(listAllHalimedaAgents);
				final ArrayList<Agent> neighborhood = SMUtils.getRadius3Neighbors(chosenAgent,true);  // chosenAgent is included in the list neighborhood
				for (Agent ag : neighborhood){
					if(ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA)){
						listAllHalimedaAgents.remove(ag);
						ag.beRemoved();      // equivalent to beGrazed() so that if the agent is on a coral colony it becomes a dead coral, otherwise barren ground, but does not affect "haveIBeenGrazed"         
//						numberHalimedaAgentsDamagedByCyclone++ ; 
					}                
				}
				percentageHalimedaAgentsDamagedByCyclone = (double)(numberHalimedaAgentsBeforeCyclone-listAllHalimedaAgents.size()) / (double)numberHalimedaAgentsBeforeCyclone*100;
			}	
			//****************************************************************************************************************
			//******************************** Turf Erosion part *************************************************************
			//****************************************************************************************************************			
			double percentageTurfToRemove = 0.0 ;  // if DMT > 110, no Turf is removed
			if(cyclone_DMT <= 10){
				percentageTurfToRemove = 90.0 ;
			}else if(cyclone_DMT > 10 && cyclone_DMT < 110){
				percentageTurfToRemove = -0.90 * cyclone_DMT + 99.00 ;
			}			
			while(listAllTurfAgents.size() != 0 && percentageTurfAgentsDamagedByCyclone < percentageTurfToRemove){			
				final Agent chosenAgent = SMUtils.randomElementOf(listAllTurfAgents);
				final ArrayList<Agent> neighborhood = SMUtils.getRadius3Neighbors(chosenAgent,true);  // chosenAgent is included in the list neighborhood
				for (Agent ag : neighborhood){
					if(ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)){
						listAllTurfAgents.remove(ag);
						ag.beRemoved();         // equivalent to beGrazed() so that if the agent is on a coral colony it becomes a dead coral, otherwise barren ground, but does not affect "haveIBeenGrazed"            
//						numberTurfAgentsDamagedByCyclone++; 
					}
				}
				percentageTurfAgentsDamagedByCyclone = (double)(numberTurfAgentsBeforeCyclone-listAllTurfAgents.size()) / (double)numberTurfAgentsBeforeCyclone*100;
			}	
//			System.out.printf("After Turf ersion part: \n");
//			System.out.printf("perc Truf  damaged CSV: %.1f \n",percentageTurfAgentsDamagedByCyclone);
//			System.out.printf("\n");
			
			//****************************************************************************************************************
			//******************************** CCA Erosion part *************************************************************
			//****************************************************************************************************************
			double percentageCCAToRemove = 0.0 ;  // if DMT > 110, no Turf is removed
			if(cyclone_DMT <= 10){
				percentageCCAToRemove = 80.0 ;
			}else if(cyclone_DMT > 10 && cyclone_DMT < 100){
				percentageCCAToRemove = -1.0 * cyclone_DMT + 90.00 ;
			}		
			while(listAllCCAAgents.size() != 0 && percentageCCAAgentsDamagedByCyclone < percentageCCAToRemove){				
				final Agent chosenAgent = SMUtils.randomElementOf(listAllCCAAgents);
				final ArrayList<Agent> neighborhood = SMUtils.getRadius3Neighbors(chosenAgent,true);  // chosenAgent is included in the list neighborhood
				for (Agent ag : neighborhood){
					if(ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
						listAllCCAAgents.remove(ag);
						ag.beRemoved();     // equivalent to beGrazed() so that if the agent is on a coral colony it becomes a dead coral, otherwise barren ground, but does not affect "haveIBeenGrazed"                
//						numberCCAAgentsDamagedByCyclone++;
					}
				}
				percentageCCAAgentsDamagedByCyclone = (double)(numberCCAAgentsBeforeCyclone-listAllCCAAgents.size())/ (double)numberCCAAgentsBeforeCyclone*100;
			}
//			System.out.printf("After CCA ersion part: \n");
//			System.out.printf("perc CCA   damaged CSV: %.1f \n",percentageCCAAgentsDamagedByCyclone);
//			System.out.printf("\n");	
			
//			System.out.printf("perc living coral doslodged  : %.1f \n",percentageLivingCoralAgentsDamagedByCyclone);
//			System.out.printf("perc Macro removed  : %.1f \n",percentageMacroalgaeAgentsDamagedByCyclone);
//			System.out.printf("perc Turf removed  : %.1f \n",percentageTurfAgentsDamagedByCyclone);
//			System.out.printf("perc CCA doslodged  : %.1f \n",percentageCCAAgentsDamagedByCyclone);
//			System.out.printf("perc AMA removed  : %.1f \n",percentageAMAAgentsDamagedByCyclone);
//			System.out.printf("perc ACA removed  : %.1f \n",percentageACAAgentsDamagedByCyclone);
//			System.out.printf("perc Halimeda removed  : %.1f \n",percentageHalimedaAgentsDamagedByCyclone);
//
//			System.out.printf("\n");
			SMUtils.calculatePercentageCover_and_NumberRecruits(yearCyclone,ContextCoralReef2.listAllAgents,Constants.cyclone);
//			PercentageCover coverMeasure = SMUtils.calculatePercertageCover_and_NumberRecruits(yearCyclone,ContextCoralReef2.listAllAgents,Constants.cyclone).get(0) ;
//			ContextCoralReef2.percentageCoverList.add(coverMeasure) ;
			Constants.eventName = Constants.eventName_cyclone ;
			ContextCoralReef2.updateAgentColonySize() ;
			
	}
}
