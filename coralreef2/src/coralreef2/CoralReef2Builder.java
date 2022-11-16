 package coralreef2;

import java.util.ArrayList;
import java.util.List;

import Disturbances.Cyclone;
import OutputData.ColonyPlanarAreaDistribution;
import OutputData.NumberCoralRecruits;
import OutputData.PercentageCover;
import OutputData.RugosityCoverGrazed;
import coralreef2.InputData.BiodiversityData;
import coralreef2.InputData.BiodiversityDataBySpecies;
import coralreef2.InputData.ColonydiameterDistributionSkewBias;
import coralreef2.InputData.CycloneData;
import coralreef2.InputData.Sand_cover;
import coralreef2.agent.Agent;
import coralreef2.common.Constants;
import coralreef2.common.SMUtils;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.SimUtilities;


// for issue with resetting context buidlder
// https://sourceforge.net/p/repast/mailman/message/30479203/




/**
 * Initialization of the functional trait values --> functionalTraitDF_model.csv
 * Initialization of the composition of the benthic community based on the community number --> communities_benthic.csv
 * 
 * 
 * @author carturan
 *
 */
public class CoralReef2Builder  implements ContextBuilder<Object> {
	
	// List of objects, each of them contains the % damage of cyclone for a specific year, the 1st one being the 
	// 1st year, etc.
//	public List<CycloneData> dataCycloneBuilder = null;
	public static int IDNumberGenerator = 0;   // to set the IDNumber of each coral colony
	public static double timer = 0;            // timer is one time step ahead of year
	
	public static List<PercentageCover> percentageCover_updated_sink = new ArrayList<PercentageCover>();       // PercentageCover list containing only one instance whose value of attributes are going to be updated at every event
	public static List<NumberCoralRecruits> numberRecruits_updated_list = new ArrayList<NumberCoralRecruits>();  // same here, numberRecruits_updated_list contains only one instance of NumberCoralRecruits
	public static ArrayList<ColonyPlanarAreaDistribution> colonyPlanarAreaDistList = new ArrayList<ColonyPlanarAreaDistribution>() ; // a unique instance of ColonyPlanarAreaDistribution is created for each species, the list is erased by ColonyPlanarAreaDistribution.writeCSVColonyPlanarAreaList_EraseList()
	public static ArrayList<ColonyPlanarAreaDistribution> colonyPlanarAreaDistListCopy = new ArrayList<ColonyPlanarAreaDistribution>() ; // to use in case Constants.RugosityCoverGrazing is TRUE. The copy is only done once at the very beginning and then at the end of each period. 
	// It is done because colonyPlanarAreaDistList is erased every time the csv file is updated. 
	// colonyPlanarAreaDistListCopy is created 

