package br.com.sp.senai.auditorio.auditorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.senai.auditorio.auditorio.model.Periodo;
import br.com.sp.senai.auditorio.auditorio.model.Reserva;

public interface ReservaRepository extends PagingAndSortingRepository<Reserva, Long>{
		
	public Reserva findByDataAndPeriodo(String data, Periodo p);
	
	@Query("SELECT b FROM Reserva b WHERE b.data LIKE %:b% ")
	public List<Reserva> buscar(@Param("b") String geral);
}
