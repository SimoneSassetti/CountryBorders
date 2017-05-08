package it.polito.tdp.country.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.db.CountryDao;

public class Model {
	
	private UndirectedGraph<Country, DefaultEdge> graph  ;
	private List<Country> countries;
	private Map<Country, Country> alberoVisita;
	
	public Model() {
		
	}
	public List<Country> getCountries(){
		//MEGLIO NON CHIAMARLO PIU VOLTE DAL DAO XK CI SERVIRA CHIAMARLO IN DIVERSI CASI
		if(countries==null){
			CountryDao dao= new CountryDao();
			countries=dao.listCountry();
		}
		return countries; //RITORNO IL VALORE CHE GIA CONOSCEVO SE NON ENTRO NELL IF
	}
	
	public List<Country> getRaggiungibili(Country partenza){
		//IN QUESTO CASO IL GRAFO VIENE CREATO UNA VOLTA SOLA XK NON CAMBIA DAL PARAMENTRO PASSATO
		//USIAMO QUESTO METODO X SAPERE SE L'ABBIAMO GIA INIZIALIZZATO OPPURE NO
		UndirectedGraph<Country, DefaultEdge> g=this.getGrafo();
		BreadthFirstIterator<Country, DefaultEdge> bfi= new BreadthFirstIterator<Country, DefaultEdge>(g, partenza);
		
		List<Country> list=new ArrayList<Country>();
		Map<Country, Country> albero=new HashMap<Country, Country>();
		//LO INIZIALIZZO
		albero.put(partenza, null);
		
		//CREO COSI UN LISTENER X TENERE TRACCIA DI QUELLO CHE FA L'ITERATORE PASSP PASSO
		bfi.addTraversalListener(new CountryTraversalListener(g, albero));
		
		while(bfi.hasNext()){
			list.add(bfi.next());
		}
		this.alberoVisita=albero;
		
		return list;
	}
	
	public List<Country> getPercorso(Country destinazione){
		List<Country> percorso= new ArrayList<Country>();
		
		Country c=destinazione;
		while(c!=null){
			percorso.add(c);
			c=alberoVisita.get(c);
		}
		return percorso;
	}

	private UndirectedGraph<Country, DefaultEdge> getGrafo() {
		if(this.graph==null){
			this.creaGrafo3();
		}
		return graph;
	}
	/**
	 * Creazione del grafo CountryBorders.
	 * Prima versione: per ogni coppia di vertici, chiedo al database se esiste un arco.
	 */
	public void creaGrafo1() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, /*dao.listCountry()*/ this.getCountries()) ;//non chiamo la variabile countries xk non sappiamo se è gia stata in izializzata!!!!
	
		// crea gli archi del grafo -- versione 1
		for(Country c1: graph.vertexSet()) {
			for(Country c2: graph.vertexSet()) {
				if(!c1.equals(c2)) {
					if( dao.confinanti(c1, c2) ) {
						graph.addEdge(c1, c2) ;
					}
				}
			}
		}
	}
	
	/**
	 * Creazione del grafo CountryBorders.
	 * Seconda versione: per ogni vertice, chiedo al database la lista dei vertici ad esso confinanti.
	 */
	public void creaGrafo2() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries()) ;
	
		// crea gli archi del grafo -- versione 2
		for(Country c: graph.vertexSet()) {
			List<Country> adiacenti = dao.listAdiacenti(c) ;
			for(Country c2: adiacenti)
				graph.addEdge(c, c2) ;
		}
	}
	
	/**
	 * Creazione del grafo CountryBorders.
	 * Terza versione: una sola volta, chiedo al database l'elenco delle coppie di vertici confinanti.
	 */
	public void creaGrafo3() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries()) ;
	
		// crea gli archi del grafo -- versione 3
		for(CountryPair cp : dao.listCoppieCountryAdiacenti()) {
			graph.addEdge(cp.getC1(), cp.getC2()) ;
		}
	}

	public void printStats() {
		System.out.format("Grafo: Vertici %d, Archi %d\n", graph.vertexSet().size(), graph.edgeSet().size());
	}

}
