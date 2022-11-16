package coralreef2.agent;

import java.util.ArrayList;
import java.util.List;

import coralreef2.ContextCoralReef2;
import coralreef2.CoralReef2Builder;
import coralreef2.InputData.FunctionalTraitData;
import coralreef2.common.Constants;
import coralreef2.common.SMUtils;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;


public class Agent {
	protected final Grid<Object> grid; 	// grid that holds all agents
	protected Context context;
	protected int x;
	protected int y;
	public String substrateCategory;
	public String substrateSubCategory;
	public String species;
	public double aggressiveness;      // rank strength between agents: lower number = 
	public String coloniality ;
	public double colony_max_diameter;
	public double corallite_area;      // in cm2
	public double egg_diameter;        // diameter in mm --> to determine the % of spawner larvae remaining on the reef.
	public double fecundity_polyp;			// number of oocytes by corallite/polyp
	public String growth_form;			   // distance in centimeter that agent grows per time interval
	public double growth_rate;		
	public String mode_larval_development;		// spawner or brooder (or both ???)
	public double reduced_scattering_coefficient;
	//public double response_bleaching_index;
	// public double skeletal_density;		// in g.cm3 ?
	public double size_maturity;		// the size of the colony at which it can start to produce eggs NOT USED
	public double age_maturity;		// the age of the colony at which it can start to produce eggs
	public double age;						// age of a colony, so that each polyp of the colony have the same age, no matter when they have been created. 
	public double timeRecoveryBleaching ;  // time need for the agent to recover from bleaching
	public int IDNumber;                   // number unic to each colony
	public double red;
	public double green;
	public double blue;
	public boolean canIGrow ;
	public boolean haveIBeenConverted ;
	public boolean haveIBeenGrazed ;
	public double planar_area_colony ;                // planar surface area in cm2 
//	public double maxRadiusSize ;
	public boolean sizeUpDated ;
	public double bleaching_probability ; 
	//public double x0_logitBleaching ;
	//public double r_logitBleaching ;
	public boolean newRecruit ;
	public String sexual_system ;   // "gonochore"  or   "hermaphrodite"
	public double correction_coeff_polypFecundity ;   // for small coral species --> see Cc in appendix 2

	// constructor
	public Agent (Grid<Object> grid, Context context, int x, int y, 
					String substrateCategory,						// BarrenGround, Algae, Coral 
					String substrateSubCategory,					// barrenGround, Macroalgae, Turf, CCA, LiveCoral, BleachedCoral, DeadCoral
					String species,									// Macroalgae, Turf, CCA, Sp1, Sp2, etc.
					double aggressiveness,
					String coloniality,
					double colony_max_diameter,
					double corallite_area,
					double egg_diameter,
					double fecundity_polyp,
					String growth_form,
					double growth_rate,
					String mode_larval_development,
					double reduced_scattering_coefficient,
					//double response_bleaching_index,
					//double skeletal_density,
					double size_maturity,
					double age_maturity,
					double age, 
					double timeRecoveryBleaching ,
					int IDNumber,
					double red, double green, double blue,
					boolean canIGrow,
					boolean haveIBeenConverted,
					boolean haveIBeenGrazed,
					double planar_area_colony,
//					double maxRadiusSize,
					boolean sizeUpDated,
					double bleaching_probability,
					//double x0_logitBleaching,
					//double r_logitBleaching) 
					boolean newRecruit,
					String sexual_system,
					double correction_coeff_polypFecundity) {
		
		this.grid = grid;
		this.context = context ;
		this.x = x;
		this.y = y;
		this.substrateCategory = substrateCategory;
		this.substrateSubCategory = substrateSubCategory;
		this.species = species;
		this.aggressiveness = aggressiveness ;
		this.coloniality = coloniality;
		this.colony_max_diameter = colony_max_diameter ;
		this.corallite_area = corallite_area ;
		this.egg_diameter = egg_diameter;
		this.fecundity_polyp = fecundity_polyp;
		this.growth_form = growth_form;
		this.growth_rate = growth_rate;
		this.mode_larval_development = mode_larval_development;
		this.reduced_scattering_coefficient = reduced_scattering_coefficient;
		//this.response_bleaching_index = response_bleaching_index;
		//this.skeletal_density = skeletal_density;
		this.size_maturity = size_maturity;
		this.age_maturity = age_maturity;
		this.age = age;
		this.timeRecoveryBleaching = timeRecoveryBleaching ;
		this.IDNumber = IDNumber;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.canIGrow = canIGrow;
		this.haveIBeenConverted = haveIBeenConverted;
		this.haveIBeenGrazed = haveIBeenGrazed;
		this.planar_area_colony = planar_area_colony ;
//		this.maxRadiusSize = maxRadiusSize;
		this.sizeUpDated = sizeUpDated;
		this.bleaching_probability = bleaching_probability ;
		//this.x0_logitBleaching = x0_logitBleaching ;
		//this.r_logitBleaching = r_logitBleaching ;
		this.newRecruit  = newRecruit ;
		this.sexual_system = sexual_system ;
		this.correction_coeff_polypFecundity = correction_coeff_polypFecundity ;
		
		context.add(this);
		grid.moveTo(this, x, y);
	}
	
	/**
	 ************************************** GETTERS COMMANDS ***********************************************
	 */
	// returns the x coordinate of the agent
	public int getAgentX(){
		return this.x;
	}
	// returns the y coordinate of the agent
	public int getAgentY(){
		return this.y;
	}
	public String getSubstrateCategory(){
		return this.substrateCategory;
	}
	public String getSubstrateSubCategory(){
		return this.substrateSubCategory;
	}
	public String getSpecies(){
		return this.species;
	}
	public double getAggressiveness(){
		return this.aggressiveness;
	}
	public String getColoniality(){
		return this.coloniality;
	}
	public double getColonyMaxDiameter(){
		return this.colony_max_diameter;
	}
	public double getCoralliteArea(){
		return this.corallite_area;
	}
	public double getEggDiameter(){
		return this.egg_diameter;
	}
	public double getFecundityPolyp(){
		return this.fecundity_polyp;
	}
	public String getGrowthForm(){
		return this.growth_form;
	}
	public double getGrowthRate(){
		return this.growth_rate;
	}
	public String getModeLarvalDevelopment(){
		return this.mode_larval_development;
	}
	public double getReducedScatteringCoefficient(){
		return this.reduced_scattering_coefficient;
	}
//	public double getResponseBleachingIndex(){
//		return this.response_bleaching_index;
//	}
//	public double getSkeletalDensity(){
//		return this.skeletal_density;
//	}
	public double getSizeMaturity(){
		return this.size_maturity;
	}
	public double getAgeMaturity(){
		return this.age_maturity;
	}
	public double getRed(){
		return this.red;
	}
	public double getGreen(){
		return this.green;
	}
	public double getBlue(){
		return this.blue;
	}
	public int getIDNumber(){
		return this.IDNumber;
	}
	public boolean getCanIGrow(){
		return this.canIGrow;
	}
	public boolean getHaveIBeenConverted(){
		return this.haveIBeenConverted;
	}
	public boolean getHaveIBeenGrazed(){
		return this.haveIBeenGrazed;
	}
	public double getAge(){
		return this.age;
	}
	public double getTimeRecoveryBleaching(){
		return this.timeRecoveryBleaching ;
	}
	public double getPlanarAreaColony(){
		return this.planar_area_colony ;
	}
	public double getBleaching_probability(){
		return this.bleaching_probability;
	}
//	public double getX0_logitBleaching(){
//		return this.x0_logitBleaching ;
//	}
//	public double getR_logitBleaching(){
//		return this.r_logitBleaching ;
//	}
	public boolean getNewRecruit() {
		return this.newRecruit ;
	}
	public String getSexualSystem() {
		return this.sexual_system ;
	}
	public double getCoeffCorrectionPolypfecundity() {
		return this.correction_coeff_polypFecundity ;
	}
	
/***********************************************************************************************************************
************************************** SETTER COMMANDS ****************************************************************
***********************************************************************************************************************/
	
	public void  setSubstrateCategory(String name){
		this.substrateCategory = name;
	}
	public void  setSubstrateSubCategory(String name){
		this.substrateSubCategory = name;
	}
	public void  setSpecies(String name){
		this.species = name;
	}
	public void  setAggressiveness(double ag){
		this.aggressiveness = ag;
	}
	public void  setColoniality(String name){
		this.coloniality = name;
	}
	public void  setColonyMaxDiameter(double cmd){
		this.colony_max_diameter = cmd;
	}	
	public void  setCoralliteArea(double ca){
		this.corallite_area = ca;
	}
	public void setEggDiameter(double ed){
		this.egg_diameter =  ed;
	}
	public void  setFecundityPolyp(double fe){
		this.fecundity_polyp = fe;
	}
	public void  setGrowthForm(String name){
		this.growth_form = name;
	}
	public void  setGrowthRate(double gr){
		this.growth_rate = gr;
	}
	public void  setModeLarvalDevelopment(String name){
		this.mode_larval_development = name;
	}
	public void setReducedScatteringCoefficient(double rsc){
		this.reduced_scattering_coefficient = rsc;
	}
//	public void setResponseBleachingIndex(double rbi){
//		this.response_bleaching_index = rbi;
//	}
//	public void setSkeletalDensity(double sd){
//		this.skeletal_density = sd;
//	}
	public void setSizeMaturity(double sam){
		this.size_maturity = sam;
	}
	public void setAgeMaturity(double aam){
		this.age_maturity = aam;
	}
	public void  setRed(double r){
		this.red = r ;
	}
	public void  setGreen(double g){
		this.green = g ;
	}
	public void  setBlue(double b){
		this.blue = b ;
	}

	public void setPlanarAreaColony(double colonyArea){
		this.planar_area_colony = colonyArea ;
	}
	public void setAge(double age){
		this.age = age ;
	}
	public void setTimeRecoveryBleaching(double tab){
		this.timeRecoveryBleaching = tab ;
	}
	public void setBleaching_probability(double tab){
		this.bleaching_probability = tab ;
	}
//	public void setX0_logitBleaching(double x0){
//		this.x0_logitBleaching = x0 ;
//	}
//	public void setR_logitBleaching(double r ){
//		this.r_logitBleaching = r ;
//	}
	public void setNewRecruits(boolean newR) {
		this.newRecruit = newR ;
	}
	public void setSexualSystem(String SS) {
		this.sexual_system = SS ;
	}
	public void setCoeffCorrectionPolypfecundity(double ccpf) {
		this.correction_coeff_polypFecundity = ccpf ;
	}
	
//	public void set_colors(){
//		int r = RandomHelper.nextIntFromTo(0, 100);
//		int g = RandomHelper.nextIntFromTo(0, 100);
//		int b = RandomHelper.nextIntFromTo(0, 100);
//		if (this.species == "Sp1"){					// red range
//			this.red = 255 - r/2 ;
//			this.green = 120 - g;
//			this.blue = 120 - b;
//		} else if (this.species == "Sp2"){			// purple range
//			this.red = 220 - r;
//			this.green = 140 - g;
//			this.blue = 255 - b;
//		}
//	}

