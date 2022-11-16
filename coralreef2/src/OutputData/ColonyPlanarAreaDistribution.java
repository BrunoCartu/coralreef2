package OutputData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import coralreef2.ContextCoralReef2;
import coralreef2.CoralReef2Builder;
import coralreef2.common.Constants;

public class ColonyPlanarAreaDistribution {
	
	private double year ;
	private String event ;
	private String speciesName ;
	private ArrayList<Double> colonyPlanarAreaList ;
	private ArrayList<Integer> colonyIDNumberList ; // to record the corresponding IDnumber of the colony
	
	public ColonyPlanarAreaDistribution (
			double year ,
			String event,
			String speciesName,
			ArrayList<Double> colonyPlanarAreaList,
			ArrayList<Integer>  colonyIDNumberList
			) {
		
		this.year = year ;
		this.event = event ;
		this.speciesName = speciesName ;
		this.colonyPlanarAreaList = colonyPlanarAreaList ;
		this.colonyIDNumberList = colonyIDNumberList ;

	}
	
	public double getYear() {
		return this.year ;
	}
	public String getEvent() {
		return this.event ;
	}
	public String getSpeciesName() {
		return this.speciesName ;
	}
	public ArrayList<Double> getColonyPlanarAreaList(){
		return this.colonyPlanarAreaList ;
	}
	public ArrayList<Integer> getColonyIDNumberList(){
		return this.colonyIDNumberList ;
	}
	
//	public void addValToColonyPlanarArea(double val) {
//		this.colonyPlanarArea.add(val) ;
//	}
	
	public void setYear(double yr) {
		this.year = yr ;
	}
	public void setEvent(String et) {
		this.event = et ;
	}
	public void setSpeciesName(String sn) {
		this.speciesName = sn ;
	}
	public void setColonyPlanarAreaList(ArrayList<Double> cpa){
		this.colonyPlanarAreaList = cpa ;
	}
	public void setColonyIDNumberList(ArrayList<Integer> cidn){
		this.colonyIDNumberList = cidn ;
	}
	
	/**
	 * Initiates CoralReef2Builder.colonyPlanarAreaDistList for each coral species with empty array lists.
	 */
	public static void initializeColonyPlanarAreaDistList() {
		for(String coralSpName : Constants.speciesNamesList) {
			ArrayList<Double> colonyPlanarAreaList = new ArrayList<Double>() ;
			ArrayList<Integer> colonyIDNumberList = new ArrayList<Integer>() ;
			boolean itIsCoral = !(coralSpName.equals(Constants.SPECIES_MACROALGAE)) && !(coralSpName.equals(Constants.SPECIES_TURF)) && !(coralSpName.equals(Constants.SPECIES_CCA)) &&
							    !(coralSpName.equals(Constants.SPECIES_AMA)) && !(coralSpName.equals(Constants.SPECIES_ACA)) && !(coralSpName.equals(Constants.SPECIES_HALIMEDA)) &&
							    !(coralSpName.equals(Constants.SPECIES_SAND)) ;
			if(itIsCoral) {
				ColonyPlanarAreaDistribution cpad = new ColonyPlanarAreaDistribution(0.0,"inital",coralSpName, colonyPlanarAreaList,colonyIDNumberList) ;
				CoralReef2Builder.colonyPlanarAreaDistList.add(cpad) ;
//				System.out.printf("****** HHHEEEYYYYYY year: %.1f %s %s \n",cpad.getYear(),cpad.getSpeciesName(), cpad.getEvent()) ;
			}
		}
	}
	
	/** 
	 * Find the ColonyPlanarAreaDistribution instance corresponding to "SpeciesName" and add "colonyPlanarArea" value to 
	 * the "colonyPlanarAreaList" of the ColonyPlanarAreaDistribution.
	 * @param SpeciesName
	 * @param colonyPlanarArea
	 * @param event
	 */
	public static void upDateColonyPlanarAreaDistList(String SpeciesName, double colonyPlanarArea, int colonyIDNumber){
//		for(String coralSpName : Constants.coralSpeciesPresentList) {
		for(ColonyPlanarAreaDistribution cpad : CoralReef2Builder.colonyPlanarAreaDistList) {
			if(SpeciesName.equals(cpad.getSpeciesName())){			
				cpad.getColonyPlanarAreaList().add(colonyPlanarArea) ;
				cpad.getColonyIDNumberList().add(colonyIDNumber) ;			
				break ;
			}
		}
//		return CoralReef2Builder.colonyPlanarAreaDistList ;
	}
	
	/**
	 * Only update the year and event variable of each cpad in CoralReef2Builder.colonyPlanarAreaDistList (the is a unique one for each species present)
	 */
	public static void upDateColonyPlanarAreaDistList_onlyYear_and_Event(String event){
		for(ColonyPlanarAreaDistribution cpad : CoralReef2Builder.colonyPlanarAreaDistList){
			cpad.setYear(ContextCoralReef2.year_event);
			cpad.setEvent(event);
		}
	}
	
