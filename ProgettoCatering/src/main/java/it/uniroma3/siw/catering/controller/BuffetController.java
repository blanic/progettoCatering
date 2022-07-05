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

import it.uniroma3.siw.catering.controller.validator.BuffetValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;
import it.uniroma3.siw.catering.service.PiattoService;


@Controller
public class BuffetController {

	@Autowired
	BuffetService buffetService;

	@Autowired
	ChefService chefService;

	@Autowired
	PiattoService piattoService;

	@Autowired
	BuffetValidator validator;

	/**************************************************************************** USER ***********************************************************************/

	@GetMapping(value="/buffet/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		Chef chef = buffet.getChef();
		model.addAttribute("chef", chef);
		List<Piatto> piatti = buffet.getPiatti();
		model.addAttribute("piatti", piatti);
		return "public/buffet/buffet.html";
	}

	@GetMapping(value="/buffet")
	public String getAllBuffet(Model model) {
		List<Buffet> listaBuffet = this.buffetService.findAll();
		listaBuffet.removeIf(buffet ->(buffet.getChef()==null));
		model.addAttribute("listaBuffet", listaBuffet);
		return "public/buffet/allBuffet.html";
	}

	/**************************************************************************** ADMIN ***********************************************************************/

	@GetMapping(value="/admin/buffet/{id}")
	public String getAdminBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		Chef chef = buffet.getChef();
		model.addAttribute("chef", chef);
		List<Piatto> piatti = buffet.getPiatti();
		model.addAttribute("piatti", piatti);
		return "admin/buffet/buffet.html";
	}

	@GetMapping(value="/admin/buffet")
	public String getAdminAllBuffet(Model model) {
		long numeroBuffet = this.buffetService.count();
		model.addAttribute("numeroBuffet", numeroBuffet);
		List<Buffet> listaBuffet = this.buffetService.findAll();
		model.addAttribute("listaBuffet", listaBuffet);
		return "admin/buffet/allBuffet.html";
	}

	@PostMapping(value="/admin/buffetForm")
	public String getNewBuffet(Model model) {
		model.addAttribute("buffet", new Buffet()); 
		return "admin/buffet/buffetForm.html";
	}

	@PostMapping(value="/admin/buffet")
	public String addBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		validator.validate(buffet, bindingResult);
		if(!bindingResult.hasErrors()) {
			buffetService.save(buffet);
			model.addAttribute("buffet", buffet);
			return "admin/buffet/buffet.html";
		}
		return "admin/buffet/buffetForm.html";
	}


	@PostMapping(value="/admin/toDeleteBuffet/{id}")
	public String toDeleteBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		return "admin/buffet/toDeleteBuffet.html";
	}

	@PostMapping(value="/admin/deleteBuffet/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		List<Chef> elencoChef = this.chefService.findAll();
		for(Chef chef : elencoChef) {
			if(chef.getBuffetProposti().contains(buffet)) {
				chef.getBuffetProposti().remove(buffet);
				this.chefService.save(chef);
			}
		}
		buffetService.deleteById(id);
		long numeroBuffet = this.buffetService.count();
		model.addAttribute("numeroBuffet", numeroBuffet);
		List<Buffet> listaBuffet = this.buffetService.findAll();
		model.addAttribute("listaBuffet", listaBuffet);
		return "admin/buffet/allBuffet.html";
	}

	@PostMapping(value="/admin/toUpdateBuffet/{id}")
	public String toUpdateBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		return "admin/buffet/toUpdateBuffet.html";
	}

	@PostMapping(value="/admin/toSetChef/{id}")
	public String toUpdateChef(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		Chef chef = buffet.getChef();
		List<Chef> elencoChef = this.chefService.findAll();
		elencoChef.remove(chef);
		model.addAttribute("buffet", buffet);
		model.addAttribute("elencoChef", elencoChef);
		return "admin/buffet/toSetChef.html";
	}

	@PostMapping(value="/admin/setChef/{idBuffet}/{idChef}") 
	public String updateChef(@PathVariable("idBuffet") Long idBuffet, 
			@PathVariable("idChef") Long idChef,
			Model model){
		Buffet buffet = this.buffetService.findById(idBuffet);
		model.addAttribute("buffet", buffet);
		Chef chef = this.chefService.findById(idChef);
		model.addAttribute("chef", chef);
		buffet.setChef(chef);
		chef.getBuffetProposti().add(buffet);
		this.buffetService.save(buffet);
		this.chefService.save(chef);
		List<Piatto> piatti = buffet.getPiatti();
		model.addAttribute("piatti", piatti);
		return "admin/buffet/buffet.html";
	}

	@PostMapping(value="admin/toRemoveChef/{id}")
	public String toRemoveChef(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		Chef chef = buffet.getChef();
		chef.getBuffetProposti().remove(buffet);
		buffet.setChef(null);
		buffetService.save(buffet);
		chefService.save(chef);
		model.addAttribute("buffet", buffet);
		model.addAttribute("chef", null);
		List<Piatto> piatti = buffet.getPiatti();
		model.addAttribute("piatti", piatti);
		return "admin/buffet/buffet.html";
	}

	@PostMapping(value="/admin/toRemovePiatto/{idBuffet}/{idPiatto}")
	public String removePiatto(@PathVariable("idBuffet") Long idBuffet, 
			@PathVariable("idPiatto") Long idPiatto,
			Model model) {
		Buffet buffet = this.buffetService.findById(idBuffet);
		Piatto piatto = this.piattoService.findById(idPiatto);
		buffet.getPiatti().remove(piatto);
		this.buffetService.save(buffet);
		model.addAttribute("buffet", buffet);
		Chef chef = buffet.getChef();
		model.addAttribute("chef", chef);
		List<Piatto> piatti = buffet.getPiatti();
		model.addAttribute("piatti", piatti);
		return "admin/buffet/buffet.html";
	}

	@PostMapping(value="/admin/toAddPiatto/{id}")
	public String toAddPiatto(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		List<Piatto> piattiDelBuffet= buffet.getPiatti();
		List<Piatto> elencoPiatti = this.piattoService.findAll();
		elencoPiatti.removeAll(piattiDelBuffet);
		model.addAttribute("buffet", buffet);
		model.addAttribute("elencoPiatti", elencoPiatti);
		return "admin/buffet/toaddPiatto.html";
	}

	@PostMapping(value="/admin/addPiatto/{idBuffet}/{idPiatto}") 
	public String addPiatto(@PathVariable("idBuffet") Long idBuffet, 
			@PathVariable("idPiatto") Long idPiatto,
			Model model){
		Buffet buffet = this.buffetService.findById(idBuffet);
		Piatto piatto = this.piattoService.findById(idPiatto);
		buffet.getPiatti().add(piatto);
		this.buffetService.save(buffet);
		List<Piatto> piatti = buffet.getPiatti();
		Chef chef = buffet.getChef();
		model.addAttribute("chef", chef);		
		model.addAttribute("piatti", piatti);
		model.addAttribute("buffet", buffet);
		return "admin/buffet/buffet.html";
	}
}
