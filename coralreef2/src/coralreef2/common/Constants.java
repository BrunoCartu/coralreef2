package coralreef2.common;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import OutputData.ColonyPlanarAreaDistribution;
import OutputData.NumberCoralRecruits;
import OutputData.PercentageCover;
import OutputData.RugosityCoverGrazed;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunInfo;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.DefaultRandomRegistry;
import coralreef2.ContextCoralReef2;
import coralreef2.CoralReef2Builder;
import coralreef2.InputData.BiodiversityData;
import coralreef2.InputData.BiodiversityDataBySpecies;
import coralreef2.InputData.ColonydiameterDistributionSkewBias;
import coralreef2.InputData.Disturbance_bleaching;
import coralreef2.InputData.Disturbance_cyclone;
import coralreef2.InputData.Disturbance_grazing;
import coralreef2.InputData.Disturbance_larvalConnectivity;
import coralreef2.InputData.Disturbances_priority;
import coralreef2.InputData.FunctionalTraitData;
import coralreef2.InputData.Sand_cover;
import coralreef2.agent.Agent;

public class Constants {	
	
	public static String wd = "./" ; // "./" or "" work for normal runs
	public static String wd_absolute = "temp" ;
	
	public static String UUID_here = "temp" ;
	
//	public static String wd = "/Users/carturan/Documents/My_Research/Modeling/coralreef2" ;   // THIS VARIABLE NEED TO BE CHANGED DEPENDING ON THE COMPUTEUR USED
//	public static String wd_output_cloud = "/Users/carturan/Documents/My Research/Modeling/coralreef2/outputs_simulations" ;  // THIS VARIABLE NEED TO BE CHANGED DEPENDING ON THE COMPUTEUR USED
    
	public static String  INPUT_WORKSPACE = "temp" ; // = wd + "datasets_inputs" ;
    public static  String OUTPUT_WORKSPACE =  "temp" ;  // wd + "outputs_simulations" ;
    public static  String R_SCRIPTS_WORKSPACE = "temp" ; // wd + "R_scripts" ;
   
    public static String OUTOUT_NUMBER_RECRUITS = "temp" ;
    public static String OUTOUT_PERCENTAGE_COVER = "temp" ;
    
	public static String R_path = "null" ;    // your own personal path of Rscript.exe - to place in datasets_inputs > R_path.csv
	
	public static  String DISTURBANCE_PRIORITY_FILE = "temp" ;
    public static final int DISTURBANCE_FILE_HEADER_LINES = 1;
    
	public static  String SAND_COVER_DATA_FILE = "temp" ;
    public static final int SAND_COVER_FILE_HEADER_LINES = 1;
    
	public static  String CYCLONE_DMT_DATA_FILE = "temp" ;
    public static final int CYCLONE_DMT_DATA_FILE_HEADER_LINES = 1;
    
    public static  String GRAZING_DATA_FILE = "temp" ;
    public static final int GRAZING_DATA_FILE_HEADER_LINES = 1;
    
    public static  String LARVAL_CONNECTIVITY_DATA_FILE = "temp" ;
    public static final int LARVAL_CONNECTIVITY_DATA_FILE_HEADER_LINES = 1;
        
    public static  String FUNCTIONAL_TRAIT_DATAFILE = "temp" ; //Functional_traits_RF_final2.csv";
    public static final int FUNCTIONAL_TRAIT_HEADER_LINES = 1;  // number of rows to skip when reading a csv file
    
    public static  String SKEW_BIAS_COLONY_SIZE_CLASS_DISTRIBUTION = "temp" ; // skew and bias define the distribution shape of the class 
    public static final int SKEW_BIAS_HEADER_LINES = 1;
    
    public static  String BLEACHING_DATA_FILE = "temp" ;
    public static final int BLEACHING_DATA_FILE_HEADER_LINES = 1;

    public static int counter ;
        
//    public static final String BIODIVERSITY_DATA_FILE = "/Users/carturan/Documents/My Research/Modeling/coralreef2/Biodiversity_High_final.csv";     // "Biodiversity_Low_final.csv"
//    public static final String BIODIVERSITY_DATA_FILE_HIGHDIV = "/Users/carturan/Documents/My Research/Modeling/coralreef2/Biodiversity_High_Sim_10.csv";     
//    public static final String BIODIVERSITY_DATA_FILE_LOWDIV = "/Users/carturan/Documents/My Research/Modeling/coralreef2/Biodiversity_Low_Sim_10.csv";     
    
    public static  String COMPETITION_R_SCRIPT = "temp" ;
    
    public static  String BIODIVERSITY_DATA_FILE = "temp" ; 
    public static final int BIODIVERSITY_HEADER_LINES = 1 ;
    
//  public static final String CORAL_PAIR_COMP_PROBA_DATA_FILE = wd + "/datasets_inputs/coral_pair_competition_probability_simulation_community_"; 
    public static final int CORAL_PAIR_COMP_PROBA_HEADER_LINES = 1 ;

    public static  String R_PATH_FILE = "temp" ; 
    public static final int R_PATH_HEADER_LINES = 1 ;
    
	public static final String GRID_ID = "grid";
//	public static final String CONTEXT_ID = "coralreef1";
		
	public static double numberYearSimulation = 0.0 ;  // UI parameter, determines the number of years of the simulations = numberYears in csv + 1 period
	public static double tick_max = 0.0 ;
	public static int communityNumber = 0 ;  // UI parameter, determines the benthic community to select from communities_benthic.csv  
	public static int disturbanceScenarioNumber = 0 ; // UI parameter, determines the 
	public static int numberCycloneDMTmodel = 0 ;    // UI parameter, for the model to use 
	public static int numberGrazingModel = 0 ;
//	public static int numberBleachingModel = 0 ;
	public static boolean outprint_data = false ;
	public static int yearDivision = 0 ;    // the number of year division to implement 
	public static int randomSeed ;
	
	public static boolean RugosityGrazingDo = false ; //  If true, the relationship between rugosity and grazing is considered
	
	public static String eventName = "temp" ; // values = "inital" "cyclone" "bleaching"
	public final static String eventName_initial = "initial" ;
	public final static String eventName_beginingSeason = "begining_season" ;
	public final static String eventName_endSeason = "end_season" ;
	public final static String eventName_cyclone = "cyclone" ;
	public final static String eventName_bleaching = "bleaching" ;

	public static int width = 0 ;
	public static int height = 0 ;
	public static double sizeReef = 0 ;
		
	public static String connectivity = "temp" ;
	public static final String connectivityHigh = "high(5km)" ;
	public static final String connectivityMedium = "medium(10km)";
	public static final String connectivityLow = "low(20km)";
	public static final String connectivityIsolated100 = "isolated(100km)";
	public static final String connectivityIsolated200 = "isolated(200km)";
	public static final String connectivityNone = "noConnectivity";
	public static final String connectivityCSV = "connectivityCSV";  // the number of larvae comes from Disturbance_larvaeConnectivity.csv in data folder
	
	public static String nameSite = "temp" ;   // noy used yet
	// names processes:
	public static final String cyclone = "cyclone";
	public static final String bleaching = "bleaching";
	public static final String reproduction = "reproduction";

//    public static final String CYCLONE_DAMAGE_DATA_FILE = wd + "/datasets_inputs/Cyclone_Damage_final.csv";
//    public static final int CYCLONE_DAMAGE_FILE_HEADER_LINES = 1;
     
	public static Double ratioAreaBranchingPlating_OvertopColonies =  0.0 ;   // change in GUI
	public static Double height_BigAlgae = 0.0 ;      						// change in GUI, default = 30cm
	public static Double height_Turf = 0.0 ;    							// change in GUI, default = 10
	public static Double height_CCA_EncrustingCoral = 0.0 ;  // Value taken from GUI, default = 2.0  ; encrusting_long_upright coral are concerned by this index 
	public static Double areaBranchingPlating_OvertopFlat = 0.0 ;    // updated later
	public static Double areaBranchingPlating_OvertopTurf =  0.0 ;    // updated later
	public static Double areaBranchingPlating_OvertopBigAlgae =  0.0 ;   // updated later
	
	public static Double proba_Algae_coverCCA = 0.0 ;       // change in GUI, default = 0.5
	public final static Double proba_MA_winCoral = 0.7 ;      //         // From Brown personal communication
	public final static Double proba_Halimeda_winCoral = 0.15 ;      //
	public final static Double proba_AMA_winCoral = 0.8 ;      // 
	public final static Double proba_Turf_winCoral = 1.0 ;      // 
	public final static Double proba_ACA_winCoral = 0.4 ;      // 
	public final static Double proba_CCA_winCoral = 0.1 ;      // 

	public static Double proba_grazing_MA = 0.0 ;    // 
	public static Double proba_grazing_AMA = 0.0 ;
	public static Double proba_grazing_ACA = 0.0 ;
	public static Double proba_grazing_Halimeda = 0.0 ;
	public static Double proba_grazing_Turf = 1.0 ;     
	public static Double proba_grazing_CCA = 0.0 ;
	
	// Bleaching parameters: values from from Bleaching_logistic_response_DHW_Eakin_2010.R
	public final static Double interceptLogistBleachingResponse = -2.7792 ;    // see Table S 4 of Appendix 4
	public final static Double slopeLogistBleachingResponse = 0.2853346 ;    // 
	public final static Double minBleachingSusceptilityCaribbeanSp = 0.06009352 ;    //  See Appendix S4 - Implementation bleaching response.R Figure S14
	public final static Double maxBleachingSusceptilityCaribbeanSp = 0.5997697 ;    // 
	public final static Double meanBleachingSusceptilityCaribbeanSp = 0.2732041   ;    // 
	public static Double Bleaching_diff_response = 0.0  ;    // a parameter defined in the UI (phi in Appendix 4)
	
	public static Double GR_reduction_interaction = 0.0 ; 
	public static Double GR_reduction_bleachedCoral = 0.5 ; // other possible values: 0.33; 0.25
	public final static Double Fecundity_reduction_bleachedCoral = 0.0 ; // other possible values: [0,1]

	public static Double maxRadiusColonyInitialization = 0.0 ; // in CoralReef2Buidler: a colony cannot occupy more than 10 % of the reef 
	
    public static List<String> speciesNamesList = new ArrayList<String>() ;  // name of all the 12 coral species and 6 algae FG and sand, no matter if there are actually present in the reef (i.e., initial cover = 999 in cvs)
    public static List<String> algaePresentList = new ArrayList<String>() ;  // used for the algae invasion part
    public static List<String> coralSpeciesPresentList = new ArrayList<String>() ;  // used for coral larvae input from the regional pool (to control for the species present in the pool)
    
