package it.polito.tdp.imdb.model;

public class DirectorAdiacenti implements Comparable<DirectorAdiacenti> {

	Director d;
	Integer peso;
	public DirectorAdiacenti(Director d, Integer peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return d.toString()+" ("+this.peso+")";
	}
	@Override
	public int compareTo(DirectorAdiacenti o) {
		// TODO Auto-generated method stub
		return -(this.peso.compareTo(o.getPeso()));
	}
	
	
	
}