	public void set_color_agent_black(){
		this.red = 0;
		this.green = 0;
		this.blue = 0;
	}
	/**
	 * set the agent-own variable haveIBeenGrazed to either T of F
	 * @param bool
	 */
	public void set_HaveIBeenGrazed(boolean bool){
		if(bool == true){
			this.haveIBeenGrazed = true ;
		}else{
			this.haveIBeenGrazed = false ;
		}	
	}
	
	// set to true or false the variable 'canIGrow'
	public void set_canIGrow(String x){
		if (x.equals("true")){
			this.canIGrow = true;
		} else if (x.equals("false")) {
			this.canIGrow = false;
		} else {
			System.out.println("Wrong spelling...");
		}
	}
// *************************************************** useless
	// set to true or false the variable 'didIGrowOrHaveBeenCoverted'
//	public void set_HaveIBeenConverted(String x){
//		if (x.equals("true")){
//			this.haveIBeenConverted = true;
//		} else if (x.equals("false")) {
//			this.haveIBeenConverted = false;
//		} else {
//			System.out.println("Wrong spelling...");
//		}
//	}
// *************************************************** useless
	public void setSizeUpDated(boolean bool){
		if (bool = true){
			this.sizeUpDated = true;
		} else if (bool = false) {
			this.sizeUpDated = false;
		} else {
			System.out.println("Wrong spelling...");
		}
	}
	
	/* #####################################
	 * ######## Schedule period 1 #########
	 * #####################################
	 */
	
	/**
	 *  Update the state of agent:
	 *  - if the agent was bleached, it may eventually recover 
	 *  - new recruits lose this state
	 */
	@ScheduledMethod(start = 1, interval = 4, priority = 0.5)
	public void setDefaultParameters(){
		
		boolean  bool = this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_BARREN_GROUND) || 
				        this.substrateSubCategory.equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL);
		if(!bool){  // none living agents don't get older
//			System.out.printf("Age agent before: %.2f ", this.age) ;
			this.age = this.age + 1 / (double)Constants.yearDivision ;
//			System.out.printf("after: %.2f \n", this.age) ;
		}
		this.canIGrow = false ;		// not in use (?)
		this.haveIBeenConverted = false ;
		this.sizeUpDated = false ;
		this.haveIBeenGrazed = false ;		
		this.newRecruit = false ;
		/*
		 * Recovery from bleaching:
		 * Growth rate and aggressiveness that were reduced by 1/2 return to normal values after 6 months
		 * Fecundity that was reduced to 0 is re-established after one year
		 * Need to consider the number of divisions of the year
		 */
		boolean bleachingBool = this.getTimeRecoveryBleaching() > 0 ;
		if(bleachingBool){
//			System.out.printf("timerecivery: %.2f \n",this.timeRecoveryBleaching);
			this.timeRecoveryBleaching = this.timeRecoveryBleaching - 1 / (double)Constants.yearDivision ; // timeRecoveryBleaching = 1.0 at onset bleaching
			if(this.timeRecoveryBleaching <= 0.50 && this.timeRecoveryBleaching != 0.00){          // in case year division = 3 (and timeRecoveryBleaching = 0.33 -> recovery, or 0.66 -> no recovery) or 4 (and timeRecoveryBleaching = 0.5 or 0.75 -> no recovery)
				// no need to do case where Constants.yearDivision == 1 because in this case this.timeRecoveryBleaching == 0.00
				double gr = this.getGrowthRate() ;
				this.setGrowthRate((gr-0.5)*2.0) ;
				//double ag = this.getAggressiveness() ;
//				if(Constants.yearDivision == 2){          // WHY DID I DO THAT ?!
//					this.setGrowthRate((gr-0.5)*2.0) ;     // check growthRate_randomRadiusConversion.csv to understand
//				}else if(Constants.yearDivision == 3){
//					this.setGrowthRate(gr * 3.0 - 2.0) ;  // check growthRate_randomRadiusConversion.csv to understand
//				}else if(Constants.yearDivision == 4){
//					this.setGrowthRate((gr-0.75)*4.0) ; 
//				}
				// this.setAggressiveness(ag * 2.0) ;  --> if aggressiveness is affected by bleaching, need to change probability of winning between 2 coral species used in prioroty to aggressiveness index
				this.setRed(SMUtils.getDoubleTraitFromFTTable(this.getSpecies(),Constants.red));
				this.setBlue(SMUtils.getDoubleTraitFromFTTable(this.getSpecies(),Constants.blue));
				this.setGreen(SMUtils.getDoubleTraitFromFTTable(this.getSpecies(),Constants.green));
				this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL ;
			// CHECKED	
			}else if(this.timeRecoveryBleaching == 0.00){ 				
				this.setFecundityPolyp(SMUtils.getDoubleTraitFromFTTable(this.getSpecies(),Constants.fecundity_polyp)) ;
			}
		}
	}
	
//	@ScheduledMethod(start = 1, interval = 1, priority = -1)
//	public void upDateParameters(){
//		calculateAndUpDateSize();
//	}



	/**
	 * function that calculate the CSF (Colony Shape factor) of each colony depending on its shape (functional trait)
	 * and the size of the colony (cf., Madin et al., 2006). CSFs (slopes) and intercepts are taken from madin et al., 2014
	 * dataset.
	 * THe size if the colony is the planar surface area and is in m2 --> need to convert from cm2 to m2
	 * 
	 *                       CSF = exp(Cte).size^a  --> ln(CSF) = Cte + a.ln(size)
	 *                       
	 * For values, cf. AppendixS2 CSF analyis.R in datasets_original
	 */
	public double getCSF(int sizeColony){         // the CSF for laminar, encrusting, encrusting_long_upright, columnar has been estimated based on the CSF of the other GFs published in (Madin et al., 2014)
		double CSF = 0.0;
		double sizeColonyInM2 = (double) sizeColony / 10000 ;   // conversion from cm2 to m2
		if(this.getGrowthForm().equals(Constants.growthFormBranching)){
			CSF = Math.exp(8.34) * Math.pow(sizeColonyInM2, 0.79);   
		}else if (this.getGrowthForm().equals(Constants.growthFormTables_or_plates)){  
			CSF = Math.exp(4.47) * Math.pow(sizeColonyInM2, 0.39);
		}else if (this.getGrowthForm().equals(Constants.growthFormCorymbose)){
			CSF = Math.exp(2.28) * Math.pow(sizeColonyInM2, 0.16);	
		}else if (this.getGrowthForm().equals(Constants.growthFormLaminar)){          // similar to plating but a bit more resistant because closer to the ground and plates smallers
			CSF = Math.exp(3.8) * Math.pow(sizeColonyInM2, 0.27);	
		}else if (this.getGrowthForm().equals(Constants.growthFormDigitate)){
			CSF = Math.exp(1.25) * Math.pow(sizeColonyInM2, -0.04);
		}		else if (this.getGrowthForm().equals(Constants.growthFormColumnar)){  // between table and corymbose
			CSF = Math.exp(3) * Math.pow(sizeColonyInM2, 0.20);
		}else if (this.getGrowthForm().equals(Constants.growthFormMassive)){
			CSF = Math.exp(-0.94) * Math.pow(sizeColonyInM2, -0.23);
		}else if (this.getGrowthForm().equals(Constants.growthFormEncrusting_long_upright)){  // CSF is cte / independent of the colony size
			CSF = Math.exp(1.4) * Math.pow(sizeColonyInM2, 0);
		}else if (this.getGrowthForm().equals(Constants.growthFormEncrusting)){  // CSF is cte / independent of the colony size, and more resistant that Encrusting_long_upright
			CSF = Math.exp(1) * Math.pow(sizeColonyInM2, 0);
		}
		return CSF;
	}
	
	/** NOT CHECKED BUT HOW?
	 * Methods that 1st allows agent to try growing. An agent can grow if it is not dead coral or barren ground, if it has not been 
	 * converted yet. Agents being surrounded only by agents of the same functional group of algae or coral agents from the same colony
	 * can also try growing because their neighboring agents might change their state during the simulation. 
	 * Then, for each agent that can grow: the method makes 3 lists: one with full growth rate radius around the agent of focus,
	 * for the cases when growth rate is no reduced by spatial expansion; 2 other lists with a shorter radius (growth rate radius / 2 or 4) 
	 * in case the growth rate is reduced due to competition or effect of bleaching. These lists contain the agents that are going to be converted by the agent of focus.
	 */
	@ScheduledMethod(start = 4, interval = 4, priority = 1)
	public void grow(){
//		System.out.printf("IN GROW() this: %s %s ; agent1: %s %s,proba win: %.2f, win? %b \n",this.getSubstrateSubCategory(),this.getSpecies(),agent1.getSubstrateSubCategory(),agent1.getSpecies(),probaOverGrowth,this_wins) ;

		// System.out.printf("egg diameter: %.2f \n",this.egg_diameter);
		// System.out.printf("x: %d y: %d\n",this.x,this.y);
		
		//******* this section determines if an agent can grow during this tick *************
		// only concerns the 'living agents' and those who have not been converted yet (so growth rate over the tick is correct)		
		String substrateCategorythis = this.getSubstrateCategory() ;
		String substrateSubCategorythis = this.getSubstrateSubCategory() ;
		final double radiusThis = this.getColonyMaxDiameter() / 2.0 ;     			// to calculate the maximum colony area that the coral can reach
		final double thisPlanarAreaColony = this.getPlanarAreaColony() ;            // = 0 in case "this" is not a coral or on a coral colony
		final boolean this_Coral = substrateCategorythis.equals(Constants.SUBSTRATE_CATEGORY_CORAL) ;
		final boolean cannotGrow1 = substrateCategorythis.equals(Constants.SUBSTRATE_CATEGORY_BARREN_GROUND) || substrateSubCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL) ; // sand is barrenground
		final boolean cannotGrow2 = this.getHaveIBeenConverted() == true ;
		final boolean cannotGrow3 = this_Coral && thisPlanarAreaColony >= Math.PI * radiusThis * radiusThis ;  // colony of agent 'this' i larger or equal to its maximum surface area
		
//		Constants.counter++ ;
//		System.out.printf("%d \n",Constants.counter);
//		System.out.printf("%d IN GROW() %d %d %s %s %b %b %b ;\n",Constants.counter,this.getAgentX(),this.getAgentY(),this.getSubstrateSubCategory(),this.getSpecies(),!cannotGrow1,!cannotGrow2,!cannotGrow3) ;