	public static ArrayList<ColonydiameterDistributionSkewBias> colonyDiameterDistributionSkewBiasList = new ArrayList<ColonydiameterDistributionSkewBias>() ; // 
	public static ArrayList<RugosityCoverGrazed> rugosityCoverGrazedList = new ArrayList<RugosityCoverGrazed>() ;

	
	// Build the context
	@Override
	public Context <Object> build (Context<Object> context) {
		
		context = new ContextCoralReef2();
		context.setId("coralreef2");
		
		/*
		 * Set the Constant values:
		 */
//		Constants.getRandomSeed() ;
//		SMUtils.writeCSVRandomSeeds() ;
		Constants.resetLists() ;                    // need to reset list between runs (useless for batch mode)
		Constants.setGUIparameters() ;             // set the values of all the parameters whose value is defined in the GUI
		Constants.setProba_Xmonth_recruitment() ;   // set the probability of larvae recruiting in a time step.
		Constants.setWorkingDirectories() ;
		Constants.setNumberLarvaeComingFromOtherReef() ;
		Constants.importFunctionalTraitTable() ; // CHECKED create a List<FunctionalTrait> in class Constants to be used in all other classes without having to read the csv file anymore, has to come before Constants.setFunctionalTraitFromFile()!), It also sets the value of nameSite
		Constants.setSpeciesNamesList();         // creates a list of the coral species names and FG of algae in class Constants (! it has to be placed before Constants.setFunctionalTraitFromFile()!)
		Constants.setSpeciesNames();				// attribute 
		Constants.setFunctionalTraitFromFile();  // import functional trait data from csv file and update constant values in class Constants
		Constants.setInitialBiodiversityData();  // set the List<BiodiversityData>, which represent the matrix of initial % cover composition as is comes in the communities_benthic.csv
		Constants.setInitialBiodiversityDataBySpecies();  // same as before except that the % cover is list by species
		Constants.setDisturbancesPriority();    // set the list of disturbance scenarios, from csv file and depending on disturbance scenario number
		Constants.setSandCoverScenarios();       // create the list of Sand_cover for each year
		Constants.setCycloneScenarios();
		Constants.setGrazingScenarios();   
		Constants.setBleachingScenarios() ;
		Constants.setNumberYearSimulation() ; 
		
	  // Constants.setLarvaeConnectivityScenarios() // called in Constants.setNumberLarvaeComingFromOtherReef()
//		SMUtils.read_R_path() ;
				
		// Create the grid
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
			new WrapAroundBorders(),
			new SimpleGridAdder<Object>(),
			false,											// so the cells of the grid cannot be occupied by more than 1 agent (multi occupancy --> ' true')
			Constants.width, Constants.height));
		
		ColonyPlanarAreaDistribution.initializeColonyPlanarAreaDistList() ; // create the arrayList of ColonyPlanarAreaDistribution instances (one for each species) 
		ColonydiameterDistributionSkewBias.getSkewBiasDistributionColonySize() ; // 
		
//		for(ColonydiameterDistributionSkewBias CDDSB :  colonyDiameterDistributionSkewBiasList) {
//			System.out.printf("****** %s skew: %.2f bias: %.2f \n", CDDSB.getSpeciesName(),CDDSB.getSkew(), CDDSB.getBias()) ;
//		}
		
		SMUtils.writeCSVParametersSimulation() ;   // record the value of ALL the parameters of the actual simulation
		
		PercentageCover percentageCover_updated = PercentageCover.createPercentageCoverInstance(context);
		percentageCover_updated_sink.add(percentageCover_updated) ;
		NumberCoralRecruits numberRecruits_updated_updated = NumberCoralRecruits.createNumberCoralRecruitsInstance();
		numberRecruits_updated_list.add(numberRecruits_updated_updated) ;
				
//		for(Sand_cover sc : Constants.sandCoverDataList){
//			 System.out.printf("********** %.1f %.1f \n", sc.getYear(),sc.getSand_Cover()) ;
//		}
			
		timer = 0 ;
