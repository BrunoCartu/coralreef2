package coralreef2.InputData;

public class Disturbances_priority {
	private double year ;
	private String priority_1 ;
	private String priority_2 ;
	private String priority_3 ;
	private String season ;
	
	public Disturbances_priority (final double year ,
								final String priority_1 ,
								final String priority_2 ,
								final String priority_3 ,
								final String season){
		this.year = year ;
		this.priority_1 = priority_1 ;
		this.priority_2 = priority_2 ;
		this.priority_3 = priority_3 ;
		this.season = season ;
	}
	
	public double getYear(){
		return this.year ;
	}
	public String getPriority_1(){
		return this.priority_1;
	}
	public String getPriority_2(){
		return this.priority_2 ;
	}
	public String getPriority_3(){
		return this.priority_3 ;
	}
	public String getSeason(){
		return this.season ;
	}
}
