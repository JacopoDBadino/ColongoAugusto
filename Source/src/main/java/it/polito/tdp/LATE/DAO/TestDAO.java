package it.polito.tdp.LATE.DAO;

public class TestDAO {

	public static void main(String[] args) {
		System.out.println(AirportSimDAO.getRelevantPlanes().size());
		System.out.println("Controlla che siano 26");
		
		System.out.println(AirportSimDAO.getRelevantPlanes(29).size());
		System.out.println("Controlla che siano 17");
	}

}
