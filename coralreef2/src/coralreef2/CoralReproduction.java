package coralreef2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import coralreef2.InputData.Disturbance_larvalConnectivity;
import coralreef2.InputData.FunctionalTraitData;
import coralreef2.agent.Agent;
import coralreef2.common.Constants;
import coralreef2.common.SMUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;

public class CoralReproduction {
		
	
	/** TO CHECK
	 * This method call the CoralReproduction.coralLarvaeTotalProduction() in order to obtain the list of coral larvae that are going to try to 
	 * settle in the reef.
	 * Then it calls for CoralReproduction.coralSettlement() to let these larvae try to settle: until the list is not empty, between 5 and 20 larvae are going to try settling in the reef by randomly landing on agents. This is done
	 * for all the coral species, one after another, in the order of appearance of the name in FunctionalDiversity.cvs. 
	 * Only between 5 and 20 so that there is not priority effect.
	 * If the remaining larvae in a given species is < 20, then all the remaining larvae try to settle (so that all the larvae in the list for 
	 * a given species try to settle).
	 * Probability of settling:
	 * - on CCA:                       p = 0.2
	 * - on barren ground, dead coral: p = 0.1
	 * - on other substrata:           p = 0
	 */
//	@ScheduledMethod(start = 3, interval = 4, priority = 0)
	public static void coralReproduction(){
		if(Constants.outprint_data){
			System.out.printf("\nREPRODUCTION ") ;
		}		
		if(Constants.coralSpeciesPresentList.size() > 0) {
			/*
			 *  Reproduction is called if yearDivision equal:
			 *  - 1: once for both brooders and spawners     
			 *  - 2: 1st: none (0.00), 2nd both (0.50), and brooder reproduce at the end again 
			 *  - 3: 1st: none (0.00), 2nd both (0.33), 3rd: brooder (0.67)
			 *  - 4: 1st: none (0.00), 2nd:none (0.25), 3rd: both (0.50), 4th brooders (0.75)
			 */

			String reproduction_brooder_spawner = "none" ;          // gets value "brooder", "both" or "none" if only brooder, both brooder  and spawner or none of them can reproduce
			double decimalYear = Math.round((ContextCoralReef2.year - (int)ContextCoralReef2.year) * 100.0) / 100.0 ;   // get the decimal value of the year	
			
			if(Constants.outprint_data){
				System.out.printf("decimalYear: %.1f season: %s ", decimalYear,ContextCoralReef2.season) ; 
			}
			
			boolean brooderReproduce = Constants.yearDivision == 1 |  Constants.yearDivision == 2 |
									  (Constants.yearDivision == 3 & (decimalYear == 0.33 |
									  								 (ContextCoralReef2.season.equals("wet_season") & (decimalYear == 0.00 | decimalYear == 0.67)))) |
									  (Constants.yearDivision == 4 & ((ContextCoralReef2.season.equals("wet_season") | ContextCoralReef2.season.equals("dry_season")) & (decimalYear == 0.25 | decimalYear == 0.50))) ;
																	
			boolean spawnerReproduce = Constants.yearDivision == 1 |  
									  (Constants.yearDivision == 2 &	ContextCoralReef2.season.equals("wet_season")) | 
									  (Constants.yearDivision == 3 &	(ContextCoralReef2.season.equals("wet_season") & (decimalYear == 0.00 | decimalYear == 0.67))) |
									  (Constants.yearDivision == 4 & (ContextCoralReef2.season.equals("wet_season") & (decimalYear == 0.50 | decimalYear == 0.25))) ;   		
			
			if(brooderReproduce & spawnerReproduce) {
				reproduction_brooder_spawner = Constants.both ;
			}else if(brooderReproduce) {
				reproduction_brooder_spawner = Constants.brooder ;
			}
			
			if(Constants.outprint_data){
				System.out.printf("year: %.2f %s \n",ContextCoralReef2.year,reproduction_brooder_spawner) ;                           ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}

			if(!reproduction_brooder_spawner.equals("none")){		
				/*
				 * Return a list of the numbers of larvae that are going to try settling on the reef, by coral species
				 * Depend on local and regional input
				 */
				int[] numberLarvaeProducedArrayTotal = coralLarvaeTotalProduction(reproduction_brooder_spawner);  // list of numbers of larvae produced in total (locally and form the regional pool)
				
				if(Constants.outprint_data){
					System.out.printf("Total production: ");
					int i = 0 ;
					for(String spName : Constants.coralSpeciesPresentList) {
						System.out.printf("%s: %d ; ",spName,numberLarvaeProducedArrayTotal[i]);     
						i++ ;
					}
					System.out.printf("\n");
				}
				
				/* Settlement part:
				 * Return a list of number of larvae successfully settling in the reef
				 */
				int[] numberLarvaeSuccessfullySettling = coralSettlement(numberLarvaeProducedArrayTotal); // list of numbers of larvae successfully settling by coral species 
//				System.out.printf("number larvae settling: \n");
//				int z = 0;
//				for(String nameSpecies : Constants.coralSpeciesPresentList){
//					System.out.printf("%s: %d ; ", nameSpecies,numberLarvaeSuccessfullySettling[z]);
//					z++;
//				}
//				System.out.printf("\n");
			}
//			listAllAgents = SMUtils.getAllAgentList();	
		}
	}
	
	
	/** CHECKED
	 * Method that returns a array of integers that contains for each coral species the total number of larvae produced.
	 * It 1st calculates the quantity of oocytes produced locally for each species.
	 * Then ...
	 * 
	 */
	public static int[] coralLarvaeTotalProduction(String reproduction_brooder_spawner){
		// local production
		int[] numberLarvaeProducedArrayTotal = coralLarvaeLocalProduction(reproduction_brooder_spawner) ;
		
//		System.out.printf("number larvae locally produced: \n");
//		int z = 0;
//		for(String nameSpecies : Constants.coralSpeciesPresentList){
//			System.out.printf("%s: %d ; ", nameSpecies,numberLarvaeProducedArrayTotal[z]);
//			z++;
//		}
//		System.out.printf("\n");
		
		// add regional input
		numberLarvaeProducedArrayTotal = coralLarvaeRegionalPoolProductionAdded(reproduction_brooder_spawner,numberLarvaeProducedArrayTotal) ;
//		System.out.printf("number larvae produced in total: \n");
//		z = 0;
//		for(String nameSpecies : Constants.coralSpeciesPresentList){
//			System.out.printf("%s: %d ; ", nameSpecies,numberLarvaeProducedArrayTotal[z]);
//			z++;
//		}
//		System.out.printf("\n");
		
		return numberLarvaeProducedArrayTotal ;
		
	}	
	
