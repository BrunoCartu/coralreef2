package coralreef2.InputData;

import java.util.List;

import coralreef2.common.Constants;
import coralreef2.common.SMUtils;

public class BiodiversityData {

	private int communitiesNumber;
	
	private double percentageCoralSp1;
	private double percentageCoralSp2;
	private double percentageCoralSp3;
	private double percentageCoralSp4;
	private double percentageCoralSp5;
	private double percentageCoralSp6;
	private double percentageCoralSp7;
	private double percentageCoralSp8;
	private double percentageCoralSp9;
	private double percentageCoralSp10;
	private double percentageCoralSp11;
	private double percentageCoralSp12;
	
	private double percentageMacroalgae;
	private double percentageTurf;
	private double percentageCCA;
	private double percentageAMA;
	private double percentageACA;
	private double percentageHalimeda;
	
	private double percentageSand ;
	
	public BiodiversityData (int communitiesNumber,
							 double percentageCoralSp1,
							 double percentageCoralSp2,
							 double percentageCoralSp3,
							 double percentageCoralSp4,
							 double percentageCoralSp5,
							 double percentageCoralSp6,
							 double percentageCoralSp7,
							 double percentageCoralSp8,
							 double percentageCoralSp9,
							 double percentageCoralSp10,
							 double percentageCoralSp11,
							 double percentageCoralSp12,
			 				 double percentageMacroalgae,
			 				 double percentageTurf,
			 				 double percentageCCA,
			 				 double percentageAMA,
			 				 double percentageACA,
			 				 double percentageHalimeda,
			 				 double percentageSand
	){
		
		this.communitiesNumber = communitiesNumber;
		this.percentageCoralSp1 = percentageCoralSp1;
		this.percentageCoralSp2 = percentageCoralSp2;
		this.percentageCoralSp3 = percentageCoralSp3;
		this.percentageCoralSp4 = percentageCoralSp4;
		this.percentageCoralSp5 = percentageCoralSp5;
		this.percentageCoralSp6 = percentageCoralSp6;
		this.percentageCoralSp7 = percentageCoralSp7;
		this.percentageCoralSp8 = percentageCoralSp8;
		this.percentageCoralSp9 = percentageCoralSp9;
		this.percentageCoralSp10 = percentageCoralSp10;
		this.percentageCoralSp11 = percentageCoralSp11;
		this.percentageCoralSp12 = percentageCoralSp12;
		this.percentageMacroalgae = percentageMacroalgae;
		this.percentageTurf = percentageTurf;
		this.percentageCCA = percentageCCA;
		this.percentageAMA = percentageAMA;
		this.percentageACA = percentageACA;
		this.percentageHalimeda = percentageHalimeda ;
		this.percentageSand = percentageSand;
	}
	
	public int getCommunitiesNumber(){
		return this.communitiesNumber;
	}
	public double getPercentageCoralSp1 (){
		return this.percentageCoralSp1;
	}
	public double getPercentageCoralSp2 (){
		return this.percentageCoralSp2;
	}
	public double getPercentageCoralSp3 (){
		return this.percentageCoralSp3;
	}
	public double getPercentageCoralSp4 (){
		return this.percentageCoralSp4;
	}
	public double getPercentageCoralSp5 (){
		return this.percentageCoralSp5;
	}
	public double getPercentageCoralSp6 (){
		return this.percentageCoralSp6;
	}
	public double getPercentageCoralSp7 (){
		return this.percentageCoralSp7;
	}
	public double getPercentageCoralSp8 (){
		return this.percentageCoralSp8;
	}
	public double getPercentageCoralSp9 (){
		return this.percentageCoralSp9;
	}
	public double getPercentageCoralSp10 (){
		return this.percentageCoralSp10;
	}
	public double getPercentageCoralSp11 (){
		return this.percentageCoralSp11;
	}
	public double getPercentageCoralSp12 (){
		return this.percentageCoralSp12;
	}
	public double getPercentageMacroalgae (){
		return this.percentageMacroalgae;
	}
	public double getPercentageTurf(){
		return this.percentageTurf;
	}
	public double getPercentageCCA(){
		return this.percentageCCA;
	}
	public double getPercentageAMA(){
		return this.percentageAMA;
	}
	public double getPercentageACA(){
		return this.percentageACA;
	}
	public double getPercentageHalimeda(){
		return this.percentageHalimeda ;
	}
	public double getPercentageSand() {
		return this.percentageSand ;
	}
	
	public double getPecentageAgentTypeAsked(String nameSpecies){
		double percentage = 0;		
		if(nameSpecies.equals(Constants.SPECIES_MACROALGAE)){
			percentage = this.percentageMacroalgae;
		}else if(nameSpecies.equals(Constants.SPECIES_TURF)){
			percentage = this.percentageTurf;
		}else if(nameSpecies.equals(Constants.SPECIES_CCA)){
			percentage = this.percentageCCA;
		}else if(nameSpecies.equals(Constants.SPECIES_AMA)){
			percentage = this.percentageAMA;
		}else if(nameSpecies.equals(Constants.SPECIES_ACA)){
			percentage = this.percentageACA;
		}else if(nameSpecies.equals(Constants.SPECIES_HALIMEDA)){
			percentage = this.percentageHalimeda;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP1)){
			percentage = this.percentageCoralSp1;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP2)){
			percentage = this.percentageCoralSp2;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP3)){
			percentage = this.percentageCoralSp3;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP4)){
			percentage = this.percentageCoralSp4;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP5)){
			percentage = this.percentageCoralSp5;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP6)){
			percentage = this.percentageCoralSp6;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP7)){
			percentage = this.percentageCoralSp7;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP8)){
			percentage = this.percentageCoralSp8;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP9)){
			percentage = this.percentageCoralSp9;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP10)){
			percentage = this.percentageCoralSp10;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP11)){
			percentage = this.percentageCoralSp11;
		}else if(nameSpecies.equals(Constants.SPECIES_CORAL_SP12)){
			percentage = this.percentageCoralSp12;
		}else if(nameSpecies.equals(Constants.SPECIES_SAND)){
			percentage = this.percentageSand ;
		}else {	
			System.out.printf("In BiodiversityData class, nameSpecies does not exist: %s \n",nameSpecies);
		}
		
//		System.out.printf("%s %s %s \n", Constants.SPECIES_MACROALGAE,Constants.SPECIES_CORAL_SP1,Constants.SPECIES_CORAL_SP3);
		
		
		return percentage;
	}
}