//		if(Constants.counter < 1000){
//			System.out.printf("%d IN GROW() %d %d %s %s %b %b %b ;\n",Constants.counter,this.getAgentX(),this.getAgentY(),this.getSubstrateSubCategory(),this.getSpecies(),!cannotGrow1,!cannotGrow2,!cannotGrow3) ;
//		}
		
		if(!cannotGrow1 && !cannotGrow2){
			double normalGR = RandomHelper.nextDoubleFromTo(0,this.growth_rate); // reminder: growth rate is in fact the number to use in randomFloat, extremes excluded
			
			double reduced_GR = normalGR / Constants.GR_reduction_interaction + (Constants.GR_reduction_interaction - 1)/Constants.GR_reduction_interaction ; // CHECKED. Check growthRate_randomRadiusConversion.csv to understand
			
			int IDThis = this.getIDNumber() ;
			String this_species = this.getSpecies();
			final ArrayList<Agent> inRadiusRange_resheeting = new ArrayList<Agent>();									
			final ArrayList<Agent> inRadiusMaxRangeTemporal = SMUtils.getAgentsInRadiusBis(this, normalGR);	         // contains all the agents within normalGR radius. This list is going to be emptied.
			final ArrayList<Agent> inRadiusReducedRangeTemporal = SMUtils.getAgentsInRadiusBis(this, reduced_GR);	 // contains all the agents within halfGR radius. This list is going to be emptied.

			// coral resheeting dead parts of its own colony
			if(this_Coral){
//				double resheetingCoralTissueGR =  normalGR * 4.0 / 3.0 - 0.33 ;      // coral colonies can resheet dead portion of their own colony 1/3 faster than their growth rate as they don't have to build up coral skeleton
				double resheetingCoralTissueGR =  normalGR * 2.0 - 1.0 ;      // coral colonies can resheet dead portion of their own colonytwice faster than their growth rate as they don't have to build up coral skeleton

				final ArrayList<Agent> inRadiusRangeTemporal_resheeting = SMUtils.getAgentsInRadiusBis(this, resheetingCoralTissueGR);	         // contains all the agents within normalGR radius
				if(inRadiusRangeTemporal_resheeting.size() != 0){
					for(Agent agent : inRadiusRangeTemporal_resheeting){
						if(agent.getSpecies().equals(this_species) && agent.substrateSubCategory.equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL)){ // if agent is dead and a dead coral and from the same species, = phoenix effect (Roff et al., 2014 and ref therein)
							inRadiusRange_resheeting.add(agent);
							inRadiusMaxRangeTemporal.remove(agent);
							inRadiusReducedRangeTemporal.remove(agent);
						}
					}
				}
			}
			// all other cases where agent can eventually grow
			if(!cannotGrow1 && !cannotGrow2 && !cannotGrow3 && inRadiusMaxRangeTemporal.size() != 0){
				String growthFromThis = this.getGrowthForm() ;	
				final ArrayList<Agent> inRadiusMaxRange = new ArrayList<Agent>();				// no direct contact 						
				final ArrayList<Agent> inRadiusReducedRange = new ArrayList<Agent>();		// direct contact 
							
				boolean this_branching_or_plating = growthFromThis.equals(Constants.growthFormTables_or_plates) ||  growthFromThis.equals(Constants.growthFormBranching) ;  // to account for "escape in height strategy"
				boolean this_encrusting = growthFromThis.equals(Constants.growthFormEncrusting) ||  growthFromThis.equals(Constants.growthFormEncrusting_long_upright) ; 
				// this is algae
				boolean this_Algae = substrateCategorythis.equals(Constants.SUBSTRATE_CATEGORY_ALGAE) ;
				boolean this_Macroalgae = substrateSubCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE) ;
				boolean this_Halimeda = substrateSubCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA) ;
				boolean this_AMA = substrateSubCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_AMA) ;
				boolean this_ACA = substrateSubCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_ACA) ;
				boolean this_Turf = substrateSubCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_TURF) ;
				boolean this_CCA = substrateSubCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_CCA) ;
				boolean this_BigAlgae = this_Macroalgae || this_Halimeda || this_AMA || this_ACA ;         // big algae have a size of Constants.height_BigAlgae
				// this in algae on different substratums
	//			boolean this_Algae_on_barrenGRound = this_Algae && IDThis == 0 ;
	//			boolean this_Algae_on_coralEncrusting = this_Algae && this_encrusting ;    // algae agents going on dead corals keep the information about the coral they cover
	//			boolean this_Algae_on_coralBranchingPlating = this_Algae && this_branching_or_plating ;
				
				// this is coral
				boolean this_Coral_alive_or_bleached = substrateCategorythis.equals(Constants.SUBSTRATE_CATEGORY_CORAL) && !substrateCategorythis.equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL); // last statement is just for extra security
				boolean this_Coral_alive_or_bleached_branching_plating = this_Coral_alive_or_bleached && this_branching_or_plating ;
				boolean this_Coral_alive_or_bleached_encrusting = this_Coral_alive_or_bleached && this_encrusting ;
				boolean this_Coral_alive_or_bleached_otherGrowthForms = this_Coral_alive_or_bleached && !this_encrusting && !this_branching_or_plating ;
				
				/*
				 * Go over the agents contained inRadiusMaxRangeTemporal and place then in either one or the other of the following list:
				 * - inRadiusMaxRange: agent 'this' can grow over agents without physical opposition, either because agent is not alive or because 'this' overtoping agent
				 * - inRadiusReducedRange: agent 'this' can grow over agents with restriction (i.e., competition between coral, or turf on CCA) --> only if the agent is close enough
				 */
				for (Agent agent1 : inRadiusMaxRangeTemporal){
					
					String substrateCategoryAgent1 = agent1.getSubstrateCategory() ;
					String substrateSubCategoryAgent1 = agent1.getSubstrateSubCategory() ; 
					String growthFormAgent1 = agent1.getGrowthForm() ;
					
					// cases where no need compete: same coral species or agent1 is dead and reachable
					boolean agent1_Coral = substrateCategoryAgent1.equals(Constants.SUBSTRATE_CATEGORY_CORAL) ;
					boolean agent1_Coral_dead = substrateSubCategoryAgent1.equals(Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL) ;
					boolean agent1_Coral_alive_or_bleached = agent1_Coral && !agent1_Coral_dead ;
					boolean sameCoralSpecies = this_Coral_alive_or_bleached && agent1_Coral_alive_or_bleached && this_species.equals(agent1.getSpecies()) ;
					// cases where no need compete: between algae competition (except for CCA which is inferior competitor)		
					boolean agent1_Algae = substrateCategoryAgent1.equals(Constants.SUBSTRATE_CATEGORY_ALGAE) ;
					boolean agent1_hasNotBeenGrazed = agent1.getHaveIBeenGrazed() == false  ;
					boolean sameAlgaeSubcat = this.getSubstrateSubCategory().equals(agent1.getSubstrateSubCategory()) && this_Algae && agent1_Algae ;
					boolean bothAlgaeThisCCA = 	this_Algae && agent1_Algae && this_CCA ;		// CCA is inferior competitor against algae 
					boolean thisAlgaeAgent1Grazed = this_Algae && !agent1_hasNotBeenGrazed ;
					
					if(!sameCoralSpecies &&  !sameAlgaeSubcat && !bothAlgaeThisCCA && !thisAlgaeAgent1Grazed){   // there is a need to convert only in these cases

						boolean agent1_barrenGround = agent1.getSpecies().equals(Constants.SPECIES_BARREN_GROUND)  ;
						boolean agent1_sand = agent1.getSpecies().equals(Constants.SPECIES_SAND)  ;
						boolean agent1_branching_or_plating = growthFormAgent1.equals(Constants.growthFormTables_or_plates) ||  growthFormAgent1.equals(Constants.growthFormBranching) ;
						boolean agent1_encrusting = growthFormAgent1.equals(Constants.growthFormEncrusting) ||  growthFormAgent1.equals(Constants.growthFormEncrusting_long_upright) ;
						double areaAgent1 = agent1.getPlanarAreaColony() ;  // = 0 if agent is not on a colony
						boolean agent1_Coral_dead_encrusting = agent1_Coral_dead && agent1_encrusting ;
						boolean agent1_Coral_dead_branching_plating = agent1_Coral_dead && agent1_branching_or_plating ;
						// easy cases where there is no restriction on the growth rate of agent 'this' --> 		CASE A IN EXCEL SHEET "Spatial competition relationship.xlsx"
						boolean agents1_not_alive_and_being_covered = (((this_Algae && agent1_hasNotBeenGrazed) || this_Coral_alive_or_bleached) && agent1_barrenGround) ||
																	 (((this_Algae && agent1_hasNotBeenGrazed) || this_Coral_alive_or_bleached) && (agent1_Coral_dead && !agent1_branching_or_plating)) ||   // CASE A in "Spatial competition realtionships_lastVersion.xlsx"
																	 ((this_Algae && agent1_hasNotBeenGrazed) && (agent1_Coral_dead && agent1_branching_or_plating)) || 		    							//   CASE G in "Spatial competition realtionships_lastVersion.xlsx"
																	 (agent1_Coral_dead_branching_plating && ((this_Coral_alive_or_bleached_branching_plating && thisPlanarAreaColony*Constants.ratioAreaBranchingPlating_OvertopColonies > areaAgent1 ) ||  //  CASE F in "Spatial competition realtionships_lastVersion.xlsx"
																			 											  (this_Coral_alive_or_bleached_encrusting && areaAgent1 <= Constants.areaBranchingPlating_OvertopFlat) ||
																			 											   this_Coral_alive_or_bleached_otherGrowthForms && thisPlanarAreaColony > areaAgent1*Constants.ratioAreaBranchingPlating_OvertopColonies));	 																				 
					
						/*
						 * For no interactions where growth rate is not reduced - easy cases  CASE A in excel sheet
						 */
						if(agents1_not_alive_and_being_covered){
							inRadiusMaxRange.add(agent1);
							inRadiusReducedRangeTemporal.remove(agent1) ;  // agent1 is removed from the other shorter list in order to save some time
//							inRadiusDividedByFourRangeTemporal.remove(agent1) ;
						
						}else{  // then need to get into more details
							
//							System.out.printf("%s %s %.2f \n",agent1.getSubstrateSubCategory(),agent1.getSpecies(),agent1.getPlanarAreaColony());
							
							int IDAgent1 = agent1.getIDNumber() ;
							double agent1_radiusColony = 0 ;
							if(IDAgent1 != 0){ // the agent is or is on a colony --> calculate the radius of the colony, supposing the colony has a circular surface areas
								agent1_radiusColony = Math.sqrt(areaAgent1/Math.PI) ;
							}
							// agent1 is algae
							boolean agent1_Macroalgae = substrateSubCategoryAgent1.equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE) ;
							boolean agent1_Halimeda = substrateSubCategoryAgent1.equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA) ;
							boolean agent1_AMA = substrateSubCategoryAgent1.equals(Constants.SUBSTRATE_SUBCATEGORY_AMA) ;
							boolean agent1_ACA = substrateSubCategoryAgent1.equals(Constants.SUBSTRATE_SUBCATEGORY_ACA) ;
							boolean agent1_CCA = substrateSubCategoryAgent1.equals(Constants.SUBSTRATE_SUBCATEGORY_CCA) ;
							boolean agent1_Turf = substrateSubCategoryAgent1.equals(Constants.SUBSTRATE_SUBCATEGORY_TURF) ;
							boolean agent1_BigAlgae = agent1_Macroalgae || agent1_Halimeda || agent1_AMA || agent1_ACA ;
							boolean agent1_Algae_on_barrenGRound = agent1_Algae && IDAgent1 == 0 ;
							boolean agent1_Algae_on_coralEncrusting = agent1_Algae && agent1_encrusting ;  
							boolean agent1_Algae_on_coralBranchingPlating = agent1_Algae && agent1_branching_or_plating ;
							boolean agent1_Algae_on_coralOtherGrowthForms = agent1_Algae && !agent1_branching_or_plating && !agent1_encrusting &&  IDAgent1 != 0 ;

							// agent1 is coral
							boolean agent1_Coral_alive_or_bleached_branching_plating = agent1_Coral_alive_or_bleached && agent1_branching_or_plating ;
							boolean agent1_Coral_alive_or_bleached_encrusting = agent1_Coral_alive_or_bleached && agent1_encrusting ;
							boolean agent1_Coral_alive_or_bleached_otherGrowthForms = agent1_Coral_alive_or_bleached && !agent1_encrusting && !agent1_branching_or_plating ;

							// areas that branching and plating corals have to pass when algae is on coral colony
							Double surFaceBigAlgaeOnCoral = Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(agent1_radiusColony + Constants.height_BigAlgae,2)) ;          // TODO: add 5 cm to hieght algae
							Double surFaceTurfOnCoral = Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(agent1_radiusColony + Constants.height_Turf,2)) ;   				 // TODO: add 5 cm to hieght algae
							Double surFaceCCAOnCoral = Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(agent1_radiusColony + Constants.height_CCA_EncrustingCoral,2)) ;    // TODO: add 5 cm to hieght algae
							
							if(this_Coral_alive_or_bleached_branching_plating){          // cases where branching and plating colonies can overtop other colonies or algae        
								double thresholdToPassForBranchingPlatingCoral = 999.0 ;
								double probaWinningFight = 0.0 ; 
								boolean canBranchingPlatingCoralOvertopAgent = false ;
								boolean canBranchingPlatingCoralOverGrowAgent = false ;
								
								// CASE E: branchingPlating coral overtop coral
								if(agent1_Coral_alive_or_bleached_branching_plating || agent1_Coral_alive_or_bleached_encrusting || agent1_Coral_alive_or_bleached_otherGrowthForms){								
									canBranchingPlatingCoralOvertopAgent = (agent1_Coral_alive_or_bleached_encrusting && thisPlanarAreaColony > Constants.areaBranchingPlating_OvertopFlat)  ||
																		  ((agent1_Coral_alive_or_bleached_branching_plating || agent1_Coral_alive_or_bleached_otherGrowthForms) && thisPlanarAreaColony > Constants.ratioAreaBranchingPlating_OvertopColonies*areaAgent1) ;	
								}
								// CASES B, C, D, O and N in in "Spatial competition realtionships_lastVersion.xlsx"
								else if(agent1_Algae_on_coralBranchingPlating || agent1_Algae_on_coralOtherGrowthForms){
									if(agent1_BigAlgae) {
										thresholdToPassForBranchingPlatingCoral = Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(agent1_radiusColony + Constants.height_BigAlgae,2)) ;		// CASE B    // TODO: add 5 cm to hieght algae
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {   // 'this' is in contact with an algae                     // CASE O
											if(agent1_Macroalgae){
												probaWinningFight = 1.0 - Constants.proba_MA_winCoral ;
											}else if(agent1_Halimeda) {
												probaWinningFight = 1.0 - Constants.proba_Halimeda_winCoral ;
											}else if(agent1_AMA) {
												probaWinningFight = 1.0 - Constants.proba_AMA_winCoral ;
											}else {
												probaWinningFight = 1.0 - Constants.proba_ACA_winCoral ;
											}
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}else if(agent1_Turf){
										thresholdToPassForBranchingPlatingCoral =  Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(agent1_radiusColony + Constants.height_Turf,2)) ; 		// CASE C
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {		// 'this' is in contact with an algae                     // CASE O
											probaWinningFight = 1.0 -  Constants.proba_Turf_winCoral ;
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}else if(agent1_CCA){
										thresholdToPassForBranchingPlatingCoral =  Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(agent1_radiusColony + Constants.height_CCA_EncrustingCoral,2)) ; ;			// CASE D
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {		// 'this' is in contact with an algae                     // CASE O
											probaWinningFight = 1.0 -  Constants.proba_CCA_winCoral ;
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}
								}else if(agent1_Algae_on_coralEncrusting){
									if(agent1_BigAlgae) {
										// thresholdToPassForBranchingPlatingCoral = Constants.areaBranchingPlating_OvertopBigAlgae ;																				// CASE B
										thresholdToPassForBranchingPlatingCoral =  Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(Constants.height_CCA_EncrustingCoral + Constants.height_BigAlgae + 5,2)) ; //  TODO: DONE remove previous line and active this one
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {   // 'this' is in contact with an algae                     // CASE O
											if(agent1_Macroalgae){
												probaWinningFight = 1.0 - Constants.proba_MA_winCoral ;
											}else if(agent1_Halimeda) {
												probaWinningFight = 1.0 - Constants.proba_Halimeda_winCoral ;
											}else if(agent1_AMA) {
												probaWinningFight = 1.0 - Constants.proba_AMA_winCoral ;
											}else {
												probaWinningFight = 1.0 - Constants.proba_ACA_winCoral ;
											}
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}else if(agent1_Turf){
										// thresholdToPassForBranchingPlatingCoral =  Constants.areaBranchingPlating_OvertopTurf ;																					// CASE C
										thresholdToPassForBranchingPlatingCoral =  Constants.ratioAreaBranchingPlating_OvertopColonies*(Math.PI*Math.pow(Constants.height_CCA_EncrustingCoral + Constants.height_Turf + 5,2)) ;  // TODO: DONE remove previous line and active this one
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {		// 'this' is in contact with an algae                     // CASE O
											probaWinningFight = 1.0 -  Constants.proba_Turf_winCoral ;
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}else if(agent1_CCA){
										// thresholdToPassForBranchingPlatingCoral =  Constants.areaBranchingPlating_OvertopFlat ;																					// CASE D
										thresholdToPassForBranchingPlatingCoral =  Math.PI*Math.pow(Constants.height_CCA_EncrustingCoral + Constants.height_CCA_EncrustingCoral + 5,2) ;  // TODO: DONE remove previous line and active this one
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {		// 'this' is in contact with an algae                     // CASE O
											probaWinningFight = 1.0 -  Constants.proba_CCA_winCoral ;
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}
								}else if(agent1_Algae_on_barrenGRound) {
									if(agent1_BigAlgae){       // CASE B1
										thresholdToPassForBranchingPlatingCoral = Constants.areaBranchingPlating_OvertopBigAlgae ;																				// CASE B1
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {   // 'this' is in contact with an algae                     // CASE O
											if(agent1_Macroalgae){
												probaWinningFight = 1.0 - Constants.proba_MA_winCoral ;
											}else if(agent1_Halimeda) {
												probaWinningFight = 1.0 - Constants.proba_Halimeda_winCoral ;
											}else if(agent1_AMA) {
												probaWinningFight = 1.0 - Constants.proba_AMA_winCoral ;
											}else {
												probaWinningFight = 1.0 - Constants.proba_ACA_winCoral ;
											}
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}
									else if(agent1_Turf){           // CASE C1
										thresholdToPassForBranchingPlatingCoral =  Constants.areaBranchingPlating_OvertopTurf ;																					// CASE C1
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {		// 'this' is in contact with an algae                     // CASE O
											probaWinningFight = 1.0 -  Constants.proba_Turf_winCoral ;
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}
									else if(agent1_CCA){				// CASE D1
										thresholdToPassForBranchingPlatingCoral =  Constants.areaBranchingPlating_OvertopFlat ;																					// CASE D1
										canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
										if(canBranchingPlatingCoralOvertopAgent == false && agent1.agentPresentInList(inRadiusReducedRangeTemporal)) {		// 'this' is in contact with an algae                     // CASE O
											probaWinningFight = 1.0 -  Constants.proba_CCA_winCoral ;
											canBranchingPlatingCoralOverGrowAgent =  RandomHelper.nextDoubleFromTo(0,1) < probaWinningFight ;
										}
									}
								}else if(agent1_sand){
									thresholdToPassForBranchingPlatingCoral =  Constants.areaBranchingPlating_OvertopFlat ;           																				// CASE N
									canBranchingPlatingCoralOvertopAgent = thisPlanarAreaColony > thresholdToPassForBranchingPlatingCoral ;  
								}
								if(canBranchingPlatingCoralOvertopAgent == true) {
									inRadiusMaxRange.add(agent1);
									inRadiusReducedRangeTemporal.remove(agent1) ;
								}else if(canBranchingPlatingCoralOverGrowAgent == true) {
									inRadiusReducedRange.add(agent1);
									inRadiusReducedRangeTemporal.remove(agent1) ;
								}
							}
//						   /*
//							* The rest of the interactions only happen in inRadiusReducedRangeTemporal
//							*/
//							} 
							else if(agent1.agentPresentInList(inRadiusReducedRangeTemporal)){	
								
								boolean this_growOver_agent1 = false ;
								// interaction between algae
								if(this_Algae && agent1_CCA && agent1_hasNotBeenGrazed){   // only CCA can be overgrown by other algae FG													// CASE H
									boolean thisBigAlgae = this_Macroalgae || this_AMA || this_Halimeda || this_ACA ; 
									boolean algaeNonCCA_growOver_CCA = (thisBigAlgae || this_Turf) && RandomHelper.nextDoubleFromTo(0,1) < Constants.proba_Algae_coverCCA ;                 	
									this_growOver_agent1 = algaeNonCCA_growOver_CCA ;
								// interaction between corals
								}else if(this_Coral_alive_or_bleached && agent1_Coral_alive_or_bleached){																					// CASE I, J, K			
									boolean this_wins = false ;
									double probaOverGrowth = SMUtils.getProbaOvergrowCoralPair(this_species,agent1.getSpecies()) ;  // returns the probability of "this" to overgrow agent1 from coral_pair_competition_probability_simulation. If "this' or agent1 are not present in csv file, probaOverGrowth = 999
									if(probaOverGrowth == 999){    // one if one of the 2 species or both are not in the list coral_pair_competition_probability_simulation.csv
										this_wins = agent1.getAggressiveness() < this.getAggressiveness() ;
									}else{
										this_wins = RandomHelper.nextDoubleFromTo(0,1) < probaOverGrowth ;
									}					
//									System.out.printf("IN GROW() this: %s %s ; agent1: %s %s,proba win: %.2f, win? %b \n",this.getSubstrateSubCategory(),this.getSpecies(),agent1.getSubstrateSubCategory(),agent1.getSpecies(),probaOverGrowth,this_wins) ;
									if(this_wins){ // then check the other conditions
										boolean this_branchingOrPlating_notBigEnough_coralAgent1 = this_Coral_alive_or_bleached_branching_plating &&    				      														// CASE I
												 												 ((agent1_Coral_alive_or_bleached_encrusting & thisPlanarAreaColony <= Constants.areaBranchingPlating_OvertopFlat) ||
												 												 ((agent1_Coral_alive_or_bleached_branching_plating || agent1_Coral_alive_or_bleached_otherGrowthForms) && thisPlanarAreaColony <= Constants.ratioAreaBranchingPlating_OvertopColonies*areaAgent1 )) ;				
										boolean this_encrusting_coralAgent1 = this_Coral_alive_or_bleached_encrusting && 
																			 (agent1_Coral_alive_or_bleached_encrusting || agent1_Coral_alive_or_bleached_otherGrowthForms ||            											// CASE J
																		 	 (agent1_Coral_alive_or_bleached_branching_plating && areaAgent1 <= Constants.areaBranchingPlating_OvertopFlat)) ; 
										boolean this_otherGrowthForm_coralAgent1 = this_Coral_alive_or_bleached_otherGrowthForms && 
																				  (agent1_Coral_alive_or_bleached_encrusting || agent1_Coral_alive_or_bleached_otherGrowthForms || 												// CASE K
																				  (agent1_Coral_alive_or_bleached_branching_plating && areaAgent1 <= Constants.ratioAreaBranchingPlating_OvertopColonies*thisPlanarAreaColony)) ;
										if(this_branchingOrPlating_notBigEnough_coralAgent1 || this_encrusting_coralAgent1 || this_otherGrowthForm_coralAgent1){
											this_growOver_agent1 = true ;
										}
									}
								
								// coral attacking algae
								}else if(this_Coral_alive_or_bleached && agent1_Algae){   	                               		// CASE M  
									boolean coralWinsAlgae =  RandomHelper.nextDoubleFromTo(0,1) > agent1.getAggressiveness() ;   // the aggressiveness of the algae agent is its probability of winning against corals, values come from Brown's personal communication
									if(coralWinsAlgae){
										boolean thisBranchingPlatingContact = this_Coral_alive_or_bleached_branching_plating &&		
																			(agent1_BigAlgae && 
																			((agent1_Algae_on_barrenGRound || agent1_Coral_alive_or_bleached_encrusting) && thisPlanarAreaColony <= Constants.areaBranchingPlating_OvertopBigAlgae) ||
																			((agent1_Algae_on_coralBranchingPlating || agent1_Algae_on_coralOtherGrowthForms) && thisPlanarAreaColony <= surFaceBigAlgaeOnCoral)) ||
																			(agent1_Turf && 
																			((agent1_Algae_on_barrenGRound || agent1_Coral_alive_or_bleached_encrusting) && thisPlanarAreaColony <= Constants.areaBranchingPlating_OvertopTurf) ||
																			((agent1_Algae_on_coralBranchingPlating || agent1_Algae_on_coralOtherGrowthForms) && thisPlanarAreaColony <= surFaceTurfOnCoral)) ||
																			(agent1_CCA && 
																			((agent1_Algae_on_barrenGRound || agent1_Coral_alive_or_bleached_encrusting) && thisPlanarAreaColony <= Constants.areaBranchingPlating_OvertopFlat) ||
																			((agent1_Algae_on_coralBranchingPlating || agent1_Algae_on_coralOtherGrowthForms) && thisPlanarAreaColony <= surFaceCCAOnCoral))	;
										boolean thisEncrustingContact = this_Coral_alive_or_bleached_encrusting &&
																		(agent1_Algae_on_coralBranchingPlating && areaAgent1 <= Constants.areaBranchingPlating_OvertopFlat) ;  // agent1 is algae on branchingPlating colony too small to avoid contact
										boolean thisOtherGrowthFormsContact = this_Coral_alive_or_bleached_otherGrowthForms && 
																			  ((agent1_Algae_on_coralBranchingPlating && Constants.ratioAreaBranchingPlating_OvertopColonies*thisPlanarAreaColony >= areaAgent1) ||   // 
																			  !agent1_Algae_on_coralBranchingPlating ) ;
										if(thisBranchingPlatingContact || thisEncrustingContact || thisOtherGrowthFormsContact){
											this_growOver_agent1 = true ;
	 									}
									}
								// algae attacking coral														// CASE L 
								}else if(this_Algae && agent1_Coral_alive_or_bleached && agent1_hasNotBeenGrazed){                                        // we don't consider the fact that the algae (this) can be on a coral colony
									boolean algaeWinsCoral = RandomHelper.nextDoubleFromTo(0,1) <= this.getAggressiveness() ;   // the aggressivenss of the algae agent is its probability of winning against corals, according to Brown's personal communication
									if(algaeWinsCoral){
										boolean agent1BranchingPlatingContact = agent1_Coral_alive_or_bleached_branching_plating && 
																				((this_BigAlgae && areaAgent1 <= Constants.areaBranchingPlating_OvertopBigAlgae) ||
																				(this_Turf && areaAgent1 <= Constants.areaBranchingPlating_OvertopTurf) ||
																				(this_CCA && areaAgent1 <= Constants.areaBranchingPlating_OvertopFlat)) ;
										boolean agent1CoralContanct = 	agent1_Coral_alive_or_bleached_encrusting || agent1_Coral_alive_or_bleached_otherGrowthForms ;	
										if(agent1BranchingPlatingContact || agent1CoralContanct){
											this_growOver_agent1 = true ;
										}
									}
								}
								if(this_growOver_agent1){
									inRadiusReducedRange.add(agent1) ;
								}	
							}				
						}
					}
				}
				/*
				 * Conversion 
				 */
				for(Agent agent : inRadiusRange_resheeting){
//					System.out.printf("No contact, this: %s %s,agent1: %s %s \n",this.getSubstrateSubCategory(),this.getSpecies(),agent.getSubstrateSubCategory(),agent.getSpecies()) ;
					this.conversionFromAgent(agent);
				}
				
				for(Agent agent : inRadiusMaxRange){
//					System.out.printf("No contact, this: %s %s,agent1: %s %s \n",this.getSubstrateSubCategory(),this.getSpecies(),agent.getSubstrateSubCategory(),agent.getSpecies()) ;
					this.conversionFromAgent(agent);
				}
				for(Agent agent : inRadiusReducedRange){
//					System.out.printf("Contact, this: %s %s,agent1: %s %s \n",this.getSubstrateSubCategory(),this.getSpecies(),agent.getSubstrateSubCategory(),agent.getSpecies()) ;
					this.conversionFromAgent(agent);
				}
//				for(Agent agent : inRadiusDividedByFourRange){
//					this.conversionFromAgent(agent);
//									
			}
		}
	}

	/**********************************************************************************************************
	 ************************************** CONVERSION COMMANDS ***********************************************
	 **********************************************************************************************************/

