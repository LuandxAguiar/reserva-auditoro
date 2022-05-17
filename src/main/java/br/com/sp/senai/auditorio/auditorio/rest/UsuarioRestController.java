package br.com.sp.senai.auditorio.auditorio.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.senai.auditorio.auditorio.model.Erro;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;
import br.com.sp.senai.auditorio.auditorio.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository repository;
	
	//variavel de tokken 
	
	
	//
	public ResponseEntity<Object> crieUsuario(@RequestBody Usuario usuario){
		try {
			repository.save(usuario);
			//retorna um cod Http 201 
			return ResponseEntity.created(URI.create("/api/usuario/"+usuario.getId())).body(usuario);
			
		}catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
					e.getMessage(), e.getClass().getName());
			return new ResponseEntity<Object>(erro,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