    public static List<FunctionalTraitData> functionalTraitsTable = new ArrayList<FunctionalTraitData>() ;
    
	public static List<Disturbances_priority> disturbancesPriorityList = new ArrayList<Disturbances_priority>();
	public static List<Sand_cover> sandCoverDataList = new ArrayList<Sand_cover>() ;
	public static List<Disturbance_cyclone> cycloneDMTDataList = new ArrayList<Disturbance_cyclone>() ;
	public static List<Disturbance_grazing> grazingDataList = new ArrayList<Disturbance_grazing>() ;
	public static List<Disturbance_bleaching> bleachingDHWDataList = new ArrayList<Disturbance_bleaching>() ;
	public static List<Disturbance_larvalConnectivity> larvalInputList = new ArrayList<Disturbance_larvalConnectivity>() ; // only used if connectivity == connectivityCSV

	
	public static List<BiodiversityData> dataBiodiversityList = new ArrayList<BiodiversityData>();
	public static List<BiodiversityDataBySpecies> dataBiodiversityBySpeciesList = new ArrayList<BiodiversityDataBySpecies>() ;
					
//    public static final double coefficientMorphologyBranching = 7.6 ;			
//    public static final double coefficientMorphologyTables_or_plates = 2.7 ;
//    public static final double coefficientMorphologyTables_or_plates_intercept = 180 ;
//    public static final double coefficientMorphologyCorymbose = 6.0 ; 
//    public static final double coefficientMorphologyLaminar = 3.04 ;     // ???
//    public static final double coefficientMorphologyDigitate = 5.00 ;
//    public static final double coefficientMorphologyColumnar = 5.5 ;    // ???
//    public static final double coefficientMorphologyMassive = 4.55 ;
//    public static final double coefficientMorphologyEncrusting_long_upright = 1.6 ;
//    public static final double coefficientMorphologyEncrusting = 1.2 ;
//    public static final double coefficientMorphologyMushroom = 1;
    
    // parameter values of the geometric models for each growth form (MacWilliam et al., 2018, Table S2)
    public static final double rb_tables_or_plates = 0.5 ;	 // radius branches (cm) 	
    public static final double rb_branching = 1 ;	 //  averaged between "simple" "complex" branching	
    public static final double rb_corymbose = 1 ;	 	
    public static final double rb_digitate = 2 ;		
    public static final double rb_columnar = 3 ;	
    public static final double rb_encrusting_long_upright = 0.5 ;			
    public static final double Nb_tables_or_plates = 2.5 ;	 // number of branches  	/ cm2
    public static final double Nb_branching = 0.225 ;	 //  averaged between "simple" "complex" branching	
    public static final double Nb_corymbose = 0.5 ;	 	
    public static final double Nb_digitate = 0.2 ;		
    public static final double Nb_columnar = 0.05 ;	
    public static final double Nb_encrusting_long_upright = 0.2 ;		
    public static final double hb_tables_or_plates = 1 ;	 // branch height  	(cm)
    public static final double hb_branching = 10 ;	 //  averaged between "simple" "complex" branching	
    public static final double hb_corymbose = 5 ;	 	
    public static final double hb_digitate = 5 ;	
    public static final double hb_laminar = 20 ;		
    public static final double hb_columnar = 25 ;	
    public static final double hb_encrusting_long_upright = 5 ;		
 
    public static final String growthFormBranching = "branching";
    public static final String growthFormTables_or_plates = "tables_or_plates";
    public static final String growthFormCorymbose = "corymbose";
    public static final String growthFormLaminar = "laminar";
    public static final String growthFormDigitate = "digitate";
    public static final String growthFormColumnar = "columnar";
    public static final String growthFormMassive = "massive";
    public static final String growthFormEncrusting_long_upright = "encrusting_long_uprights";
    public static final String growthFormEncrusting = "encrusting";
//    public static final String growthFormMushroom = "mushroom";
    
	public static double fertilizationRate = 0.5;   		// see ODD, (Oliver and Babcock, 1992)
	public static double predationRate = 0.3;			// see ODD, (Pratchett et al., 2001)
//	final double proportionLarvaeCompetent = 0.8;  		//  estimated from Connolly and Barid 2010 and Figueiredo et al., 2013
	public static double otherProportions = 0.0001 ;		// including non reproductive zone ("sterile zone") Hall and Hughes 1996, Harrison and Wallace 1990
    public static double retentionTime = 4.69 ; 			// in days, choices: 16.3, 10.24, 7.66, 6.97, 4.69, 2.14, 1.5, 1.21, 0.90, 0.7, in days, from (Figueiredo et al., 2013)	
	public static double proba_competency = 1.0 ; // 0.4 ;        // REMOVED see ODD, (average from Connolly and Baird, 2010)

    public static double proba_settle_CCA =  0.0 ;            // Value taken from GUI, default = 0.5
	public static double proba_settle_Barren_ground = 0.0 ;  // Value taken from GUI, default = 0.5
	public static double proba_settle_Dead_coral = 0.0 ;     // Value taken from GUI, default = 0.5
	
	public static double proba_Xmonth_recruitment = 0.0 ;        // base on Ritson-Williams et al., 2016, Appendix S2: 7.3.2
	
	public static double proportion_Male_polyps = 0.5 ;
	public static int numberLarvae_leaving_otherReef = 0 ; // 1st 0.5: 50 % coral cover in remote reef; 1000000 is the # larvae produced /m2 of coral cover (Hall and Hughes 1996, Pratchett et al., 2001)
	public static int numberLarvaeRemoteReef = 0 ;                     // number of larvae from remote reef reaching reef of focus
	public static double proportionLarvae_leaving_remoteReef = 0.5 ;
//	public static int numberLarvaeRegionalPool_HighConnectivity = 0 ;   // / m2
//	public static int numberLarvaeRegionalPool_MediumConnectivity = 0 ;   // / m2
//	public static int numberLarvaeRegionalPool_LowConnectivity = 0 ;   // / m2
//	public static int numberLarvaeRegionalPool_Isolated100Connectivity = 0 ;   // / m2
//	public static int numberLarvaeRegionalPool_Isolated200Connectivity = 0;   // / m2
	
	// Appendix2: Table S 7
	public static double proportion_larvae_from_otherReef_reachingFocalReef_HighConnectivity = 0.5 ;
	public static double proportion_larvae_from_otherReef_reachingFocalReef_MediumConnectivity = 0.1 ;
	public static double proportion_larvae_from_otherReef_reachingFocalReef_LowConnectivity = 0.01 ;
	public static double proportion_larvae_from_otherReef_reachingFocalReef_Isolated100km = 0.001 ;
	public static double proportion_larvae_from_otherReef_reachingFocalReef_Isolated200km = 0.0001 ;
	
	// Appendix2: Table S 8 , Dispersal_larvae_Connolly_Baird_2010.R
	public static double proportionCompetentAlive_larvae_from_otherReef_HighConnectivity = 0.40 ;      // from Connolly and Baird 2010
	public static double proportionCompetentAlive_larvae_from_otherReef_MediumConnectivity = 0.40 ;
	public static double proportionCompetentAlive_larvae_from_otherReef_LowConnectivity = 0.40 ;
	public static double proportionCompetentAlive_larvae_from_otherReef_Isolated100km = 0.38 ;
	public static double proportionCompetentAlive_larvae_from_otherReef_Isolated200km = 0.17 ;

    // Parameter of the model used are averaged from Alvarez-Noriega et al., 2016
	public static double intercept_polypMaturity =  8.626 ;
	public static double slope_polypMaturity =  1.682 ;
		
    public static final String spawner = "spawner";
    public static final String brooder = "brooder";
    public static final String both = "both" ;
    
    // trait names:
    public static final String aggressiveness = "aggressiveness";
    public static final String coloniality = "coloniality";
    public static final String colony_max_diameter = "colony_max_diameter";
    public static final String corallite_area = "coralitte_area";
    public static final String egg_diameter = "egg_diameter";
    public static final String fecundity_polyp = "fecundity_polyp";
    public static final String growth_form = "growth_form";
    public static final String growth_rate = "growth_rate"; 
    public static final String mode_larval_development = "mode_larval_development"; 
    public static final String reduced_scattering_coefficient = "reduced_scattering_coefficient";
    public static final String response_bleaching_index = "response_bleaching_index";
    public static final String skeletal_density = "skeletal_density";
    public static final String size_maturity = "size_maturity"; 
    public static final String age_maturity = "age_maturity"; 
    public static final String red = "red" ;
    public static final String green = "green" ;
    public static final String blue = "blue" ;
    public static final String bleaching_probability = "bleaching_probability" ;
//    public static final String x0_logitBleaching = "x0_logitBleaching" ;
//    public static final String r_logitBleaching = "r_logitBleaching" ;
    public static String sexual_system = "sexual_system" ;
    public static final String correction_coeff_polypFecundity = "correction_coeff_polypFecundity" ;

	public static String SUBSTRATE_CATEGORY_BARREN_GROUND = "temp";
	public static String SUBSTRATE_CATEGORY_ALGAE = "temp";						// that wil need to be changed, and specifyed for each type of algae
	public static String SUBSTRATE_CATEGORY_CORAL = "temp";
		
	public static String SUBSTRATE_SUBCATEGORY_BARREN_GROUND = "temp";
	public static String SUBSTRATE_SUBCATEGORY_MACROALGAE = "temp";						// that wil need to be changed, and specifyed for each type of algae
	public static String SUBSTRATE_SUBCATEGORY_AMA = "temp";							// hallelopathic MA
	public static String SUBSTRATE_SUBCATEGORY_HALIMEDA = "temp";						// less aggressive MA
	public static String SUBSTRATE_SUBCATEGORY_TURF = "temp";
	public static String SUBSTRATE_SUBCATEGORY_CCA = "temp";
	public static String SUBSTRATE_SUBCATEGORY_ACA = "temp";							// articulated coralline algae
	public static String SUBSTRATE_SUBCATEGORY_LIVE_CORAL = "temp";
	public static String SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL = "temp";
	public static String SUBSTRATE_SUBCATEGORY_DEAD_CORAL = "temp";
	
	public static final int numberSpeciesCoral = 12 ;               // make a methid later that determines the number of species from the the csv file
	