//		dataCycloneBuilder = SMUtils.readCycloneDataFile(Constants.CYCLONE_DAMAGE_DATA_FILE);
		
		ArrayList<Agent> totalAgentList = new ArrayList<Agent>();
		
		// creates a list of all the cells of the grid  
		final List<GridCell<Agent>> listAllFreeCells = new ArrayList<GridCell<Agent>>();
		for (int i = 0; i < Constants.width ; i++){
			for (int j = 0; j < Constants.height ; j++){
				final GridPoint pt = new GridPoint(i,j);
				final GridCell<Agent> gridCell = new GridCell(pt, Agent.class);
				listAllFreeCells.add(gridCell);
			}
		}
		SimUtilities.shuffle(listAllFreeCells, RandomHelper.getUniform());  		// shuffle the listAllFreeCells
		
		if(Constants.outprint_data){
			System.out.printf("In CoralReef2Builder: *** SIMULATION WITH COMMUNITY %d DISTURBANCE SCENARIO %d CONNECTIVITY %s Print: %b \n",Constants.communityNumber,Constants.disturbanceScenarioNumber,Constants.connectivity,Constants.outprint_data);
		}else {
			System.out.printf("In CoralReef2Builder: *** SIMULATION WITH COMMUNITY %d DISTURBANCE SCENARIO %d CONNECTIVITY %s Print: %b",Constants.communityNumber,Constants.disturbanceScenarioNumber,Constants.connectivity,Constants.outprint_data);
		}
		// print available memory (should be close to Xms value
		System.out.println(", Free memory (bytes): " +  Runtime.getRuntime().freeMemory()) ;


		//********************** BIODIVERSITY MATRICES **********************************************************************
		/*  The following part fill up biodiversityMatrix with the % cover of the different types of agents for the 
		 *  different communities contained in the csv file.
		 *  It only contains the %cover (not the 1st column with the number of the community.
		 *  Update: it is only use now to fill up Constants.algaePresentList and Constants.coralSpeciesPresentList
		 */
		// matrix that contains the % cover of the different benthic groups, 1st row : alive, 2nd: bleached and 3rd dead, from coral_sp_1 to coral_sp_12 and then algae_fg_1, alage_fg_2 and algae_fg_3, and then sand
		double[][] biodiversityMatrix = SMUtils.createBiodiversityMatrixFromCSVFile(Constants.BIODIVERSITY_DATA_FILE,1,Constants.communityNumber);
		double initialPercentageCover = 0 ;
		int column = 0;
		for (String nameColumn : Constants.speciesNamesList){		
			initialPercentageCover = biodiversityMatrix[0][column] + biodiversityMatrix[1][column] + biodiversityMatrix[2][column]; // add all these % cover and then later make some part of coral bleached (row 2) or dead (row 3)			
			column++ ;
			boolean algaeBool = nameColumn.equals(Constants.SPECIES_MACROALGAE) || nameColumn.equals(Constants.SPECIES_TURF)  || nameColumn.equals(Constants.SPECIES_CCA) || 
							    nameColumn.equals(Constants.SPECIES_AMA) || nameColumn.equals(Constants.SPECIES_ACA) || nameColumn.equals(Constants.SPECIES_HALIMEDA);
			boolean sandBool = nameColumn.equals(Constants.SPECIES_SAND) ;
			if(algaeBool && initialPercentageCover < 999){  // 999 % cover in the csv mean that the species is not present 
				Constants.algaePresentList.add(nameColumn) ;
			}else if(!algaeBool && !sandBool && initialPercentageCover < 999){
				Constants.coralSpeciesPresentList.add(nameColumn) ;
			}
		}
		
		 /* The following code create patches of agents until each type reached the initial percentage cover present 
		 * in csv file. :):):)
		 */
//		for(int i = 0 ; i < 3 ; i++){
//			for(int j = 0 ; j < Constants.speciesNamesList.size() ; j++){
//				System.out.printf("%.1f ", biodiversityMatrix[i][j]) ;
//			}
//			System.out.printf("\n");
//		}

		// create speciesnameListOrdered so that the species with the highest radius are treated 1st, then the smallest ones then the algae 
		List<String>  coralSpeciesPresentListOrdered = SMUtils.orderStringsAccordingNumVal(Constants.coralSpeciesPresentList, Constants.colony_max_diameter);
		// combine speciesnameListOrdered and algaePresentList
		List<String> speciesNamesListOrdered = new ArrayList<String>();
		for(String name : coralSpeciesPresentListOrdered){
			speciesNamesListOrdered.add(name) ;
		}
		for(String name : Constants.algaePresentList){
			speciesNamesListOrdered.add(name) ;
		}
		