	/** 
	 * Returns a list of integer corresponding to the total number of larvae produced by each coral species locally
	 * @param reproduction_brooder_spawner
	 * @return
	 */
	public static int[] coralLarvaeLocalProduction(String reproduction_brooder_spawner){
//		System.out.printf("in CoralReproduction.coralLarvaeTotalProduction(): Reproduction !!! \n") ;	
		
		/* CHECKED
		 * This part corresponds to the local production of oocytes for each coral species.
		 * The real surface of coral colonies is estimated,as well as fertilization rate and larval predation on the reef and the proportion 
		 * of larvae becoming competent (from Connolly and Barid 2010 and Figueiredo et al., 2013)
		 * Finally, the portion of larvae staying on the reef is determined by estimating the proportion of competent larvae staying 
		 * on the reef.
		 * The proportion of competent larvae is given by 	   pr = 0.18 + 0.545e(-1.354.tm)           (Figueiredo et al., 2013)
		 * with tm being the time to mobility:
		 * 										tm = 0 for brooder as larvae are ready to settle as soon as realized
		 * 										tm = 0.059 × ed  + 0,067  for spawners, with ed being egg diameter in micro meter (Figueiredo et al., 2013)
		 */
		if(Constants.outprint_data){
			System.out.printf("Local production: ") ;
		}
		//final Parameters paraFD = RunEnvironment.getInstance().getParameters();
		//List<FunctionalTraitData> FTDList = SMUtils.readFunctionalTraitDataFile(Constants.FUNCTIONAL_TRAIT_DATAFILE);
//		int[] numberLarvaeProducedArrayTotal = null  ;
		int[] numberLarvaeProducedArrayTotal = new int[Constants.coralSpeciesPresentList.size()];		
		ArrayList<Agent> listAllLivingCoral = SMUtils.getLivingCoralAgentListFromList(ContextCoralReef2.listAllAgents);  // list of all the living coral agents, whose content is going to decrease as we move from one species to another 		
//		System.out.printf("CoralReproduction.coralLarvaeTotalProduction: \n");
//		for(int i = 0 ; i < 10 ; i ++){
//			System.out.printf("%s %d %d %.1f %b \n",ContextCoralReef2.listAllAgents.get(i).getSpecies(),ContextCoralReef2.listAllAgents.get(i).getAgentX(),ContextCoralReef2.listAllAgents.get(i).getAgentY(),ContextCoralReef2.listAllAgents.get(i).getArea(),ContextCoralReef2.listAllAgents.get(i).getHaveIBeenGrazed()) ;
//		}		
		int i = 0;	
//		System.out.printf("Local production coral \n");
		for(String nameSpecies : Constants.coralSpeciesPresentList){		
						
			String reproductiveMode = SMUtils.getStringTraitFromFTTable(nameSpecies,Constants.mode_larval_development) ;
			boolean bothReproduce = reproduction_brooder_spawner.equals(Constants.both) ;
			boolean brooderReproduce = reproduction_brooder_spawner.equals(Constants.brooder) && reproductiveMode.equals(Constants.brooder)  ;
			boolean reproductionHappening = bothReproduce || brooderReproduce ;  // reproduction happens			
//			System.out.printf("%s, %s, %b  \n",nameSpecies,reproductiveMode,booley);                                   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
			if(!reproductionHappening){
				numberLarvaeProducedArrayTotal[i] = 0 ;
			}else{	
				double total3DSurfaceAreaSp = 0.0 ; // 
				// Make a list of living coral agents from species 'nameSpecies' (the 2 false parameters --> bleached and dead coral are not included).
				List<ArrayList<Agent>> lists_CoralAgentSameSpecies_RemainingList = SMUtils.getListCoralAgentFromSameSpeciesFromListRemove(nameSpecies, listAllLivingCoral, false, false); // only return alive coral agent (not bleached or dead --> false and false)
				ArrayList<Agent> listCoralAgentSameSpeciesThatCanReproduce = lists_CoralAgentSameSpecies_RemainingList.get(0);  // list that is going to contain all the agents of a same coral species that are mature enough to reproduce. The ones not mature have to be removed
				listAllLivingCoral = lists_CoralAgentSameSpecies_RemainingList.get(1) ;		// the rest of the living coral agent of a different species						
//				System.out.printf("size listCoralAgentSameSpeciesThatCanReproduce: %d \n",listCoralAgentSameSpeciesThatCanReproduce.size());				
				// if the species is present in the reef (CHECKED)	
				if(listCoralAgentSameSpeciesThatCanReproduce.size() == 0){  // the species is not present so no larvae can be produced locally
					numberLarvaeProducedArrayTotal[i] = 0 ;
				}else{
					/*
					 * The listCoralAgentSameSpecies is going to contain only the mature coral agents (i.e., having reached ageAtMaturity 
					 * OR being bigger that their sizeAtMAturity). The other one are removed.
					 */
					ArrayList<Agent> listCoralAgentSameSpeciesTemporal = (ArrayList) listCoralAgentSameSpeciesThatCanReproduce.clone();   // this list is used to go over all the living coral agent of this certain species 
					//System.out.printf("listCoralAgentSameSpecies and temporal: %d, %d \n", listCoralAgentSameSpecies.size(), listCoralAgentSameSpeciesTemporal.size());
					Agent coralAgent = listCoralAgentSameSpeciesTemporal.get(0) ;
					String growthFormSp = coralAgent.getGrowthForm() ;  // get growth form of the species here
					double proportionMalePolyps = 1.0 ;
					if(coralAgent.sexual_system.equals("gonochore")) {
						proportionMalePolyps = Constants.proportion_Male_polyps ;
					}
					while(listCoralAgentSameSpeciesTemporal.size() > 0){
						Agent coral = listCoralAgentSameSpeciesTemporal.get(0) ;  // get the 1st agent in the list
						ArrayList<Agent> agentsFromSameColony = SMUtils.getAgentsFromSameColonyFromList(coral,listCoralAgentSameSpeciesTemporal); // get the agents with the same IDNumber, from a list o		
						boolean colonyIsMature = coral.getAge() > coral.getAgeMaturity() ; // && agentsFromSameColony.size() > coral.getSizeMaturity() ; --> we do not use SizeMaturity
						// remove agents from listCoralAgentSameSpeciesTemporal and eventually from listCoralAgentSameSpeciesThatCanReproduce is the colony is not mature enough
						for (Agent agentSameColony : agentsFromSameColony){
							listCoralAgentSameSpeciesTemporal.remove(agentSameColony);
//							System.out.printf("Is agent coral alive: %b \n",agentSameColony.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL)) ;
							if(!colonyIsMature){
								listCoralAgentSameSpeciesThatCanReproduce.remove(agentSameColony) ;  // non mature agent are removed from list
							}
							// System.out.printf("%s Cc: %.1f colonyMaxDiam: %.1f sizeColony: %d colonyID: %d \n",agentSameColony.getSpecies(),agentSameColony.getCoeffCorrectionPolypfecundity(),agentSameColony.getColonyMaxDiameter(),agentsFromSameColony.size(),agentSameColony.getIDNumber()) ;
						}
						if(colonyIsMature){ // calculate 3D surface area of this colony and then the proportion of mature polyp
							double planarSurfacearea = (double)agentsFromSameColony.size() ;
							// correct for proportion of polyp being mature in function of colony size (pm, Alvarez-Noriega et al., 2016)
							double pm = SMUtils.proportionPolypFecund(planarSurfacearea,coral.getCoeffCorrectionPolypfecundity()) ;
							total3DSurfaceAreaSp = total3DSurfaceAreaSp + pm *  proportionMalePolyps * SMUtils.return3DSurfaceAreaColony(planarSurfacearea, growthFormSp) ;
//							System.out.printf("%s Cc: %.3f colonyMaxDiam: %.1f sizeColony: %.1f pm: %.3f colonyID: %d \n",coral.getSpecies(),coral.getCoeffCorrectionPolypfecundity(),coral.getColonyMaxDiameter(),planarSurfacearea,pm,coral.getIDNumber()) ;
//							System.out.printf("\n%s %s proportionMalePolyps: %.3f colonyID: %d proportion fertile polyps: %.1f planarSurface: %.1f 3Dsurface: %.1f",coral.getSpecies(),coral.getSexualSystem(),proportionMalePolyps,coral.getIDNumber(),pm,planarSurfacearea,SMUtils.return3DSurfaceAreaColony(planarSurfacearea, growthFormSp)) ;
						}
					}
					//System.out.printf("listCoralAgentSameSpecies and temporal: %d, %d \n", listCoralAgentSameSpecies.size(), listCoralAgentSameSpeciesTemporal.size());
					/* CHECKED
					 * Then the number of larvae produces on site by this species is determined by considering:
					 * - the real surface area of the colonies depending on their morphology: total3DSurfaceAreaSp
					 * - the fecundity (number of oocytes produced by polyps)
					 * - the size of the corallites
					 * - the fertilization rate = 50% (see ODD)
					 * - the predation rate = 30 %
					 * - the percentage of competent larvae staying on the reef  --> (Figueiredo et al., 2013)
					 * - the proportion of larvae that are not mature at the same time, the fact that all the polyps of a coral colony are not fecund and a certain proportion of 
					 * oocytes are not viables. = 10% *** TO CHECK***
					 */
					if(total3DSurfaceAreaSp == 0 ) {
						numberLarvaeProducedArrayTotal[i] = 0 ;
					}else {
						double timeToMotility = 0.0 ;
						double proportionCompetentLarvaeRetained = 0.0 ;	
						if (reproductiveMode.equals(Constants.spawner)){			// for brooder, timeToMotility = 0
							// units: eggsize in micrometers and timeMotilty in hour
							timeToMotility = 0.0596 * coralAgent.getEggDiameter() * 1000 + 0.067 ;    // (Figueiredo et al., 2013), x 1000 for conversion from mm to microm				
							// System.out.printf("timeToMotilityhours: %.1f, ", timeToMotility);						
							timeToMotility = timeToMotility / 24 ;					// conversion from hour to day
						}
						// determine the proportionCompetentLarvaeRetained depending on retention time, from (Figueiredo et al., 2013)	
						if(Constants.retentionTime == 16.3) {
							proportionCompetentLarvaeRetained =  0.801 - 0.222 * timeToMotility ;
						}else if(Constants.retentionTime == 10.24) {
							proportionCompetentLarvaeRetained =  0.768 - 0.247 * timeToMotility ;
						}else if(Constants.retentionTime == 7.66) {
							proportionCompetentLarvaeRetained =  0.741 - 0.267 * timeToMotility ;
						}else if(Constants.retentionTime == 6.97) {
							proportionCompetentLarvaeRetained =  0.731 - 0.274 * timeToMotility ;
						}else if(Constants.retentionTime == 4.69) {
							proportionCompetentLarvaeRetained =  0.18 + 0.545 * Math.exp(-1.354 * timeToMotility) ;
						}else if(Constants.retentionTime == 2.14) {
							proportionCompetentLarvaeRetained =  0.09 + 0.557 * Math.exp(-2.74 * timeToMotility) ;
						}else if(Constants.retentionTime == 1.5) {
							proportionCompetentLarvaeRetained =  0.05 + 0.551 * Math.exp(-3.4 * timeToMotility) ;
						}else if(Constants.retentionTime == 1.21) {
							proportionCompetentLarvaeRetained =  0.031 + 0.536 * Math.exp(-3.8 * timeToMotility) ;
						}else if(Constants.retentionTime == 0.90) {
							proportionCompetentLarvaeRetained =  0.014 + 0.501 * Math.exp(-4.5 * timeToMotility) ;
						}else if(Constants.retentionTime == 0.7) {
							proportionCompetentLarvaeRetained =  0.006 + 0.461 * Math.exp(-5.1 * timeToMotility) ;
						}
						
						//	System.out.printf("timeToMotilityDays: %.1f, \n", timeToMotility);
						//	System.out.printf("proportionCompetentLarvaeRetained: %.1f, \n", proportionCompetentLarvaeRetained);	
						numberLarvaeProducedArrayTotal[i] = (int)( total3DSurfaceAreaSp / coralAgent.getCoralliteArea()     // gives the number of polyps
								* coralAgent.getFecundityPolyp() 
								* Constants.fertilizationRate
								* (1 - Constants.predationRate)
								* proportionCompetentLarvaeRetained
								* Constants.otherProportions 
								* Constants.proba_competency
//								* Constants.proportion_mature_polyps  // implemented earlier
//								* Constants.proportion_female_polyps  // implemented earlier
								+ 0.5 ) ;						       // int + 0.5 is to round to the double # to the closest integer number
					}
//					System.out.printf("%s %d ",nameSpecies,numberLarvaeProducedArrayTotal[i]);
				}				
			}	
			if(Constants.outprint_data){
				 System.out.printf("%s: %d ; ",nameSpecies,numberLarvaeProducedArrayTotal[i]);                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
			}		
			i++;
		}	
		if(Constants.outprint_data){
			System.out.printf("\n") ;
		}
		return numberLarvaeProducedArrayTotal ;
	}
	
