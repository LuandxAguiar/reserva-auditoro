package br.com.sp.senai.auditorio.auditorio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.sp.senai.auditorio.auditorio.model.Reserva;
import br.com.sp.senai.auditorio.auditorio.repository.ReservaRepository;
import br.com.sp.senai.auditorio.auditorio.repository.TipoReservaRepository;

@Controller
public class ReservaController {
	
	@Autowired
	private ReservaRepository repository;
	@Autowired
	private TipoReservaRepository trRep;
	
	@RequestMapping(value = "reserva", method = RequestMethod.GET)
	private String form(Model model) {
		model.addAttribute("tipo", trRep.findAll());
		return "reserva/cadReserva";
	}
	
	@RequestMapping(value = "salvareserva", method = RequestMethod.POST)
	public String salvar(Reserva reserva) {
		repository.save(reserva);
		return "redirect:reserva";
	}
	
	@RequestMapping("listareserva/{page}")
	public String listaReserva(Model model, @PathVariable("page")int page) {
		PageRequest pageble = PageRequest.of(page-1, 10, Sort.by(Sort.Direction.ASC, "data"));
		
		Page<Reserva> pagina =  repository.findAll(pageble);
		
		model.addAttribute("reserva", pagina.getContent());
		
		int totalPaginas = pagina.getTotalPages();
		//vetor para lista 
		List<Integer> numPaginas = new ArrayList<Integer>();
		//prenchendo as lista
		
		for (int i = 1; i  <= totalPaginas; i++) {
			numPaginas.add(i);
		}
		//fazendo a model para valores serem add
		
		model.addAttribute("numPagina",numPaginas);
		model.addAttribute("totalPages",totalPaginas);
		model.addAttribute("pagAtual",page);
		
		return "listas/listaReserva";
	}
	
	@RequestMapping("alteraReserva")
	public String alteraReserva(Long id, Model model) {
		Reserva reserva = repository.findById(id).get();
		model.addAttribute("r", reserva);
		return "forward:reserva";
	}
	
	@RequestMapping("exclueReserva")
	public String excluir(Long id) {
		Reserva reserva = repository.findById(id).get();
		repository.delete(reserva);
		return "redirect:listareserva/1";
	}
	
}
