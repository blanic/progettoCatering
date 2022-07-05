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

import it.uniroma3.siw.catering.controller.validator.ChefValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;

@Controller
public class ChefController {

	@Autowired
	ChefService chefService;

	@Autowired
	BuffetService buffetService;

	@Autowired
	ChefValidator validator;

	/**************************************************************************** USER ***********************************************************************/

	@GetMapping(value="/chef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		List<Buffet> buffetProposti = chef.getBuffetProposti();
		model.addAttribute("buffetProposti", buffetProposti);
		return "public/chef/chef.html";
	}

	@GetMapping(value="/chef")
	public String getAllChef(Model model) {
		List<Chef> listaChef = this.chefService.findAll();
		model.addAttribute("listaChef", listaChef);
		return "public/chef/allChef.html";
	}

	/**************************************************************************** ADMIN ***********************************************************************/

	@GetMapping(value="/admin/chef/{id}")
	public String getAdminChef(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		List<Buffet> buffetProposti = chef.getBuffetProposti();
		model.addAttribute("buffetProposti", buffetProposti);
		return "admin/chef/chef.html";
	}

	@GetMapping(value="/admin/chef")
	public String getAdminAllChef(Model model) {
		long numeroChef = this.chefService.count();
		model.addAttribute("numeroChef", numeroChef);
		List<Chef> listaChef = this.chefService.findAll();
		model.addAttribute("listaChef", listaChef);
		return "admin/chef/allChef.html";
	}

	@PostMapping(value="/admin/chefForm")
	public String getNewChef(Model model) {
		model.addAttribute("chef", new Chef()); 
		return "admin/chef/chefForm.html";
	}

	@PostMapping(value="/admin/chef")
	public String addChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult, Model model) {
		validator.validate(chef, bindingResult);
		if(!bindingResult.hasErrors()) {
			chefService.save(chef);
			model.addAttribute("chef", chef);
			return "admin/chef/chef.html";
		}
		return "admin/chef/chefForm.html";
	}


	@PostMapping(value="/admin/toDeleteChef/{id}")
	public String toDeleteChef(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		return "admin/chef/toDeleteChef.html";
	}

	@PostMapping(value="/admin/deleteChef/{id}")
	public String deleteChef(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		List<Buffet> elencoBuffet = chef.getBuffetProposti();
		for(Buffet buffet : elencoBuffet) {
			buffet.setChef(null);
			this.buffetService.save(buffet);
		}
		chefService.deleteById(id);
		long numeroChef = this.chefService.count();
		model.addAttribute("numeroChef", numeroChef);
		List<Chef> listaChef = this.chefService.findAll();
		model.addAttribute("listaChef", listaChef);
		return "admin/chef/allChef.html";
	}

	@GetMapping(value="/admin/toUpdateChef/{id}")
	public String toUpdateChef(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		return "admin/chef/toUpdateChef.html";
	}

	@PostMapping(value="/admin/toRemoveBuffet/{id}")
	public String toRemovBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		Chef chef = buffet.getChef();
		buffet.setChef(null);
		chef.getBuffetProposti().remove(buffet);
		model.addAttribute("chef", chef);
		List<Buffet> buffetProposti = chef.getBuffetProposti();
		model.addAttribute("buffetProposti", buffetProposti);
		this.buffetService.save(buffet);
		this.chefService.save(chef);
		return "admin/chef/chef.html";
	}

	@PostMapping(value="admin/toAddBuffet/{id}")
	public String toAddBuffet(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);

		List<Buffet> elencoBuffet = this.buffetService.findAll();
		elencoBuffet.removeIf(buffet -> (buffet.getChef()!=null));
		model.addAttribute("elencoBuffet", elencoBuffet);
		return "admin/chef/toAddBuffet.html";
	}

	@PostMapping(value="admin/addBuffet/{idChef}/{idBuffet}")
	public String addBuffet(@PathVariable("idChef") Long idChef,
			@PathVariable("idBuffet") Long idBuffet,
			Model model) {
		Chef chef = this.chefService.findById(idChef);
		Buffet buffet = this.buffetService.findById(idBuffet);
		buffet.setChef(chef);
		this.buffetService.save(buffet);
		model.addAttribute("chef", chef);
		List<Buffet> buffetProposti = chef.getBuffetProposti();
		model.addAttribute("buffetProposti", buffetProposti);
		return "admin/chef/chef.html";
	}




}
