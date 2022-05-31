package br.com.sp.senai.auditorio.auditorio.rest;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.sp.senai.auditorio.auditorio.annotation.Administrador;
import br.com.sp.senai.auditorio.auditorio.model.Erro;
import br.com.sp.senai.auditorio.auditorio.model.TokenJWT;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;
import br.com.sp.senai.auditorio.auditorio.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository repository;
	
	//variavel de tokken 
	public static final String EMISSOR = "RESERVAAUT";
	public static final String SECRET = "@Ud1T0r10";
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
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
	
	
	@Administrador
	@RequestMapping("{id}")
	public ResponseEntity<Usuario> getUser(@PathVariable("id") Long idUser){
		Optional<Usuario> optional = repository.findById(idUser);
		if(optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<br.com.sp.senai.auditorio.auditorio.model.TokenJWT> logar(@RequestBody Usuario usuario) {
		// Buscar o usuario no banco de dados
		usuario = repository.findByNifAndSenha(usuario.getNif(), usuario.getSenha());
		// Verifica se o usuário não é nulo
		if (usuario != null) {
			// Variável para inserir dados no payload
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("id", usuario.getNif());
			payload.put("nome", usuario.getNome());
			payload.put("hierarquia", usuario.getHierarquia());
			// Variável para a data de expiração
			Calendar expiracao = Calendar.getInstance();
			// Adicona um valor de tempo
			expiracao.add(Calendar.HOUR, 1);
			// Algoritimo para assinar o Token
			Algorithm algoritmo = Algorithm.HMAC256(SECRET);
			// Cria o objeto para receber o Token
			TokenJWT tokenJwt = new TokenJWT();
			// Gera o Token
			tokenJwt.setToken(JWT.create().withPayload(payload).withIssuer(EMISSOR).withExpiresAt(expiracao.getTime())
					.sign(algoritmo));
			return ResponseEntity.ok(tokenJwt);
		} else {
			return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
		}

	
	
	}
}
