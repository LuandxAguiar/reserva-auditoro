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

import br.com.sp.senai.auditorio.auditorio.annotation.Administrador;
import br.com.sp.senai.auditorio.auditorio.model.TipoReserva;
import br.com.sp.senai.auditorio.auditorio.repository.TipoReservaRepository;

@Controller
public class TipoReservaController {
	@Autowired
	private TipoReservaRepository repository;
	
	@Administrador
	@RequestMapping(value = "tiporeservacad", method = RequestMethod.GET)
	private String form() {
		return "cadastro/tipoReserva";
	}
	
	@Administrador
	@RequestMapping(value = "salvatiporeserva", method = RequestMethod.POST)
	public String salvar (TipoReserva tr) {
		repository.save(tr);
		return "redirect:tiporeservacad";
	}
	
	@Administrador
	@RequestMapping("listatiporeserva/{page}")
	public String list(Model model, @PathVariable("page")int page) {
		//criar uma pageble para informar os parametros da pagina 
		PageRequest pageble = PageRequest.of(page-1, 6 , Sort.by(Sort.Direction.ASC,"nome"));
		//criando lista 
		
		Page<TipoReserva> pagina = repository.findAll(pageble);
		//add a model a lista 
		
		model.addAttribute("tipo", pagina.getContent());
		//gerar total de paginas
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
		
		return "listas/listatiporeserva";
		
		
		
	}
	
	@Administrador
	@RequestMapping("excluirTipo")
	public String exclua (Long id) {
		TipoReserva excluirTipo = repository.findById(id).get();
		repository.delete(excluirTipo );
		return "redirect:listatiporeserva/1";
	}
	
}
