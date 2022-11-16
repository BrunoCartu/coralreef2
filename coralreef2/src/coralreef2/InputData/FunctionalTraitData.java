package coralreef2.InputData;

/**
 * 
 * @author carturan
 *
 */
public class FunctionalTraitData {

	private String substrate_category;
	private String substrate_subcategory;
	private String species;
	private double aggressiveness;
	private String coloniality;
	private double colony_max_diameter;
	private double corallite_area;
	private double egg_diameter;
	private double fecundity_polyp;
	private String growth_form;
	private double growth_rate;
	private String mode_larval_development;
	private double reduced_scattering_coefficient;
//	private double response_bleaching_index ;
//	private double skeletal_density;
	private double size_maturity;
	private double age_maturity;
	private int red;
	private int green; 
	private int blue;
	private double bleaching_probability ;
	//private double x0_logitBleaching ;
	//private double r_logitBleaching ;
	//private double x0_logitBRI ;		// similar to x0_logitBleaching but value obtained using taxon-BRI directly instead of intrinsic bleaching index defined with resistance traits
	//private double r_logitBRI ;		// similar to r_logitBleaching but value obtained using taxon-BRI directly instead of intrinsic bleaching index defined with resistance traits
	private String sexual_system ;
	private double correction_coeff_polypFecundity ;
	
	public FunctionalTraitData (final String substrate_category,
								final String substrate_subcategory,
								final String species,
								final double aggressiveness,
								final String coloniality,
								final double colony_max_diameter,
								final double corallite_area,
								final double egg_diameter,
								final double fecundity_polyp,
			                    final String growth_form,
			          	        final double growth_rate,
			          	        final String mode_larval_development,
			          	        final double reduced_scattering_coefficient,
//			          	        final double response_bleaching_index,
//			          	        final double skeletal_density,
			          	        final double size_maturity,
			          	        final double age_maturity,
			                    final int red,
			                    final int green,
			                    final int blue,
			                    final double bleaching_probability,
			                    final String sexual_system,
			                    final double correction_coeff_polypFecundity){
//			                    final double x0_logitBleaching,
//			                    final double r_logitBleaching,
//			                    final double x0_logitBRI,
//			                    final double r_logitBRI){
		
		this.substrate_category = substrate_category;
		this.substrate_subcategory = substrate_subcategory;
		this.species = species;
		this.aggressiveness = aggressiveness;
		this.coloniality = coloniality;
		this.colony_max_diameter = colony_max_diameter;
		this.corallite_area = corallite_area;
		this.egg_diameter = egg_diameter;
		this.fecundity_polyp = fecundity_polyp;
		this.growth_form = growth_form;
		this.growth_rate = growth_rate;
		this.mode_larval_development = mode_larval_development;
		this.reduced_scattering_coefficient = reduced_scattering_coefficient;
//		this.response_bleaching_index = response_bleaching_index;
//		this.skeletal_density = skeletal_density ;
		this.size_maturity = size_maturity;
		this.age_maturity = age_maturity;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.bleaching_probability = bleaching_probability ;
//		this.x0_logitBleaching = x0_logitBleaching ;
//		this.r_logitBleaching = r_logitBleaching ;
//		this.x0_logitBRI = x0_logitBRI ;
//		this.r_logitBRI = r_logitBRI ;
		this.sexual_system = sexual_system ;
		this.correction_coeff_polypFecundity = correction_coeff_polypFecundity ;
	}
	
	public String getSubstrateCategory(){
		return substrate_category;
	}
	public String getSubstrateSubCategory(){
		return substrate_subcategory;
	}
	public String getSpecies(){
		return this.species;
	}
	public double getAggressiveness(){
		return this.aggressiveness;
	}
	public String getColoniality(){
		return this.coloniality;
	}
	public double getColonyMaxDiameter(){
		return this.colony_max_diameter;
	}
	public double getCoralliteArea(){
		return this.corallite_area;
	}
	public double getEggDiameter(){
		return this.egg_diameter;
	}
	public double getFecundityPolyp(){
		return this.fecundity_polyp;
	}
	public String getGrowthForm(){
		return this.growth_form;
	}
	public double getGrowthRate(){
		return this.growth_rate;
	}
 	public String getModeLarvalDevelopment(){
		return this.mode_larval_development;
	}
	public double getReducedScatteringCoefficient(){
		return this.reduced_scattering_coefficient;
	}
//	public double getResponseBleachingIndex(){
//		return this.response_bleaching_index;
//	} 
//	public double getSkeletalDensity(){
//		return this.skeletal_density;
//	}
	public double getSizeMaturity(){
		return this.size_maturity;
	}
	public double getAgeMaturity(){
		return this.age_maturity;
	}
	public int getRed(){
		return this.red;
	}
	public int getGreen(){
		return this.green;
	}
	public int getBlue(){
		return this.blue;
	}
	public double getBleachingProbability(){
		return this.bleaching_probability;
	}
//	public double getX0LogitBleaching(){
//		return this.x0_logitBleaching ;
//	}
//	public double getRLogitBleaching(){
//		return this.r_logitBleaching ;
//	}
//	public double getX0LogitBRI(){
//		return this.x0_logitBRI ;
//	}
//	public double getRLogitBRI(){
//		return this.r_logitBRI ;
//	}
	public String getSexualSystem() {
		return this.sexual_system ;
	}
	public double getCorrectionCoeffPolypFecundity() {
		return this.correction_coeff_polypFecundity ;
	}
}

