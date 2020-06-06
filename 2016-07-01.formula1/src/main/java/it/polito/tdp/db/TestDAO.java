package it.polito.tdp.db;

import java.util.List;

import it.polito.tdp.model.Circuit;
import it.polito.tdp.model.Constructor;
import it.polito.tdp.model.Season;



public class TestDAO {

	public static void main(String[] args) {

		FormulaOneDAO dao = new FormulaOneDAO();

		List<Integer> years = dao.getAllYearsOfRace();
		System.out.println(years);

		List<Season> seasons = dao.getAllSeasons();
		System.out.println(seasons);

		List<Circuit> circuits = dao.getAllCircuits();
		System.out.println(circuits);

		List<Constructor> constructors = dao.getAllConstructors();
		System.out.println(constructors);

	}

}