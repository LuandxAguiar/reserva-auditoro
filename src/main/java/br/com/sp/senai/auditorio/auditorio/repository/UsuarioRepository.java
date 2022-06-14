package br.com.sp.senai.auditorio.auditorio.repository;


import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;


import br.com.sp.senai.auditorio.auditorio.model.Hierarquia;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

	public List<Usuario> findByNif(String nif);

	public Usuario findByNifAndSenhaAndAtivoDesat(String nif, String senha, boolean ad);

	public Usuario findByNifAndSenhaAndHierarquia(String nif, String senha, Hierarquia hierarquia);
	
	public List<Usuario> findByHierarquia(Hierarquia hierarquia);
	
}
