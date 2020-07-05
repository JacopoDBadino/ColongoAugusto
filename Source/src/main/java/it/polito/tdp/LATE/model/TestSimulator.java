package it.polito.tdp.LATE.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import it.polito.tdp.LATE.airportModel.Airport;
import it.polito.tdp.LATE.airportModel.Gate;
import it.polito.tdp.LATE.dataImport.AirportDataImport;
import it.polito.tdp.LATE.planeModel.Flight;
import it.polito.tdp.LATE.planeModel.Plane;
import it.polito.tdp.LATE.utility.MyUtility;

public class TestSimulator {

	public static void main(String[] args) {
		
		String in = "";
		try {
			in = MyUtility.readTextFile("/home/augusto/Desktop/LIMF_Scenery_Pack/Earth nav data/apt.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
		in = AirportDataImport.deleteDoubleSpaces(in);
		ArrayList<String> splitted = new ArrayList<String>(Arrays.asList(in.split("\n")));
		Airport a = AirportDataImport.importAirportFromXplane(splitted);
		ArrayList<Plane> pps= new ArrayList<Plane>();
		Plane p = new Plane("Airbus", "A380", 25, 120.00, 575005.9);
		pps.add(p);
		Simulator s = new Simulator(a, pps, "North flow");
		s.init();
		s.run();
		
		System.out.println("Plane Stats");
		for (Flight f: s.getOutput().getFlights()) {
			System.out.println(f.getId() + " " + f.getNumGoAround() + " " + f.getGroundCycleTime() + " " + f.getTotalCycleTime()  + " " + f.getTimeWaitingOnGround());
		}
		
		System.out.println("Gate Stats");
		for (Gate g: s.getOutput().getGatesUsage().keySet()) {
			System.out.println(g.getId() + " " + s.getOutput().getGatesUsage().get(g));
		}

	}

}
