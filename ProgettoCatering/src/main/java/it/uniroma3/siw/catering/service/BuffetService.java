package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.comparator.comparatoreDiBuffetPerNome;
import it.uniroma3.siw.catering.repository.BuffetRepository;



@Service
public class BuffetService {
	
	@Autowired
	private BuffetRepository buffetRepository;

	public boolean alreadyExists(Buffet buffet) {
		return buffetRepository.existsByNomeAndDescrizione(buffet.getNome(), buffet.getDescrizione());
	}

	public Buffet findById(Long id) {
		return buffetRepository.findById(id).get();
	}

	@Transactional
	public void save(Buffet buffet) {
		buffetRepository.save(buffet);
	}
	
	public List<Buffet> findAll() {
		List<Buffet> listaBuffet = new ArrayList<Buffet>();
		for(Buffet buffet : buffetRepository.findAll()) {
			listaBuffet.add(buffet);
		}
		listaBuffet.sort(new comparatoreDiBuffetPerNome());
				
		return listaBuffet;
	}
	
	@Transactional
	public void deleteById(Long id) {
		buffetRepository.deleteById(id);
	}
	
	public long count() {
		return this.buffetRepository.count();
	}
	
	
}

