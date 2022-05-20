package br.com.sp.senai.auditorio.auditorio.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.senai.auditorio.auditorio.model.Reserva;

public interface ReservaRepository extends PagingAndSortingRepository<Reserva, Long>{
		
	public List<Reserva> findByData(String data);
	
}
