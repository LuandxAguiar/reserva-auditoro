package br.com.sp.senai.auditorio.auditorio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import br.com.sp.senai.auditorio.auditorio.util.HashUtil;
import lombok.Data;

@Data
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	private String senha;
	private String email;
	@Column(nullable = false, unique = true)
	@NotEmpty
	private String nif;
	private String hierarquia;

	
	
	public void setSenha(String senha) {
		this.senha = HashUtil.hash(senha);
	}
	
	
	//Métod que "seta" o hash na senha 
		public void setSenhaComHash(String hash) {
				this.senha = hash;
		}
	
}
