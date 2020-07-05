package it.polito.tdp.LATE.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.LATE.airportModel.Gate;
import it.polito.tdp.LATE.airportModel.Runway;
import it.polito.tdp.LATE.airportModel.TaxiRoutingEdge;
import it.polito.tdp.LATE.planeModel.Flight;;

public class SimulationOutput {
	private List<Flight> flights;
	private Map<Gate, Duration> gatesUsage;
	private Map<Runway, Duration> runwayUsage;
	private Map<TaxiRoutingEdge, Integer> edgesUsage;
	private LocalTime timeout;
	private LocalTime lastAction;
	
	
	public SimulationOutput() {
		this.flights = new ArrayList<Flight>();
		this.gatesUsage = new HashMap<Gate, Duration>();
		this.edgesUsage = new HashMap<TaxiRoutingEdge, Integer>();
		this.runwayUsage = new HashMap<Runway, Duration>();
	}
	
	public void addFlight(Flight flight) {
		this.flights.add(flight);
	}
	
	public void addGate(Gate gate) {
		this.gatesUsage.put(gate, Duration.ZERO);
	}
	
	public void addEdge(TaxiRoutingEdge edge) {
		this.edgesUsage.put(edge, 0);
	}
	
	public void addRunway (Runway runway) {
		this.runwayUsage.put(runway, Duration.ZERO);
	}
	
	public void addTimeGate(Gate gate, Duration duration) {
		Duration value = this.gatesUsage.get(gate).plus(duration);
		this.gatesUsage.replace(gate, value);
	}
	
	public void addVisitEdge(TaxiRoutingEdge edge) {
		int value = this.edgesUsage.get(edge) + 1;
		this.edgesUsage.replace(edge, value);
	}
	
	public void addTimeRunway(Runway runway, Duration duration) {
		Duration value = this.runwayUsage.get(runway).plus(duration);
		this.runwayUsage.replace(runway, value);
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public Map<Gate, Duration> getGatesUsage() {
		return gatesUsage;
	}

	public Map<TaxiRoutingEdge, Integer> getEdgesUsage() {
		return edgesUsage;
	}

	public LocalTime getTimeout() {
		return timeout;
	}

	public void setTimeout(LocalTime timeout) {
		this.timeout = timeout;
	}

	public LocalTime getLastAction() {
		return lastAction;
	}

	public void setLastAction(LocalTime lastAction) {
		this.lastAction = lastAction;
	}

	public Map<Runway, Duration> getRunwayUsage() {
		return runwayUsage;
	}
	
	
}