	/**
	 * This part corresponds to the addition of larvae coming from the regional pool, which depends on the level of connectivity.
	 * @param reproduction_brooder_spawner
	 * @param numberLarvaeProducedArrayTotal
	 * @return
	 */
	public static int[] coralLarvaeRegionalPoolProductionAdded(String reproduction_brooder_spawner,int[] numberLarvaeProducedArrayTotal){
		
//		int numberSpecies = nameCoralSpeciesNames.size();
//		int totalNumberLarvae = 0 ;
//		for(int k = 0 ; k < numberSpecies ;  k++){
//			totalNumberLarvae = totalNumberLarvae + numberLarvaeProducedArrayTotal[k] ;
//		}
//		System.out.printf("totalNumberLarvae in class CoralReproduction, before regional pool input: %d \n",totalNumberLarvae) ;		
		/* CHECKED
		 * Below, the regional input of larvae is added to the list in a random way (i.e., the species the most present in the reef are not 
		 * necessarily the most present in the regional pool).
		 * Addition of regional input to the list depending on connectivity.
		 * The number of larvae coming from the regional pool is calculated in the ODD:
		 * - If connectivity is High (5 km):        
		 * - If connectivity is medium(3413 km):     
		 * - If connectivity is low (20 km):          
		 * - If Connectivity is isolated (100 km):     
		 * - If Connectivity is very isolated (200 km): 
		 */	
			
		int numberLarvaeRegionalPool = 0 ;	
		
		if(Constants.connectivity.equals(Constants.connectivityCSV)){
			
			for(Disturbance_larvalConnectivity dlc : Constants.larvalInputList) {
				if(dlc.getYear() == ContextCoralReef2.year){
					Constants.numberLarvaeRemoteReef = (int) (dlc.getNumberLarvae_m2() * Constants.sizeReef / 10000) ; 
					break ;
				}
			}
			
			System.out.printf("In CoralReproduction class, numberLarvaeRegionalPool: %d \n",Constants.numberLarvaeRemoteReef) ;
				
		}
		
		numberLarvaeRegionalPool = Constants.numberLarvaeRemoteReef ;	
		
//		System.out.printf("********* Constants.connectivity: %s, Constants.numberLarvaeRemoteReef : %d \n",Constants.connectivity,Constants.numberLarvaeRemoteReef ) ;
//		if(Constants.connectivity.equals(Constants.connectivityHigh)){
//			numberLarvaeRegionalPool = (int)(Constants.numberLarvaeRegionalPool_HighConnectivity * Constants.height * Constants.width / 10000  + 0.5);    // TODO: check the proportions, especially 10000
//		}else if(Constants.connectivity.equals(Constants.connectivityMedium)){
//			numberLarvaeRegionalPool = (int)(Constants.numberLarvaeRegionalPool_MediumConnectivity * Constants.height * Constants.width / 10000  + 0.5);
//		}else if(Constants.connectivity.equals(Constants.connectivityLow)){
//			numberLarvaeRegionalPool = (int)(Constants.numberLarvaeRegionalPool_LowConnectivity * Constants.height * Constants.width / 10000  + 0.5);											
//		}else if(Constants.connectivity.equals(Constants.connectivityIsolated100)){
//			numberLarvaeRegionalPool = (int)(Constants.numberLarvaeRegionalPool_Isolated100Connectivity * Constants.height * Constants.width / 10000  + 0.5);											
//		}else if(Constants.connectivity.equals(Constants.connectivityIsolated200)){
//			numberLarvaeRegionalPool = (int)(Constants.numberLarvaeRegionalPool_Isolated200Connectivity * Constants.height * Constants.width / 10000  + 0.5);											
//		}
//		System.out.printf("\n numberLarvaeRegionalPool: %d  \n",numberLarvaeRegionalPool);
		/* CHECKED
		 * In this section, the quantity of regional larvae is distributed in the
		 * numberLarvaeProducedArrayTotal[] randomly, by turns, until numberLarvaeRegionalPool reaches 0.
		 */
		while(numberLarvaeRegionalPool > 0 ){
			List<String> shuffledListStrings = new ArrayList<String>(Constants.coralSpeciesPresentList) ; // create a copy of Constants.coralSpeciesPresentList and shuffle it every time 
			Collections.shuffle(shuffledListStrings) ;
			// to keep the posotion of the species name so that the number of larvae is correctly attributed
			List<Integer> positionNames = new ArrayList<Integer>() ;
			int numberSp = Constants.coralSpeciesPresentList.size() ;
			for(String nameSp : shuffledListStrings){
				for(int i = 0 ; i < numberSp ; i++){
					if(Constants.coralSpeciesPresentList.get(i).equals(nameSp)) {
						positionNames.add(i) ;
						break ;
					}
				}
			}
//			System.out.printf("******************** \n") ;
//			for(String s:Constants.coralSpeciesPresentList) {
//				System.out.printf("%s ", s) ;
//			}
//			System.out.printf("\n") ;
//			for(String s:shuffledListStrings) {
//				System.out.printf("%s ", s) ;
//			}
//			System.out.printf("\n") ;
//			for(int pos : positionNames) {
//				System.out.printf("%d ", pos) ;
//			}
//			System.out.printf("\n") ;
			int j = 0 ;
			for(String nameSpecies : shuffledListStrings){
				int randomNumber = 0 ;
				String modeLarvalDevelp = SMUtils.getStringTraitFromFTTable(nameSpecies,Constants.mode_larval_development) ;
				if(modeLarvalDevelp.equals(Constants.brooder)){  // brooder are less connected --> they recruits three times less than spawning species
					if(numberLarvaeRegionalPool > 10) {
//						randomNumber = RandomHelper.nextIntFromTo(5,10);
						randomNumber = 10 ;
						numberLarvaeRegionalPool = numberLarvaeRegionalPool - randomNumber ;
					}else {
						randomNumber = numberLarvaeRegionalPool ;
						numberLarvaeRegionalPool = 0 ;
					}
				}else {
					if(numberLarvaeRegionalPool > 30) {
//						 randomNumber = RandomHelper.nextIntFromTo(5,40);
						 randomNumber = 30 ;
						 numberLarvaeRegionalPool = numberLarvaeRegionalPool - randomNumber ;
					}else {
						randomNumber = numberLarvaeRegionalPool ;
						numberLarvaeRegionalPool = 0 ;
					}
				}
				// only add the larvae if the species could reproduce 
				boolean larvaeConsidered = reproduction_brooder_spawner.equals(Constants.both) || 
										  (reproduction_brooder_spawner.equals(Constants.brooder) && modeLarvalDevelp.equals(Constants.brooder)) ;
				if(larvaeConsidered){
					numberLarvaeProducedArrayTotal[positionNames.get(j)] = numberLarvaeProducedArrayTotal[positionNames.get(j)] + randomNumber ;
				}
			j++;
//			System.out.printf("%s : %d ",nameSpecies,numberLarvaeProducedArrayTotal[j]) ;
			}
//			System.out.printf("numberLarvaeRegionalPool: %d \n",numberLarvaeRegionalPool) ;
		}
		return numberLarvaeProducedArrayTotal ;
	}
	/**
	 * Until the list numberLarvaeProducedArrayTotal is not empty, 20 larvae at a time by species are going to try settling in the reef by randomly landing on agents. This is done
	 * for all the coral species, one after another, in the order of appearance of the name in Constants.coralSpeciesPresentList. 
	 * Only 20 so that there is no priority effect.
	 * If the remaining larvae in a given species is < 20, then all the remaining larvae try to settle (so that all the larvae in the list for 
	 * a given species try to settle).
	 * Probability of settling:
	 * - on CCA:                       p = 0.2
	 * - on barren ground, dead coral: p = 0.1
	 * - on other substrata:           p = 0
	 * @param numberLarvaeProducedArrayTotal --> number of larvae for each cpral species that are going 
	 * @param nameSpeciesNames
	 * @return
	 */
	public static int[] coralSettlement(int[] numberLarvaeProducedArrayTotal){
		
		
		// reduce the number of larvae for each species by the proportion of available substratum (i.e., barren ground and dead coral or CCA).
//		int numberSuitableAgent = 0 ;
//		for(Agent ag:ContextCoralReef2.listAllAgents) {
//			boolean agentIsSuitable = ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL) || 
//									 ag.getSpecies().equals(Constants.SPECIES_BARREN_GROUND) || 
//									 ag.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA) ;
//			if(agentIsSuitable) {
//				numberSuitableAgent++ ;
//			}
//		}
//		double proportionReefSuitable = (double)numberSuitableAgent / Constants.sizeReef ;
		
				
		
		// CHECKED
		final int numberSpecies = Constants.coralSpeciesPresentList.size();
		
		int totalNumberLarvae = 0 ;									// used for the while()											
		for(int i = 0 ; i < numberSpecies ;  i++){
			totalNumberLarvae = totalNumberLarvae + numberLarvaeProducedArrayTotal[i] ;
		}	
		
//		System.out.printf("totalNumberLarvae in class ContextCoralReef: %d \n",totalNumberLarvae);
//		int l = 0;
//		for(String nameSpecies : Constants.coralSpeciesPresentList){
//			System.out.printf("%s: %d ; ",nameSpecies,numberLarvaeProducedArrayTotal[l]);                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
//			l++;
//		}
//		System.out.printf("\n");	
		
		int[] numberLarvaeSuccessfullySettling = new int[numberSpecies];
		while(totalNumberLarvae > 0){
			int j = 0;
			for(String name : Constants.coralSpeciesPresentList){
				if(numberLarvaeProducedArrayTotal[j] > 0){
					int randomNumber = 0;			
					if(numberLarvaeProducedArrayTotal[j] > 20){
//						randomNumber =	RandomHelper.nextIntFromTo(5,20);
						randomNumber = 20 ;
					}else{
						randomNumber = numberLarvaeProducedArrayTotal[j] ;
					}
					// returns a list of agents on which the larvae are going to try to settle, they are randomly chosen in the reef
					ArrayList<Agent> settlementAgents = SMUtils.getGivenNumberAgentsFromList(randomNumber,ContextCoralReef2.listAllAgents,true);		// true: a several larvae can try to settle on the same agent
					// the number of larvae for species j is reduced by the value of random number no matter if the larvae successfully settle
					numberLarvaeProducedArrayTotal[j] = numberLarvaeProducedArrayTotal[j] - randomNumber ;							
					totalNumberLarvae = totalNumberLarvae - randomNumber;						
					for(Agent agent : settlementAgents){
						if(agent.getSpecies().equals(Constants.SPECIES_BARREN_GROUND)){    // if species = "sand" --> no settlement
							double random = RandomHelper.nextDoubleFromTo(0,1) ;
							if(random < Constants.proba_settle_Barren_ground * Constants.proba_Xmonth_recruitment){
								//	System.out.printf("***************settlement on: %s random: %d \n",agent.getSubstrateSubCategory(),random);								
								// the agent is converted into a newly coral larvae corresponding to species in position j in list
								agent.coralLarvaeSettlement(name);								
								numberLarvaeSuccessfullySettling[j] = numberLarvaeSuccessfullySettling[j] + 1 ;							
							}
						}else if (agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL)) {
							double random = RandomHelper.nextDoubleFromTo(0,1) ;
							if(random < Constants.proba_settle_Dead_coral * Constants.proba_Xmonth_recruitment){	// there is 0.5% chance for a larvae to successfully settle on barren ground or dead colony
								//	System.out.printf("***************settlement on: %s random: %d \n",agent.getSubstrateSubCategory(),random);								
								// the agent is converted into a newly coral larvae corresponding to species in position j in list
								agent.coralLarvaeSettlement(name);								
								numberLarvaeSuccessfullySettling[j] = numberLarvaeSuccessfullySettling[j] + 1 ;							
							}
						}else if(agent.getSubstrateSubCategory().equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
							double random = RandomHelper.nextDoubleFromTo(0,1) ;
							if(random < Constants.proba_settle_CCA * Constants.proba_Xmonth_recruitment){	// there is 1% chance for a larvae to successfully settle on CCA								
								//	System.out.printf("***************settlement on: %s random: %d \n",agent.getSubstrateSubCategory(),random);									
								agent.coralLarvaeSettlement(name);								
								numberLarvaeSuccessfullySettling[j] = numberLarvaeSuccessfullySettling[j] + 1;
							}											
						}
					}
				}
				j++;
			}	
		}
		return numberLarvaeSuccessfullySettling ;
		
	}
	
