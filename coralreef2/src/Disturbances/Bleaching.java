package Disturbances;

import java.util.ArrayList;

import OutputData.PercentageCover;
import repast.simphony.random.RandomHelper;
// import apple.laf.JRSUIConstants.Size;
import coralreef2.ContextCoralReef2;
import coralreef2.agent.Agent;
import coralreef2.common.Constants;
import coralreef2.common.SMUtils;

public class Bleaching {
	
	public static void bleaching(double yearBleaching,double bleaching_DHW){
				
		ArrayList<Agent> listBleachedAndAliveCoral = SMUtils.getAllCoralAgentListFromList(ContextCoralReef2.listAllAgents,true,false) ; // get lit of alive and bleached coral agents
		ArrayList<Agent> listBleached = new ArrayList<Agent>() ;
		ArrayList<Agent> listDead = new ArrayList<Agent>() ;
		double coverInitialBleachOrAliveCoral = listBleachedAndAliveCoral.size();
		
		if(Constants.outprint_data){
			System.out.printf("\n") ;
			System.out.printf("BLEACHING: DHW = %.1f , ",bleaching_DHW) ;
		}
		
		while(listBleachedAndAliveCoral.size() > 0 ){		
			Agent chosenAgent = SMUtils.randomElementOf(listBleachedAndAliveCoral) ;
			ArrayList<Agent> coralAgentSameColony = SMUtils.getAgentsFromSameColonyFromList(chosenAgent, listBleachedAndAliveCoral) ;		
//			double x0 = chosenAgent.getX0_logitBleaching() ;
//			double r = chosenAgent.getR_logitBleaching() ;
			double probaBleaching = SMUtils.logisticBleachingResponse(chosenAgent.getBleaching_probability(),bleaching_DHW); 
			double thresholdBleaching = RandomHelper.nextDoubleFromTo(0, 1);
//			System.out.printf("In Bleaching.java: substrasubCate: %s colonyNumber: %d threshold: %.2f probaBleahching: %.2f BLEACHED? %b \n",chosenAgent.getSubstrateSubCategory(),chosenAgent.getIDNumber() ,thresholdBleaching,probaBleaching,probaBleaching > thresholdBleaching) ;
			if(probaBleaching > thresholdBleaching){    // bleaching happen if true
//				System.out.printf("YES DEATH: ");
				// possibility to die from bleaching
				double morta_proba = SMUtils.logisticBleachingMortality(bleaching_DHW) ;
				double sizeColonie = coralAgentSameColony.size()  ;
				double thresholdDeath = RandomHelper.nextDoubleFromTo(0,1) ;  
				boolean tooSmallToSurvive = sizeColonie <= 9.0 ;					
				for(Agent agent : coralAgentSameColony){	
					boolean alreadyBleached = agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL) ; // if agent is already bleached, its probab pof dying is * by 2
					if(alreadyBleached ){				// if the agent is already in a bleached state, its probability to die is * 2
						morta_proba = (morta_proba + 2 )/3 ;
					}
					if(tooSmallToSurvive){
						morta_proba = 1 ;
					}
					boolean dieFromBleaching = morta_proba > thresholdDeath ;					
					if(dieFromBleaching){
//						System.out.printf("DEATH YES subsubCate: %s colonyNumber: %d thresholdDeath: %.2f morta_proba: %.2f \n",agent.getSubstrateSubCategory(),agent.getIDNumber() ,thresholdDeath,morta_proba) ;
						agent.conversionToDeadCoral();
						listDead.add(agent) ;
					}else {						
						agent.conversionToBleachedCoral();
						listBleached.add(agent) ;
					}
				}
//				System.out.printf("\n") ;     
			}
			for(Agent agent : coralAgentSameColony){
				listBleachedAndAliveCoral.remove(agent) ;
			}
		}
		if(Constants.outprint_data){
			System.out.printf("proportion Dead by bleaching: %.2f, proportion bleached: %.2f \n",(double)listDead.size()/coverInitialBleachOrAliveCoral*100.0,(double)listBleached.size()/coverInitialBleachOrAliveCoral*100.0) ;
		}
		
		SMUtils.calculatePercentageCover_and_NumberRecruits(yearBleaching,ContextCoralReef2.listAllAgents,Constants.bleaching);
//		PercentageCover coverMeasure = SMUtils.calculatePercertageCover_and_NumberRecruits(yearBleaching,ContextCoralReef2.listAllAgents,Constants.bleaching).get(0) ;
//		ContextCoralReef2.percentageCoverList.add(coverMeasure) ;
		Constants.eventName = Constants.eventName_bleaching ;
		ContextCoralReef2.updateAgentColonySize() ;
		
	}
	
//	/**
//	 * Call Bleaching.bleaching(), then update the listAllAgents list and then creates a instance "coverMeasure" of PercentageCover that is
//	 * placed in the list percentageCoverList.
//	 */
//	public void bleaching(double yearBleaching){          // TODO place it in class bleaching 
//		Bleaching.bleaching(bleaching_DHW) ;
////		for(int i = 0 ; i < 10 ; i ++){
////			System.out.printf("%s %d %d %.1f %b \n",listAllAgents.get(i).getSpecies(),listAllAgents.get(i).getAgentX(),listAllAgents.get(i).getAgentY(),listAllAgents.get(i).getPlanarArea(),listAllAgents.get(i).getHaveIBeenGrazed()) ;
////		}
////		listAllAgents = SMUtils.getAllAgentList() ;    // the list is updated
//	}
	
	
	
}