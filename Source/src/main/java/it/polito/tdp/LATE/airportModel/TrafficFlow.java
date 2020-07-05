package it.polito.tdp.LATE.airportModel;

import java.util.ArrayList;
import java.util.List;

public class TrafficFlow {
	
	public static enum TrafficFlowType{
		ARRIVAL,
		DEPARTURE
	}
	
	private String name; // Traffic flow ident name
	private List<Runway> arrivalRules;
	private List<Runway> departureRules;
	
	public TrafficFlow(String name) {
		this.name = name;
		this.arrivalRules = new ArrayList<Runway>();
		this.departureRules = new ArrayList<Runway>();
	}
	
	public void addRunwayInUseRule(Runway runway, TrafficFlowType type) {
		switch (type) {
		case ARRIVAL:
			this.arrivalRules.add(runway);
			break;
		case DEPARTURE:
			this.departureRules.add(runway);
			break;
		default:
			break;
		}
	}

	public String getName() {
		return name;
	}

	public List<Runway> getArrivalRules() {
		return arrivalRules;
	}

	public List<Runway> getDepartureRules() {
		return departureRules;
	}

	@Override
	public String toString() {
		return name;
	}
	


}

