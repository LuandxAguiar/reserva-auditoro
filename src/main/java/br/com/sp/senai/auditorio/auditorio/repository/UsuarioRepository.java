package br.com.sp.senai.auditorio.auditorio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.senai.auditorio.auditorio.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario,Long>{

}
