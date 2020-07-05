package it.polito.tdp.LATE.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import it.polito.tdp.LATE.airportModel.Airport;
import it.polito.tdp.LATE.airportModel.Gate;
import it.polito.tdp.LATE.airportModel.Runway;
import it.polito.tdp.LATE.airportModel.TaxiRoutingEdge;
import it.polito.tdp.LATE.airportModel.TaxiRoutingNode;
import it.polito.tdp.LATE.airportModel.TrafficFlow;
import it.polito.tdp.LATE.model.Event.EventType;
import it.polito.tdp.LATE.planeModel.Flight;
import it.polito.tdp.LATE.planeModel.Plane;

public class Simulator {
	Random random = new Random();

	// PARAMETRI DI SIMULAZIONE
	private Airport airport;

	// Arrivi
	private int arrivalsPerHour = 50;
	private List<Plane> planeRoster;

	// Procedure
	private String trafficFlowName;

	// Tempo di simulazione
	private int simulationHours = 1;
	private Duration simulationTime;

	private int timeoutHours = 23;
	private LocalTime simulationTimeout;

	// Tempi di processo
	private double goAroundThreshold = 3;
	private int goAroundMinutes = 10;
	private Duration goAroundTime;

	private int incomingMinutes = 10;
	private Duration incomingTime;

	private int waitOnGroundSeconds = 20;
	private Duration waitOnGroundTime;

	private int paxTurnaroundMinutes = 30;
	private Duration paxTurnaroundTime;

	// Pathfinding
	private DijkstraShortestPath<TaxiRoutingNode, TaxiRoutingEdge> pathfinder;

	// STATO DEL SISTEMA
	Map<TaxiRoutingEdge, Flight> usedEdges;
	Map<Gate, Flight> usedGates;
	Map<Runway, Flight> usedRunways;
	int numArrivals = 0;
	int numDepartures = 0;

	// OUTPUT DA CALCOLARE
	SimulationOutput output;
	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;

	// INIT
	public void init() {
		this.pathfinder = new DijkstraShortestPath<TaxiRoutingNode, TaxiRoutingEdge>(this.airport.getGraph());
		this.output = new SimulationOutput();

		this.usedEdges = new HashMap<TaxiRoutingEdge, Flight>();
		this.usedGates = new HashMap<Gate, Flight>();
		this.usedRunways = new HashMap<Runway, Flight>();
		this.numArrivals = 0;
		this.numDepartures = 0;

		this.simulationTime = Duration.ofHours(simulationHours);
		this.simulationTimeout = LocalTime.MIDNIGHT.plus(Duration.ofHours(timeoutHours));
		this.goAroundTime = Duration.ofMinutes(this.goAroundMinutes);
		this.incomingTime = Duration.ofMinutes(this.incomingMinutes);
		this.waitOnGroundTime = Duration.ofSeconds(this.waitOnGroundSeconds);
		this.paxTurnaroundTime = Duration.ofMinutes(this.paxTurnaroundMinutes);

		this.queue = new PriorityQueue<Event>();
		
		// CREA GLI EVENTI DI ARRIVO

		LocalTime eventTime = LocalTime.MIDNIGHT;
		LocalTime endTime = LocalTime.MIDNIGHT.plus(simulationTime);
		Duration interval = Duration.ofSeconds(3600 / arrivalsPerHour);
		int flightID = 0;
		while (eventTime.isBefore(endTime)) {
			Flight newFlight = new Flight(flightID++, planeRoster.get(random.nextInt(planeRoster.size())));
			this.output.addFlight(newFlight);
			
			Event newArrival = new Event(eventTime, EventType.REQUEST_RUNWAY_ARRIVAL, newFlight);
			queue.add(newArrival);
			newFlight.setTimeArrival(eventTime);
			eventTime = eventTime.plus(interval);
			
			this.output.setTimeout(this.simulationTimeout);
		}
		
		// OUTPUT SETUP
		for (Gate g: this.airport.getGates()) {
			this.output.addGate(g);
		}
		for (Runway r: this.airport.getRunways()) {
			this.output.addRunway(r);
		}
	}

	// RUN

	public void run() {
		while (!this.queue.isEmpty() && this.queue.peek().getTime().isBefore(this.simulationTimeout)) {
			Event e = this.queue.poll();
			this.output.setLastAction(e.getTime());
			//System.out.println(e);
			processEvent(e);
		}
	}

