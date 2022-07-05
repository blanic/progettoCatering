package it.uniroma3.siw.catering.controller;

import java.util.ArrayList;
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

import it.uniroma3.siw.catering.controller.validator.PiattoValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.service.PiattoService;


@Controller
public class PiattoController {

	@Autowired
	PiattoService piattoService;
	
	@Autowired
	BuffetService buffetService;

	@Autowired
	IngredienteService ingredienteService;

	@Autowired
	PiattoValidator validator;

	/**************************************************************************** USER ***********************************************************************/

	@GetMapping(value="/piatto/{id}")
	public String getPiatto(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		List <Ingrediente> listaIngredienti = piatto.getIngredienti();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "public/piatto/piatto.html";
	}

	@GetMapping(value="/piatto")
	public String getAllPiatti(Model model) {
		List<Piatto> listaPiatti = this.piattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "public/piatto/allPiatti.html";
	}

	/**************************************************************************** ADMIN ***********************************************************************/

	@GetMapping(value="/admin/piatto/{id}")
	public String getAdminPiatto(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		List <Ingrediente> listaIngredienti = piatto.getIngredienti();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/piatto/piatto.html";
	}

	@GetMapping(value="/admin/piatto")
	public String getAdminAllPiatti(Model model) {
		long numeroPiatti = this.piattoService.count();
		model.addAttribute("numeroPiatti", numeroPiatti);
		List<Piatto> listaPiatti = this.piattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "admin/piatto/allPiatti.html";
	}

	@PostMapping(value="/admin/piattoForm")
	public String getNewPiatto(Model model) {
		model.addAttribute("piatto", new Piatto()); 
		return "admin/piatto/piattoForm.html";
	}

	@PostMapping(value="/admin/piatto")
	public String addPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, BindingResult bindingResult, Model model) {
		validator.validate(piatto, bindingResult);
		if(!bindingResult.hasErrors()) {
			piattoService.save(piatto);
			model.addAttribute("piatto", piatto);
			return "admin/piatto/piatto.html";
		}
		return "admin/piatto/piattoForm.html";
	}

	@PostMapping(value="/admin/toDeletePiatto/{id}")
	public String toDeletePiatto(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		return "admin/piatto/toDeletePiatto.html";
	}

	@PostMapping(value="/admin/deletePiatto/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		List<Buffet> elencoBuffet = this.buffetService.findAll();
		for(Buffet buffet : elencoBuffet) {
			if(buffet.getPiatti().contains(piatto)) {
				buffet.getPiatti().remove(piatto);
				this.buffetService.save(buffet);
			}
		}
		piattoService.deleteById(id);
		List<Piatto> listaPiatti = this.piattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "admin/piatto/allPiatti.html";
	}

	@GetMapping(value="/admin/toUpdatePiatto/{id}")
	public String toUpdatePiatto(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		return "admin/piatto/toUpdatePiatto.html";
	}

	@PostMapping(value="/admin/toAddIngrediente/{id}")
	public String addIngrediente(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		List<Ingrediente> ingredienti = ingredienteService.findAll();
		List<Ingrediente> ingredientiAggiungibili = new ArrayList<>();

		for(Ingrediente ingrediente : ingredienti) {
			if(!piatto.getIngredienti().contains(ingrediente)) {
				ingredientiAggiungibili.add(ingrediente);
			}
		}

		model.addAttribute("ingredientiAggiungibili", ingredientiAggiungibili);

		return "admin/piatto/toAddIngrediente.html";
	}

	@PostMapping(value="/admin/addIngrediente/{idPiatto}/{idIngrediente}")
	public String addIngrediente(@PathVariable("idPiatto") Long idPiatto, 
			@PathVariable("idIngrediente") Long idIngrediente,
			Model model) {
		Piatto piatto = this.piattoService.findById(idPiatto);
		model.addAttribute("piatto", piatto);
		Ingrediente ingrediente = this.ingredienteService.findById(idIngrediente);
		if(!piatto.getIngredienti().contains(ingrediente)) {
			piatto.getIngredienti().add(ingrediente);
		}
		piattoService.save(piatto);
		List <Ingrediente> listaIngredienti = piatto.getIngredienti();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/piatto/piatto.html";
	}


	@PostMapping(value="admin/toRemoveIngrediente/{idPiatto}/{idIngrediente}")
	public String removeIngrediente(@PathVariable("idPiatto") Long idPiatto,
			@PathVariable("idIngrediente") Long idIngrediente, 
			Model model) {
		Piatto piatto = piattoService.findById(idPiatto);
		model.addAttribute("piatto", piatto);
		Ingrediente ingrediente = ingredienteService.findById(idIngrediente);
		piatto.getIngredienti().remove(ingrediente);
		piattoService.save(piatto);
		List <Ingrediente> listaIngredienti = piatto.getIngredienti();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/piatto/piatto.html";
	}









}
