package it.polito.tdp.LATE.airportModel;

public class TaxiRoutingNode {
	
	public static enum TaxiRoutingNodeType {
		DESTINATION,
		SOURCE,
		BOTH,
		JUNCTION
	}
	
	private int id;
	private double latitude;
	private double longitude;
	private TaxiRoutingNodeType usage; // 0 -> destination; 1 -> source; 2 -> both; 3 -> junction;
	
	public TaxiRoutingNode(int id, double latitude, double longitude, TaxiRoutingNodeType usage) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.usage = usage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		TaxiRoutingNode other = (TaxiRoutingNode) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaxiRoutingNode [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", usage=" + usage
				+ "]";
	}

	public int getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public TaxiRoutingNodeType getUsage() {
		return usage;
	}
	
	public static double calculateDistance(TaxiRoutingNode n1, TaxiRoutingNode n2) {
		/* 
		 * Great circle 
		 * https://www.movable-type.co.uk/scripts/latlong.html
		 * const x = (λ2-λ1) * Math.cos((φ1+φ2)/2); 
		 * const y = (φ2-φ1); 
		 * const d = Math.sqrt(x*x + y*y) * R;
		 * where φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km) note that angles need to be in radians to pass to trig functions!
		 */
		
		double lat1 = n1.getLatitude()*Math.PI/180;
		double lat2 = n2.getLatitude()*Math.PI/180;
		double lon1 = n1.getLongitude()*Math.PI/180;
		double lon2 = n2.getLongitude()*Math.PI/180;

		double x = (lon2 - lon1) * Math.cos((lat1 + lat2)/2);
		double y = lat2 - lat1;
		double d = Math.sqrt(x*x + y*y) * 6371000;
		return d;
	}
	

}