	/**
	 * Do a deep copy of ColonyPlanarAreaDistribution instance
	 */
	public ColonyPlanarAreaDistribution cloneDeepCopy() {
		ColonyPlanarAreaDistribution cpad = new ColonyPlanarAreaDistribution(year, event, event, colonyPlanarAreaList, colonyIDNumberList) ;
		cpad.year = this.year ;
		cpad.event = this.event ;
		cpad.speciesName = this.speciesName ;
		cpad.colonyPlanarAreaList = this.colonyPlanarAreaList ;
		cpad.colonyIDNumberList = this.colonyIDNumberList ;
		return(cpad) ;
	}
	
	/**
	 * Called by  ContextCoralReef2.updateAgentColonySize(), which is called after a cyclone, a bleaching event and in ContextCoralReef2.substrateCompositionCSV() at the end of the period.
	 * Save the contain of the list CoralReef2Builder.colonyPlanarAreaDistList (colonies planar area and IDNUmber for each species at this given time ).
	 * Then erase CoralReef2Builder.colonyPlanarAreaDistList. 
	 */
	public static void writeCSVColonyPlanarAreaList_EraseList() {
		final String COMMA_DELIMITER = "," ;
		final String NEW_LINE_SEPARATOR = "\n" ;
//		String FILE_HEADER = "year";		
		String nameCSV = "/ColonyPlanarArea_"+ "UUID_" + Constants.UUID_here + "_SEED_" + Constants.randomSeed + ".csv" ;	
		final String workSpace = Constants.OUTPUT_WORKSPACE + "/ColonyPlanarArea" ;
	
		FileWriter fileWriter = null ;

		try{	
			File file = new File(workSpace + nameCSV) ;
			fileWriter = new FileWriter(file,true); 

			for(ColonyPlanarAreaDistribution cpad : CoralReef2Builder.colonyPlanarAreaDistList){
				
//				ArrayList<Integer> listCS = cpad.getColonyIDNumberList() ;
//				Collections.sort(listCS) ;
//				for(int i = 0 ; i < listCS.size() - 1 ; i ++) {
//					System.out.printf("DUPLICSTED IDNumbers for %s ???? \n",cpad.getSpeciesName()) ;
//					if(listCS.get(i) == listCS.get(i + 1)) {
//						System.out.println("!!!!!!! DUPLICATED ITEM !!!!!!"+ listCS.get(i + 1)+" at Location" + (i+1) );
//					}
//				}
//				System.out.printf("\n") ;
				
				fileWriter.append(String.valueOf(cpad.getYear()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(cpad.getEvent()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(cpad.getSpeciesName()));
				fileWriter.append(COMMA_DELIMITER);
				// write size of each colony one after another
				for(int i = 0 ; i < cpad.getColonyPlanarAreaList().size(); i++) {
					fileWriter.append(String.valueOf(cpad.getColonyPlanarAreaList().get(i)));
					fileWriter.append(COMMA_DELIMITER);					
				}
//				System.out.printf("\n");
				fileWriter.append(NEW_LINE_SEPARATOR) ;
	
				fileWriter.append(String.valueOf(cpad.getYear()));
				fileWriter.append(COMMA_DELIMITER) ; 
				fileWriter.append("IDNumber") ;
				fileWriter.append(COMMA_DELIMITER) ; // skip event column
				fileWriter.append(String.valueOf(cpad.getSpeciesName()));
				fileWriter.append(COMMA_DELIMITER) ; 
				for(int i = 0 ; i < cpad.getColonyPlanarAreaList().size(); i++) {
					fileWriter.append(String.valueOf(cpad.getColonyIDNumberList().get(i)));
					fileWriter.append(COMMA_DELIMITER);
				}
				fileWriter.append(NEW_LINE_SEPARATOR) ;
			}
//			 System.out.println("CSV file was created successfully !!!") ;
		}catch (Exception e) {
			System.out.println("Error in CsvFileWriter writeCSVColonyPlanarAreaList_EraseList !!!") ;
			e.printStackTrace();
		}finally {
			try{
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e){
				System.out.println("Error while flushing/closing fileWriter writeCSVColonyPlanarAreaList_EraseList !!!");
				 e.printStackTrace();
			}	
		}
		// Erase the lists of colony planar area and IDNumber  
		for(ColonyPlanarAreaDistribution cpad : CoralReef2Builder.colonyPlanarAreaDistList) {
			ArrayList<Double> newColonyPlanarAreaList = new ArrayList<Double>() ;
			ArrayList<Integer> newIDNumberColonyList = new ArrayList<Integer>() ;
			cpad.setColonyPlanarAreaList(newColonyPlanarAreaList) ;
			cpad.setColonyIDNumberList(newIDNumberColonyList) ;
		}
	}
	

	
	
}
