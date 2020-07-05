package it.polito.tdp.LATE.airportModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Airport {
	
	private int elevation; // elevation above mean sea level in feet
	private String name; // airport name
	private String icaoCode; // airport ICAO code
	private List<Runway> runways; // list of runways
	private List<TrafficFlow> trafficFlows; // list of traffic flows
	private Graph<TaxiRoutingNode, TaxiRoutingEdge> graph;
	private Map<String, String> metadata; // for example city_name
	private List<Gate> gates; // list of gates
	
	
	public Airport(int elevation, String name, String icaoCode) {
		this.elevation = elevation;
		this.name = name;
		this.icaoCode = icaoCode;
		this.runways = new ArrayList<Runway>();
		this.trafficFlows = new ArrayList<TrafficFlow>();
		this.graph = new SimpleDirectedWeightedGraph<>(TaxiRoutingEdge.class);
		this.metadata = new HashMap<String, String>();
		this.gates = new ArrayList<Gate>();
	}

	public int getElevation() {
		return elevation;
	}

	public String getName() {
		return name;
	}

	public String getIcaoCode() {
		return icaoCode;
	}

	public List<Runway> getRunways() {
		return runways;
	}
	
	public Runway getRunwayFromName(String name) {
		for (Runway r: this.runways) {
			if (r.getNum().contentEquals(name)) {
				return r;
			}
		}
		
		throw new RuntimeException("Requested non existent Runway");
	}

	public List<TrafficFlow> getTrafficFlows() {
		return trafficFlows;
	}
	
	public TrafficFlow getTrafficFlow(String name) {
		for (TrafficFlow t: this.trafficFlows) {
			if (t.getName().contentEquals(name)) {
				return t;
			}
		}
		throw new RuntimeException("Requested wrong Traffic Flow");
	}

	public Graph<TaxiRoutingNode, TaxiRoutingEdge> getGraph() {
		return graph;
	}
	
	public void addRunway(Runway r) {
		this.runways.add(r);
	}
	
	public void addMetadata(String key, String value) {
		this.metadata.put(key, value);
	}
	
	public void addTrafficFlow(TrafficFlow tf) {
		this.trafficFlows.add(tf);
	}
	
	public void addTaxiRoutingNode(TaxiRoutingNode node) {
		this.graph.addVertex(node);
	}
	
	public TaxiRoutingNode getNodeByID(int id) {
		for (TaxiRoutingNode node: this.graph.vertexSet()) {
			if (node.getId() == id) {
				return node;
			}
		}
		return null;
	}
	
	public void addTaxiRoutingEdge(TaxiRoutingNode nodeStart, TaxiRoutingNode nodeEnd, int id) {
		double distance = TaxiRoutingNode.calculateDistance(nodeStart, nodeEnd);
		TaxiRoutingEdge edge = new TaxiRoutingEdge(id);
		this.graph.addEdge(nodeStart, nodeEnd, edge);
		this.graph.setEdgeWeight(edge, distance);
		//System.out.println(nodeStart.getId() + " " + nodeEnd.getId() + " " + distance);
	}

	public int nextEdgeID() {
		int res = 0;
		for (TaxiRoutingEdge edge: this.graph.edgeSet()) {
			if (edge.getId() > res) {
				res = edge.getId();
			}
		}
		return res;
	}
	
	public void addGate(Gate g) {
		this.gates.add(g);
	}
	
	public void clearUnusedNodes() {
		int edgeCount;
		ArrayList<TaxiRoutingNode> removableNodes = new ArrayList<TaxiRoutingNode>();
		for (TaxiRoutingNode node: this.graph.vertexSet()) {
			/*boolean isRunwayNode = false;
			for (Runway r: this.runways) {
				if (r.getNodes().contains(node)) {
					isRunwayNode = true;
				}
			}*/
			edgeCount = 0;
			edgeCount += graph.outgoingEdgesOf(node).size();
			edgeCount += graph.incomingEdgesOf(node).size();
			if (edgeCount == 0) {
				removableNodes.add(node);
			}
		}
		this.graph.removeAllVertices(removableNodes);
	}

	public List<Gate> getGates() {
		return gates;
	}
	
	public List<Gate> getGatesAccepting(double planeWingspan){
		ArrayList<Gate> res = new ArrayList<Gate>(); 
		for (Gate g: gates) {
			if (planeWingspan <= g.getMaxWingspan()) {
				res.add(g);
			}
		}
		return res;
	}
	
	public double getMaxWingspan() {
		double val = 0.0;
		for (Gate g: this.gates) {
			if (g.getMaxWingspan() > val) {
				val = g.getMaxWingspan();
			}
		}
		return val;
	}
	
	public boolean isValid() {
		
		boolean tfCheck = this.trafficFlows.size() > 0;
		boolean rwyCheck = this.runways.size() > 0;
		/*boolean graphCheck = GraphTests.isStronglyConnected(this.graph);*/
		boolean gatesCheck = this.gates.size() > 0;
		
		return tfCheck && rwyCheck && gatesCheck;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}
	
	
	
}