	// PROCESS EVENT

	private void processEvent(Event e) {
		switch (e.getType()) {
		
		case REQUEST_RUNWAY_ARRIVAL:
			TrafficFlow tfA = this.airport.getTrafficFlow(this.trafficFlowName);
			Runway runwayChosenA = tfA.getArrivalRules().get(this.numArrivals++ % tfA.getArrivalRules().size());
			e.getFlight().setRunway(runwayChosenA);

			queue.add(new Event(e.getTime().plus(this.incomingTime), EventType.ASK_LANDING_CLEARANCE, e.getFlight()));
			return;

		case ASK_LANDING_CLEARANCE:
			Runway runwayLanding = e.getFlight().getRunway();
			if (this.usedRunways.get(runwayLanding) == null
					&& this.usedRunways.get(runwayLanding.getOtherSide()) == null) {
				// LAND
				// reserve runway
				this.usedRunways.put(runwayLanding, e.getFlight());
				// schedule completed landing
				Plane planeLanding = e.getFlight().getPlane();
				Duration landingReserveTime = Duration.ofSeconds(planeLanding.calculateApproachTime(this.goAroundThreshold)
						+ planeLanding.calculateRunwayTime(e.getFlight().getRunway().length()));
				queue.add(new Event(e.getTime().plus(landingReserveTime), EventType.LEAVING_RUNWAY, e.getFlight()));
				// System.out.println("Landing on RWY " + runwayLanding.getNum());
				
				// Stats for landing
				this.output.addTimeRunway(runwayLanding, landingReserveTime);
				e.getFlight().setTimeLanding(e.getTime().plus(landingReserveTime));
				e.getFlight().setArrivalRunway(runwayLanding);
				
			} else {
				// GO AROUND
				queue.add(
						new Event(e.getTime().plus(this.goAroundTime), EventType.ASK_LANDING_CLEARANCE, e.getFlight()));
				//System.out.println("Go around");

				// Stats
				e.getFlight().addGoAround();
			}
			return;

		case LEAVING_RUNWAY:
			if (e.getFlight().getGateAssignedTo() == null) {
				// REQUEST GATE ASSIGNMENT
				List<Gate> gatesAccepting = this.airport.getGatesAccepting(e.getFlight().getPlane().getWingspan());
				
				if (gatesAccepting.size() == 0) {
					throw new RuntimeException("No gates can accept this plane. Stopping simulation.");
				}
				
				// shuffle to randomly select one gate
				Collections.shuffle(gatesAccepting);
				boolean notAssigned = true;
				for (Gate g : gatesAccepting) {
					if (this.usedGates.get(g) == null) {
						e.getFlight().setGateAssignedTo(g);
						this.usedGates.put(g, e.getFlight());
						notAssigned = false;
						break;
					}
				}

				if (notAssigned) {
					// SCHEDULE AGAIN FOR NEXT TIME
					queue.add(new Event(e.getTime().plus(this.waitOnGroundTime), EventType.LEAVING_RUNWAY,
							e.getFlight()));
					// Stats for waiting
					e.getFlight().addTimeWaitingOnGround(waitOnGroundTime);
					return;
				}
				// ASK FOR PATH
				TaxiRoutingNode destNode = e.getFlight().getGateAssignedTo().getNode();
				TaxiRoutingNode sourceNode;
				double minDist = Double.MAX_VALUE;
				for (TaxiRoutingNode n : e.getFlight().getRunway().getOtherSide().getNodes()) {
					double dist = TaxiRoutingNode.calculateDistance(n, destNode);
					GraphPath<TaxiRoutingNode, TaxiRoutingEdge> path = pathfinder.getPath(n, destNode);
					if (dist < minDist && path != null) {
						minDist = dist;
						sourceNode = n;
						e.getFlight().setNextEdges(path.getEdgeList());
					}
				}
			}

			// CHECK IF NEXT EDGE IS FREE
			if (this.usedEdges.get(e.getFlight().nextEdge()) == null) {
				// IF TRUE: REMOVE RWY AND MOVE TO NEXT EDGE
				// remove runway block
				this.usedRunways.remove(e.getFlight().getRunway());
				e.getFlight().setRunway(null);

				// move to next edge
				this.usedEdges.put(e.getFlight().nextEdge(), e.getFlight()); // block edge
				e.getFlight().setCurrentEdge(e.getFlight().nextEdge()); // set current edge
				e.getFlight().removeNextEdge(); // remove current edge from edge list
				queue.add(new Event(
						e.getTime()
								.plus(Duration.ofSeconds(e.getFlight().getPlane()
										.calculateTaxiTime(e.getFlight().getCurrentEdge().getLength()))),
						EventType.ARRIVED_TO_NODE, e.getFlight()));
			} else {
				// IF FALSE: RESCHEDULE LEAVING RUNWAY
				queue.add(new Event(e.getTime().plus(this.waitOnGroundTime), EventType.LEAVING_RUNWAY, e.getFlight()));
				// add stats for waiting
				this.output.addTimeRunway(e.getFlight().getRunway(), this.waitOnGroundTime);
				e.getFlight().addTimeWaitingOnGround(waitOnGroundTime);
			}
			return;
			
		case ARRIVED_TO_NODE:
			if (e.getFlight().getNextEdges().size() == 0) {
				// ARRIVED TO GATE OR RUNWAY
				if (e.getFlight().getGateAssignedTo() == null) {
					// RUNWAY
					queue.add(new Event(e.getTime().plus(this.waitOnGroundTime), EventType.ASK_TAKEOFF_CLEARANCE,
							e.getFlight()));
					return;
				} else {
					// GATE
					queue.add(new Event(e.getTime().plus(Duration.ofSeconds(30)), EventType.ARRIVED_TO_GATE,
							e.getFlight()));
				}
			} else {
				// CHECK IF NEXT EDGE IS FREE
				if (this.usedEdges.get(e.getFlight().nextEdge()) == null) {
					// IF TRUE:
					this.usedEdges.remove(e.getFlight().getCurrentEdge()); // remove block from current edge
					// move to next edge
					this.usedEdges.put(e.getFlight().nextEdge(), e.getFlight()); // block next edge
					e.getFlight().setCurrentEdge(e.getFlight().nextEdge()); // set next edge as current edge
					e.getFlight().removeNextEdge(); // remove current edge from list
					queue.add(new Event(
							e.getTime()
									.plus(Duration.ofSeconds(e.getFlight().getPlane()
											.calculateTaxiTime(e.getFlight().getCurrentEdge().getLength()))),
							EventType.ARRIVED_TO_NODE, e.getFlight()));
				} else {
					// IF FALSE: RESCHEDULE ARRIVED TO NODE
					queue.add(new Event(e.getTime().plus(this.waitOnGroundTime), EventType.ARRIVED_TO_NODE,
							e.getFlight()));
					// add stats waiting
					e.getFlight().addTimeWaitingOnGround(waitOnGroundTime);
				}
			}
			return;
			
		case ARRIVED_TO_GATE:
			//Turnaround time
			int fuelingTurnaroundSeconds = e.getFlight().getPlane().calculateFuelingTime();
			Duration turnaroundTime = this.paxTurnaroundTime.plus(Duration.ofSeconds(fuelingTurnaroundSeconds));
			queue.add(new Event(e.getTime().plus(turnaroundTime), EventType.REQUEST_RUNWAY_DEPARTURE, e.getFlight()));
			// Stats for gate usage
			this.output.addTimeGate(e.getFlight().getGateAssignedTo(), turnaroundTime);
			e.getFlight().setGateUsed(e.getFlight().getGateAssignedTo());
			// remove block from old edge
			this.usedEdges.remove(e.getFlight().getCurrentEdge());
			e.getFlight().setCurrentEdge(null);
			return;
		case REQUEST_RUNWAY_DEPARTURE:
			TrafficFlow tfD = this.airport.getTrafficFlow(this.trafficFlowName);
			Runway runwayChosenD = tfD.getDepartureRules().get(this.numDepartures++ % tfD.getDepartureRules().size());
			e.getFlight().setRunway(runwayChosenD);
			e.getFlight().setDepartureRunway(runwayChosenD);

			queue.add(new Event(e.getTime().plus(this.waitOnGroundTime), EventType.LEAVING_GATE, e.getFlight()));
			
			return;
		case LEAVING_GATE:
			// CHECK IF PATH IS NOT ALREADY ASSIGNED:
			if (e.getFlight().getNextEdges().size() == 0) {
				// IF TRUE ASSIGN PATH
				TaxiRoutingNode sourceNode = e.getFlight().getGateAssignedTo().getNode();
				TaxiRoutingNode destNode;
				double minDist = Double.MAX_VALUE;
				for (TaxiRoutingNode n : e.getFlight().getRunway().getNodes()) {
					double dist = TaxiRoutingNode.calculateDistance(n, sourceNode);
					GraphPath<TaxiRoutingNode, TaxiRoutingEdge> path = pathfinder.getPath(sourceNode, n);
					if (dist < minDist && path != null) {
						minDist = dist;
						destNode = n;
						e.getFlight().setNextEdges(path.getEdgeList());
					}
				}
			}
			// CHECK IF NEXT EDGE IS FREE
			if (this.usedEdges.get(e.getFlight().nextEdge()) == null) {
				// IF TRUE: REMOVE GATE AND MOVE TO NEXT EDGE
				this.usedGates.remove(e.getFlight().getGateAssignedTo()); // remove gate blocking
				e.getFlight().setGateAssignedTo(null); // remove assigned gate

				// move to next edge
				this.usedEdges.put(e.getFlight().nextEdge(), e.getFlight()); // block next edge
				e.getFlight().setCurrentEdge(e.getFlight().nextEdge()); // set next edge as current edge
				e.getFlight().removeNextEdge(); // remove current edge from edge list
				queue.add(new Event(
						e.getTime()
								.plus(Duration.ofSeconds(e.getFlight().getPlane()
										.calculateTaxiTime(e.getFlight().getCurrentEdge().getLength()))),
						EventType.ARRIVED_TO_NODE, e.getFlight()));
			} else {
				// IF FALSE: RESCHEDULE LEAVING GATE
				queue.add(new Event(e.getTime().plus(this.waitOnGroundTime), EventType.LEAVING_GATE, e.getFlight()));
				// add stats waiting 
				e.getFlight().addTimeWaitingOnGround(waitOnGroundTime);
			}
			return;
			
		case ASK_TAKEOFF_CLEARANCE:
			Runway runwayDeparting = e.getFlight().getRunway();
			if (this.usedRunways.get(runwayDeparting) == null
					&& this.usedRunways.get(runwayDeparting.getOtherSide()) == null) {
				// TAKEOFF
				// free current edge
				usedEdges.remove(e.getFlight().getCurrentEdge());
				// reserve runway
				this.usedRunways.put(runwayDeparting, e.getFlight());
				// schedule completed takeoff
				Plane planeDeparting = e.getFlight().getPlane();
				Duration takeoffReserveTime = Duration
						.ofSeconds(planeDeparting.calculateTakeOffTime(e.getFlight().getRunway().length()));
				queue.add(new Event(e.getTime().plus(takeoffReserveTime), EventType.AIRBORNE, e.getFlight()));
				this.output.addTimeRunway(runwayDeparting, takeoffReserveTime);
			} else {
				// RETRY
				queue.add(new Event(e.getTime().plus(this.waitOnGroundTime), EventType.ASK_TAKEOFF_CLEARANCE,
						e.getFlight()));
				// add stats for waiting
				e.getFlight().addTimeWaitingOnGround(waitOnGroundTime);
				//System.out.println("Retry");
			}
			return;
			
		case AIRBORNE:
			// remove runway block
			this.usedRunways.remove(e.getFlight().getRunway());
			e.getFlight().setRunway(null);
			e.getFlight().setTimeTakeOff(e.getTime());
			return;
		default:
			break;
		}
	}

	public Simulator(Airport airport, List<Plane> planeRoster, String trafficFlowName) {
		this.airport = airport;
		this.planeRoster = planeRoster;
		this.trafficFlowName = trafficFlowName;
	}

	public SimulationOutput getOutput() {
		return output;
	}

	public void setOutput(SimulationOutput output) {
		this.output = output;
	}

	public void setArrivalsPerHour(int arrivalsPerHour) {
		this.arrivalsPerHour = arrivalsPerHour;
	}

	public void setSimulationHours(int simulationHours) {
		this.simulationHours = simulationHours;
	}

	public void setGoAroundThreshold(double goAroundThreshold) {
		this.goAroundThreshold = goAroundThreshold;
	}

}