	public static String SPECIES_BARREN_GROUND = "temp";			// sand is like barrenground except that nothing can grow on it (see Agent.conversionToSand())
	public static String SPECIES_SAND = "sand";
	public static String SPECIES_MACROALGAE = "temp";				// algae don't have species names because of bleaching procedure
	public static String SPECIES_TURF = "temp";
	public static String SPECIES_CCA = "temp";
	public static String SPECIES_ACA = "temp";
	public static String SPECIES_HALIMEDA = "temp";
	public static String SPECIES_AMA = "temp";
	public static String SPECIES_CORAL_SP1 = "temp";
	public static String SPECIES_CORAL_SP2 = "temp";
	public static String SPECIES_CORAL_SP3 = "temp";
	public static String SPECIES_CORAL_SP4 = "temp";
	public static String SPECIES_CORAL_SP5 = "temp";
	public static String SPECIES_CORAL_SP6 = "temp";
	public static String SPECIES_CORAL_SP7 = "temp";
	public static String SPECIES_CORAL_SP8 = "temp";
	public static String SPECIES_CORAL_SP9 = "temp";
	public static String SPECIES_CORAL_SP10 = "temp";
	public static String SPECIES_CORAL_SP11 = "temp";
	public static String SPECIES_CORAL_SP12 = "temp";
	
	public static double BARREN_GROUND_AGGRESSIVENESS = 0;
	public static double MACROALGAE_AGGRESSIVENESS = 0;
	public static double TURF_AGGRESSIVENESS = 0;
	public static double CCA_AGGRESSIVENESS = 0;
	public static double ACA_AGGRESSIVENESS = 0;
	public static double HALIMEDA_AGGRESSIVENESS = 0;
	public static double AMA_AGGRESSIVENESS = 0;
	public static double DEAD_CORAL_AGGRESSIVENESS = 0;
	public static double BLEACHED_CORAL_AGGRESSIVENESS = 0;		
	public static double CORAL_Sp1_AGGRESSIVENESS = 0;
	public static double CORAL_Sp2_AGGRESSIVENESS = 0;
	public static double CORAL_Sp3_AGGRESSIVENESS = 0;
	public static double CORAL_Sp4_AGGRESSIVENESS = 0;
	public static double CORAL_Sp5_AGGRESSIVENESS = 0;
	public static double CORAL_Sp6_AGGRESSIVENESS = 0;
	public static double CORAL_Sp7_AGGRESSIVENESS = 0;
	public static double CORAL_Sp8_AGGRESSIVENESS = 0;
	public static double CORAL_Sp9_AGGRESSIVENESS = 0;
	public static double CORAL_Sp10_AGGRESSIVENESS = 0;
	public static double CORAL_Sp11_AGGRESSIVENESS = 0;
	public static double CORAL_Sp12_AGGRESSIVENESS = 0;
	
	public static String BARREN_GROUND_COLONIALITY = "None";
	public static String MACROALGAE_COLONIALITY = "None";		
	public static String TURF_COLONIALITY = "None";
	public static String CCA_COLONIALITY = "None";	
	public static String ACA_COLONIALITY = "temp";
	public static String HALIMEDA_COLONIALITY = "temp";
	public static String AMA_COLONIALITY = "temp";
	public static String CORAL_Sp1_COLONIALITY = "temp";		
	public static String CORAL_Sp2_COLONIALITY = "temp";	
	public static String CORAL_Sp3_COLONIALITY = "temp";		
	public static String CORAL_Sp4_COLONIALITY = "temp";	
	public static String CORAL_Sp5_COLONIALITY = "temp";		
	public static String CORAL_Sp6_COLONIALITY = "temp";	
	public static String CORAL_Sp7_COLONIALITY = "temp";		
	public static String CORAL_Sp8_COLONIALITY = "temp";	
	public static String CORAL_Sp9_COLONIALITY = "temp";		
	public static String CORAL_Sp10_COLONIALITY = "temp";	
	public static String CORAL_Sp11_COLONIALITY = "temp";		
	public static String CORAL_Sp12_COLONIALITY = "temp";	
	
	public static double BARREN_GROUND_COLONY_MAX_DIAMETER = 0;
	public static double MACROALGAE_COLONY_MAX_DIAMETER = 0;				
	public static double TURF_COLONY_MAX_DIAMETER = 0;	
	public static double CCA_COLONY_MAX_DIAMETER = 0;	
	public static double ACA_COLONY_MAX_DIAMETER = 0;
	public static double HALIMEDA_COLONY_MAX_DIAMETER = 0;
	public static double AMA_COLONY_MAX_DIAMETER = 0;
//	public static double DEAD_CORAL_COLONY_MAX_DIAMETER = 0;
//	public static double BLEACHED_CORAL_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp1_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp2_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp3_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp4_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp5_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp6_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp7_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp8_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp9_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp10_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp11_COLONY_MAX_DIAMETER = 0;
	public static double CORAL_Sp12_COLONY_MAX_DIAMETER = 0;
	public static double MAX_RADIUS_COLONY_SIZE_ALL_SPECIES = 0;	// ??????!!!!!!
	
	public static double BARREN_GROUND_CORALLITE_AREA = 0;
	public static double MACROALGAE_CORALLITE_AREA = 0;
	public static double TURF_CORALLITE_AREA = 0;
	public static double CCA_CORALLITE_AREA = 0;
	public static double ACA_CORALLITE_AREA = 0;
	public static double HALIMEDA_CORALLITE_AREA = 0;
	public static double AMA_CORALLITE_AREA = 0;
//	public static double DEAD_CORAL_CORALLITE_AREA = 0;
//	public static double BLEACHED_CORAL_CORALLITE_AREA = 0;		
	public static double CORAL_Sp1_CORALLITE_AREA = 0;
	public static double CORAL_Sp2_CORALLITE_AREA = 0;
	public static double CORAL_Sp3_CORALLITE_AREA = 0;
	public static double CORAL_Sp4_CORALLITE_AREA = 0;
	public static double CORAL_Sp5_CORALLITE_AREA = 0;
	public static double CORAL_Sp6_CORALLITE_AREA = 0;
	public static double CORAL_Sp7_CORALLITE_AREA = 0;
	public static double CORAL_Sp8_CORALLITE_AREA = 0;
	public static double CORAL_Sp9_CORALLITE_AREA = 0;
	public static double CORAL_Sp10_CORALLITE_AREA = 0;
	public static double CORAL_Sp11_CORALLITE_AREA = 0;
	public static double CORAL_Sp12_CORALLITE_AREA = 0;
	
	public static double BARREN_GROUND_EGG_DIAMETER = 0;
	public static double MACROALGAE_EGG_DIAMETER = 0;
	public static double TURF_EGG_DIAMETER = 0;
	public static double CCA_EGG_DIAMETER = 0;
	public static double ACA_EGG_DIAMETER = 0;
	public static double HALIMEDA_EGG_DIAMETER = 0;
	public static double AMA_EGG_DIAMETER = 0;
	public static double CORAL_Sp1_EGG_DIAMETER = 0;
	public static double CORAL_Sp2_EGG_DIAMETER = 0;
	public static double CORAL_Sp3_EGG_DIAMETER = 0;
	public static double CORAL_Sp4_EGG_DIAMETER = 0;
	public static double CORAL_Sp5_EGG_DIAMETER = 0;
	public static double CORAL_Sp6_EGG_DIAMETER = 0;
	public static double CORAL_Sp7_EGG_DIAMETER = 0;
	public static double CORAL_Sp8_EGG_DIAMETER = 0;
	public static double CORAL_Sp9_EGG_DIAMETER = 0;
	public static double CORAL_Sp10_EGG_DIAMETER = 0;
	public static double CORAL_Sp11_EGG_DIAMETER = 0;
	public static double CORAL_Sp12_EGG_DIAMETER = 0;
	
	public static double BARREN_GROUND_FECUNDITY_POLYP = 0;
	public static double MACROALGAE_FECUNDITY_POLYP = 0;
	public static double TURF_FECUNDITY_POLYP = 0;
	public static double CCA_FECUNDITY_POLYP = 0;
	public static double ACA_FECUNDITY_POLYP = 0;
	public static double HALIMEDA_FECUNDITY_POLYP = 0;
	public static double AMA_FECUNDITY_POLYP = 0;
	public static double DEAD_CORAL_FECUNDITY_POLYP = 0;
	public static double BLEACHED_CORAL_FECUNDITY_POLYP = 0;		
	public static double CORAL_Sp1_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp2_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp3_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp4_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp5_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp6_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp7_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp8_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp9_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp10_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp11_FECUNDITY_POLYP = 0;
	public static double CORAL_Sp12_FECUNDITY_POLYP = 0;
	
	public static String BARREN_GROUND_GROWTH_FORM = "None";
	public static String MACROALGAE_GROWTH_FORM = "None";		
	public static String TURF_GROWTH_FORM = "None";
	public static String CCA_GROWTH_FORM = "None";	
	public static String ACA_GROWTH_FORM = "temp";
	public static String HALIMEDA_GROWTH_FORM = "temp";
	public static String AMA_GROWTH_FORM = "temp";
	public static String CORAL_Sp1_GROWTH_FORM = "temp";		
	public static String CORAL_Sp2_GROWTH_FORM = "temp";	
	public static String CORAL_Sp3_GROWTH_FORM = "temp";		
	public static String CORAL_Sp4_GROWTH_FORM = "temp";	
	public static String CORAL_Sp5_GROWTH_FORM = "temp";		
	public static String CORAL_Sp6_GROWTH_FORM = "temp";	
	public static String CORAL_Sp7_GROWTH_FORM = "temp";		
	public static String CORAL_Sp8_GROWTH_FORM = "temp";	
	public static String CORAL_Sp9_GROWTH_FORM = "temp";		
	public static String CORAL_Sp10_GROWTH_FORM = "temp";	
	public static String CORAL_Sp11_GROWTH_FORM = "temp";		
	public static String CORAL_Sp12_GROWTH_FORM = "temp";	
	
	public static double BARREN_GROUND_GROWTH_RATE = 0;
	public static double MACROALGAE_GROWTH_RATE = 0;		
	public static double TURF_GROWTH_RATE = 0;
	public static double CCA_GROWTH_RATE = 0;
	public static double ACA_GROWTH_RATE = 0;
	public static double HALIMEDA_GROWTH_RATE = 0;
	public static double AMA_GROWTH_RATE = 0;
	public static double DEAD_CORAL_GROWTH_RATE = 0;
	public static double BLEACHED_CORAL_GROWTH_RATE = 0;	
	public static double CORAL_Sp1_GROWTH_RATE = 0;		
	public static double CORAL_Sp2_GROWTH_RATE = 0;	
    public static double CORAL_Sp3_GROWTH_RATE = 0;		
	public static double CORAL_Sp4_GROWTH_RATE = 0;		
	public static double CORAL_Sp5_GROWTH_RATE = 0;		
	public static double CORAL_Sp6_GROWTH_RATE = 0;		
	public static double CORAL_Sp7_GROWTH_RATE = 0;		
	public static double CORAL_Sp8_GROWTH_RATE = 0;		
	public static double CORAL_Sp9_GROWTH_RATE = 0;		
	public static double CORAL_Sp10_GROWTH_RATE = 0;		
	public static double CORAL_Sp11_GROWTH_RATE = 0;		
	public static double CORAL_Sp12_GROWTH_RATE = 0;
	
