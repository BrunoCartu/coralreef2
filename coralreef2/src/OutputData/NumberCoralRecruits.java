package OutputData;

import coralreef2.common.Constants;
import repast.simphony.context.Context;

public class NumberCoralRecruits {
	private double year ;
	private int communityNumber ;
	private double numberRecruitsSp1 ; 
	private double numberRecruitsSp2 ; 
	private double numberRecruitsSp3 ; 
	private double numberRecruitsSp4 ; 
	private double numberRecruitsSp5 ; 
	private double numberRecruitsSp6 ; 
	private double numberRecruitsSp7 ; 
	private double numberRecruitsSp8 ; 
	private double numberRecruitsSp9 ; 
	private double numberRecruitsSp10 ; 
	private double numberRecruitsSp11 ; 
	private double numberRecruitsSp12 ;
//	private double numberRecruitsMacroalgae ; 
//	private double numberRecruitsTurf ;
//	private double numberRecruitsCCA ;
//	private double numberRecruitsAMA ;
//	private double numberRecruitsACA ;
//	private double numberRecruitsHalimeda ;
//	private double numberRecruitsSand ;
	private String event ;    // the event corresponding to the change in numberRecruits
	
	public NumberCoralRecruits(double year,
						   int  communityNumber,
						   double numberRecruitsSp1,
						   double numberRecruitsSp2,
						   double numberRecruitsSp3,
						   double numberRecruitsSp4,
						   double numberRecruitsSp5,
						   double numberRecruitsSp6,
						   double numberRecruitsSp7,
						   double numberRecruitsSp8,
						   double numberRecruitsSp9,
						   double numberRecruitsSp10,
						   double numberRecruitsSp11,
						   double numberRecruitsSp12,						   
//						   double numberRecruitsMacroalgae,
//						   double numberRecruitsTurf,
//						   double numberRecruitsCCA,
//						   double numberRecruitsAMA,
//						   double numberRecruitsACA,
//						   double numberRecruitsHalimeda,
//						   double numberRecruitsSand,
						   String event
							){
		
		this.year = year ;
		this.communityNumber = communityNumber ;
		this.numberRecruitsSp1 = numberRecruitsSp1 ;
		this.numberRecruitsSp2 = numberRecruitsSp2 ;
		this.numberRecruitsSp3 = numberRecruitsSp3 ;
		this.numberRecruitsSp4 = numberRecruitsSp4 ;
		this.numberRecruitsSp5 = numberRecruitsSp5 ;
		this.numberRecruitsSp6 = numberRecruitsSp6 ;
		this.numberRecruitsSp7 = numberRecruitsSp7 ;
		this.numberRecruitsSp8 = numberRecruitsSp8 ;
		this.numberRecruitsSp9 = numberRecruitsSp9 ;
		this.numberRecruitsSp10 = numberRecruitsSp10 ;
		this.numberRecruitsSp11 = numberRecruitsSp11 ;
		this.numberRecruitsSp12 = numberRecruitsSp12 ;
//		this.numberRecruitsMacroalgae = numberRecruitsMacroalgae;
//		this.numberRecruitsTurf = numberRecruitsTurf ;
//		this.numberRecruitsCCA = numberRecruitsCCA ;
//		this.numberRecruitsAMA = numberRecruitsAMA ;
//		this.numberRecruitsACA = numberRecruitsACA ;
//		this.numberRecruitsHalimeda = numberRecruitsHalimeda ;
//		this.numberRecruitsSand = numberRecruitsSand ;
		this.event = event ;
	}
	