//	convert the agent inside the brackets into the same agent as the one who is converting it
// if the agent converting is a alage and the on converted is a coral (dead/bleached/alive)
// then the IDnumber and and species are not changed
	
	
	/**
	 * Convert the agent inside the brackets into the same agent as the one who is converting it ("this")
	 * if the agent converting is a alage and the on converted is a coral (dead/bleached/alive)
	 * then certain attribute are not changed (i.e., IDnumber, coral species name, growth form, size colony,etc.)
	 * @param agent
	 */
	public void conversionFromAgent (Agent agent){	
		// make sure that conversion does not happen if agent has been grazed and this is an algae
//		if(!(this.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_ALGAE) && agent.getHaveIBeenGrazed() == true)){
			// If an algae agent is about to cover another agent that is on a colony, then the information about the colony is not erased.
			if (!(this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_ALGAE) && agent.IDNumber != 0)){ // to preserve the identity of the coral agent being cover by an algae
				agent.species = this.species;
				agent.coloniality = this.coloniality ;
				agent.colony_max_diameter = this.colony_max_diameter ;
				agent.corallite_area = this.corallite_area;
				agent.egg_diameter = this.egg_diameter ;		
				agent.fecundity_polyp = this.fecundity_polyp ;
				agent.growth_form = this.growth_form;
				agent.mode_larval_development = this.mode_larval_development ;	 
				agent.reduced_scattering_coefficient = this.reduced_scattering_coefficient ;
//				agent.response_bleaching_index = this.response_bleaching_index ;
//				agent.skeletal_density = this.skeletal_density ;	
				agent.size_maturity = this.size_maturity ;		
				agent.age_maturity = this.age_maturity ;
				if(this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_CORAL)){   // only expending coral agent can increase the size of their colony
					agent.planar_area_colony = this.planar_area_colony + 1 ;	// so that the area correspond to the size of the colony --> in case of a cylcone  
				}
				agent.IDNumber = this.IDNumber ;
				agent.sexual_system = this.sexual_system ;
				agent.correction_coeff_polypFecundity = this.correction_coeff_polypFecundity ;
			}
			agent.substrateCategory = this.substrateCategory ;
			agent.substrateSubCategory = this.substrateSubCategory ;
			agent.aggressiveness = this.aggressiveness ;
			agent.growth_rate = this.growth_rate ;
			agent.age = this.age ;
			agent.timeRecoveryBleaching = this.timeRecoveryBleaching ;
			agent.red = this.red;							// why do I have to change to color manually, it should be done automatically in the AgentStyleOGL2D class....						
			agent.green = this.green;
			agent.blue = this.blue;
			// agent.haveIBeenGrazed nort concerned 
			agent.canIGrow = false;							// an agent that is converted cannot grow during the actual tick
			agent.haveIBeenConverted = true;
