package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.repository.IngredienteRepository;


@Service
public class IngredienteService {
	
	@Autowired
	private IngredienteRepository ingredienteRepository;

	public boolean alreadyExists(Ingrediente ingrediente) {
		return ingredienteRepository.existsByNomeAndOrigineAndDescrizione(ingrediente.getNome(), ingrediente.getOrigine(), ingrediente.getDescrizione());
	}

	public Ingrediente findById(Long id) {
		return ingredienteRepository.findById(id).get();
	}

	@Transactional
	public void save(Ingrediente ingrediente) {
		ingredienteRepository.save(ingrediente);
	}
	
	public List<Ingrediente> findAll() {
		List<Ingrediente> listaIngredienti = new ArrayList<Ingrediente>();
		for(Ingrediente ingrediente : ingredienteRepository.findAll()) {
			listaIngredienti.add(ingrediente);
		}
		return listaIngredienti;
	}

	@Transactional
	public void deleteById(Long id) {
		ingredienteRepository.deleteById(id);
		
	}

	public long count() {
		return ingredienteRepository.count();
	}
	
}
