package br.com.sp.senai.auditorio.auditorio.controller;


import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.data.domain.Page;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;
import br.com.sp.senai.auditorio.auditorio.repository.UsuarioRepository;



@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repository;
	
	@RequestMapping(value = "cadastro", method = RequestMethod.GET)
	private String form() {
		System.out.println("passei");
		//return o nome do arquivo html 
		return "cadastro/cadastroUsuario";
		
	}
	//salvar no banco 
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String salvar(Usuario usuario) {
		System.out.println("passou");
		
		repository.save(usuario);
		return "redirect:cadastro";
	}
	
	@RequestMapping("lista/{page}")
	public String list(Model model, @PathVariable("page")int page) {
		//criar uma pageble para informar os parametros da pagina 
		PageRequest pageble = PageRequest.of(page-1, 6 , Sort.by(Sort.Direction.ASC,"nome"));
		//criando lista 
		
		Page<Usuario> pagina = repository.findAll(pageble);
		//add a model a lista 
		
		model.addAttribute("usuario", pagina.getContent());
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
		
		return "listas/listaUsuarios";
		
		
		
	}
	
	@RequestMapping("alterando")
	public String alterare(Long id,Model model) {
		Usuario usuario = repository.findById(id).get();
		model.addAttribute("users", usuario);
		return "forward:cadastro";
	}
	
	// metodo excluir
	@RequestMapping("exclue")
	public String exclua (Long id) {
		Usuario userExclu = repository.findById(id).get();
		repository.delete(userExclu);
		return "redirect:lista/1";
	}	
		//buscar
	
	
	
}


