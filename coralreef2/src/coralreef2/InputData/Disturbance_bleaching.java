package coralreef2.InputData;

public class Disturbance_bleaching {
	private double year ;
	private double bleaching_DHW ;
	
	public Disturbance_bleaching (final double year ,
								  final double bleaching_DHW ){
		this.year = year ;
		this.bleaching_DHW = bleaching_DHW ;

	}
	
	public double getYear(){
		return this.year ;
	}
	public double getBleaching_DHW (){
		return this.bleaching_DHW ;
	}
}



