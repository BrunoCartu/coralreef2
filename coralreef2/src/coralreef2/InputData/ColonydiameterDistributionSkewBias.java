package coralreef2.InputData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import OutputData.ColonyPlanarAreaDistribution;
import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import coralreef2.CoralReef2Builder;
import coralreef2.common.Constants;

public class ColonydiameterDistributionSkewBias{
	
	private String speciesName ;
	private double skew ;
	private double bias ;
	
	public ColonydiameterDistributionSkewBias(String speciesName,
											 double skew,
											 double bias) {
		this.speciesName = speciesName ;
		this.skew = skew ;
		this.bias = bias ;
		
	}
	
	
	public String getSpeciesName() {
		return speciesName;
	}
	public double getSkew() {
		return skew;
	}
	public double getBias() {
		return bias;
	}


	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}
	public void setSkew(Double skew) {
		this.skew = skew;
	}
	public void setBias(Double bias) {
		this.bias = bias;
	}


	/**
	 * Read csv file colonySize_distribution_initial.csv and create the ColonydiameterDistributionSkewBias instance corresponding to each each coral species.
	 * Each instance is then placed in CoralReef2Buidler.colonyDiameterDistributionSkewBiasList
	 * 
	 */
	public static void getSkewBiasDistributionColonySize(){
		CSVReader br = null ;
		String nameFile = Constants.SKEW_BIAS_COLONY_SIZE_CLASS_DISTRIBUTION ;
		double bias = 0.0 ;
		double skew = 0.0 ;
		
		for(String coralSpName : Constants.speciesNamesList) {
			
    		System.out.printf("*********** %s \n", coralSpName) ;
			
			boolean itIsCoral = !(coralSpName.equals(Constants.SPECIES_MACROALGAE)) && !(coralSpName.equals(Constants.SPECIES_TURF)) && !(coralSpName.equals(Constants.		SPECIES_CCA)) &&
							    !(coralSpName.equals(Constants.SPECIES_AMA)) && !(coralSpName.equals(Constants.SPECIES_ACA)) && !(coralSpName.equals(Constants.SPECIES_HALIMEDA)) &&
							    !(coralSpName.equals(Constants.SPECIES_SAND)) ;
			if(itIsCoral) {
				try {
		            br = new CSVReader (new FileReader(nameFile),',','\'',CSVParser.DEFAULT_QUOTE_CHARACTER,Constants.SKEW_BIAS_HEADER_LINES); // CSVParser.DEFAULT_QUOTE_CHARACTER: quotes are removed for strings
		            String[] line = null;
		            line = br.readNext();
		            String speciesHere = "none" ;
		            while (!speciesHere.equals(coralSpName)) {    	// select the right community number
		            		int idx = 0 ;
		            		speciesHere = line[idx++] ;
		                if(speciesHere.equals(coralSpName)){
		                	

		                	
		                	skew = Double.parseDouble(line[idx++]);
		                	bias = Double.parseDouble(line[idx]) ;
		                }else{
		                		line = br.readNext() ;
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
			}
			ColonydiameterDistributionSkewBias newCDDSB = new ColonydiameterDistributionSkewBias(coralSpName, skew, bias) ;
			CoralReef2Builder.colonyDiameterDistributionSkewBiasList.add(newCDDSB) ;
		}
	}
	
	/**
	 * Returns the value of skew and bias corresponding to each coral species by looking CoralReef2Builder.colonyDiameterDistributionSkewBiasList.
	 * @param nameColumn
	 * @return
	 */
	public static ArrayList<Double> getValueSkewBias(String nameColumn) {
		ArrayList<Double> CDDSB_list = new ArrayList<Double>() ;
		
		for(ColonydiameterDistributionSkewBias CDDSB : CoralReef2Builder.colonyDiameterDistributionSkewBiasList) {
			if(nameColumn.equals(CDDSB.getSpeciesName())) {
				CDDSB_list.add(CDDSB.getSkew()) ;
				CDDSB_list.add(CDDSB.getBias()) ;
				break ;
			}
		}
		return CDDSB_list ;
	}
	
}
