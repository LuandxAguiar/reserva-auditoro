package br.com.sp.senai.auditorio.auditorio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;

import br.com.sp.senai.auditorio.auditorio.annotation.Administrador;
import br.com.sp.senai.auditorio.auditorio.annotation.Professor;
import br.com.sp.senai.auditorio.auditorio.annotation.Publico;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;
import br.com.sp.senai.auditorio.auditorio.repository.UsuarioRepository;
import br.com.sp.senai.auditorio.auditorio.util.HashUtil;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;

	
	@Administrador
	@RequestMapping(value = "cadastro", method = RequestMethod.GET)
	private String form() {
		System.out.println("passei");
		// return o nome do arquivo html
		return "cadastro/cadastroUsuario2";

	}

	// salvar no banco
	
	@Administrador
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr, String nif) {
		System.out.println("passou");
		System.out.println(usuario.getNif() + usuario.getNome() + usuario.getEmail() + usuario.getSenha());

		// verificando se houve Erro na valid
		if (result.hasErrors()) {
			attr.addFlashAttribute("mensagemErro", "verificar os campos novamente...");
			// redireciona
			return "redirect:cadastro";
		}
		boolean alterando = usuario.getId() != null ? true : false;
		// verificando a senha
		if (usuario.getSenha().equals(HashUtil.hash(""))) {
			if (!alterando) {

				// setar a parte da senha do admin
				String parte = usuario.getEmail().substring(0, usuario.getEmail().indexOf("@"));
				usuario.setSenha(parte);
				// buscando a senha atual no bd

			} else {
				String hash = repository.findById(usuario.getId()).get().getSenha();

				usuario.setSenhaComHash(hash);
			}
		}

		try {
			repository.save(usuario);
			attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso ID:" + usuario.getId());
		} catch (Exception e) {
			System.out.println("erro ao cadastrar");
			attr.addFlashAttribute("mensagemErro", "Verificar os campos novamente, Este Nif já existe");
		}
		return "redirect:cadastro";
	}

	@Professor
	@Administrador
	@RequestMapping("lista/{page}")
	public String list(Model model, @PathVariable("page") int page) {
		// criar uma pageble para informar os parametros da pagina
		PageRequest pageble = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "nome"));
		// criando lista

		Page<Usuario> pagina = repository.findAll(pageble);
		// add a model a lista

		model.addAttribute("usuario", pagina.getContent());
		// gerar total de paginas
		int totalPaginas = pagina.getTotalPages();
		// vetor para lista
		List<Integer> numPaginas = new ArrayList<Integer>();
		// prenchendo as lista

		for (int i = 1; i <= totalPaginas; i++) {
			numPaginas.add(i);
		}
		// fazendo a model para valores serem add

		model.addAttribute("numPagina", numPaginas);
		model.addAttribute("totalPages", totalPaginas);
		model.addAttribute("pagAtual", page);

		return "listas/listaUsuarios";

	}

	@Administrador
	@RequestMapping("alterando")
	public String alterare(Long id, Model model) {
		Usuario usuario = repository.findById(id).get();
		model.addAttribute("users", usuario);
		return "forward:cadastro";
	}

	// metodo excluir
	@Administrador
	@RequestMapping("exclue")
	public String exclua(Long id) {
		Usuario userExclu = repository.findById(id).get();
		repository.delete(userExclu);
		return "redirect:lista/1";
	}
	// buscar

	//

	@Publico
	@RequestMapping("login")
	public String login(Usuario admLogin, RedirectAttributes attr, HttpSession session) {
		// buscar o adm no banco
		System.out.println(admLogin.getNif());
		System.out.println(admLogin.getSenha());

		Usuario user = repository.findByNifAndSenha(admLogin.getNif(), admLogin.getSenha());
		// verificar se existe
		if (user == null) {
			System.out.println("usuario não existe");
			attr.addFlashAttribute("mensagemErro", "Login ou Senha invalida(s)");
			return "login/login";

		} else {
			System.out.println("usuario existe");
			// salva o adm na sessão
			session.setAttribute("usuarioLogado", user);
			session.setAttribute("nivel", user.getHierarquia());
			return "redirect:lista/1";
		}
		// logout

	}

	@Publico
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		// invalida a sessão
		session.invalidate();
		// voltar a pagina inicial
		// redirect pagina inical
		return "login/login";

	}
}