	public static String BARREN_GROUND_MODE_LARVAL_DEVELOPMENT = "None";
	public static String MACROALGAE_MODE_LARVAL_DEVELOPMENT = "None";			
	public static String TURF_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CCA_MODE_LARVAL_DEVELOPMENT = "None";
	public static String ACA_MODE_LARVAL_DEVELOPMENT = "temp";
	public static String HALIMEDA_MODE_LARVAL_DEVELOPMENT = "temp";
	public static String AMA_MODE_LARVAL_DEVELOPMENT = "temp";
	public static String CORAL_Sp1_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp2_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp3_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp4_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp5_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp6_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp7_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp8_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp9_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp10_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp11_MODE_LARVAL_DEVELOPMENT = "None";
	public static String CORAL_Sp12_MODE_LARVAL_DEVELOPMENT = "None";
	
	public static double BARREN_GROUND_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double MACROALGAE_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double TURF_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CCA_REDUCED_SCATTERING_COEFFICIENT  = 0;
	public static double ACA_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double HALIMEDA_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double AMA_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp1_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp2_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp3_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp4_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp5_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp6_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp7_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp8_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp9_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp10_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp11_REDUCED_SCATTERING_COEFFICIENT = 0;
	public static double CORAL_Sp12_REDUCED_SCATTERING_COEFFICIENT = 0;

	public static String BARREN_GROUND_SEXUAL_SYSTEM = "None" ;
	public static String ALGAE_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp1_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp2_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp3_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp4_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp5_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp6_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp7_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp8_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp9_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp10_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp11_SEXUAL_SYSTEM = "None" ;
	public static String CORAL_Sp12_SEXUAL_SYSTEM = "None" ;

	public static double BARREN_GROUND_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp1_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp2_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp3_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp4_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp5_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp6_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp7_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp8_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp9_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp10_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp11_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;
	public static double CORAL_Sp12_CORRECTION_COEFF_FECUNDITY_POLYP = 0.0 ;

	/**
	 * Reset the differents list because the "Reset run" buttton in the GUI does not do it (the bacth mode does it).
	 * Other option: use .clear() instead of creating a new list but apprently it does not matter much:
	 * https://stackoverflow.com/questions/6961356/list-clear-vs-list-new-arraylistinteger
	 */
	public static void resetLists() {
		ContextCoralReef2.listAllAgents = new ArrayList<Agent>();
		ContextCoralReef2.listAgentToCheckSizeColony = new ArrayList<Agent>() ;
		
		CoralReef2Builder.colonyPlanarAreaDistList =  new ArrayList<ColonyPlanarAreaDistribution>() ;
		CoralReef2Builder.colonyPlanarAreaDistListCopy =  new ArrayList<ColonyPlanarAreaDistribution>() ;

		CoralReef2Builder.percentageCover_updated_sink = new ArrayList<PercentageCover>() ;
		CoralReef2Builder.numberRecruits_updated_list = new ArrayList<NumberCoralRecruits>() ;
		CoralReef2Builder.colonyDiameterDistributionSkewBiasList = new ArrayList<ColonydiameterDistributionSkewBias>() ;
		CoralReef2Builder.rugosityCoverGrazedList = new ArrayList<RugosityCoverGrazed>() ;
		
		speciesNamesList = new ArrayList<String>() ;  // name of all the 12 coral species and 6 algae FG and sand, no matter if there are actually present in the reef (i.e., initial cover = 999 in cvs)
		algaePresentList = new ArrayList<String>() ;  // used for the algae invasion part
		coralSpeciesPresentList = new ArrayList<String>() ;  // used for coral larvae input from the regional pool (to control for the species present in the pool)
		functionalTraitsTable = new ArrayList<FunctionalTraitData>() ;
		disturbancesPriorityList = new ArrayList<Disturbances_priority>();
		sandCoverDataList = new ArrayList<Sand_cover>() ;
		cycloneDMTDataList = new ArrayList<Disturbance_cyclone>() ;
		grazingDataList = new ArrayList<Disturbance_grazing>() ;
		bleachingDHWDataList = new ArrayList<Disturbance_bleaching>() ;
		dataBiodiversityList = new ArrayList<BiodiversityData>();
		dataBiodiversityBySpeciesList = new ArrayList<BiodiversityDataBySpecies>() ;
		
		larvalInputList = new ArrayList<Disturbance_larvalConnectivity>() ;
		
		ContextCoralReef2.year_event = 0.0 ;
		
		System.runFinalization() ; // to suggest the GC to free some memory
		
	}
	
//	public static double BARREN_GROUND_RESPONSE_BLEACHING_INDEX = 0;
//	public static double MACROALGAE_RESPONSE_BLEACHING_INDEX = 0;
//	public static double TURF_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CCA_RESPONSE_BLEACHING_INDEX  = 0;
//	public static double ACA_RESPONSE_BLEACHING_INDEX =0;
//	public static double HALIMEDA_RESPONSE_BLEACHING_INDEX = 0;
//	public static double AMA_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp1_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp2_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp3_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp4_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp5_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp6_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp7_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp8_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp9_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp10_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp11_RESPONSE_BLEACHING_INDEX = 0;
//	public static double CORAL_Sp12_RESPONSE_BLEACHING_INDEX = 0;
	
//	public static double BARREN_GROUND_SKELETAL_DENSITY = 0;
//	public static double MACROALGAE_SKELETAL_DENSITY = 0;
//	public static double TURF_SKELETAL_DENSITY = 0;
//	public static double CCA_SKELETAL_DENSITY  = 0;
//	public static double ACA_SKELETAL_DENSITY = 0;
//	public static double HALIMEDA_SKELETAL_DENSITY  = 0;
//	public static double AMA_SKELETAL_DENSITY  = 0;
//	public static double CORAL_Sp1_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp2_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp3_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp4_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp5_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp6_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp7_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp8_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp9_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp10_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp11_SKELETAL_DENSITY = 0;
//	public static double CORAL_Sp12_SKELETAL_DENSITY = 0;

	public static double BARREN_GROUND_SIZE_MATURITY = 0;
	public static double MACROALGAE_SIZE_MATURITY = 0;
	public static double TURF_SIZE_MATURITY = 0;
	public static double CCA_SIZE_MATURITY  = 0;
	public static double ACA_SIZE_MATURITY = 0;
	public static double HALIMEDA_SIZE_MATURITY  = 0;
	public static double AMA_SIZE_MATURITY  = 0;
	public static double CORAL_Sp1_SIZE_MATURITY = 0;
	public static double CORAL_Sp2_SIZE_MATURITY = 0;
	public static double CORAL_Sp3_SIZE_MATURITY = 0;
	public static double CORAL_Sp4_SIZE_MATURITY = 0;
	public static double CORAL_Sp5_SIZE_MATURITY = 0;
	public static double CORAL_Sp6_SIZE_MATURITY = 0;
	public static double CORAL_Sp7_SIZE_MATURITY = 0;
	public static double CORAL_Sp8_SIZE_MATURITY = 0;
	public static double CORAL_Sp9_SIZE_MATURITY = 0;
	public static double CORAL_Sp10_SIZE_MATURITY = 0;
	public static double CORAL_Sp11_SIZE_MATURITY = 0;
	public static double CORAL_Sp12_SIZE_MATURITY = 0;
	
	public static double BARREN_GROUND_AGE_MATURITY = 0;
	public static double MACROALGAE_AGE_MATURITY = 0;
	public static double TURF_AGE_MATURITY = 0;
	public static double CCA_AGE_MATURITY  = 0;
	public static double ACA_AGE_MATURITY = 0;
	public static double HALIMEDA_AGE_MATURITY  = 0;
	public static double AMA_AGE_MATURITY  = 0;
	public static double CORAL_Sp1_AGE_MATURITY = 0;
	public static double CORAL_Sp2_AGE_MATURITY = 0;
	public static double CORAL_Sp3_AGE_MATURITY = 0;
	public static double CORAL_Sp4_AGE_MATURITY = 0;
	public static double CORAL_Sp5_AGE_MATURITY = 0;
	public static double CORAL_Sp6_AGE_MATURITY = 0;
	public static double CORAL_Sp7_AGE_MATURITY = 0;
	public static double CORAL_Sp8_AGE_MATURITY = 0;
	public static double CORAL_Sp9_AGE_MATURITY = 0;
	public static double CORAL_Sp10_AGE_MATURITY = 0;
	public static double CORAL_Sp11_AGE_MATURITY = 0;
	public static double CORAL_Sp12_AGE_MATURITY = 0;
	
	public static double BARREN_GROUND_BLEACHING_PROBABILITY = 0;
	public static double MACROALGAE_BLEACHING_PROBABILITY = 0;
	public static double TURF_BLEACHING_PROBABILITY = 0;
	public static double CCA_BLEACHING_PROBABILITY  = 0;
	public static double ACA_BLEACHING_PROBABILITY = 0;
	public static double HALIMEDA_BLEACHING_PROBABILITY  = 0;
	public static double AMA_BLEACHING_PROBABILITY  = 0;
	public static double CORAL_Sp1_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp2_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp3_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp4_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp5_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp6_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp7_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp8_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp9_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp10_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp11_BLEACHING_PROBABILITY = 0;
	public static double CORAL_Sp12_BLEACHING_PROBABILITY = 0;
	
//	public static double BARREN_GROUND_R_LOGIT_BLEACHING = 0;
//	public static double MACROALGAE_R_LOGIT_BLEACHING = 0;
//	public static double TURF_R_LOGIT_BLEACHING = 0;
//	public static double CCA_R_LOGIT_BLEACHING  = 0;
//	public static double ACA_R_LOGIT_BLEACHING = 0;
//	public static double HALIMEDA_R_LOGIT_BLEACHING  = 0;
//	public static double AMA_R_LOGIT_BLEACHING  = 0;
//	public static double CORAL_Sp1_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp2_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp3_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp4_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp5_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp6_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp7_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp8_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp9_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp10_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp11_R_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp12_R_LOGIT_BLEACHING = 0;
	
//	public static double BARREN_GROUND_X0_LOGIT_BLEACHING = 0;
//	public static double MACROALGAE_X0_LOGIT_BLEACHING = 0;
//	public static double TURF_X0_LOGIT_BLEACHING = 0;
//	public static double CCA_X0_LOGIT_BLEACHING  = 0;
//	public static double ACA_X0_LOGIT_BLEACHING = 0;
//	public static double HALIMEDA_X0_LOGIT_BLEACHING  = 0;
//	public static double AMA_X0_LOGIT_BLEACHING  = 0;
//	public static double CORAL_Sp1_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp2_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp3_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp4_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp5_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp6_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp7_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp8_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp9_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp10_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp11_X0_LOGIT_BLEACHING = 0;
//	public static double CORAL_Sp12_X0_LOGIT_BLEACHING = 0;
	
