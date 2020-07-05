package it.polito.tdp.LATE.planeModel;

import java.util.Comparator;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class TablePlane extends Plane {
	
	private Spinner<Integer> value;


	public TablePlane(String manufacturer, String model, double wingspan, double approachSpeed, double MTOW) {
		super(manufacturer, model, wingspan, approachSpeed, MTOW);
		this.value = new Spinner<Integer>();
		this.value.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
		this.value.prefWidth(20.0);
	}
	
	public TablePlane(Plane p) {
		super(p.getManufacturer(), p.getModel(), p.getWingspan(), p.getApproachSpeed(), p.getMTOW());
		this.value = new Spinner<Integer>();
		this.value.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
		this.value.prefWidth(20.0);
	}
	
	public Plane getPlane() {
		return new Plane(this.getManufacturer(), this.getModel(), this.getWingspan(), this.getApproachSpeed(), this.getMTOW());
	}
	
	public Spinner<Integer> getValue() {
		return this.value;
	}

	public static class ReverseMTOWComparator implements Comparator<TablePlane> {
		@Override
		public int compare(TablePlane arg0, TablePlane arg1) {
			return (int) (arg1.getMTOW()-arg0.getMTOW());
		}
		
	}
}
