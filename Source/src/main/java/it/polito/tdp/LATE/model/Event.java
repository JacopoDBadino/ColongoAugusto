package it.polito.tdp.LATE.model;

import java.time.LocalTime;

import it.polito.tdp.LATE.planeModel.Flight;

public class Event implements Comparable<Event>{
	public static enum EventType{
		REQUEST_RUNWAY_ARRIVAL,
		ASK_LANDING_CLEARANCE,
		LEAVING_RUNWAY,
		ARRIVED_TO_NODE,
		ARRIVED_TO_GATE,
		REQUEST_RUNWAY_DEPARTURE,
		LEAVING_GATE,
		ASK_TAKEOFF_CLEARANCE,
		AIRBORNE
	}
	
	private LocalTime time;
	private EventType type;
	private Flight flight;
	
	public LocalTime getTime() {
		return time;
	}
	public EventType getType() {
		return type;
	}
	public Flight getFlight() {
		return flight;
	}
	
	public Event(LocalTime time, EventType type, Flight flight) {
		this.time = time;
		this.type = type;
		this.flight = flight;
	}
	
	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.getTime());
	}
	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + ", flight=" + flight + "]";
	}
	
	
}
