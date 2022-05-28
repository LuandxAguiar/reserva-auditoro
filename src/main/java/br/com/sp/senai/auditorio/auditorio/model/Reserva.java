package br.com.sp.senai.auditorio.auditorio.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Reserva {

	

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String nome;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private String data;
		private String descricaoReserva;
		private String fotos; 
		@ManyToOne
		private TipoReserva tipo;
		private Periodo periodo;
}
