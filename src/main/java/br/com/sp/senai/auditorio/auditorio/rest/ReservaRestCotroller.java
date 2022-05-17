package br.com.sp.senai.auditorio.auditorio.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.senai.auditorio.auditorio.model.Reserva;
import br.com.sp.senai.auditorio.auditorio.repository.ReservaRepository;

@RestController
@RequestMapping("/api/reserva")
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
}
	

