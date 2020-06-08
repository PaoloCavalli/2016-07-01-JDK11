package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.model.Adiacenza;
import it.polito.tdp.model.Circuit;
import it.polito.tdp.model.Constructor;
import it.polito.tdp.model.Driver;
import it.polito.tdp.model.Season;



public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT Distinct year FROM races ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	public List<Driver> getPiloti(Integer anno, Map<Integer,Driver> idMap){
		String sql="SELECT DISTINCT  d.driverId, d.driverRef " + 
				"FROM results r1, races ra,drivers d " + 
				"WHERE r1.positionText != \"R\" AND r1.driverId=d.driverId AND r1.raceId=ra.raceId AND ra.year=? ";
		List<Driver> piloti= new ArrayList<Driver>();
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
		    st.setInt(1, anno);
		    ResultSet rs = st.executeQuery();
		    while (rs.next()) {
		    	if(!idMap.containsKey(rs.getInt("driverId"))) {
		    		Driver r = new Driver(rs.getInt("d.driverId"),rs.getString("d.driverRef"));
		    		idMap.put(r.getDriverId(), r);
		    		piloti.add(r);
		    	}else {
		    		piloti.add(idMap.get(rs.getInt("d.driverId")));
		    	}
		    	
		    }
		    conn.close();
		    return piloti;
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Adiacenza> getAdiacenze(Integer anno, Map<Integer,Driver> idMap){
		String sql= "SELECT r1.driverId as d1,r2.driverId as d2,COUNT(*) as peso " + 
				"FROM results r1, races ra,results r2 " + 
				"WHERE r1.raceId=r2.raceId  AND r2.positionText!=\"R\"  AND r1.positionText!=\"R\"  " + 
				"AND r1.driverId<> r2.driverId AND ra.year=? AND ra.raceId= r1.raceId " + 
				"AND r1.position< r2.position " + 
				"GROUP BY r1.driverId,r2.driverId";
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
		    st.setInt(1, anno);
		    ResultSet rs = st.executeQuery();
		
		    while(rs.next()) {
		    	//Mi dava una IllegalArgument Exception (no loops allowed) perche quì aggiungevo uno stesso elemento:
		    	//al nodo sorgente(d1) al nodo destinazione ((sempre)d1) ATTENZIONE!
		    	adiacenze.add(new Adiacenza(idMap.get(rs.getInt("d1")), idMap.get(rs.getInt("d2")), rs.getInt("peso")));
		    	
		    }
		    conn.close();
		    return adiacenze;
		     
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
		
		
	}
}