//			agent.area = this.area + 1;						// + 1 so it include itself 
			agent.sizeUpDated = false;
			agent.bleaching_probability = this.bleaching_probability ;
//			agent.x0_logitBleaching = this.x0_logitBleaching ;
//			agent.r_logitBleaching = this.r_logitBleaching ;
			agent.newRecruit = false ;
//		}else{
		if(this.getSubstrateCategory().equals(Constants.SUBSTRATE_CATEGORY_ALGAE) && agent.getHaveIBeenGrazed() == true){
			System.out.printf("CONVERSION TO ALGAE DONE EVEN IF THIS IS ALGAE AND AGENT HAS BEEN GRAZED \n") ;
		}
	}
	
// convert the agent into macrolgae agent
// if the agent is a coral, the species and IDnumber are not changed
	/**
	 * convert the agent into macrolgae agent
	 * if the agent is a coral, the species, IDnumber and other functional traits are not changed
	 */
	public void conversionToMacroalgae(){
		if(this.species.equals(Constants.SPECIES_SAND)){
			System.out.printf("COVERTION OF SAND !!!!! \n");
		}
		// make sure that conversion does not happen if agent has been grazed
//		if(this.getHaveIBeenGrazed() == false){
			// in the case the agent is a coral or dead coral, its IDnumber, species and shape are preserved
			// the if after treat the case where the agent is not a coral
			if (!this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
				this.species = Constants.SPECIES_MACROALGAE ;
				this.coloniality = Constants.MACROALGAE_COLONIALITY ;
				this.colony_max_diameter = Constants.MACROALGAE_COLONY_MAX_DIAMETER ;
				this.corallite_area = Constants.MACROALGAE_CORALLITE_AREA;
				this.egg_diameter = Constants.MACROALGAE_EGG_DIAMETER ;		
				this.fecundity_polyp = Constants.MACROALGAE_FECUNDITY_POLYP ;
				this.growth_form = Constants.MACROALGAE_GROWTH_FORM ;
				this.mode_larval_development = Constants.MACROALGAE_MODE_LARVAL_DEVELOPMENT ;	 
				this.reduced_scattering_coefficient = Constants.MACROALGAE_REDUCED_SCATTERING_COEFFICIENT ;
//				this.response_bleaching_index = Constants.MACROALGAE_RESPONSE_BLEACHING_INDEX ;
//				this.skeletal_density = Constants.MACROALGAE_SKELETAL_DENSITY ;	
				this.size_maturity = Constants.MACROALGAE_SIZE_MATURITY ;		
				this.age_maturity = Constants.MACROALGAE_AGE_MATURITY ;
				this.planar_area_colony = 0 ;	// = 0 for algae not on colonies
				this.IDNumber = 0 ;
				this.sexual_system = Constants.ALGAE_SEXUAL_SYSTEM ;
				this.correction_coeff_polypFecundity = Constants.ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP ;
			}
			this.substrateCategory = Constants.SUBSTRATE_CATEGORY_ALGAE;
			this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE;
			this.aggressiveness = Constants.MACROALGAE_AGGRESSIVENESS ;
			this.growth_rate = Constants.MACROALGAE_GROWTH_RATE ;
			this.age = 0 ;
			this.timeRecoveryBleaching = 0 ;
			this.red = 20;						
			this.green = 160;
			this.blue = 0 ;
			this.canIGrow = false;					// an agent that is converted cannot grow during the actual tick	
			this.haveIBeenConverted = true;
//			this.area = this.area + 1;				// + 1 so it include itself 
			this.sizeUpDated = false;
			this.bleaching_probability = Constants.MACROALGAE_BLEACHING_PROBABILITY ;
//			this.x0_logitBleaching = Constants.MACROALGAE_X0_LOGIT_BLEACHING ;
//			this.r_logitBleaching = Constants.MACROALGAE_R_LOGIT_BLEACHING ;
			this.newRecruit = false ;
//		}else{
		if(this.getHaveIBeenGrazed() == true){
				System.out.printf("CONVERSION TO MACROALGAE DONE EVEN IF THIS IS ALGAE AND AGENT HAS BEEN GRAZED \n") ;
			}
	}
