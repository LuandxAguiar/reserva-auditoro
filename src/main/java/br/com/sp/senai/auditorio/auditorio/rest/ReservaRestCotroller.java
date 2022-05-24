package br.com.sp.senai.auditorio.auditorio.rest;

import java.net.URI;
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

import br.com.sp.senai.auditorio.auditorio.model.Erro;
import br.com.sp.senai.auditorio.auditorio.model.Reserva;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;
import br.com.sp.senai.auditorio.auditorio.repository.ReservaRepository;

@RestController
@RequestMapping("")
public class ReservaRestCotroller {
	
	@Autowired
	private ReservaRepository repository;
	
	//lista menor 
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Reserva> getResera(){
		
		return repository.findAll();
	}
	
	
	@RequestMapping("/{id}")
	public ResponseEntity<Reserva> getReserva(@PathVariable("id") Long idReserva){
		//tentado
		
		Optional<Reserva> optional = repository.findById(idReserva);
		//
		if(optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
					}else {
						return ResponseEntity.notFound().build();
					}
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarUsuario(@RequestBody Reserva reserva){
		//insere usuario no banco 
		//
		//quando cair no try catch no caso de duplicado 
		try {
		repository.save(reserva);
		//retorna um cod HTTP 201 informa como acessar o recroso inserido 
		//e acrescenta no corpo da resposta o objeto inserido 
		return ResponseEntity.created(URI.create("/api/reserva/"+reserva.getId())).body(reserva);
		}catch(DataIntegrityViolationException e ) {
			e.printStackTrace();
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
					"Registro Duplcado", e.getClass().getName());
			return new ResponseEntity<Object>(erro,HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e ) {
			e.printStackTrace();
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
					e.getMessage(), e.getClass().getName());
			return new ResponseEntity<Object>(erro,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}
}
	

