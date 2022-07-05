package it.uniroma3.siw.catering.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	public boolean existsByNomeAndOrigineAndDescrizione(String nome, String origine, String descrizione);

}
