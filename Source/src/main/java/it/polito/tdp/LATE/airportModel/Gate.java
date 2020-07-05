package it.polito.tdp.LATE.airportModel;

import java.util.Set;

import it.polito.tdp.LATE.airportModel.TaxiRoutingNode.TaxiRoutingNodeType;

public class Gate {
	public static enum GateType{
		GATE,
		TIE_DOWN
	}
	
	private double latitude; // Gate latitude
	private double longitude; // Gate longitude
	private GateType type; // Gate type. 0 -> gate; 1 -> tie_down
	private String id; // Gate id.
	private double maxWingspan; // From icao width code: reference at https://www.skybrary.aero/index.php/ICAO_Aerodrome_Reference_Code
	private int operationType; // Operation type allowed at gate. 0 -> airline (passenger_flights); 1 -> general_aviation; 2 -> cargo
	private TaxiRoutingNode node;
	
	public Gate(double latitude, double longitude, GateType type, String id) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.id = id;
		this.maxWingspan = 0;
		this.operationType = 0;
		this.node = null;
	}

	public GateType getType() {
		return type;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	
	public void setMaxWingspan(double maxWingspan) {
		this.maxWingspan = maxWingspan;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getId() {
		return id;
	}

	public double getMaxWingspan() {
		return maxWingspan;
	}

	public boolean addGateNode(Set<TaxiRoutingNode> vertexSet) {
		TaxiRoutingNode gate = new TaxiRoutingNode(0, latitude, longitude, TaxiRoutingNodeType.BOTH);
		TaxiRoutingNode nearest = null;
		double minDistance = Double.MAX_VALUE;
		for(TaxiRoutingNode n: vertexSet) {
			double thisDistance = TaxiRoutingNode.calculateDistance(n, gate);
			if (thisDistance < minDistance) {
				nearest = n;
				minDistance = thisDistance;
			}
		}
		if (nearest != null) {
			this.node = nearest;
			return true;
		}
		return false;
	}

	public TaxiRoutingNode getNode() {
		return node;
	}
	
	
	
	
}
