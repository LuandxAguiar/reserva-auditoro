package br.com.sp.senai.auditorio.auditorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.senai.auditorio.auditorio.model.Periodo;
import br.com.sp.senai.auditorio.auditorio.model.Reserva;

public interface ReservaRepository extends PagingAndSortingRepository<Reserva, Long>{
		
	public Reserva findByStartAndPeriodo(String start, Periodo p);
	
	public Reserva findByPeriodo(Periodo p);
	
	public List<Reserva> findByStart(String start);
	
	
	
	@Query("SELECT b FROM Reserva b WHERE b.start LIKE %:b%")
	public List<Reserva> buscar(@Param("b") String geral);
}
