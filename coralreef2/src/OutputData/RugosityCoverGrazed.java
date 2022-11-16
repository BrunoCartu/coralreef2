package OutputData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import coralreef2.CoralReef2Builder;
import coralreef2.common.Constants;
import repast.simphony.context.Context;

public class RugosityCoverGrazed {
	
	private double year ;
	private double rugosity ;
	private double grazingFish ; // the proportion of reef grazed by fish as a function of rugosity
	
	public RugosityCoverGrazed(double year,
							   double rugosity,
							   double grazingFish) {
		this.year = year ;
		this.rugosity = rugosity ;
		this.grazingFish = grazingFish ;
		
	}

	public double getYear() {
		return year;
	}

	public void setYear(double year) {
		this.year = year;
	}

	public double getRugosity() {
		return rugosity;
	}

	public void setRugosity(double rugosity) {
		this.rugosity = rugosity;
	}

	public double getGrazingFish() {
		return grazingFish;
	}

	public void setGrazingFish(double grazingFish) {
		this.grazingFish = grazingFish;
	}
	
	
	public static RugosityCoverGrazed createRugosityCoverGrazed(){
		RugosityCoverGrazed rugosityCoverGrazed = new RugosityCoverGrazed(0,0,0); // 
		return rugosityCoverGrazed ;
	}
	
	/**
	 * 
	 * @param percentage_reef_grazed
	 */
	public static void writeCSVRugosityCoverGrazedList_EraseList(double percentage_reef_grazed) {
		final String COMMA_DELIMITER = "," ;
		final String NEW_LINE_SEPARATOR = "\n" ;		
		String nameCSV = "/RugosityCoverGrazed_"+ "UUID_" + Constants.UUID_here + "_SEED_" + Constants.randomSeed + ".csv" ;	
		final String workSpace = Constants.OUTPUT_WORKSPACE + "/RugosityCoverGrazed" ;
	
		String FILE_HEADER = "year" + COMMA_DELIMITER + "Rugosity" + COMMA_DELIMITER + "cover_grazed_fish" + COMMA_DELIMITER + "cover_grazed_total" ;
		
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

			for(RugosityCoverGrazed rcg : CoralReef2Builder.rugosityCoverGrazedList){ //there is only one instance in the list
				
				fileWriter.append(String.valueOf(rcg.getYear()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(rcg.getRugosity()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(rcg.getGrazingFish()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(percentage_reef_grazed));
				
				fileWriter.append(NEW_LINE_SEPARATOR) ;
				
			}
//			 System.out.println("CSV file was created successfully !!!") ;
		}catch (Exception e) {
			System.out.println("Error in CsvFileWriter writeCSVRugosityCoverGrazedList_EraseList !!!") ;
			e.printStackTrace() ;
		}finally {
			try{
				fileWriter.flush() ;
				fileWriter.close() ;
			} catch (IOException e){
				System.out.println("Error while flushing/closing fileWriter writeCSVRugosityCoverGrazedList_EraseList !!!");
				 e.printStackTrace();
			}	
		}
		// Erase the lists of colony planar area and IDNumber  
		CoralReef2Builder.rugosityCoverGrazedList.clear() ;		
	}


}