// convert the agent into turf agent
// if the agent is a coral, the species and IDnumber are not changed
	public void conversionToTurf(){
		if(this.species.equals(Constants.SPECIES_SAND)){
			System.out.printf("COVERTION OF SAND !!!!! \n");
		}
		// make sure that conversion does not happen if agent has been grazed
//		if(this.getHaveIBeenGrazed() == false){
			// in the case the agent is a coral or dead coral, its IDnumber and species are preserved
			if (!this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
				this.species = Constants.SPECIES_TURF ;
				this.coloniality = Constants.TURF_COLONIALITY ;
				this.colony_max_diameter = Constants.TURF_COLONY_MAX_DIAMETER ;
				this.corallite_area = Constants.TURF_CORALLITE_AREA;
				this.egg_diameter = Constants.TURF_EGG_DIAMETER ;		
				this.fecundity_polyp = Constants.TURF_FECUNDITY_POLYP ;
				this.growth_form = Constants.TURF_GROWTH_FORM ;
				this.mode_larval_development = Constants.TURF_MODE_LARVAL_DEVELOPMENT ;	 
				this.reduced_scattering_coefficient = Constants.TURF_REDUCED_SCATTERING_COEFFICIENT ;
//				this.response_bleaching_index = Constants.TURF_RESPONSE_BLEACHING_INDEX ;
//				this.skeletal_density = Constants.TURF_SKELETAL_DENSITY ;	
				this.size_maturity = Constants.TURF_SIZE_MATURITY ;		
				this.age_maturity = Constants.TURF_AGE_MATURITY ;
				this.planar_area_colony = 0 ; // = 0 for algae not on colonies
				this.IDNumber = 0 ;
				this.sexual_system = Constants.ALGAE_SEXUAL_SYSTEM ;
				this.correction_coeff_polypFecundity = Constants.ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP ;
			}
			this.substrateCategory = Constants.SUBSTRATE_CATEGORY_ALGAE;
			this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_TURF;
			this.aggressiveness = Constants.TURF_AGGRESSIVENESS ;
			this.growth_rate = Constants.TURF_GROWTH_RATE ;
			this.age = 0 ;
			this.timeRecoveryBleaching = 0 ;
			this.red = 20;						
			this.green = 160;
			this.blue = 0 ;
			this.canIGrow = false;					// an agent that is converted cannot grow during the actual tick	
			this.haveIBeenConverted = true;
//			this.area = this.area + 1;				// + 1 so it include itself 
			this.sizeUpDated = false;
			this.bleaching_probability = Constants.TURF_BLEACHING_PROBABILITY ;
//			this.x0_logitBleaching = Constants.TURF_X0_LOGIT_BLEACHING ;
//			this.r_logitBleaching = Constants.TURF_R_LOGIT_BLEACHING ;

//			}else{
			if(this.getHaveIBeenGrazed() == true){
					System.out.printf("CONVERSION TO TURF DONE EVEN IF THIS IS ALGAE AND AGENT HAS BEEN GRAZED \n") ;
				}
	} 
// convert the agent into CCA agent
// if the agent is a coral, the species and IDnumber are not changed	
	public void conversionToCCA(){
		if(this.species.equals(Constants.SPECIES_SAND)){
			System.out.printf("COVERTION OF SAND !!!!! \n");
		}
		// make sure that conversion does not happen if agent has been grazed
//		if(this.getHaveIBeenGrazed() == false){
			// a colony covered by algae keeps it's coral species name and ID number
			if (!this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
				this.species = Constants.SPECIES_CCA ;
				this.coloniality = Constants.CCA_COLONIALITY ;
				this.colony_max_diameter = Constants.CCA_COLONY_MAX_DIAMETER ;
				this.corallite_area = Constants.CCA_CORALLITE_AREA;
				this.egg_diameter = Constants.CCA_EGG_DIAMETER ;		
				this.fecundity_polyp = Constants.CCA_FECUNDITY_POLYP ;
				this.growth_form = Constants.CCA_GROWTH_FORM ;
				this.mode_larval_development = Constants.CCA_MODE_LARVAL_DEVELOPMENT ;	 
				this.reduced_scattering_coefficient = Constants.CCA_REDUCED_SCATTERING_COEFFICIENT ;
//				this.response_bleaching_index = Constants.CCA_RESPONSE_BLEACHING_INDEX ;
//				this.skeletal_density = Constants.CCA_SKELETAL_DENSITY ;	
				this.size_maturity = Constants.CCA_SIZE_MATURITY ;		
				this.age_maturity = Constants.CCA_AGE_MATURITY ;
				this.planar_area_colony = 0 ; // = 0 for algae not on colonies 
				this.IDNumber = 0 ;
				this.sexual_system = Constants.ALGAE_SEXUAL_SYSTEM ;
				this.correction_coeff_polypFecundity = Constants.ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP ;
			}
			this.substrateCategory = Constants.SUBSTRATE_CATEGORY_ALGAE;
			this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_CCA;
			this.aggressiveness = Constants.CCA_AGGRESSIVENESS ;
			this.growth_rate = Constants.CCA_GROWTH_RATE ;
			this.age = 0 ;
			this.timeRecoveryBleaching = 0 ;
			this.red = 20;						
			this.green = 160;
			this.blue = 0 ;
			this.canIGrow = false;					// an agent that is converted cannot grow during the actual tick	
			this.haveIBeenConverted = true;
//			this.area = this.area + 1;				// + 1 so it include itself 
			this.sizeUpDated = false;
			this.bleaching_probability = Constants.CCA_BLEACHING_PROBABILITY ;
//			this.x0_logitBleaching = Constants.CCA_X0_LOGIT_BLEACHING ;
//			this.r_logitBleaching = Constants.CCA_R_LOGIT_BLEACHING ;
			this.newRecruit = false ;
//			}else{
			if(this.getHaveIBeenGrazed() == true){
					System.out.printf("CONVERSION TO CCA DONE EVEN IF THIS IS ALGAE AND AGENT HAS BEEN GRAZED \n") ;
				}
	}
	
	// convert the agent into ACA agent
	// if the agent is a coral, the species and IDnumber are not changed	
		public void conversionToACA(){
			if(this.species.equals(Constants.SPECIES_SAND)){
				System.out.printf("COVERTION OF SAND !!!!! \n");
			}
			// make sure that conversion does not happen if agent has been grazed
//			if(this.getHaveIBeenGrazed() == false){
				// a colony covered by algae keeps it's coral species name and ID number
				if (!this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
					this.species = Constants.SPECIES_ACA ;
					this.coloniality = Constants.ACA_COLONIALITY ;
					this.colony_max_diameter = Constants.ACA_COLONY_MAX_DIAMETER ;
					this.corallite_area = Constants.ACA_CORALLITE_AREA;
					this.egg_diameter = Constants.ACA_EGG_DIAMETER ;		
					this.fecundity_polyp = Constants.ACA_FECUNDITY_POLYP ;
					this.growth_form = Constants.ACA_GROWTH_FORM ;
					this.mode_larval_development = Constants.ACA_MODE_LARVAL_DEVELOPMENT ;	 
					this.reduced_scattering_coefficient = Constants.ACA_REDUCED_SCATTERING_COEFFICIENT ;
//					this.response_bleaching_index = Constants.ACA_RESPONSE_BLEACHING_INDEX ;
//					this.skeletal_density = Constants.ACA_SKELETAL_DENSITY ;	
					this.size_maturity = Constants.ACA_SIZE_MATURITY ;		
					this.age_maturity = Constants.ACA_AGE_MATURITY ;
					this.planar_area_colony = 0 ; // = 0 for algae not on colonies 
					this.IDNumber = 0 ;
					this.sexual_system = Constants.ALGAE_SEXUAL_SYSTEM ;
					this.correction_coeff_polypFecundity = Constants.ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP ;
				}
				this.substrateCategory = Constants.SUBSTRATE_CATEGORY_ALGAE;
				this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_ACA;
				this.aggressiveness = Constants.ACA_AGGRESSIVENESS ;
				this.growth_rate = Constants.ACA_GROWTH_RATE ;
				this.age = 0 ;
				this.timeRecoveryBleaching = 0 ;
				this.red = 20;						
				this.green = 160;
				this.blue = 0 ;
				this.canIGrow = false;					// an agent that is converted cannot grow during the actual tick	
				this.haveIBeenConverted = true;
//				this.area = this.area + 1;				// + 1 so it include itself 
				this.sizeUpDated = false;
				this.bleaching_probability = Constants.ACA_BLEACHING_PROBABILITY ;
//				this.x0_logitBleaching = Constants.ACA_X0_LOGIT_BLEACHING ;
//				this.r_logitBleaching = Constants.ACA_R_LOGIT_BLEACHING ;
				this.newRecruit = false ;
//				}else{
				if(this.getHaveIBeenGrazed() == true){
						System.out.printf("CONVERSION TO ACA DONE EVEN IF THIS IS ALGAE AND AGENT HAS BEEN GRAZED \n") ;
					}
		}
	
		// convert the agent into HALIMEDA agent
		// if the agent is a coral, the species and IDnumber are not changed	
			public void conversionToHALIMEDA(){
				if(this.species.equals(Constants.SPECIES_SAND)){
					System.out.printf("COVERTION OF SAND !!!!! \n");
				}
				// make sure that conversion does not happen if agent has been grazed
//				if(this.getHaveIBeenGrazed() == false){
					// a colony covered by algae keeps it's coral species name and ID number
					if (!this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
						this.species = Constants.SPECIES_HALIMEDA ;
						this.coloniality = Constants.HALIMEDA_COLONIALITY ;
						this.colony_max_diameter = Constants.HALIMEDA_COLONY_MAX_DIAMETER ;
						this.corallite_area = Constants.HALIMEDA_CORALLITE_AREA;
						this.egg_diameter = Constants.HALIMEDA_EGG_DIAMETER ;		
						this.fecundity_polyp = Constants.HALIMEDA_FECUNDITY_POLYP ;
						this.growth_form = Constants.HALIMEDA_GROWTH_FORM ;
						this.mode_larval_development = Constants.HALIMEDA_MODE_LARVAL_DEVELOPMENT ;	 
						this.reduced_scattering_coefficient = Constants.HALIMEDA_REDUCED_SCATTERING_COEFFICIENT ;
//						this.response_bleaching_index = Constants.HALIMEDA_RESPONSE_BLEACHING_INDEX ;
//						this.skeletal_density = Constants.HALIMEDA_SKELETAL_DENSITY ;	
						this.size_maturity = Constants.HALIMEDA_SIZE_MATURITY ;		
						this.age_maturity = Constants.HALIMEDA_AGE_MATURITY ;
						this.planar_area_colony = 0 ; // = 0 for algae not on colonies 
						this.IDNumber = 0 ;
						this.sexual_system = Constants.ALGAE_SEXUAL_SYSTEM ;
						this.correction_coeff_polypFecundity = Constants.ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP ;
					}
					this.substrateCategory = Constants.SUBSTRATE_CATEGORY_ALGAE;
					this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA;
					this.aggressiveness = Constants.HALIMEDA_AGGRESSIVENESS ;
					this.growth_rate = Constants.HALIMEDA_GROWTH_RATE ;
					this.age = 0 ;
					this.timeRecoveryBleaching = 0 ;
					this.red = 20;						
					this.green = 160;
					this.blue = 0 ;
					this.canIGrow = false;					// an agent that is converted cannot grow during the actual tick	
					this.haveIBeenConverted = true;
//					this.area = this.area + 1;				// + 1 so it include itself 
					this.sizeUpDated = false;
					this.bleaching_probability = Constants.HALIMEDA_BLEACHING_PROBABILITY ;
//					this.x0_logitBleaching = Constants.HALIMEDA_X0_LOGIT_BLEACHING ;
//					this.r_logitBleaching = Constants.HALIMEDA_R_LOGIT_BLEACHING ;
					this.newRecruit = false ;
//					}else{
					if(this.getHaveIBeenGrazed() == true){
							System.out.printf("CONVERSION TO HALIMEDA DONE EVEN IF THIS IS ALGAE AND AGENT HAS BEEN GRAZED \n") ;
						}
			}
	
			// convert the agent into AMA agent
			// if the agent is a coral, the species and IDnumber are not changed	
			public void conversionToAMA(){
				if(this.species.equals(Constants.SPECIES_SAND)){
					System.out.printf("COVERTION OF SAND !!!!! \n");
				}
				// make sure that conversion does not happen if agent has been grazed
//				if(this.getHaveIBeenGrazed() == false){
					// a colony covered by algae keeps it's coral species name and ID number
					if (!this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_CORAL)){
						this.species = Constants.SPECIES_AMA ;
						this.coloniality = Constants.AMA_COLONIALITY ;
						this.colony_max_diameter = Constants.AMA_COLONY_MAX_DIAMETER ;
						this.corallite_area = Constants.AMA_CORALLITE_AREA;
						this.egg_diameter = Constants.AMA_EGG_DIAMETER ;		
						this.fecundity_polyp = Constants.AMA_FECUNDITY_POLYP ;
						this.growth_form = Constants.AMA_GROWTH_FORM ;
						this.mode_larval_development = Constants.AMA_MODE_LARVAL_DEVELOPMENT ;	 
						this.reduced_scattering_coefficient = Constants.AMA_REDUCED_SCATTERING_COEFFICIENT ;
//						this.response_bleaching_index = Constants.AMA_RESPONSE_BLEACHING_INDEX ;
//						this.skeletal_density = Constants.AMA_SKELETAL_DENSITY ;	
						this.size_maturity = Constants.AMA_SIZE_MATURITY ;		
						this.age_maturity = Constants.AMA_AGE_MATURITY ;
						this.planar_area_colony = 0 ; // = 0 for algae not on colonies 
						this.IDNumber = 0 ;
						this.sexual_system = Constants.ALGAE_SEXUAL_SYSTEM ;
						this.correction_coeff_polypFecundity = Constants.ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP ;
					}
					this.substrateCategory = Constants.SUBSTRATE_CATEGORY_ALGAE;
					this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_AMA;
					this.aggressiveness = Constants.AMA_AGGRESSIVENESS ;
					this.growth_rate = Constants.AMA_GROWTH_RATE ;
					this.age = 0 ;
					this.timeRecoveryBleaching = 0 ;
					this.red = 20;						
					this.green = 160;
					this.blue = 0 ;
					this.canIGrow = false;					// an agent that is converted cannot grow during the actual tick	
					this.haveIBeenConverted = true;