	public double getYear(){
		return this.year ;
	}
	public int getCommunityNumber() {
		return this.communityNumber ;
	}
	public double getSp1numberRecruits( ){
		return this.numberRecruitsSp1  ;
	}
	public double getSp2numberRecruits( ){
		return this.numberRecruitsSp2  ;
	}
	public double getSp3numberRecruits( ){
		return this.numberRecruitsSp3  ;
	}
	public double getSp4numberRecruits( ){
		return this.numberRecruitsSp4  ;
	}
	public double getSp5numberRecruits( ){
		return this.numberRecruitsSp5  ;
	}
	public double getSp6numberRecruits( ){
		return this.numberRecruitsSp6  ;
	}
	public double getSp7numberRecruits( ){
		return this.numberRecruitsSp7  ;
	}
	public double getSp8numberRecruits( ){
		return this.numberRecruitsSp8  ;
	}
	public double getSp9numberRecruits( ){
		return this.numberRecruitsSp9  ;
	}
	public double getSp10numberRecruits( ){
		return this.numberRecruitsSp10  ;
	}
	public double getSp11numberRecruits( ){
		return this.numberRecruitsSp11  ;
	}
	public double getSp12numberRecruits( ){
		return this.numberRecruitsSp12  ;
	}
//	public double getMacroalgaenumberRecruits(){
//		return this.numberRecruitsMacroalgae ;
//	}
//	public double getTurfnumberRecruits( ){
//		return this.numberRecruitsTurf   ;
//	}
//	public double getCCAnumberRecruits( ){
//		return this.numberRecruitsCCA  ;
//	}
//	public double getAMAnumberRecruits(){
//		return this.numberRecruitsAMA ;
//	}
//	public double getACAnumberRecruits(){
//		return this.numberRecruitsACA ;
//	}
//	public double getHalimedanumberRecruits(){
//		return this.numberRecruitsHalimeda ;
//	}
//	public double getnumberRecruitsSand() {
//		return this.numberRecruitsSand ;
//	}
	public String getEvent() {
		return this.event ;
	}
	
	
	
	public void setYear(double year){
		this.year = year ;
	}
	public void setSp1numberRecruits(double numberRecruitsSp1){
		this.numberRecruitsSp1 = numberRecruitsSp1 ;
	}
	public void setSp2numberRecruits(double numberRecruitsSp2){
		this.numberRecruitsSp2 = numberRecruitsSp2 ;
	}
	public void setSp3numberRecruits(double numberRecruitsSp3){
		this.numberRecruitsSp3 = numberRecruitsSp3 ;
	}
	public void setSp4numberRecruits(double numberRecruitsSp4){
		this.numberRecruitsSp4 = numberRecruitsSp4 ;
	}
	public void setSp5numberRecruits(double numberRecruitsSp5){
		this.numberRecruitsSp5 = numberRecruitsSp5 ;
	}
	public void setSp6numberRecruits(double numberRecruitsSp6){
		this.numberRecruitsSp6 = numberRecruitsSp6 ;
	}
	public void setSp7numberRecruits(double numberRecruitsSp7){
		this.numberRecruitsSp7 = numberRecruitsSp7 ;
	}
	public void setSp8numberRecruits(double numberRecruitsSp8){
		this.numberRecruitsSp8 = numberRecruitsSp8 ;
	}
	public void setSp9numberRecruits(double numberRecruitsSp9){
		this.numberRecruitsSp9 = numberRecruitsSp9 ;
	}
	public void setSp10numberRecruits(double numberRecruitsSp10){
		this.numberRecruitsSp10 = numberRecruitsSp10 ;
	}
	public void setSp11numberRecruits(double numberRecruitsSp11){
		this.numberRecruitsSp11 = numberRecruitsSp11 ;
	}
	public void setSp12numberRecruits(double numberRecruitsSp12){
		this.numberRecruitsSp12 = numberRecruitsSp12 ;
	}
//	public void setMacroalgaenumberRecruits(double numberRecruitsMacroalgae){
//		this.numberRecruitsMacroalgae = numberRecruitsMacroalgae ;
//	}
//	public void setTurfnumberRecruits(double numberRecruitsTurf){
//		this.numberRecruitsTurf = numberRecruitsTurf ;
//	}
//	public void setCCAnumberRecruits(double numberRecruitsCCA){
//		this.numberRecruitsCCA = numberRecruitsCCA ;
//	}
//	public void setAMAnumberRecruits(double numberRecruitsAMA){
//		this.numberRecruitsAMA = numberRecruitsAMA ;
//	}
//	public void setACAnumberRecruits(double numberRecruitsACA){
//		this.numberRecruitsACA = numberRecruitsACA ;
//	}
//	public void setHalimedanumberRecruits(double numberRecruitsHalimeda){
//		this.numberRecruitsHalimeda = numberRecruitsHalimeda ;
//	}
//	public void setSandnumberRecruits(double numberRecruitsSand) {
//		this.numberRecruitsSand = numberRecruitsSand ;
//	}
	public void setevent(String eventToSet){
		this.event = eventToSet ;
	}
	

	public static NumberCoralRecruits createNumberCoralRecruitsInstance(){
		NumberCoralRecruits numberCoralRecruits_updated = new NumberCoralRecruits(0.0,Constants.communityNumber,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, "temp"); // create a single PercentageInstance which is updated every ticks --> for sink data 
		return numberCoralRecruits_updated ;
	}
	
}
