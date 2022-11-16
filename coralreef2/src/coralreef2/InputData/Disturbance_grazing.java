package coralreef2.InputData;

public class Disturbance_grazing {
		
	private double year ;
	private double cover_grazed ;
	
	public Disturbance_grazing (final double year ,
							 	final double cover_grazed ){
		this.year = year ;
		this.cover_grazed = cover_grazed ;

	}
	
	public double getYear(){
		return this.year ;
	}
	public double getCoverGrazed (){
		return this.cover_grazed ;
	}
	
}
