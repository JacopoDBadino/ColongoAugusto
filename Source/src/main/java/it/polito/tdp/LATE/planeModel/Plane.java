package it.polito.tdp.LATE.planeModel;

public class Plane {
	private String manufacturer;
	private String model;
	private double wingspan;
	private double approachSpeed; // in m/s
	private double taxiSpeed; // in m/s
	private double MTOW; // in m/s
	
	public Plane(String manufacturer, String model, double wingspan, double approachSpeed, double MTOW){
		this.manufacturer = manufacturer;
		this.model = model;
		this.wingspan = wingspan;
		this.approachSpeed = approachSpeed;
		this.taxiSpeed = 10.289; // 20kts
		this.MTOW = MTOW;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getModel() {
		return model;
	}

	public double getWingspan() {
		return wingspan;
	}

	public double getApproachSpeed() {
		return approachSpeed;
	}
	
	// returns plane runway time in seconds
	public int calculateRunwayTime(double runwayLength) {
		double decel = 2.012;
		
		double decelTime = (this.approachSpeed - this.taxiSpeed)/decel;
		double decelLength = this.approachSpeed * decelTime - 0.5 * decel * decelTime * decelTime;
		double remainingLength = runwayLength - decelLength;
		
		return (int) (remainingLength / this.taxiSpeed + decelTime);		
	}
	
	// returns time from landing clearance to landing
	public int calculateApproachTime(double goAroundThreshold) {
		double landingClearanceDistance = 1852 * goAroundThreshold;
		return (int) (landingClearanceDistance / this.approachSpeed);
	}
	
	public int calculateTaxiTime(double edgeLength) {
		return (int) (edgeLength / this.taxiSpeed);
	}

	public int calculateFuelingTime() {
		double fuelDensity = 0.719; //kg/L
		double refuelSpeed = 50.0; //L/s
		double fuelWeight = 0.17 * this.MTOW;
		
		return (int) ((fuelWeight/fuelDensity)/refuelSpeed);
	}
	
	@Override
	public String toString() {
		return "Plane [" + manufacturer + " " +  model + "]";
	}

	public int calculateTakeOffTime(double length) {
		double acc = 1.0;
		return (int) Math.sqrt(2 * length / acc);
	}

	public double getMTOW() {
		return MTOW;
	}
	
	
}


