package OutputData;

import coralreef2.ContextCoralReef2;
import coralreef2.common.Constants;
import repast.simphony.context.Context;

public class PercentageCover {
	
	protected Context context;
	private double year ;
	private int communityNumber ;
	private double coverSp1 ; 
	private double coverSp2 ; 
	private double coverSp3 ; 
	private double coverSp4 ; 
	private double coverSp5 ; 
	private double coverSp6 ; 
	private double coverSp7 ; 
	private double coverSp8 ; 
	private double coverSp9 ; 
	private double coverSp10 ; 
	private double coverSp11 ; 
	private double coverSp12 ;
	private double coverMacroalgae ; 
	private double coverTurf ;
	private double coverCCA ;
	private double coverAMA ;
	private double coverACA ;
	private double coverHalimeda ;
	private double coverSand ;
	private String event ;    // the event corresponding to the change in cover
	
	public PercentageCover( Context context,
						   double year,
						   int communityNumber,
						   double coverSp1,
						   double coverSp2,
						   double coverSp3,
						   double coverSp4,
						   double coverSp5,
						   double coverSp6,
						   double coverSp7,
						   double coverSp8,
						   double coverSp9,
						   double coverSp10,
						   double coverSp11,
						   double coverSp12,						   
						   double coverMacroalgae,
						   double coverTurf,
						   double coverCCA,
						   double coverAMA,
						   double coverACA,
						   double coverHalimeda,
						   double coverSand,
						   String event
							){
		this.context = context ;
		this.year = year ;
		this.communityNumber = communityNumber ;
		this.coverSp1 = coverSp1 ;
		this.coverSp2 = coverSp2 ;
		this.coverSp3 = coverSp3 ;
		this.coverSp4 = coverSp4 ;
		this.coverSp5 = coverSp5 ;
		this.coverSp6 = coverSp6 ;
		this.coverSp7 = coverSp7 ;
		this.coverSp8 = coverSp8 ;
		this.coverSp9 = coverSp9 ;
		this.coverSp10 = coverSp10 ;
		this.coverSp11 = coverSp11 ;
		this.coverSp12 = coverSp12 ;
		this.coverMacroalgae = coverMacroalgae;
		this.coverTurf = coverTurf ;
		this.coverCCA = coverCCA ;
		this.coverAMA = coverAMA ;
		this.coverACA = coverACA ;
		this.coverHalimeda = coverHalimeda ;
		this.coverSand = coverSand ;
		this.event = event ;
		
		context.add(this) ;
	}
	
	public double getYear(){
		return this.year ;
	}
	public int getCommunityNumber() {
		return this.communityNumber ;
	}
	public double getSp1Cover( ){
		return this.coverSp1  ;
	}
	public double getSp2Cover( ){
		return this.coverSp2  ;
	}
	public double getSp3Cover( ){
		return this.coverSp3  ;
	}
	public double getSp4Cover( ){
		return this.coverSp4  ;
	}
	public double getSp5Cover( ){
		return this.coverSp5  ;
	}
	public double getSp6Cover( ){
		return this.coverSp6  ;
	}
	public double getSp7Cover( ){
		return this.coverSp7  ;
	}
	public double getSp8Cover( ){
		return this.coverSp8  ;
	}
	public double getSp9Cover( ){
		return this.coverSp9  ;
	}
	public double getSp10Cover( ){
		return this.coverSp10  ;
	}
	public double getSp11Cover( ){
		return this.coverSp11  ;
	}
	public double getSp12Cover( ){
		return this.coverSp12  ;
	}
	public double getMacroalgaeCover(){
		return this.coverMacroalgae ;
	}
	public double getTurfCover( ){
		return this.coverTurf   ;
	}
	public double getCCACover( ){
		return this.coverCCA  ;
	}
	public double getAMACover(){
		return this.coverAMA ;
	}
	public double getACACover(){
		return this.coverACA ;
	}
	public double getHalimedaCover(){
		return this.coverHalimeda ;
	}
	public double getCoverSand() {
		return this.coverSand ;
	}
	public String getEvent() {
		return this.event ;
	}
	
	public void setYear(double year){
		this.year = year ;
	}
	public void setSp1Cover(double coverSp1){
		this.coverSp1 = coverSp1 ;
	}
	public void setSp2Cover(double coverSp2){
		this.coverSp2 = coverSp2 ;
	}
	public void setSp3Cover(double coverSp3){
		this.coverSp3 = coverSp3 ;
	}
	public void setSp4Cover(double coverSp4){
		this.coverSp4 = coverSp4 ;
	}
	public void setSp5Cover(double coverSp5){
		this.coverSp5 = coverSp5 ;
	}
	public void setSp6Cover(double coverSp6){
		this.coverSp6 = coverSp6 ;
	}
	public void setSp7Cover(double coverSp7){
		this.coverSp7 = coverSp7 ;
	}
	public void setSp8Cover(double coverSp8){
		this.coverSp8 = coverSp8 ;
	}
	public void setSp9Cover(double coverSp9){
		this.coverSp9 = coverSp9 ;
	}
	public void setSp10Cover(double coverSp10){
		this.coverSp10 = coverSp10 ;
	}
	public void setSp11Cover(double coverSp11){
		this.coverSp11 = coverSp11 ;
	}
	public void setSp12Cover(double coverSp12){
		this.coverSp12 = coverSp12 ;
	}
	public void setMacroalgaeCover(double coverMacroalgae){
		this.coverMacroalgae = coverMacroalgae ;
	}
	public void setTurfCover(double coverTurf){
		this.coverTurf = coverTurf ;
	}
	public void setCCACover(double coverCCA){
		this.coverCCA = coverCCA ;
	}
	public void setAMACover(double coverAMA){
		this.coverAMA = coverAMA ;
	}
	public void setACACover(double coverACA){
		this.coverACA = coverACA ;
	}
	public void setHalimedaCover(double coverHalimeda){
		this.coverHalimeda = coverHalimeda ;
	}
	public void setSandCover(double coverSand) {
		this.coverSand = coverSand ;
	}
	public void setevent(String eventToSet){
		this.event = eventToSet ;
	}
		
	public static PercentageCover createPercentageCoverInstance(Context context){
		PercentageCover percentageCover_updated = new PercentageCover(context,0.0,Constants.communityNumber,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,"temp"); // create a single PercentageInstance which is updated every ticks --> for sink data 
		return percentageCover_updated ;
	}
	
}



