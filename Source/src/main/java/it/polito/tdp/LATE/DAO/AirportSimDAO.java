package it.polito.tdp.LATE.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import it.polito.tdp.LATE.planeModel.Plane;

public class AirportSimDAO {
	public static Set<Plane> getRelevantPlanes () {
		Set<Plane> res = new HashSet<Plane>();
		
		String sql = "SELECT manufacturer, model, wingspanmeters, approachspeedmeterspersecond, maximumtakeoffweightkilograms FROM Aircraft WHERE relevant = 1 ;";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet resSet = st.executeQuery();

			while (resSet.next()) {
				res.add(new Plane(resSet.getString(1),
						resSet.getString(2),
						resSet.getDouble(3),
						resSet.getDouble(4),
						resSet.getDouble(5)));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return res;
	}
	
	
	public static Set<Plane> getRelevantPlanes(double maxWingspan){
Set<Plane> res = new HashSet<Plane>();
		
		String sql = "SELECT manufacturer, model, wingspanmeters, approachspeedmeterspersecond, maximumtakeoffweightkilograms FROM Aircraft WHERE relevant = 1 AND wingspanmeters < ? ;";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, maxWingspan);
			ResultSet resSet = st.executeQuery();

			while (resSet.next()) {
				res.add(new Plane(resSet.getString(1),
						resSet.getString(2),
						resSet.getDouble(3),
						resSet.getDouble(4),
						resSet.getDouble(5)));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return res;
	}
}
