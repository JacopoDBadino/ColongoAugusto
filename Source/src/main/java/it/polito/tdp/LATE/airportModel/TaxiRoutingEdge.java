package it.polito.tdp.LATE.airportModel;

import org.jgrapht.graph.DefaultWeightedEdge;

public class TaxiRoutingEdge extends DefaultWeightedEdge {
	private int id;

	public TaxiRoutingEdge(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public double getLength() {
		return this.getWeight();
	}
	
	
	
	
	
	
}
