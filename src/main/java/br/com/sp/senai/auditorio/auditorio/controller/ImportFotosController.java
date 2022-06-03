package br.com.sp.senai.auditorio.auditorio.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.sp.senai.auditorio.auditorio.model.ImportFotos;
import br.com.sp.senai.auditorio.auditorio.repository.ImportFotosRepository;
import br.com.sp.senai.auditorio.auditorio.repository.ReservaRepository;
import br.com.sp.senai.auditorio.auditorio.util.FireBaseUtil;




@Controller
public class ImportFotosController {
	@Autowired
	private FireBaseUtil fireUtil;
	@Autowired
	private ReservaRepository resRepository;
	@Autowired
	private ImportFotosRepository repository;
	
	@RequestMapping("cadastreFotos")
	public String form(Model model) {
		System.out.println("passou no cadestre");
		model.addAttribute("r", resRepository.findAll());
		model.addAttribute("fotos", repository.findAll());
		return "cadastro/cadastroFotos";
	}
	//salvar 
	 
	@RequestMapping("salvarFoto")
	public String salvar(ImportFotos fotos , @RequestParam("fileFotos") MultipartFile[] fileFotos) {
		System.out.println("PASSOU AQUI");
		String foto = fotos.getFotos();
		// String para amarazenar aas URls

		for (MultipartFile arquivo : fileFotos) {
			// verifica se o arquivo e existente

			if (arquivo.getOriginalFilename().isEmpty()) {
				// vai para o proximo arquivo
				continue;

			}
			try {
				foto += fireUtil.upload(arquivo) + ";";
			} catch (IOException e) {

				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
		// guardar na variavel bar as fotos
		fotos.setFotos(foto);
		repository.save(fotos);
		return "redirect:cadastreFotos";

	}
	@RequestMapping("alterandoFotos")
	public String alterare(Long idRest, Model model) {
		ImportFotos fot = repository.findById(idRest).get();
		model.addAttribute("fot", fot);
		return "forward:cadastreFotos";
	}

	@RequestMapping("excluirFotos")
	public String excluirFotos(Long idRest, int numFoto, Model model) {
		// buscar o bar no banco
		ImportFotos fot = repository.findById(idRest).get();
		// buscar URL
		String urlFoto = fot.verFotos()[numFoto];
		// deleta a foto
		fireUtil.deletar(urlFoto);
		// remover do atributo foto
		fot.setFotos(fot.getFotos().replace(urlFoto + ";", ""));
		// salva no banco
		repository.save(fot);
		// coloca o bar na model
		model.addAttribute("importFot", fot);
		return "forward:formularioBar";
	}
	
}