//		for(String spName : speciesNamesListOrdered) {
//			System.out.printf("%s ", spName);
//		}
//		System.out.printf("\n");
		
		// matrix that contains the % cover of the different benthic groups, 1st row : alive, 2nd: bleached and 3rd dead, from coral_sp_1 to coral_sp_12 and then algae_fg_1, alage_fg_2 and algae_fg_3
		initialPercentageCover = 0 ;
		for (String nameColumn : speciesNamesListOrdered){	// sand is not present in speciesNamesListOrdered
//			initialPercentageCover = biodiversityMatrix[0][column] + biodiversityMatrix[1][column] + biodiversityMatrix[2][column]; // add all these % cover and then later make some part of coral bleached (row 2) or dead (row 3)			 
			
			boolean algaeBool = nameColumn.equals(Constants.SPECIES_MACROALGAE) || nameColumn.equals(Constants.SPECIES_TURF)  || nameColumn.equals(Constants.SPECIES_CCA) || nameColumn.equals(Constants.SPECIES_AMA) || nameColumn.equals(Constants.SPECIES_ACA) || nameColumn.equals(Constants.SPECIES_HALIMEDA);

			initialPercentageCover = BiodiversityDataBySpecies.getPercentageCoverSpecies(nameColumn,"total") ;  // total: alive, bleached and dead added
//			System.out.printf(" Succeee %s %.1f \n",nameColumn,initialPercentageCover);
			/*
			 *  Creation of circular patches of agents of type specified by what is contained in "nameColumn".
			 *  Empty cells of the grid are filled up until the %cover reached by the type of agent is equal to 
			 *  "initialPercentageCover". 
			 */
			double percentageCover = 0 ;
			ArrayList<Agent> listAgent = new ArrayList<Agent>();  // list of agents of the same coral species or algae functional group
			// ******************* Total coral (alive, bleached, dead) and algae gridcells filling ****************** 
			while(percentageCover < initialPercentageCover && listAllFreeCells.size() != 0 && initialPercentageCover != 999){
				final GridCell<Agent> chosenFreeCell = SMUtils.randomElementOf(listAllFreeCells);		// pick up a cell randomly in listAllFreeCells		
				double radiusPatch = 0;
				if(algaeBool){
					// radiusPatch = RandomHelper.nextDoubleFromTo(1, 10);
					radiusPatch = 10.0 ;
				}else{
					ArrayList<Double> skewBiasValues = ColonydiameterDistributionSkewBias.getValueSkewBias(nameColumn) ;
					double skew = skewBiasValues.get(0) ;
					double bias = skewBiasValues.get(1) ; 					
//					System.out.printf("****************  %s skew: %.2f bias: %.2f \n",nameColumn,skew,bias);
					while(radiusPatch == 0 || radiusPatch > Constants.maxRadiusColonyInitialization){ // here the maximum radius is fixed so that the circular colony cannot be bigger than 5% of the reef
						radiusPatch = SMUtils.getRandomRadiusFromSkewedDistribution(nameColumn,skew,bias) ;
//						System.out.printf("****************  %s Planar area: %.2f  \n",nameColumn,Math.PI * Math.pow(radiusPatch,2));
//						System.out.printf("****************  radiusPatch %.2f maxRadiusColonyInitialization: %.2f still going: %b \n",radiusPatch,Constants.maxRadiusColonyInitialization,radiusPatch == 0 | radiusPatch > Constants.maxRadiusColonyInitialization) ;
					}
//					sizePatch = RandomHelper.nextDoubleFromTo(1, 20);   // TODO: pick up a distribution and randomly sample from it , between 1 and max colony size yeaaaaah
				}				
				final List<GridCell<Agent>> neighborhood = SMUtils.getCellsInRadiusFromList(chosenFreeCell, radiusPatch, listAllFreeCells);  // Get of list of empty neighboring cells within a radius measuring sizePatch, chosenFreeCell IS included in the list 
				if(!neighborhood.isEmpty()){
					// for coral species
					if(!algaeBool){
						IDNumberGenerator++;    // all the coral agents of a same colony have the same IDNumber
						// each coral species has a specific general color
						// and each colony has a color that slightly diverges from it,
						// in order to see the colonies.
						double r = 0 ;
						double g = 0 ;
						double b = 0 ;
						double i = 0 ;
						double age = 0 ;
						// fills up all the cells in neighborhood with new agents
						for (GridCell<Agent> act : neighborhood){
							GridPoint locationAct = act.getPoint();
							Agent coral = SMUtils.createAgent(nameColumn, IDNumberGenerator, grid, context, locationAct.getX(),locationAct.getY()); // create a agent 
							listAgent.add(coral);
							listAllFreeCells.remove(act);										
							if (i == 0){				// if the agent is the 1st of the list, it call for the colour in the Functional_trait dataFrame
								r = coral.getRed();
								g = coral.getGreen();
								b = coral.getBlue();
								age = coral.getAge();
								i++;
							}else{        			// otherwise, all the other agents of the list "neighborhood" have the same color as the 1 one.
								coral.setRed(r);
								coral.setGreen(g);
								coral.setBlue(b);
								coral.setAge(age);
							}	
//							System.out.printf("%s , %d \n",coral.getSpecies(),coral.getIDNumber());
						}
					// for algae groups
					}else{
						
						for (GridCell<Agent> act : neighborhood){
							GridPoint locationAct = act.getPoint();	
							Agent algae = SMUtils.createAgent(nameColumn, 0, grid, context, locationAct.getX(),locationAct.getY());
							listAgent.add(algae);
							listAllFreeCells.remove(act);								
//						 System.out.printf("%s , %d \n",algae.getSpecies(),algae.getIDNumber());
						}
					}
				}
				percentageCover = (double) listAgent.size() /  Constants.sizeReef * 100;				
			}
			// *********** Bleaching part *********
			ArrayList<Agent> listBleachedAgent = new ArrayList<Agent>();
			int numberBleachedAgent = 0;
			double percentageBleachedAgent = 0;
			final double percentageBleachedAgentFromCSV =  BiodiversityDataBySpecies.getPercentageCoverSpecies(nameColumn,"bleached") ; // biodiversityMatrix[1][column];
			while(percentageBleachedAgent < percentageBleachedAgentFromCSV && listAgent.size() != 0){
				final Agent chosenAgent = SMUtils.randomElementOf(listAgent);          // TODO: make the whole colony bleach instead of the portions
				ArrayList<Agent> agentsFromSameColony = SMUtils.getAgentsFromSameColonyFromList(chosenAgent,listAgent);
//				double sizePatch = RandomHelper.nextDoubleFromTo(1, 3);
				double sizePatch = 10.0;

				final ArrayList<Agent> neighborhood = SMUtils.getAgentsInRadiusFromList(chosenAgent,        // chosenAgent is included in the list neighborhood
																						sizePatch,
																						agentsFromSameColony);
				for (Agent aliveCoral : neighborhood){
					aliveCoral.conversionToBleachedCoral();
					listAgent.remove(aliveCoral);
					listBleachedAgent.add(aliveCoral);
					numberBleachedAgent++;
				}
				percentageBleachedAgent = (double)numberBleachedAgent / Constants.sizeReef * 100;
			}
			// *********** Dead coral part ***********
			ArrayList<Agent> listDeadAgent = new ArrayList<Agent>();
			int numberDeadAgent = 0;
			double percentageDeadAgent = 0;
			final double percentageDeadAgentFromCSV = BiodiversityDataBySpecies.getPercentageCoverSpecies(nameColumn,"dead") ; // biodiversityMatrix[2][column];
			while(percentageDeadAgent < percentageDeadAgentFromCSV && listAgent.size() != 0){
				final Agent chosenAgent = SMUtils.randomElementOf(listAgent);
				ArrayList<Agent> agentsFromSameColony = SMUtils.getAgentsFromSameColonyFromList(chosenAgent,listAgent);
//				double sizePatch = RandomHelper.nextDoubleFromTo(10, 20);
				double sizePatch = 10.0 ;
				final ArrayList<Agent> neighborhood = SMUtils.getAgentsInRadiusFromList(chosenAgent,        // chosenAgent is included in the list neighborhood
																						sizePatch,
																						agentsFromSameColony);
				for (Agent aliveCoral : neighborhood){
					aliveCoral.conversionToDeadCoral();
					listAgent.remove(aliveCoral);
					listDeadAgent.add(aliveCoral);
					numberDeadAgent++ ;
				}
				percentageDeadAgent = (double)numberDeadAgent / Constants.sizeReef * 100;
			}		
			if(Constants.outprint_data){
				System.out.printf("Cover %s : %.1f vs. %.1f \n",nameColumn,listAgent.size() / Constants.sizeReef * 100, BiodiversityDataBySpecies.getPercentageCoverSpecies(nameColumn,"alive") ) ;
			}
//			System.out.printf("Percentage Bleached agent: %.1f\n",listBleachedAgent.size() / Constants.sizeReef * 100);
//			System.out.printf("Percentage Dead agent: %.1f\n",listDeadAgent.size() / Constants.sizeReef * 100);
//			System.out.printf("\n");
			column ++;
		}
		//******************* Barren ground agents ************************************************************************************
		// the space available is filled up with barrenGround agents	