//	@ScheduledMethod(start = 3, interval = 4, priority = 0)
//	public void coralReproductionBis(){
//		/*
//		 *  Reproduction is called if yearDivision equal:
//		 *  - 1: once for both brooders and spawners     
//		 *  - 2: 1st: none (0.00), 2nd both (0.50), and brooder reproduce at the end again 
//		 *  - 3: 1st: none (0.00), 2nd both (0.33), 3rd: brooder (0.67)
//		 *  - 4: 1st: none (0.00), 2nd:none (0.25), 3rd: both (0.50), 4th brooders (0.75)
//		 */
//		double decimalYear = Math.round((year - (int)year) * 100.0) / 100.0 ;   // get the decimal value of the year	
//		if(decimalYear == 0.50 && Constants.yearDivision == 2){
//			System.out.printf("\n REPRODUCTION BROODER AGAIN \n") ;
//			int[] numberLarvaeProducedArrayTotal = CoralReproduction.coralLarvaeTotalProduction("brooder");  // list of numbers of larvae produced in total (locally and form the regional pool)
//			/* Settlement part:
//			 * Return a list of number of larvae successfully settling in the reef
//			 */
//			int[] numberLarvaeSuccessfullySettling = CoralReproduction.coralSettlement(numberLarvaeProducedArrayTotal); // list of numbers of larvae successfully settling by coral species 
////			System.out.printf("number larvae settling: \n");
//			int z = 0;
//			for(String nameSpecies : Constants.coralSpeciesPresentList){
//				System.out.printf("%s: %d ; ", nameSpecies,numberLarvaeSuccessfullySettling[z]);
//				z++;
//			}
//			System.out.printf("\n");
//			
//		}
////		listAllAgents = SMUtils.getAllAgentList();
//	}
	
}