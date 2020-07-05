package it.polito.tdp.LATE.dataImport;

import java.util.Arrays;
import java.util.List;

import it.polito.tdp.LATE.airportModel.Airport;
import it.polito.tdp.LATE.airportModel.Gate;
import it.polito.tdp.LATE.airportModel.Runway;
import it.polito.tdp.LATE.airportModel.TaxiRoutingNode;
import it.polito.tdp.LATE.airportModel.TrafficFlow;
import it.polito.tdp.LATE.airportModel.Gate.GateType;
import it.polito.tdp.LATE.airportModel.TaxiRoutingNode.TaxiRoutingNodeType;

public class AirportDataImport {
	public static Airport importAirportFromXplane(List<String> rows) {
		Airport res = null;
		TrafficFlow lastTrafficFlow = null;
		Gate lastGate = null;
		for (String row : rows) {
			String[] fields = row.split(" ");
			try {
				int rowCode = Integer.parseInt(fields[0]);
				switch (rowCode) {
				// CODE 1: AIRPORT INFO
				case 1:
					String[] auxFields = row.split(" ", 6);
					// TODO: Valuta se fare un nuovo try/catch per il parseInt
					res = new Airport((int) (Integer.parseInt(auxFields[1])/3.281), auxFields[5], auxFields[4]);
					break;
				// CODE 100: RUNWAY INFO
				case 100:
					Runway side1 = new Runway(fields[8], Double.parseDouble(fields[9]), Double.parseDouble(fields[10]),Double.parseDouble(fields[1]));
					Runway side2 = new Runway(fields[17], Double.parseDouble(fields[18]), Double.parseDouble(fields[19]) ,Double.parseDouble(fields[1]));
					side1.setOtherSide(side2);
					side2.setOtherSide(side1);
					res.addRunway(side1);
					res.addRunway(side2);
					break;
				// CODE 1302: AIRPORT METADATA
				case 1302:
					String[] auxFields2 = row.split(" ", 3);
					if (fields.length == 3) {
						res.addMetadata(auxFields2[1], auxFields2[2]);
					}
					break;
				// CODE 1000: TRAFFIC FLOW
				case 1000:
					String[] auxFields3 = row.split(" ", 2);
					lastTrafficFlow = new TrafficFlow(auxFields3[1]);
					res.addTrafficFlow(lastTrafficFlow);
					break;
					// CODE 1110: RUNWAY IN USE RULES
				case 1110:
					Runway r = res.getRunwayFromName(fields[1]);
					List<String> arrDep = Arrays.asList(fields[3].split("\\|"));
					
					if (arrDep.contains("arrivals")){
						lastTrafficFlow.addRunwayInUseRule(r, TrafficFlow.TrafficFlowType.ARRIVAL);
					} 
					if(arrDep.contains("departures")) {
						lastTrafficFlow.addRunwayInUseRule(r, TrafficFlow.TrafficFlowType.DEPARTURE);
					} 
					break;
					// CODE 1201: TAXI ROUTING NODE
				case 1201:
					TaxiRoutingNodeType nodeValue = TaxiRoutingNodeType.BOTH;
					if (fields[3].contentEquals("dest")) {
						nodeValue = TaxiRoutingNodeType.DESTINATION;
					} else if (fields[3].contentEquals("init")) {
						nodeValue = TaxiRoutingNodeType.SOURCE;
					} else if (fields[3].contentEquals("junc")) {
						nodeValue = TaxiRoutingNodeType.JUNCTION;
					}
					res.addTaxiRoutingNode(new TaxiRoutingNode(Integer.parseInt(fields[4]), Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), nodeValue));
					break;
					// CODE 1202: TAXI ROUTING EDGE
				case 1202:
					if (!fields[4].contentEquals("runway")) {
						TaxiRoutingNode node1 = res.getNodeByID(Integer.parseInt(fields[1]));
						TaxiRoutingNode node2 = res.getNodeByID(Integer.parseInt(fields[2]));
						if (node1 != null && node2 != null) {
							int id = res.nextEdgeID();
							res.addTaxiRoutingEdge(node1, node2, id);
							if (fields[3].contentEquals("twoway")) {
								res.addTaxiRoutingEdge(node2, node1, id);
							}
						}
					}
					break;
					// CODE 1300:
				case 1300:
					GateType gateTypeValue = GateType.TIE_DOWN;
					if (fields[4].contentEquals("gate")) {
						gateTypeValue = GateType.GATE;
					}
					lastGate = new Gate(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), gateTypeValue, fields[6]);
					res.addGate(lastGate);
					break;
					// CODE 1301:
				case 1301:
					double maxWingspan = 0;
					char icaoWCode = fields[1].toUpperCase().charAt(0);
					switch (icaoWCode) {
					case 'A':
						maxWingspan = 15;
						break;
					case 'B':
						maxWingspan = 24;
						break;
					case 'C':
						maxWingspan = 36;
						break;
					case 'D':
						maxWingspan = 52;
						break;
					case 'E':
						maxWingspan = 65;
						break;
					case 'F':
						maxWingspan = 80;
						break;
					default:
						break;
					}
					lastGate.setMaxWingspan(maxWingspan);
					
					int operationType = 1;
					if (fields[2].contentEquals("airline")){
						operationType = 0;
					} else if (fields[2].contentEquals("cargo")) {
						operationType = 2;
					}
					lastGate.setOperationType(operationType);
					break;
				default:
					break;
				}
			} catch (NumberFormatException e) {
				assert true;
			}
		}
		// CLEAR UNUSED NODES (ex. Ground services nodes)
		res.clearUnusedNodes();
		
		// GET NEAREST NODES TO RWY
		for (Runway r: res.getRunways()) {
			if (!r.addRunwayNodes(res.getGraph().vertexSet())) {
				return null;
			}
		}
		
		for (Gate g: res.getGates()) {
			if (!g.addGateNode(res.getGraph().vertexSet())) {
				return null;
			};
		}
		return res;
	}
	
	public static String deleteDoubleSpaces (String in){
		int lenPrev = 1;
		int lenNext = 0;
		in = in.replace("\r", "");
		while (lenPrev > lenNext) {
			lenPrev = in.length();
			in = in.replace("  ", " ");
			lenNext = in.length();
		}
		return in;
	}

}