//		System.out.printf("Percentage barren ground: %.1f \n",listAllFreeCells.size() / Constants.sizeReef * 100);
		// 
		List<Agent> listBarrenGroundAgents = new ArrayList<Agent>() ;
		for (GridCell<Agent> cell : listAllFreeCells){
			GridPoint locationCell = cell.getPoint();	
			Agent barrenGround = SMUtils.createAgent("BarrenGround", 0, grid, context, locationCell.getX(),locationCell.getY());
			totalAgentList.add(barrenGround);
			listBarrenGroundAgents.add(barrenGround) ;
		}

		// Create sand patches
		double initialSandCover = Constants.sandCoverDataList.get(0).getSand_Cover() ;   // get the 1st Sand_cover instance, whihc corresponds to year 0.0 and get the "sandCover" value 
//		double initialSandCoverBIS = Constants.dataBiodiversityList.get(0).getPercentageSand() ;
//		System.out.printf("*******  initialSandCover: %.3f, initialSandCoverBIS: %.3f \n", initialSandCover,initialSandCoverBIS) ;
		double sandCoverSoFar = 0 ;
		List<Agent> sandAgents = new ArrayList<Agent>() ;
		while(sandCoverSoFar < initialSandCover && !listBarrenGroundAgents.isEmpty()){
			final Agent chosenAgent = SMUtils.randomElementOf(listBarrenGroundAgents);         
//			double sizePatch = RandomHelper.nextDoubleFromTo(5, 10);
			double sizePatch = 10.0 ;
			ArrayList<Agent> agentsSameListPatch = SMUtils.getAgentsInRadiusFromList(chosenAgent, sizePatch,listBarrenGroundAgents) ;  // cannot use getAgentsInRadiusBisFromList() because it refers to the context and context has not been returned yet
			for(Agent ag : agentsSameListPatch){
				ag.conversionToSand();
				sandAgents.add(ag);
				listBarrenGroundAgents.remove(ag);
			}
			sandCoverSoFar = (double)sandAgents.size() / Constants.sizeReef * 100.0 ;
		}
		if(Constants.outprint_data){
			System.out.printf("Cover sand %.1f vs. %.1f \n",sandCoverSoFar,initialSandCover) ;
		}		
		// SMUtils.createCSVCoralPairInteracrtion() ;  // create the csv file of coral pair interactions by cutting the Precoda et al., 2017's one with only the species present here
		
		SMUtils.createCSVCoralPairInteracrtionBIS() ;  // create the csv file of coral pair interactions by cutting the Precoda et al., 2017's one with only the species present here

		return context;

	}
}


