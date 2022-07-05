package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.repository.ChefRepository;


@Service
public class ChefService {
	
	@Autowired
	private ChefRepository chefRepository;

	public boolean alreadyExists(Chef chef) {
		return chefRepository.existsByNomeAndCognomeAndNazionalita(chef.getNome(), chef.getCognome(), chef.getNazionalita());
	}

	public Chef findById(Long id) {
		return chefRepository.findById(id).get();
	}

	@Transactional
	public void save(Chef chef) {
		chefRepository.save(chef);
	}
	
	public List<Chef> findAll() {
		List<Chef> listaChef = new ArrayList<Chef>();
		for(Chef chef : chefRepository.findAll()) {
			listaChef.add(chef);
		}
		return listaChef;
	}

	public void deleteById(Long id) {
		chefRepository.deleteById(id);
	}

	public long count() {
		return chefRepository.count();
	}

}
