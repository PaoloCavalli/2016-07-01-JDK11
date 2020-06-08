package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.db.FormulaOneDAO;


public class Model {
	private Map<Integer,Driver> idMap;
	private Graph<Driver, DefaultWeightedEdge> grafo;

	private FormulaOneDAO dao ;
	//Punto 2 : variabili
	//private List<Driver> dreamTeam = new ArrayList<Driver>();
	private List<Driver> bestDreamTeam ;
	private int bestDreamTeamValue;
	
	public Model() {
	idMap = new HashMap<Integer, Driver>();	
	dao = new FormulaOneDAO();	
	}
	
	public List<Integer> getAllYears(){
		return this.dao.getAllYearsOfRace();
	}
	
	public void creaGrafo(Integer anno) {
		grafo = new SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.getPiloti(anno, idMap));
		
		List<Adiacenza> adiacenze = this.dao.getAdiacenze(anno, idMap);
		for(Adiacenza a : adiacenze) {
			if(this.grafo.containsVertex(a.getD1()) && this.grafo.containsVertex(a.getD2())) {
				if(!this.grafo.containsEdge(a.getD1(), a.getD2()) && !this.grafo.containsEdge(a.getD2(), a.getD1())) {
				Graphs.addEdge(this.grafo, a.getD1(), a.getD2(),(int) a.getPeso());
				}
			}
		}
		
		
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
		}

		public int nArchi() {
		return this.grafo.edgeSet().size();
		}
	
	public Driver getGoldenWeel() {
		Driver golden = null;
		Integer var= 0;
		for(Driver d :this.grafo.vertexSet()) {
			Integer prov= this.grafo.outDegreeOf(d)-this.grafo.inDegreeOf(d);
			if(prov >var) {
				var= prov;
				golden = d;
			}
		}
		return golden;
	}	
		
		//PUNTO 2 Ricorsione
		//credo sia giusta ma ha una complessità computazionale altissima
		
		/*public Integer verificaTasso(List<Driver> parziale) {
	    	Integer tassoTeam = 0;
	    	
	    	for(int i = 0; i< parziale.size()-1 ; i++) {
	    	   tassoTeam += calcolaTassoSconfitta(parziale.get(i),parziale);
	    	}
	    	return tassoTeam;
	      }
		
		public Integer calcolaTassoSconfitta(Driver pilota, List<Driver> parziale) {
			
		    	Integer tSconfitta = 0;
		    	for(Driver d : Graphs.predecessorListOf(this.grafo, pilota)) {
		    		
		    	    if(!parziale.contains(d)) {
		    		DefaultWeightedEdge e = this.grafo.getEdge(d, pilota);
		    		tSconfitta += (int) this.grafo.getEdgeWeight(e);
		    	}
		    }
		    return tSconfitta;	
		}
		
	
		public List<Driver> trovaDreamTeam(int k) {
		List<Driver> parziale = new ArrayList<Driver>();
		this.dreamTeam= new ArrayList<Driver>();
		ricorsiva(k,0, parziale);
		return dreamTeam;
	     
		}
		
	private void ricorsiva(int k,int livello, List<Driver> parziale) {
		
		if(livello== k) {
			if(verificaTasso(parziale)< verificaTasso(dreamTeam)) {
				this.dreamTeam= new ArrayList<>(parziale);
				return;
			}
		}
		for(Driver d :grafo.vertexSet()) {
			if(!parziale.contains(d)) {
				parziale.add(d);
				ricorsiva(k,livello+1,parziale);
				parziale.remove(parziale.size()-1);
			}
		}
	
		
	}
	
	*/
    // Anche questa ricorsione ha un' alta complessità computazionale!
	public List<Driver> getDremTeam(int k) {
		bestDreamTeam = new ArrayList<>();
		bestDreamTeamValue = Integer.MAX_VALUE;
		recursive(0, new ArrayList<Driver>(), k);
		return bestDreamTeam;
	}

	private int evaluate(ArrayList<Driver> tempDreamTeam) {
		int sum = 0;
		
		Set<Driver> in = new HashSet<Driver>(tempDreamTeam);
		Set<Driver> out = new HashSet<Driver>(grafo.vertexSet());
		out.removeAll(in);
		
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (out.contains(grafo.getEdgeSource(e)) && in.contains(grafo.getEdgeTarget(e))) {
				sum += grafo.getEdgeWeight(e);
			}
		}
		return sum;
	}
	
	private void recursive(int step, ArrayList<Driver> tempDreamTeam, int k) {
		
		// condizione di terminazione
		if (step >= k) {
			if (evaluate(tempDreamTeam) < bestDreamTeamValue) {
				bestDreamTeamValue = evaluate(tempDreamTeam);
				bestDreamTeam = new ArrayList<>(tempDreamTeam);
				return;
			}
		}
		
		for (Driver d : grafo.vertexSet()) {
			if (!tempDreamTeam.contains(d)) {
				tempDreamTeam.add(d);
				recursive(step+1, tempDreamTeam, k);
				tempDreamTeam.remove(d);
			}
		}
		
	}
	
	
	
	
	
	
	
	
}
