package coralreef2.InputData;

public class Disturbance_cyclone {
	private double year ;
	private double cyclone_DMT ;
	
	public Disturbance_cyclone (final double year ,
							final double cyclone_DMT ){
		this.year = year ;
		this.cyclone_DMT = cyclone_DMT ;

	}
	
	public double getYear(){
		return this.year ;
	}
	public double getCyclone_DMT (){
		return this.cyclone_DMT ;
	}
}
