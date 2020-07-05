package it.polito.tdp.LATE.planeModel;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import it.polito.tdp.LATE.airportModel.Gate;
import it.polito.tdp.LATE.airportModel.Runway;
import it.polito.tdp.LATE.airportModel.TaxiRoutingEdge;

public class Flight {
	private int id;
	private Plane plane;
	
	private Runway runway;
	
	// Fields for results
	private int numGoAround = 0; 
	private LocalTime timeArrival = null;
	private LocalTime timeLanding = null;
	private LocalTime timeTakeOff = null;
	private Duration timeWaitingOnGround = Duration.ZERO;
	private Runway departureRunway;
	private Runway arrivalRunway; 
	private Gate gateUsed;
	private Duration timeAtGate;
	
	
	// Path
	private Gate gateAssignedTo;
	private List<TaxiRoutingEdge> nextEdges; 
	private TaxiRoutingEdge currentEdge;
	
	public Flight(int id, Plane plane) {
		this.id = id;
		this.plane = plane;
	}
	@Override
	public String toString() {
		return "Flight [id=" + id + ", plane=" + plane + "]";
	}
	public Runway getRunway() {
		return runway;
	}
	public void setRunway(Runway runway) {
		this.runway = runway;
	}
	public int getId() {
		return id;
	}
	public Plane getPlane() {
		return plane;
	}
	public Gate getGateAssignedTo() {
		return gateAssignedTo;
	}
	public void setGateAssignedTo(Gate gateAssignedTo) {
		this.gateAssignedTo = gateAssignedTo;
	}
	public List<TaxiRoutingEdge> getNextEdges() {
		return nextEdges;
	}
	public void setNextEdges(List<TaxiRoutingEdge> nextEdges) {
		this.nextEdges = nextEdges;
	}
	public TaxiRoutingEdge nextEdge() {
		return this.nextEdges.get(0);
	}
	public void removeNextEdge() {
		this.nextEdges.remove(0);
	}
	public TaxiRoutingEdge getCurrentEdge() {
		return currentEdge;
	}
	public void setCurrentEdge(TaxiRoutingEdge currentEdge) {
		this.currentEdge = currentEdge;
	}
	
	public void addGoAround() {
		this.numGoAround++;
	}
	
	public void addTimeWaitingOnGround(Duration waited) {
		this.timeWaitingOnGround = this.timeWaitingOnGround.plus(waited);
	}
	public LocalTime getTimeArrival() {
		return timeArrival;
	}
	public void setTimeArrival(LocalTime timeArrival) {
		this.timeArrival = timeArrival;
	}
	public LocalTime getTimeLanding() {
		return timeLanding;
	}
	public void setTimeLanding(LocalTime timeLanding) {
		this.timeLanding = timeLanding;
	}
	public LocalTime getTimeTakeOff() {
		return timeTakeOff;
	}
	public void setTimeTakeOff(LocalTime timeTakeOff) {
		this.timeTakeOff = timeTakeOff;
	}
	public Runway getDepartureRunway() {
		return departureRunway;
	}
	public void setDepartureRunway(Runway departureRunway) {
		this.departureRunway = departureRunway;
	}
	public Runway getArrivalRunway() {
		return arrivalRunway;
	}
	public void setArrivalRunway(Runway arrivalRunway) {
		this.arrivalRunway = arrivalRunway;
	}
	public int getNumGoAround() {
		return numGoAround;
	}
	public Duration getTimeWaitingOnGround() {
		return timeWaitingOnGround;
	}
	
	public Duration getGroundCycleTime() {
		return Duration.between(this.timeLanding, this.timeTakeOff);
	}
	
	public Duration getTotalCycleTime() {
		return Duration.between(this.timeArrival, this.timeTakeOff);
	}
	public Gate getGateUsed() {
		return gateUsed;
	}
	public void setGateUsed(Gate gateUsed) {
		this.gateUsed = gateUsed;
	}
	public Duration getTimeAtGate() {
		return timeAtGate;
	}
	public void setTimeAtGate(Duration timeAtGate) {
		this.timeAtGate = timeAtGate;
	}
	
	
	
	
}
