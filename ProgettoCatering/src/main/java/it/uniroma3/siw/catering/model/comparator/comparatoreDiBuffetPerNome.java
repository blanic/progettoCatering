package it.uniroma3.siw.catering.model.comparator;

import java.util.Comparator;

import it.uniroma3.siw.catering.model.Buffet;

public class comparatoreDiBuffetPerNome implements Comparator<Buffet> {

	@Override
	public int compare(Buffet o1, Buffet o2) {
		return o1.getNome().compareTo(o2.getNome());
			}
	

}
