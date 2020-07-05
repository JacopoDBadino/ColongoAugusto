package it.polito.tdp.LATE.airportModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.LATE.airportModel.TaxiRoutingNode.TaxiRoutingNodeType;

public class Runway {
	
	public static double NODE_RUNWAY_ASSIGNMENT_THRESHOLD = 200.0;
	
	private String num; // Runway number
	private double lat; // Runway centerline latitude
	private double lon; // Runway centerline longitude
	private List<TaxiRoutingNode> nodes; // Nearest nodes to this side
	private Runway otherSide; // Other side of this runway
	private double width; // Runway width
	
	public Runway(String startNum, double startLat, double startLon,
			double width) {
		this.num = startNum;
		this.lat = startLat;
		this.lon = startLon;
		this.width = width;
		this.nodes = new ArrayList<TaxiRoutingNode>();
	}
	
	public boolean addRunwayNodes(Set<TaxiRoutingNode> airportNodes) {
		TaxiRoutingNode nodeStart = new TaxiRoutingNode(0, lat, lon, TaxiRoutingNodeType.SOURCE);

		for (TaxiRoutingNode node: airportNodes) {
			if (TaxiRoutingNode.calculateDistance(nodeStart, node) < NODE_RUNWAY_ASSIGNMENT_THRESHOLD) {
				nodes.add(node);
			}
		}
		
		if (nodes.size() > 0) {
			return true;
		}
		
		return false;
	}

	public void setOtherSide(Runway otherSide) {
		this.otherSide = otherSide;
	}

	public String getNum() {
		return num;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public List<TaxiRoutingNode> getNodes() {
		return nodes;
	}

	public Runway getOtherSide() {
		return otherSide;
	}

	public double getWidth() {
		return width;
	}
	
	public double length() {
		TaxiRoutingNode t1 = new TaxiRoutingNode(0, this.lat, this.lon, TaxiRoutingNodeType.BOTH);
		TaxiRoutingNode t2 = new TaxiRoutingNode(0, otherSide.lat, otherSide.lon, TaxiRoutingNodeType.BOTH);
		
		return TaxiRoutingNode.calculateDistance(t1, t2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Runway other = (Runway) obj;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		return true;
	}
	
	
	
	

}
