package it.uniroma3.siw.catering.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.catering.controller.validator.IngredienteValidator;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.service.PiattoService;
import it.uniroma3.siw.catering.model.*;

@Controller
public class IngredienteController {
	
	@Autowired
	IngredienteService ingredienteService;
	
	@Autowired
	PiattoService piattoService;

	@Autowired
	IngredienteValidator validator;

	/**************************************************************************** USER ***********************************************************************/
	
	@GetMapping(value="/ingrediente/{id}")
	public String getIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		return "public/ingrediente/ingrediente.html";
	}
	
	@GetMapping(value="/ingrediente")
	public String getAllBuffet(Model model) {
		List<Ingrediente> listaIngredienti = this.ingredienteService.findAll();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "public/ingrediente/allIngrediente.html";
	}
	
	/**************************************************************************** ADMIN ***********************************************************************/
	
	@GetMapping(value="/admin/ingrediente/{id}")
	public String getAdminingrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		return "admin/ingrediente/ingrediente.html";
	}
	
	@GetMapping(value="/admin/ingrediente")
	public String getAdminAllIngrediente(Model model) {
		long numeroIngredienti = this.ingredienteService.count();
		model.addAttribute("numeroIngredienti", numeroIngredienti);
		List<Ingrediente> listaIngredienti = this.ingredienteService.findAll();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/ingrediente/allIngredienti.html";
	}
	
	@PostMapping(value="/admin/ingredienteForm")
	public String getNewIngrediente(Model model) {
		model.addAttribute("ingrediente", new Ingrediente()); 
		return "admin/ingrediente/ingredienteForm.html";
	}
	
	@PostMapping(value="/admin/ingrediente")
	public String addIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {
		validator.validate(ingrediente, bindingResult);
		if(!bindingResult.hasErrors()) {
			ingredienteService.save(ingrediente);
			model.addAttribute("ingrediente", ingrediente);
			return "admin/ingrediente/ingrediente.html";
		}
		return "admin/ingrediente/ingredienteForm.html";
	}
	
	
	@PostMapping(value="/admin/toDeleteIngrediente/{id}")
	public String toDeleteIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		return "admin/ingrediente/toDeleteIngrediente.html";
	}

	@PostMapping(value="/admin/deleteIngrediente/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		List<Piatto> piatti = this.piattoService.findAll();
		
		for(Piatto piatto : piatti) {
			if(piatto.getIngredienti().contains(ingrediente)) {
				piatto.getIngredienti().remove(ingrediente);
				this.piattoService.save(piatto);
			}
		}
			
		ingredienteService.deleteById(id);
		long numeroIngredienti = this.ingredienteService.count();
		model.addAttribute("numeroIngredienti", numeroIngredienti);
		List<Ingrediente> listaIngredienti = this.ingredienteService.findAll();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/ingrediente/allIngredienti.html";
	}
	
	@GetMapping(value="/admin/toUpdateIngrediente/{id}")
	public String toUpdateIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		return "admin/ingrediente/toUpdateIngrediente.html";
	}
}