//					this.area = this.area + 1;				// + 1 so it include itself 
					this.sizeUpDated = false;
					this.bleaching_probability = Constants.AMA_BLEACHING_PROBABILITY ;
//					this.x0_logitBleaching = Constants.AMA_X0_LOGIT_BLEACHING ;
//					this.r_logitBleaching = Constants.AMA_R_LOGIT_BLEACHING ;
					this.newRecruit = false ;
//						}else{
					if(this.getHaveIBeenGrazed() == true){
							System.out.printf("CONVERSION TO AMA DONE EVEN IF THIS IS ALGAE AND AGENT HAS BEEN GRAZED \n") ;
						}
			}
	
	public void conversionToAlgaeType(String nameAlgaeFG){
		if(nameAlgaeFG.equals(Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE)){
			this.conversionToMacroalgae();
		}else if(nameAlgaeFG.equals(Constants.SUBSTRATE_SUBCATEGORY_AMA)){
			this.conversionToAMA();
		}else if(nameAlgaeFG.equals(Constants.SUBSTRATE_SUBCATEGORY_ACA)){
			this.conversionToACA();
		}else if(nameAlgaeFG.equals(Constants.SUBSTRATE_SUBCATEGORY_HALIMEDA)){
			this.conversionToHALIMEDA();
		}else if(nameAlgaeFG.equals(Constants.SUBSTRATE_SUBCATEGORY_CCA)){
			this.conversionToCCA();
		}else if(nameAlgaeFG.equals(Constants.SUBSTRATE_SUBCATEGORY_TURF)){
			this.conversionToTurf();
		}
	}
			
	public void conversionToBarrenGround(){
		this.substrateCategory = Constants.SUBSTRATE_CATEGORY_BARREN_GROUND;
		this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_BARREN_GROUND;
		this.species = Constants.SPECIES_BARREN_GROUND;
		this.aggressiveness = Constants.BARREN_GROUND_AGGRESSIVENESS;
		this.coloniality = Constants.BARREN_GROUND_COLONIALITY ;
		this.colony_max_diameter = Constants.BARREN_GROUND_COLONY_MAX_DIAMETER ;
		this.corallite_area = Constants.BARREN_GROUND_CORALLITE_AREA ;
		this.egg_diameter = Constants.BARREN_GROUND_EGG_DIAMETER ;				
		this.fecundity_polyp = Constants.BARREN_GROUND_FECUNDITY_POLYP ;
		this.growth_form = Constants.BARREN_GROUND_GROWTH_FORM ;
		this.growth_rate = Constants.BARREN_GROUND_GROWTH_RATE;
		this.mode_larval_development = Constants.BARREN_GROUND_MODE_LARVAL_DEVELOPMENT ;	 
		this.reduced_scattering_coefficient = Constants.BARREN_GROUND_REDUCED_SCATTERING_COEFFICIENT ;
//		this.response_bleaching_index = Constants.BARREN_GROUND_RESPONSE_BLEACHING_INDEX ;
//		this.skeletal_density = Constants.BARREN_GROUND_SKELETAL_DENSITY;	
		this.size_maturity = Constants.BARREN_GROUND_SIZE_MATURITY ;		
		this.age_maturity = Constants.BARREN_GROUND_AGE_MATURITY ;
		this.IDNumber = 0;
		this.age = 0;
		this.timeRecoveryBleaching = 0 ;
		this.red = 160;						
		this.green = 160;
		this.blue = 160;
		this.canIGrow = false;				
		this.sizeUpDated = false;
		this.planar_area_colony = 0 ;
		this.bleaching_probability = Constants.BARREN_GROUND_BLEACHING_PROBABILITY ;
//		this.x0_logitBleaching = Constants.BARREN_GROUND_X0_LOGIT_BLEACHING ;
//		this.r_logitBleaching = Constants.BARREN_GROUND_R_LOGIT_BLEACHING ;
		this.newRecruit = false ;
		this.sexual_system = Constants.BARREN_GROUND_SEXUAL_SYSTEM ;
		this.correction_coeff_polypFecundity = Constants.BARREN_GROUND_CORRECTION_COEFF_FECUNDITY_POLYP ;
	}
	
	public void conversionToSand(){
		this.conversionToBarrenGround();
		this.setSpecies(Constants.SPECIES_SAND) ;
		this.red = 255 ;						
		this.green = 255 ;
		this.blue = 51 ;
	}
	
	/**
	 *  Convert an coral agent into a bleached coral agent
	 *  Growth rate is divided by 2
	 *  Polyp fecundity is reduced to 0
	 */
	public void conversionToBleachedCoral(){
		this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL ;
		if(Constants.GR_reduction_bleachedCoral  == 0.5){
			this.growth_rate = this.growth_rate / 2.0 + 0.5 ;
		}else if(Constants.GR_reduction_bleachedCoral == 0.33 ){
			this.growth_rate = (this.growth_rate + 2) / 3.0 ;
		}else if(Constants.GR_reduction_bleachedCoral == 0.25){
			this.growth_rate = this.growth_rate / 2.0 + 0.5 ;
			this.growth_rate = this.growth_rate / 2.0 + 0.5 ;
		}
		this.growth_rate = this.growth_rate / 2.0 + 0.5 ;     // growthRate is in fact the number to put in randomHelper.DoubleFromTo()
		// this.aggressiveness = this.aggressiveness / 2.0 ;
		this.fecundity_polyp = this.fecundity_polyp * Constants.Fecundity_reduction_bleachedCoral ;
		this.red = 245 ;																
		this.green = 245 ;
		this.blue = 245 ;
		this.canIGrow = true ;	          // was false before, not sure why...
		this.timeRecoveryBleaching = 1.0 ;
	}
	
	/**
	 *  convert an coral agent into a dead coral agent
	 */
	public void conversionToDeadCoral(){
		this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL;
		this.growth_rate = Constants.DEAD_CORAL_GROWTH_RATE;
		this.aggressiveness = Constants.DEAD_CORAL_AGGRESSIVENESS;
		this.age = 0;
		this.red = 255;																
		this.green = 255;
		this.blue = 255;
		this.canIGrow = false;	
		this.timeRecoveryBleaching = 0 ;
		this.newRecruit = false ;
	}
	
	/**
	 * Method that only applicable to algae agent. If alage agent is not on a coral colony,it is converted into barren ground.
	 * Otherwise, it is converted on dead coral, whose characteristics has been preserved
	 */
	public void beGrazed(){
		if(this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_ALGAE)){
			if (this.IDNumber != 0){	// i.e., if the algae was covering a coral colony
				this.substrateCategory = Constants.SUBSTRATE_CATEGORY_CORAL;
				this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL;
				this.growth_rate = Constants.DEAD_CORAL_GROWTH_RATE;
				this.age = 0;
				this.aggressiveness = Constants.DEAD_CORAL_AGGRESSIVENESS ;
				this.red = 255;						
				this.green = 255;
				this.blue = 255;
			} else {						// i.e., if the algae was covering barren ground
				this.substrateCategory = Constants.SUBSTRATE_CATEGORY_BARREN_GROUND;
				this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_BARREN_GROUND;
				this.species = Constants.SPECIES_BARREN_GROUND ;
				this.aggressiveness = Constants.BARREN_GROUND_AGGRESSIVENESS ;
				this.colony_max_diameter = Constants.BARREN_GROUND_COLONY_MAX_DIAMETER ;
				this.growth_form = Constants.BARREN_GROUND_GROWTH_FORM ;
				this.growth_rate = Constants.BARREN_GROUND_GROWTH_RATE ;
				this.age = 0;
				this.red = 160;						
				this.green = 160;
				this.blue = 160;
			}
			this.canIGrow = false ;	
			this.sizeUpDated = false ;
			// we don't change agent.maxSize as it is the same for both Macroalgae and Barren Ground and Dead_coral don't grow
			// wed don't change sizeUpDated as the agent is now barren ground or dead coral and size id not calculated for them
			// the following statement is important so that a agent grazed during a tick cannot be cover by on algae anymore (during this time step)
			this.haveIBeenGrazed = true ;
		}else{
			System.out.printf("In Agent.beGrazed(): TRYING TO GRAZED A NON ALGAE AGENT !!! \n") ;
		}
	}
	
	/**
	 * Same as beGrazed(), the only difference is the that haveIBeenGrazed is not changed to TRUE
	 */
	public void beRemoved(){
		if(this.substrateCategory.equals(Constants.SUBSTRATE_CATEGORY_ALGAE)){
			if (this.IDNumber != 0){	// i.e., if the algae was covering a coral colony
				this.substrateCategory = Constants.SUBSTRATE_CATEGORY_CORAL;
				this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL;
				this.growth_rate = Constants.DEAD_CORAL_GROWTH_RATE;
				this.age = 0;
				this.aggressiveness = Constants.DEAD_CORAL_AGGRESSIVENESS ;
				this.red = 255;						
				this.green = 255;
				this.blue = 255;
			} else {						// i.e., if the algae was covering barren ground
				this.substrateCategory = Constants.SUBSTRATE_CATEGORY_BARREN_GROUND;
				this.substrateSubCategory = Constants.SUBSTRATE_SUBCATEGORY_BARREN_GROUND;
				this.species = Constants.SPECIES_BARREN_GROUND ;
				this.aggressiveness = Constants.BARREN_GROUND_AGGRESSIVENESS ;
				this.colony_max_diameter = Constants.BARREN_GROUND_COLONY_MAX_DIAMETER ;
				this.growth_form = Constants.BARREN_GROUND_GROWTH_FORM ;
				this.growth_rate = Constants.BARREN_GROUND_GROWTH_RATE ;
				this.age = 0;
				this.red = 160;						
				this.green = 160;
				this.blue = 160;
			}
			this.canIGrow = false ;	
			this.sizeUpDated = false ;
			// we don't change agent.maxSize as it is the same for both Macroalgae and Barren Ground and Dead_coral don't grow
			// wed don't change sizeUpDated as the agent is now barren ground or dead coral and size id not calculated for them
			// the following statement is important so that a agent grazed during a tick cannot be cover by on algae anymore (during this time step)
//			this.haveIBeenGrazed = true ;
		}else{
			System.out.printf("In Agent.beRemoved(): TRYING TO GRAZED A NON ALGAE AGENT !!! \n") ;
		}
	}
	
	/**
	 * Method that converts the agent affected into a new coral agent (larvae settling)
	 * of the coral species entered as a parameter.	
	 * The new coral is NOT a new agent but has a new IDnumber.
	 */
	public void coralLarvaeSettlement(String nameSpecies) {
		CoralReef2Builder.IDNumberGenerator++;
		for(FunctionalTraitData FTD : Constants.functionalTraitsTable){
			if(FTD.getSpecies().equals(nameSpecies)){
				this.setSubstrateCategory(FTD.getSubstrateCategory());
				this.setSubstrateSubCategory(FTD.getSubstrateSubCategory());
				this.setSpecies(FTD.getSpecies());
				this.setAggressiveness(FTD.getAggressiveness());
				this.setColoniality(FTD.getColoniality()) ;
				this.setColonyMaxDiameter(FTD.getColonyMaxDiameter()) ;
				this.setCoralliteArea(FTD.getCoralliteArea()) ;
				this.setEggDiameter(FTD.getEggDiameter()) ;				
				this.setFecundityPolyp(FTD.getFecundityPolyp());
				this.setGrowthForm(FTD.getGrowthForm());
				this.setGrowthRate(FTD.getGrowthRate());
				this.setModeLarvalDevelopment(FTD.getModeLarvalDevelopment());
				this.setReducedScatteringCoefficient(FTD.getReducedScatteringCoefficient()) ;
//				this.setResponseBleachingIndex(FTD.getResponseBleachingIndex()) ;
//				this.setSkeletalDensity(FTD.getSkeletalDensity());	
				this.setSizeMaturity(FTD.getSizeMaturity());		
				this.setAgeMaturity(FTD.getAgeMaturity());
				this.setRed(FTD.getRed() - RandomHelper.nextIntFromTo(1,10));
				this.setGreen(FTD.getGreen()- RandomHelper.nextIntFromTo(1,10));
				this.setBlue(FTD.getBlue()- RandomHelper.nextIntFromTo(1,10));
				this.IDNumber = CoralReef2Builder.IDNumberGenerator;
				this.age = 0;	
				this.timeRecoveryBleaching = 0 ;
				this.canIGrow = false;			   // TODO: TO CHECK if they can grow after settling: they should be able to 	 --> canIGrow is actually not in use anymore					
				this.sizeUpDated = false;
				this.setBleaching_probability(FTD.getBleachingProbability()) ; 
//				this.setX0_logitBleaching(FTD.getX0LogitBleaching());
//				this.setR_logitBleaching(FTD.getRLogitBleaching());
				this.newRecruit = true ;
				this.setSexualSystem(FTD.getSexualSystem()) ;
				this.setCoeffCorrectionPolypfecundity(FTD.getCorrectionCoeffPolypFecundity()) ;
				break;
			}	
		}
	}	
	

	/**
	 * Calculate the size of the colony of the agent of focus by calculating the size of the list of agents 
	 * having the same IDNumber. The include alive, dead and bleached coral agents and also algae agents covering the colony.
	 * The variable area of the agent of focus is also updated but no for the other agents of the list as 
	 * they might have to call this function again, --> extra unnecessary computation time.
	 * @return this.area
	 */
