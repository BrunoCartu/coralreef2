package coralreef2.InputData;
	/**
	 * each agent created by this class is going to contain the information contained on 1 line of the data file.
	 * @author Bruno
	 *
	 */

public class CycloneData {
	private double yearCyclone;
	private double percentageCoralDamaged;
	private double percentageMacroalgaeDamaged;
	private double percentageTurfDamaged;
	private double percentageCCADamaged;
	private String intensity;
	private String habitat;
	
	public CycloneData (final double yearCyclone,
						final double percentageCoralDamaged,
						final double percentageMacroalgaeDamaged,
			            final double percentageTurfDamaged,
			            final double percentageCCADamaged,
			            final String intensity,
			            final String habitat){
		this.yearCyclone = yearCyclone;
		this.percentageCoralDamaged = percentageCoralDamaged;
		this.percentageMacroalgaeDamaged = percentageMacroalgaeDamaged;
		this.percentageTurfDamaged = percentageTurfDamaged;
		this.percentageCCADamaged = percentageCCADamaged;
		this.intensity = intensity;
		this.habitat = habitat;	
	}
	
	public double getYearCyclone(){
		return this.yearCyclone;
	}
	public double getPercentageCoralDamaged (){
		return this.percentageCoralDamaged;
	}
	public double getPercentageMacroalgaeDamaged (){
		return this.percentageMacroalgaeDamaged;
	}
	public double getPercentageTurfDamaged (){
		return this.percentageTurfDamaged;
	}
	public double getPercentageCCADamaged (){
		return this.percentageCCADamaged;
	}
	public String getIntensity(){
		return this.intensity;
	}
	public String getHabitat(){
		return this.habitat;
	}
}