	public static double NON_CORAL_MAX_SIZE = width * height; 			// ??????!!!!!!

//	public static int infoDuRun;


	/** CHECKED
	 * Import the functional trait data from csv file
	 * functionalTraitsTable is a List<FunctionalTraitData>, each object corresponds to a row in the csv file 
	 */
	public static void importFunctionalTraitTable(){
		functionalTraitsTable = SMUtils.readFunctionalTraitDataFile(Constants.FUNCTIONAL_TRAIT_DATAFILE);
//		int i = 0 ;
//		for(FunctionalTraitData FTD : functionalTraitsTable){       // CHECKED
//			i++ ;
//			System.out.printf(" %s, %s, %s, %.1f, %s, %.1f, %.1f, %.1f, %.1f, %s, %.1f, %s, %.1f, %.1f, %.1f, %d, %d, %d  \n",
//					FTD.getSubstrateCategory(),
//					FTD.getSubstrateSubCategory(),
//					FTD.getSpecies(),
//					FTD.getAggressiveness(),
//					FTD.getColoniality(),
//					FTD.getColonyMaxDiameter(),
//					FTD.getCoralliteArea(),
//					FTD.getEggDiameter(),
//					FTD.getFecundityPolyp(),
//					FTD.getGrowthForm(),
//					FTD.getGrowthRate(),
//					FTD.getModeLarvalDevelopment(),
//					FTD.getReducedScatteringCoefficient(),
//					FTD.getSizeMaturity(),
//					FTD.getAgeMaturity(),
//					FTD.getRed(),
//					FTD.getGreen(),
//					FTD.getBlue()
//					);
//			if(i > 20){
//				break ;
//			}
//		}
	}
	
