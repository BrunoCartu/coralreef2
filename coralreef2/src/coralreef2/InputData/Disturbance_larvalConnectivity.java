package coralreef2.InputData;

public class Disturbance_larvalConnectivity {

	private double year ;
	private double numberLarvae_m2 ;
	
	public  Disturbance_larvalConnectivity(final double year,
										  final double numberLarvae_m2) {
		this.year = year ;
		this.numberLarvae_m2 = numberLarvae_m2 ;
	}

	public double getYear() {
		return year;
	}

	public void setYear(double year) {
		this.year = year;
	}

	public double getNumberLarvae_m2() {
		return numberLarvae_m2;
	}

	public void setNumberLarvae_m2(double numberLarvae_m2) {
		this.numberLarvae_m2 = numberLarvae_m2;
	}
	
	

}
