package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.repository.PiattoRepository;



@Service
public class PiattoService {
	
	@Autowired
	private PiattoRepository piattoRepository;

	public boolean alreadyExists(Piatto piatto) {
		return piattoRepository.existsByNomeAndDescrizione(piatto.getNome(), piatto.getDescrizione());
	}

	public Piatto findById(Long id) {
		return piattoRepository.findById(id).get();
	}

	@Transactional
	public void save(Piatto piatto) {
		piattoRepository.save(piatto);
	}
	
	public List<Piatto> findAll() {
		List<Piatto> listaPiatti = new ArrayList<Piatto>();
		for(Piatto piatto : piattoRepository.findAll()) {
			listaPiatti.add(piatto);
		}
		return listaPiatti;
	}

	public void deleteById(Long id) {
		 piattoRepository.deleteById(id);
	}

	public long count() {
		return piattoRepository.count();
	}
}