	/**
	 * methods that determines the functional trait values based on the datafile functionalTraitDF_model.csv
	 * These values are updated in the class Constants, where the constants are.
	 */
	public static void setFunctionalTraitFromFile(){
			
		for (FunctionalTraitData FTD : functionalTraitsTable){	
			
			if (FTD.getSpecies().equals("BarrenGround")){
				SUBSTRATE_CATEGORY_BARREN_GROUND = FTD.getSubstrateCategory();
				SUBSTRATE_SUBCATEGORY_BARREN_GROUND = FTD.getSubstrateSubCategory();
				SPECIES_BARREN_GROUND = FTD.getSpecies();
				BARREN_GROUND_AGGRESSIVENESS = FTD.getAggressiveness();
				BARREN_GROUND_COLONIALITY = FTD.getColoniality();
				BARREN_GROUND_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				BARREN_GROUND_CORALLITE_AREA = FTD.getCoralliteArea();
				BARREN_GROUND_EGG_DIAMETER = FTD.getEggDiameter();
				BARREN_GROUND_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				BARREN_GROUND_GROWTH_FORM = FTD.getGrowthForm();
				BARREN_GROUND_GROWTH_RATE = FTD.getGrowthRate();
				BARREN_GROUND_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				BARREN_GROUND_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				BARREN_GROUND_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				BARREN_GROUND_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				BARREN_GROUND_SIZE_MATURITY = FTD.getSizeMaturity();
				BARREN_GROUND_AGE_MATURITY = FTD.getAgeMaturity();
				BARREN_GROUND_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
//				BARREN_GROUND_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//				BARREN_GROUND_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
				BARREN_GROUND_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				BARREN_GROUND_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;

			}else if (FTD.getSpecies().equals(SPECIES_MACROALGAE)){
				SUBSTRATE_CATEGORY_ALGAE = FTD.getSubstrateCategory();
				SUBSTRATE_SUBCATEGORY_MACROALGAE = FTD.getSubstrateSubCategory();
				MACROALGAE_AGGRESSIVENESS = proba_MA_winCoral ;                   // aggressiveness for algae corresponds to the probability to win a fight with a coral agent
				MACROALGAE_COLONIALITY = FTD.getColoniality();
				MACROALGAE_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();	
				MACROALGAE_CORALLITE_AREA = FTD.getCoralliteArea();
				MACROALGAE_EGG_DIAMETER = FTD.getEggDiameter();
				MACROALGAE_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				MACROALGAE_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){             // check growthRate_randomRadiusConversion.csv
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				MACROALGAE_GROWTH_RATE = growthRate;	
				MACROALGAE_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				MACROALGAE_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				MACROALGAE_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				MACROALGAE_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				MACROALGAE_SIZE_MATURITY = FTD.getSizeMaturity();
				MACROALGAE_AGE_MATURITY = FTD.getAgeMaturity();
				MACROALGAE_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
//				MACROALGAE_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//				MACROALGAE_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
				ALGAE_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
				
			}else if (FTD.getSpecies().equals(SPECIES_TURF)){
				SUBSTRATE_SUBCATEGORY_TURF = FTD.getSubstrateSubCategory();
				TURF_AGGRESSIVENESS = proba_Turf_winCoral ;                   // aggressiveness for algae corresponds to the probability to win a fight with a coral agent
				TURF_COLONIALITY = FTD.getColoniality();
				TURF_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				TURF_CORALLITE_AREA = FTD.getCoralliteArea();
				TURF_EGG_DIAMETER = FTD.getEggDiameter();
				TURF_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				TURF_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				TURF_GROWTH_RATE = growthRate ;
				TURF_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				TURF_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				TURF_SKELETAL_DENSITY = FTD.getSkeletalDensity();
//				TURF_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
				TURF_SIZE_MATURITY = FTD.getSizeMaturity();
				TURF_AGE_MATURITY = FTD.getAgeMaturity();
				TURF_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
//				TURF_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//				TURF_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
				ALGAE_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
				
			}else if (FTD.getSpecies().equals(SPECIES_CCA)){
				SUBSTRATE_SUBCATEGORY_CCA = FTD.getSubstrateSubCategory();
				CCA_AGGRESSIVENESS = proba_CCA_winCoral ;                   // aggressiveness for algae corresponds to the probability to win a fight with a coral agent
				CCA_COLONIALITY = FTD.getColoniality();
				CCA_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CCA_CORALLITE_AREA = FTD.getCoralliteArea();
				CCA_EGG_DIAMETER = FTD.getEggDiameter();
				CCA_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CCA_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CCA_GROWTH_RATE = growthRate ;
				CCA_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CCA_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CCA_SKELETAL_DENSITY = FTD.getSkeletalDensity();
//				CCA_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
				CCA_SIZE_MATURITY = FTD.getSizeMaturity();
				CCA_AGE_MATURITY = FTD.getAgeMaturity();
				CCA_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
//				CCA_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//				CCA_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
				ALGAE_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
				
			}else if(FTD.getSpecies().equals(SPECIES_ACA)){
				SUBSTRATE_SUBCATEGORY_ACA = FTD.getSubstrateSubCategory();
				SPECIES_ACA = FTD.getSpecies() ;
				ACA_AGGRESSIVENESS = proba_ACA_winCoral ;                   // aggressiveness for algae corresponds to the probability to win a fight with a coral agent
				ACA_COLONIALITY = FTD.getColoniality();
				ACA_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				ACA_CORALLITE_AREA = FTD.getCoralliteArea();
				ACA_EGG_DIAMETER = FTD.getEggDiameter();
				ACA_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				ACA_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				ACA_GROWTH_RATE = growthRate ;
				ACA_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				ACA_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				ACA_SKELETAL_DENSITY = FTD.getSkeletalDensity();
//				ACA_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
				ACA_SIZE_MATURITY = FTD.getSizeMaturity();
				ACA_AGE_MATURITY = FTD.getAgeMaturity();
				ACA_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
//				ACA_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//				ACA_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
				ALGAE_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
				
			}else if(FTD.getSpecies().equals(SPECIES_HALIMEDA)){
				SUBSTRATE_SUBCATEGORY_HALIMEDA = FTD.getSubstrateSubCategory();
				SPECIES_HALIMEDA = FTD.getSpecies();
				HALIMEDA_AGGRESSIVENESS = proba_Halimeda_winCoral ;                   // aggressiveness for algae corresponds to the probability to win a fight with a coral agent
				HALIMEDA_COLONIALITY = FTD.getColoniality();
				HALIMEDA_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				HALIMEDA_CORALLITE_AREA = FTD.getCoralliteArea();
				HALIMEDA_EGG_DIAMETER = FTD.getEggDiameter();
				HALIMEDA_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				HALIMEDA_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				HALIMEDA_GROWTH_RATE = growthRate ;
				HALIMEDA_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				HALIMEDA_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				HALIMEDA_SKELETAL_DENSITY = FTD.getSkeletalDensity();
//				HALIMEDA_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
				HALIMEDA_SIZE_MATURITY = FTD.getSizeMaturity();
				HALIMEDA_AGE_MATURITY = FTD.getAgeMaturity();
				HALIMEDA_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
//				HALIMEDA_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//				HALIMEDA_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
				ALGAE_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
				
			}else if(FTD.getSpecies().equals(SPECIES_AMA)){
				SUBSTRATE_SUBCATEGORY_AMA = FTD.getSubstrateSubCategory();
				SPECIES_AMA = FTD.getSpecies();
				AMA_AGGRESSIVENESS = proba_AMA_winCoral ;                   // aggressiveness for algae corresponds to the probability to win a fight with a coral agent
				AMA_COLONIALITY = FTD.getColoniality();
				AMA_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				AMA_CORALLITE_AREA = FTD.getCoralliteArea();
				AMA_EGG_DIAMETER = FTD.getEggDiameter();
				AMA_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				AMA_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				AMA_GROWTH_RATE = growthRate ;
				AMA_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				AMA_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				AMA_SKELETAL_DENSITY = FTD.getSkeletalDensity();
//				AMA_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
				AMA_SIZE_MATURITY = FTD.getSizeMaturity();
				AMA_AGE_MATURITY = FTD.getAgeMaturity();
				AMA_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
//				AMA_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//				AMA_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
				ALGAE_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				ALGAE_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
				
			}else if (FTD.getSubstrateSubCategory().equals("DeadCoral")){
				SUBSTRATE_CATEGORY_CORAL = FTD.getSubstrateCategory();
				SUBSTRATE_SUBCATEGORY_DEAD_CORAL = FTD.getSubstrateSubCategory();
				DEAD_CORAL_AGGRESSIVENESS = FTD.getAggressiveness();
				DEAD_CORAL_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				DEAD_CORAL_GROWTH_RATE = FTD.getGrowthRate();
				
			}else if (FTD.getSubstrateSubCategory().equals("BleachedCoral")){
				SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL = FTD.getSubstrateSubCategory();
				BLEACHED_CORAL_AGGRESSIVENESS = FTD.getAggressiveness();
				BLEACHED_CORAL_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				BLEACHED_CORAL_GROWTH_RATE = FTD.getGrowthRate();

			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP1)){										// get a list of name of coral species 
				SUBSTRATE_SUBCATEGORY_LIVE_CORAL = FTD.getSubstrateSubCategory();
				CORAL_Sp1_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp1_COLONIALITY = FTD.getColoniality();
				CORAL_Sp1_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp1_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp1_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp1_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp1_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp1_GROWTH_RATE = growthRate;
				CORAL_Sp1_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp1_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp1_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp1_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp1_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp1_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp1_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp1_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp1_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp1_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp1_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp1_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp1_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP2)){				
				CORAL_Sp2_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp2_COLONIALITY = FTD.getColoniality();
				CORAL_Sp2_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp2_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp2_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp2_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp2_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp2_GROWTH_RATE = growthRate;
				CORAL_Sp2_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp2_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp2_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp2_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp2_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp2_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp2_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp2_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp2_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp2_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp2_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp2_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp2_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP3)){
				CORAL_Sp3_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp3_COLONIALITY = FTD.getColoniality();
				CORAL_Sp3_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp3_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp3_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp3_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp3_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp3_GROWTH_RATE = growthRate;
				CORAL_Sp3_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp3_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp3_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp3_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp3_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp3_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp3_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp3_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp3_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp3_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp3_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp3_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp3_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP4)){
				CORAL_Sp4_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp4_COLONIALITY = FTD.getColoniality();
				CORAL_Sp4_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp4_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp4_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp4_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp4_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp4_GROWTH_RATE = growthRate;
				CORAL_Sp4_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp4_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp4_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp4_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp4_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp4_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp4_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp4_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp4_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp4_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp4_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp4_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp4_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP5)){
				CORAL_Sp5_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp5_COLONIALITY = FTD.getColoniality();
				CORAL_Sp5_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp5_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp5_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp5_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp5_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp5_GROWTH_RATE = growthRate;
				CORAL_Sp5_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp5_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp5_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp5_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp5_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp5_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp5_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp5_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp5_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp5_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp5_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp5_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp5_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP6)){
				CORAL_Sp6_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp6_COLONIALITY = FTD.getColoniality();
				CORAL_Sp6_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp6_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp6_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp6_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp6_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp6_GROWTH_RATE = growthRate;
				CORAL_Sp6_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp6_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp6_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp6_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp6_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp6_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp6_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp6_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp6_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp6_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp6_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp6_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp6_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP7)){
				CORAL_Sp7_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp7_COLONIALITY = FTD.getColoniality();
				CORAL_Sp7_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp7_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp7_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp7_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp7_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp7_GROWTH_RATE = growthRate;
				CORAL_Sp7_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp7_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp7_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp7_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp7_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp7_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp7_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp7_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp7_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp7_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp7_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp7_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp7_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP8)){
				CORAL_Sp8_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp8_COLONIALITY = FTD.getColoniality();
				CORAL_Sp8_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp8_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp8_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp8_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp8_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp8_GROWTH_RATE = growthRate;
				CORAL_Sp8_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp8_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp8_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp8_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp8_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp8_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp8_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp8_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp8_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp8_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp8_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp8_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp8_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP9)){
				CORAL_Sp9_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp9_COLONIALITY = FTD.getColoniality();
				CORAL_Sp9_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp9_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp9_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp9_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp9_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp9_GROWTH_RATE = growthRate;
				CORAL_Sp9_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp9_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp9_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp9_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp9_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp9_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp9_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp9_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp9_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp9_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp9_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp9_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp9_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP10)){
				CORAL_Sp10_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp10_COLONIALITY = FTD.getColoniality();
				CORAL_Sp10_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp10_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp10_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp10_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp10_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp10_GROWTH_RATE = growthRate;
				CORAL_Sp10_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp10_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp10_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp10_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp10_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp10_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp10_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp10_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp10_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp10_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp10_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp10_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp10_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP11)){
				CORAL_Sp11_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp11_COLONIALITY = FTD.getColoniality();
				CORAL_Sp11_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp11_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp11_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp11_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp11_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp11_GROWTH_RATE = growthRate;
				CORAL_Sp11_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp11_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp11_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp11_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp11_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp11_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp11_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp11_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp11_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp11_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp11_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp11_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp11_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
				
			}else if (FTD.getSpecies().equals(SPECIES_CORAL_SP12)){
				CORAL_Sp12_AGGRESSIVENESS = FTD.getAggressiveness();
				CORAL_Sp12_COLONIALITY = FTD.getColoniality();
				CORAL_Sp12_COLONY_MAX_DIAMETER = FTD.getColonyMaxDiameter();
				CORAL_Sp12_CORALLITE_AREA = FTD.getCoralliteArea();
				CORAL_Sp12_EGG_DIAMETER = FTD.getEggDiameter();
				CORAL_Sp12_FECUNDITY_POLYP = FTD.getFecundityPolyp();
				CORAL_Sp12_GROWTH_FORM = FTD.getGrowthForm();
				// growth rate depends on yearDivision
				double growthRate = FTD.getGrowthRate() ;
				if(Constants.yearDivision  == 2){
					growthRate = growthRate / 2.0 + 0.5 ;
				}else if(Constants.yearDivision == 3){
					growthRate = (growthRate + 2) / 3.0 ;
				}else if(Constants.yearDivision == 4){
					growthRate = growthRate / 2.0 + 0.5 ;
					growthRate = growthRate / 2.0 + 0.5 ;
				}
				CORAL_Sp12_GROWTH_RATE = growthRate;
				CORAL_Sp12_MODE_LARVAL_DEVELOPMENT = FTD.getModeLarvalDevelopment();
				CORAL_Sp12_REDUCED_SCATTERING_COEFFICIENT = FTD.getReducedScatteringCoefficient();
//				CORAL_Sp12_RESPONSE_BLEACHING_INDEX = FTD.getResponseBleachingIndex();
//				CORAL_Sp12_SKELETAL_DENSITY = FTD.getSkeletalDensity();
				CORAL_Sp12_SIZE_MATURITY = FTD.getSizeMaturity();
				CORAL_Sp12_AGE_MATURITY = FTD.getAgeMaturity();
				CORAL_Sp12_BLEACHING_PROBABILITY = FTD.getBleachingProbability() ;
				CORAL_Sp12_SEXUAL_SYSTEM = FTD.getSexualSystem() ;
				CORAL_Sp12_CORRECTION_COEFF_FECUNDITY_POLYP	 = FTD.getCorrectionCoeffPolypFecundity() ;
//				if(numberBleachingModel == 1) {
//					CORAL_Sp12_X0_LOGIT_BLEACHING = FTD.getX0LogitBleaching() ;
//					CORAL_Sp12_R_LOGIT_BLEACHING = FTD.getRLogitBleaching() ;
//				}else if(numberBleachingModel == 2) {
//					CORAL_Sp12_X0_LOGIT_BLEACHING = FTD.getX0LogitBRI() ;
//					CORAL_Sp12_R_LOGIT_BLEACHING = FTD.getRLogitBRI() ;
//				}
			}
		}
		