//	public double calculateColonyArea(){
//		final ArrayList<Agent> agentsWithSameIDNumber = SMUtils.getAgentsFromSameColony(this);
//		this.planar_area_colony = agentsWithSameIDNumber.size();
//		return this.planar_area_colony ;
//	}
	/**
	 *  Same as before but calling a pre-existing list in in oder to reduce the time of implementation
	 */
	public double calculateColonyAreaFromList(ArrayList<Agent> listAllAgents){
		final ArrayList<Agent> agentsWithSameIDNumber = SMUtils.getAgentsFromSameColonyFromList(this,listAllAgents);
		this.planar_area_colony = agentsWithSameIDNumber.size();
		return this.planar_area_colony ;
	}

	/** TO CHECK
	 * Method that returns true if the agent has 0, 1, 2, 3 or 4 Von Numann neighbors with differing feature.
	 * The feature to considered is given by parameter "condition":
	 * - condition = "sameColony"
	 * - The minimum number of Von Numann neighbors determining is the agent is alone is given by the parameter "minNumberdifferentNeighbors"
	 * @param minNumberNeighbors; min = 0, max = 4
	 * @param listAgents
	 * @return
	 */
	public boolean amIAloneFromListCondition(int minNumberdifferentNeighbors, String condition){
		boolean amIAlone = false ;
		int numberDifferentNeighbors = 0 ;
		if(condition.equals("sameColony")) {
			int IDNumberThis = this.getIDNumber() ;
			if(IDNumberThis != 0){
//				ArrayList<Agent> neighborAgents = SMUtils.getAgentsInRadiusBis(this, 1) ;
				ArrayList<Agent> neighborAgents = SMUtils.getVonNeumannNeighbors(this, false) ;
				for(Agent agentNeighbor : neighborAgents){
					if (IDNumberThis != agentNeighbor.IDNumber){
						numberDifferentNeighbors++ ;
					}
				}
			}else {
				System.out.printf("In SMUtils.amIAloneFromListCondition: agent is not on a colony \n") ;
			}	
		}else if(condition.equals("sameSpecies")){
			String speciesName = this.getSpecies() ;
			ArrayList<Agent> neighborAgents = SMUtils.getVonNeumannNeighbors(this, false) ;
			for(Agent agentNeighbor : neighborAgents){
				if (!speciesName.equals(agentNeighbor.getSpecies())){
					numberDifferentNeighbors++ ;
				}
			}
		}
		if(numberDifferentNeighbors >= minNumberdifferentNeighbors){
			amIAlone = true ;
		}
		return amIAlone ;
	}
	
	/**
	 * method that returns true if the agent is present in the list of agent and false otherwise.
	 * It looks at the x and y coordinates as they are unique for each agent
	 * @param agent
	 * @param listAgent
	 * @return
	 */
	public boolean agentPresentInList (ArrayList<Agent> listAgent){
		boolean agentPresentInList = false ;
		int xAgent = this.getAgentX() ;
		int yAgent = this.getAgentY() ;
		for(Agent ag : listAgent){
			if(ag.getAgentX() == xAgent && ag.getAgentY() == yAgent){
				agentPresentInList = true ;
				break ;
			}
		}
		return agentPresentInList ;
	}
	
	
	public void die() {
	    Context context = ContextUtils.getContext(this);
	    context.remove(this);
	}

}
