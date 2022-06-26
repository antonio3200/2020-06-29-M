package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	
	private ImdbDAO dao;
	private SimpleWeightedGraph<Director,DefaultWeightedEdge> grafo;
	private List<Director> vertici;
	private Map<Integer,Director> idMap;
	
	public Model() {
		this.dao=new ImdbDAO();
		this.vertici= new ArrayList<>();
		this.idMap= new HashMap<>();
	}
	
	public void creaGrafo(int anno) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici= this.dao.getDirectorPerAnno(anno,idMap);
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//aggiungo archi
		List<Arco> archi= this.dao.getArco(anno, idMap);
		for(Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getVertici(){
		return this.vertici;
	}
	
	public List<DirectorAdiacenti> getAdiacenti(Director selezionato){
		List<Director> vicini= Graphs.neighborListOf(this.grafo, selezionato);
		List<DirectorAdiacenti> result= new ArrayList<>();
		for(Director d : vicini) {
			DirectorAdiacenti da= new DirectorAdiacenti(d,(int)this.grafo.getEdgeWeight(this.grafo.getEdge(selezionato, d)));
			result.add(da);
		}
		Collections.sort(result);
		return result;
	}
}