//		System.out.printf("baaaaaaaaaa \n");
//		System.out.printf("%s, %s, %s, %.1f, %,1f, %.1f, %s, %.1f, %.1f, %s, %.1f, %.1f, %.1f, %.1f \n",
//				SUBSTRATE_CATEGORY_CORAL,
//				SUBSTRATE_SUBCATEGORY_LIVE_CORAL,
//				SPECIES_CORAL_SP12,
//				CORAL_Sp12_GROWTH_RATE,
//				CORAL_Sp12_AGGRESSIVENESS,
//				CORAL_Sp12_CORALLITE_AREA,
//				CORAL_Sp12_MODE_LARVAL_DEVELOPMENT,
//				CORAL_Sp12_FECUNDITY_POLYP,
//				CORAL_Sp12_COLONY_MAX_DIAMETER,
//				CORAL_Sp12_GROWTH_FORM,
//				CORAL_Sp12_SKELETAL_DENSITY,
//				CORAL_Sp12_EGG_DIAMETER,
//				CORAL_Sp12_SIZE_MATURITY,
//				CORAL_Sp12_AGE_MATURITY
//				);
	}
	
	public static void setGUIparameters(){
		final Parameters para = RunEnvironment.getInstance().getParameters();
		connectivity = ((String) para.getString("Connectivity"));								    // set the level of connectivity
		randomSeed = ((Integer) para.getValue("randomSeed")).intValue();
		yearDivision = ((Integer) para.getValue("yearDivision")).intValue();                     // CHECKED set yearDivision, has to come before Constants.setFunctionalTraitFromFile()!)
		communityNumber = ((Integer) para.getValue("communityNumber")).intValue();  			   // CHECKED set the community number depending on value parameter entered in UI (! it has to be placed before Constants.setFunctionalTraitFromFile()!)
//		numberYearSimulation = ((Integer) para.getValue("number_year_simulation")).intValue();   // set the number of year of a simulation
		numberCycloneDMTmodel = ((Integer) para.getValue("Cyclone_DMT.model")).intValue();
		numberGrazingModel = ((Integer) para.getValue("Grazing.model")).intValue();
//		numberBleachingModel = ((Integer) para.getValue("Bleaching_response.model")).intValue();
		disturbanceScenarioNumber = ((Integer) para.getValue("disturbance_scenario_number")).intValue();  // set the disturbanceScenarioNumber, Disturbances_yearDiv_2.csv provide a different scenarios number for each scenario
		GR_reduction_interaction = ((double) para.getValue("GR_reduction_interaction"));         // define the size of the radius of the within which agent interact
		Bleaching_diff_response = ((double) para.getValue("Bleaching_diff_response"));
		ratioAreaBranchingPlating_OvertopColonies = ((double) para.getValue("ratioAreaBranchingPlating_OvertopColonies"));
		proba_Algae_coverCCA = ((double) para.getValue("proba_Algae_coverCCA"));
		otherProportions = ((double) para.getValue("otherProportions"));
		retentionTime = ((double) para.getValue("retentionTime"));
		String outprint_data_string = ((String) para.getValue("System.out.print_data")) ;
		if(outprint_data_string.equals("false")) {
			outprint_data = false ;
		}else {
			outprint_data = true ;
		}
		width = ((Integer) para.getValue("reef_width")).intValue();
		height = ((Integer) para.getValue("reef_height")).intValue();
		sizeReef = (double)width * (double)height ;
		numberLarvae_leaving_otherReef = (int)(1000000 * 0.5 * (1-predationRate) * sizeReef / 10000 + 0.5) ; // number of larvae produced (not leaving!!!) per m2 in the remote reef
		
		proba_grazing_MA = (double) para.getValue("proba_grazing_MA") ;
		proba_grazing_AMA = (double) para.getValue("proba_grazing_AMA") ;
		proba_grazing_ACA = (double) para.getValue("proba_grazing_ACA") ;
		proba_grazing_Halimeda = (double) para.getValue("proba_grazing_Halimeda") ;
		proba_grazing_CCA = (double) para.getValue("proba_grazing_CCA") ;	
		
//		System.out.printf("height_BigAlgae: %.1f ; areaBranchingPlating_OvertopBigAlgae: %.1f \n",height_BigAlgae,areaBranchingPlating_OvertopBigAlgae) ;
		
		height_BigAlgae = (double) para.getValue("height_BigAlgae") ;
		height_Turf = (double) para.getValue("height_Turf") ;
		height_CCA_EncrustingCoral = (double) para.getValue("height_CCA_EncrustingCoral") ;
		proba_settle_CCA = (double) para.getValue("proba_settle_CCA") ;
		proba_settle_Barren_ground = (double) para.getValue("proba_settle_Barren_ground") ;	
		proba_settle_Dead_coral = (double) para.getValue("proba_settle_Dead_coral") ;	
		
//		System.out.printf("height_BigAlgae: %.1f \n",height_BigAlgae);
//		System.out.printf("height_Turf: %.1f \n",height_Turf);
//		System.out.printf("height_CCA_EncrustingCoral: %.1f \n",height_CCA_EncrustingCoral);
//		System.out.printf("proba_settle_CCA: %.1f \n",proba_settle_CCA);
//		System.out.printf("proba_settle_Barren_ground: %.1f \n",proba_settle_Barren_ground);
//		System.out.printf("proba_settle_Dead_coral: %.1f \n",proba_settle_Dead_coral);
//		System.out.printf("proba_Algae_coverCCA: %.1f \n",proba_Algae_coverCCA);
		
		// update the following parameters whise value depends on the previous ones
		areaBranchingPlating_OvertopFlat = Math.PI * Math.pow(height_CCA_EncrustingCoral + 5,2) ;        // e.g., 706.9 cm2 corresponds to radius of 15 cm
		areaBranchingPlating_OvertopTurf = Math.PI * Math.pow(height_Turf + 5,2) ;  ;        			   // e.g., 1265.6 cm2 corresponds to radius of 20 cm
		areaBranchingPlating_OvertopBigAlgae =  Math.PI * Math.pow(height_BigAlgae + 5,2) ;              // e.g., 5026.0 cm2 corresponds to radius of 40 cm
				
//		System.out.printf("height_BigAlgae: %.1f ; areaBranchingPlating_OvertopBigAlgae: %.1f \n",height_BigAlgae,areaBranchingPlating_OvertopBigAlgae) ;
		
		// not GUI but whatever
		SMUtils.setUUID() ;
		maxRadiusColonyInitialization = Math.sqrt(Constants.sizeReef * 0.1 / Math.PI) ; // a colony cannot occupy more than 5 % of the reef 
		
		boolean doRugosityGrazing = ((boolean) para.getValue("Rugosity_Grazing")) ;
		if(doRugosityGrazing) { // it is false by default
			RugosityGrazingDo = true ;
		}
	}
	
	public static void setNumberLarvaeComingFromOtherReef() { 	
		double numbLarvaeLeavingremoveReef = (double)numberLarvae_leaving_otherReef * proportionLarvae_leaving_remoteReef ; 
//		System.out.printf("************************** (double)numberLarvae_leaving_otherReef: %.1f proportionLarvae_leaving_remoteReef: %.1f \n",(double)numberLarvae_leaving_otherReef,proportionLarvae_leaving_remoteReef) ;
//		System.out.printf("************************** numbLarvaeLeavingremoveReef: %.1f \n",numbLarvaeLeavingremoveReef) ;
		
		if(connectivity.equals(Constants.connectivityHigh)){
			numberLarvaeRemoteReef = (int)(numbLarvaeLeavingremoveReef*proportion_larvae_from_otherReef_reachingFocalReef_HighConnectivity*Constants.proportionCompetentAlive_larvae_from_otherReef_HighConnectivity + 0.5) ; // 35000 larvae/m2
		}else if(connectivity.equals(Constants.connectivityMedium)){
			numberLarvaeRemoteReef = (int)(numbLarvaeLeavingremoveReef*proportion_larvae_from_otherReef_reachingFocalReef_MediumConnectivity*Constants.proportionCompetentAlive_larvae_from_otherReef_MediumConnectivity + 0.5) ; // 7000 larvae/m2
		}else if(connectivity.equals(Constants.connectivityLow)){
			numberLarvaeRemoteReef = (int)(numbLarvaeLeavingremoveReef*proportion_larvae_from_otherReef_reachingFocalReef_LowConnectivity*Constants.proportionCompetentAlive_larvae_from_otherReef_LowConnectivity + 0.5) ; // 700 larvae/m2
		}else if(connectivity.equals(Constants.connectivityIsolated100)){
			numberLarvaeRemoteReef = (int)(numbLarvaeLeavingremoveReef*proportion_larvae_from_otherReef_reachingFocalReef_Isolated100km*Constants.proportionCompetentAlive_larvae_from_otherReef_Isolated100km + 0.5) ; // 66.5 larvae/m2
		}else  if(connectivity.equals(Constants.connectivityIsolated200)){
			numberLarvaeRemoteReef = (int)(numbLarvaeLeavingremoveReef*proportion_larvae_from_otherReef_reachingFocalReef_Isolated200km*Constants.proportionCompetentAlive_larvae_from_otherReef_Isolated200km + 0.5) ; // 2.98 larvae/m2
		}else if (connectivity.equals(Constants.connectivityNone)) {  // 0 larvae/m2
			numberLarvaeRemoteReef = 0  ;
		}else if(connectivity.equals(Constants.connectivityCSV)){
			
			
//			System.out.printf("IN Constants: ***** setLarvaeConnectivityScenarios is callsed !!!!! \n") ;
			setLarvaeConnectivityScenarios() ; // create a list of Disturbance_larvalConnectivity instances for each year in the csv file.
		}
//		System.out.printf("************************** numbLarvaeLeavingremoveReef: %.1f \n",numbLarvaeLeavingremoveReef) ;
//		System.out.printf("numberLarvaeRemoteReef: %d Connectivity: %s numberLarvaeRemoteReef/m-2: %.4f \n", numberLarvaeRemoteReef,connectivity, numberLarvaeRemoteReef / sizeReef * 10000);
	}	
	
	/** CHECKED
	 * Import benthic groups names from the communities_benthic.csv, with the corresponding community number
	 * Order of names: Sp_1, ..., Sp12, MA, Turf, CCA
	 * It also sets the value of nameSite
	 */
	public static void setSpeciesNamesList(){
		
		speciesNamesList = SMUtils.getNameColumnsInCSVFile(BIODIVERSITY_DATA_FILE,communityNumber);	
		// remove 1st element, which corresponds to site
		nameSite = speciesNamesList.get(0) ;
		speciesNamesList.remove(0) ;
//		System.out.printf("In class Constants, setSpeciesNamesList()\n");
//		for(String name : speciesNamesList){
//			System.out.printf("%s \n", name);
//		}	
		
	}
	
	public static void setSpeciesNames(){
		int i = 0 ;
		SPECIES_CORAL_SP1 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP2 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP3 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP4 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP5 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP6 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP7 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP8 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP9 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP10 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP11 = speciesNamesList.get(i++);
		SPECIES_CORAL_SP12 = speciesNamesList.get(i++);
		SPECIES_MACROALGAE = speciesNamesList.get(i++);
		SPECIES_TURF = speciesNamesList.get(i++);
		SPECIES_CCA = speciesNamesList.get(i++);
		SPECIES_AMA = speciesNamesList.get(i++);
		SPECIES_ACA = speciesNamesList.get(i++);
		SPECIES_HALIMEDA = speciesNamesList.get(i++);
	}
	
