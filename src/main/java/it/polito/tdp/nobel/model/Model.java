package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao= new EsameDAO();
		this.partenza= dao.getTuttiEsami();
		//il modello viene creato una sola volta quando lancio il programma quindi il new di Soluzione migliore conviene farlo nel metodo calcolaSottoinsiemeEsami
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale= new HashSet<Esame>();
		soluzioneMigliore= new HashSet<Esame>();
		mediaSoluzioneMigliore=0;
		
		//cerca1(parziale, 0, numeroCrediti);
		cerca2(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	/*COMPLESSITà DI N! INFATTI è APPROCCIO STUPIDO (simile agli anagrammi)*/
	private void cerca1(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		//2) PARZIALE.sommaCrediti()>m
		int crediti= sommaCrediti(parziale);
		if(crediti>m) 
			return;
		if(crediti==m) {
			double media= calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore= new HashSet<>(parziale); //la sovrascrivo, non copio solo il riferimento altrimenti facendo backtracking questa si modifica e la perdo
				mediaSoluzioneMigliore=media;
			}
			return;
		}
		//se arrivo a questo punto vuol dire che sicuramente crediti < m, posso andare avanti tranne nel caso seguente:		
		//1) livello=numero di esami
		if(L==partenza.size()) 
			return;
		//a sto punto posso continuare la ricorsione e continuare a generare sotto-problemi
		for(Esame e: partenza) { //controllando se esame3 è stato aggiunto posso evitare di controllare che esame2 sia stato aggiunto , così la soluzione 1 diventa più simile alla 2
			if(!parziale.contains(e)) { //devo avere una soluzione dove lo stesso esame è contenuto una sola volta
				parziale.add(e);
				cerca1(parziale,L+1,m); //lancio ricorsione
				parziale.remove(e); //backtracking				
			}
		}
		
	}
	
	/*COMPLESSITà DI 2^N, INFATTI APPROCCIO INTELLIGENTE*/
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
				//2) PARZIALE.sommaCrediti()>m
				int crediti= sommaCrediti(parziale);
				if(crediti>m) 
					return;
				if(crediti==m) {
					double media= calcolaMedia(parziale);
					if(media>mediaSoluzioneMigliore) {
						soluzioneMigliore= new HashSet<>(parziale); //la sovrascrivo, non copio solo il riferimento altrimenti facendo backtracking questa si modifica e la perdo
						mediaSoluzioneMigliore=media;
					}
					return;
				}
				//se arrivo a questo punto vuol dire che sicuramente crediti < m, posso andare avanti tranne nel caso seguente:		
				//1) livello=numero di esami
				if(L==partenza.size()) 
					return;
				
				//a sto punto posso continuare la ricorsione e continuare a generare sotto-problemi
				//partenza[L] è da aggiungere o no? provo entrambe le cose
				parziale.add(partenza.get(L)); //provo ad aggiungere e faccio andare la ricorsione
				cerca2(parziale,L+1,m);
				
				parziale.remove(partenza.get(L));
				cerca2(parziale,L+1,m);
	}
	
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
