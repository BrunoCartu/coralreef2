package coralreef2.InputData;

import coralreef2.common.Constants;

public class BiodiversityDataBySpecies {
	
	private String speciesName;
	private double percentageCoverAlive;
	private double percentageCoverBleached;
	private double percentageCoverDead;
	private double percentageCoverTotal;
	
	public BiodiversityDataBySpecies (String speciesName,
									 double percentageCoverAlive,
									 double percentageCoverBleached,
									 double percentageCoverDead,
									 double percentageCoverTotal
	){
		this.speciesName = speciesName ;
		this.percentageCoverAlive = percentageCoverAlive ;
		this.percentageCoverBleached = percentageCoverBleached ;
		this.percentageCoverDead = percentageCoverDead;
		this.percentageCoverTotal = percentageCoverTotal;
	}
	
	public String getSpeciesName(){
		return this.speciesName ;
	}
	public Double getPercentageCoverAlive(){
		return this.percentageCoverAlive ;
	}
	public Double getPercentageCoverBleached(){
		return this.percentageCoverBleached ;
	}
	public Double getPercentageCoverDead(){
		return this.percentageCoverDead ;
	}
	public Double getPercentageCoverTotal(){
		return this.percentageCoverTotal ;
	}
	
	public void setSpeciesName(String name){
		this.speciesName = name ;
	}
	public void setPercentageCoverAlive(double pc){
		this.percentageCoverAlive = pc ;
	}
	public void setPercentageCoverBleached(double pc){
		this.percentageCoverBleached = pc ;
	}
	public void setPercentageCoverDead(double pc){
		this.percentageCoverDead = pc ;
	}
	public void setPercentageCoverTotal(double pc){
		this.percentageCoverTotal = pc ;
	}
	
	public static double getPercentageCoverSpecies(String nameSpecies,String typeCover){
		double percentageCover = 0.0 ;
		for(BiodiversityDataBySpecies BDBS : Constants.dataBiodiversityBySpeciesList){
			if(BDBS.getSpeciesName().equals(nameSpecies)){
				if(typeCover.equals("alive")){
					percentageCover = BDBS.getPercentageCoverAlive() ;
				}else if(typeCover.equals("bleached")){
					percentageCover = BDBS.getPercentageCoverBleached() ;
				}else if(typeCover.equals("dead")){
					percentageCover = BDBS.getPercentageCoverDead() ;
				}else if(typeCover.equals("total")){
					percentageCover = BDBS.getPercentageCoverTotal() ;
				}else{
					System.out.printf("BiodiversityDataBySpecies.getPercentageCoverSpecies() 2nd argument: alive, bleached, dead or total are the options \n");
				}
				break;
			}
		}
		return percentageCover ;
	}
}

