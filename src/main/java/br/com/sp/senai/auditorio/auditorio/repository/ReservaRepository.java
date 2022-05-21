package br.com.sp.senai.auditorio.auditorio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.senai.auditorio.auditorio.model.Periodo;
import br.com.sp.senai.auditorio.auditorio.model.Reserva;

public interface ReservaRepository extends PagingAndSortingRepository<Reserva, Long>{
		
	public Reserva findByDataAndPeriodo(String data, Enum<Periodo>p);
	
}
