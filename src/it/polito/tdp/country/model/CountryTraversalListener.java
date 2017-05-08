package it.polito.tdp.country.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;

public class CountryTraversalListener implements TraversalListener<Country, DefaultEdge> {
	private Graph<Country, DefaultEdge> graph;
	private Map<Country, Country> map;
	
	public CountryTraversalListener(Graph graph, Map<Country, Country> map) {
		super();
		this.graph = graph;
		this.map = map;
	}

	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> evento) {
		// evento.getEdge(); è l'arco appena attraversato
		//arco graph.edgeSource/Dest(evento.getEdge)
		//dall arco estraggo il padre e il figlio; questi gli aggiungo ad una mappa
		
		Country c1=graph.getEdgeSource(evento.getEdge());
		Country c2=graph.getEdgeTarget(evento.getEdge());
		
		if(map.containsKey(c1) && map.containsKey(c2))
			return ;
		
		if(!map.containsKey(c1)){
			//c1 è quello nuovo
			map.put(c1, c2);
		}else{
			//c2 è quello nuovo
			map.put(c2, c1);
		}
			
		//COME FACCIO A SAPERE SE CE L'HO GIA IL VERTICE NUOVO?
		//NEL KEYSET HO GIA SALVATO I PAESI
		
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Country> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Country> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