//	public static List<String> returnCoralSpeciesNames(){
//		List<String> nameSpeciesNames = new ArrayList<String>() ;
//		nameSpeciesNames.add(SPECIES_CORAL_SP1);
//		nameSpeciesNames.add(SPECIES_CORAL_SP2);
//		nameSpeciesNames.add(SPECIES_CORAL_SP3);
//		nameSpeciesNames.add(SPECIES_CORAL_SP4);
//		nameSpeciesNames.add(SPECIES_CORAL_SP5);
//		nameSpeciesNames.add(SPECIES_CORAL_SP6);
//		nameSpeciesNames.add(SPECIES_CORAL_SP7);
//		nameSpeciesNames.add(SPECIES_CORAL_SP8);
//		nameSpeciesNames.add(SPECIES_CORAL_SP9);
//		nameSpeciesNames.add(SPECIES_CORAL_SP10);
//		nameSpeciesNames.add(SPECIES_CORAL_SP11);
//		nameSpeciesNames.add(SPECIES_CORAL_SP12);
//		
//		return nameSpeciesNames ;
//	}
	
	public static List<Disturbances_priority> setDisturbancesPriority(){
		disturbancesPriorityList = SMUtils.readDisturbancePriorityFile(DISTURBANCE_PRIORITY_FILE,disturbanceScenarioNumber) ;		
//		for(Disturbances_priority dp : disturbancesPriorityList) {
//			System.out.printf("year: %.1f %s %s %s %s \n", dp.getYear(),dp.getPriority_1(),dp.getPriority_2(),dp.getPriority_3(),dp.getSeason());
//		}	
		return disturbancesPriorityList ;
	}
	
	public static List<Sand_cover> setSandCoverScenarios(){
		sandCoverDataList = SMUtils.readSandCoverFile(SAND_COVER_DATA_FILE,disturbanceScenarioNumber) ;
//		for(Sand_cover sc : sandCoverDataList) {
//			System.out.printf("Constants.setSandCoverScenarios() : Sand cover, year: %.1f cover: %.1f \n", sc.getYear(), sc.getSand_Cover());
//		}
		return sandCoverDataList ;
	}
	
	public static List<Disturbance_cyclone> setCycloneScenarios(){
		cycloneDMTDataList = SMUtils.readCycloneDMTFile(CYCLONE_DMT_DATA_FILE,disturbanceScenarioNumber,numberCycloneDMTmodel) ;
//		for(Disturbance_cyclone dc : cycloneDMTDataList) {
//			System.out.printf("Constant.setCycloneScenarios(), year: %.1f DMT: %.1f \n", dc.getYear(), dc.getCyclone_DMT());
//		}
		return cycloneDMTDataList ;
	}
	
	public static List<Disturbance_grazing> setGrazingScenarios(){
		grazingDataList = SMUtils.readGrazingFile(GRAZING_DATA_FILE,disturbanceScenarioNumber,numberGrazingModel) ;
//		for(Disturbance_grazing dg : grazingDataList) {
//			System.out.printf("Constant.setGrazingScenarios(), year: %.1f cover grazed: %.1f \n", dg.getYear(), dg.getCoverGrazed());
//		}
		return grazingDataList ;
	}
	
	public static List<Disturbance_bleaching> setBleachingScenarios(){
		bleachingDHWDataList = SMUtils.readBleachingDHWFile(BLEACHING_DATA_FILE,disturbanceScenarioNumber) ;
//		for(Disturbance_bleaching db : bleachingDataList) {
//			System.out.printf("Constant.setBleachingScenarios(), year: %.1f DHW: %.1f \n", db.getYear(), db.getBleaching_DHW());
//		}
		return bleachingDHWDataList ;
	}
	
	public static List<Disturbance_larvalConnectivity> setLarvaeConnectivityScenarios(){
		larvalInputList = SMUtils.readlarvalInputFile(LARVAL_CONNECTIVITY_DATA_FILE,disturbanceScenarioNumber) ;
		
//		for(Disturbance_larvalConnectivity dlc : larvalInputList) {
//			System.out.printf("In Constant class: ");
//			System.out.printf("Constant.setBleachingScenarios(), year: %.1f nb larvae/m2: %.1f \n", dlc.getYear(), dlc.getNumberLarvae_m2());
//		}
		
		return larvalInputList ;
	}
	
	public static void setNumberYearSimulation() {
		int sizeList = disturbancesPriorityList.size() ;
		numberYearSimulation = disturbancesPriorityList.get(sizeList - 1).getYear()	;  // -1 because it starts at 0
		// the simulation ends at the end of the final period, so when the latter turns into final period + 1 time step
		double periodToAdd = 1.0 ; // in case yearDivision == 1
		if(yearDivision == 2) {
			periodToAdd = 0.5 ;
		}else if(periodToAdd == 3) {
			periodToAdd = 0.33 ;
		}else if(periodToAdd == 4) {
			periodToAdd = 0.25 ;
		}
		numberYearSimulation = numberYearSimulation + periodToAdd ;  // because similuation ends at the end of the last time step
		tick_max = numberYearSimulation * 4 * yearDivision ; // 4 ticks by time step 
		 
//		System.out.printf("************* Numberyear simulation: %.1f tick_max: %.1f \n", numberYearSimulation,tick_max) ;
	}
	
	public static void setInitialBiodiversityData(){
		dataBiodiversityList = SMUtils.readBiodiversityDataFile(Constants.BIODIVERSITY_DATA_FILE,communityNumber) ; 
//		System.out.printf("In Constants.setInitialBiodiversityData() \n");
//		for(BiodiversityData bd : dataBiodiversityList) {
//			System.out.printf("Initial percentage cover sp1: %.1f, sp2: %.1f, sand: %.1f \n",bd.getPercentageCoralSp1(),bd.getPercentageCoralSp12(),bd.getPercentageSand()) ;
//		}
	}
	
	public static void setInitialBiodiversityDataBySpecies(){	
//		for(BiodiversityData BD : dataBiodiversityList){
//			System.out.printf("%d %.1f %.1f %.1f \n", BD.getCommunitiesNumber(),BD.getPercentageCoralSp1(),BD.getPercentageCoralSp2(),BD.getPercentageCoralSp3()) ;
//		}
		for(int i = 0 ; i < Constants.speciesNamesList.size() ; i++){
			BiodiversityDataBySpecies BDBS = new BiodiversityDataBySpecies(Constants.speciesNamesList.get(i),0,0,0,0);  // create aBiodiversityDataBySpecies instance with speciesName = speciesNamesList[i] and values 0,0,0
			BDBS.setPercentageCoverAlive(dataBiodiversityList.get(0).getPecentageAgentTypeAsked(Constants.speciesNamesList.get(i))); // % cover alive
			BDBS.setPercentageCoverBleached(dataBiodiversityList.get(1).getPecentageAgentTypeAsked(Constants.speciesNamesList.get(i))); // % cover bleached
			BDBS.setPercentageCoverDead(dataBiodiversityList.get(2).getPecentageAgentTypeAsked(Constants.speciesNamesList.get(i))); // % cover dead
			BDBS.setPercentageCoverTotal(BDBS.getPercentageCoverAlive()+BDBS.getPercentageCoverBleached()+BDBS.getPercentageCoverDead());
//			System.out.printf("%s %.1f \n",BDBS.getSpeciesName(),BDBS.getPercentageCoverTotal());
			dataBiodiversityBySpeciesList.add(BDBS) ;
//			System.out.printf("%s %.1f %.1f %.1f \n", BDBS.getSpeciesName(),BDBS.getPercentageCoverAlive(),BDBS.getPercentageCoverBleached(),BDBS.getPercentageCoverDead()) ;
		}
//		for(BiodiversityDataBySpecies BDBS : dataBiodiversityBySpeciesList){
//			System.out.printf("%s %.1f \n", BDBS.getSpeciesName(),BDBS.getPercentageCoverTotal()) ;
//		}
		
	}
	
	
	public static void setWorkingDirectories() {
				
//	    Path path = FileSystems.getDefault().getPath("").toAbsolutePath() ; // import java.nio.file.Path 	  
//	    wd = path.toString() ;
//	    wd = new File("").getAbsolutePath() ; // import java.nio.file.Path 	  
//	    try {
//			wd  = new File(".").getCanonicalPath().toString() ;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		
		
		/* **************************************************************************
		 * ****** CODE THAT WORK FOR REPAST SIMPLE RUNS AND WITH RREPAST ************
		 * **************************************************************************/
		try {         // https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
			wd_absolute =  new File(Constants.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() ;  // get the absolute path of the class Constant 
			wd_absolute = wd_absolute.replace("bin", "") ;// remove /bin
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    wd_absolute = wd ; // !!! TO COMMENT OUT IF USING rrepast !!!!

//	    System.out.printf("*************** wd_absolute: %s \n", wd_absolute);
//	    System.out.printf("*************** wd: %s \n", wd);
			    
	    INPUT_WORKSPACE = wd_absolute + "data" ;    // replace wd_absolute by wd if doing batch runs with Repast
	    OUTPUT_WORKSPACE = wd_absolute + "output" ;  // leave wd_absolute if using rrepast 

	    DISTURBANCE_PRIORITY_FILE = INPUT_WORKSPACE + "/Disturbance_priority.csv";
	    SAND_COVER_DATA_FILE = INPUT_WORKSPACE + "/Disturbance_sand.csv";
	    CYCLONE_DMT_DATA_FILE = INPUT_WORKSPACE +"/Disturbance_cyclone.csv";
	    BLEACHING_DATA_FILE = INPUT_WORKSPACE +"/Disturbance_bleaching.csv";
	    GRAZING_DATA_FILE = INPUT_WORKSPACE + "/Disturbance_grazing.csv";
	    LARVAL_CONNECTIVITY_DATA_FILE = INPUT_WORKSPACE + "/Disturbance_larvalConnectivity.csv";
        BIODIVERSITY_DATA_FILE = INPUT_WORKSPACE + "/Initial_benthic_composition.csv"; 
	    FUNCTIONAL_TRAIT_DATAFILE = INPUT_WORKSPACE + "/functionalTraitDF_model.csv"; //Functional_traits_RF_final2.csv";
	    SKEW_BIAS_COLONY_SIZE_CLASS_DISTRIBUTION = INPUT_WORKSPACE + "/colonySize_distribution_initial.csv"; // skew and bias define the distribution shape of the class 
	    R_PATH_FILE = INPUT_WORKSPACE + "/R_path.csv" ; // NOT USED ANYMORE
	    
	    OUTOUT_NUMBER_RECRUITS = OUTPUT_WORKSPACE + "/Number_recruits"   ;
	    OUTOUT_PERCENTAGE_COVER = OUTPUT_WORKSPACE + "/Percentage_cover" ;

	    
	    R_SCRIPTS_WORKSPACE =  wd_absolute + "/R_scripts" ;
        COMPETITION_R_SCRIPT = R_SCRIPTS_WORKSPACE + "/coral_pair_competition_probability.R" ;

//        System.out.printf("*************** OUTPUT_WORKSPACE: %s \n", OUTPUT_WORKSPACE);

        // create a csv file in datasets_inputs with the INPUT_WORKSPACE for R to find its working directory
        // SMUtils.createCSV_Path_for_Rscript(INPUT_WORKSPACE) ;
        
	}
	 
	/**
	 * Determines the proportion of settled larvae that successfully recruit at the end of the time step.
	 * Model fitted on Ritson-Williams et al, 2016's data (Appendix S2: 7.3.2).
	 */
	public static void setProba_Xmonth_recruitment(){
		double numberMonths = 12.0 / (double)yearDivision ; 
		proba_Xmonth_recruitment = 10.3 + 43.3 * 1 / numberMonths ;
		System.out.printf("****** proba_Xmonth_recruitment: %.2f ****** \n", proba_Xmonth_recruitment) ;
	}

	
}
