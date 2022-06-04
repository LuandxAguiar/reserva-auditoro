package br.com.sp.senai.auditorio.auditorio.repository;


import org.springframework.data.repository.PagingAndSortingRepository;


import br.com.sp.senai.auditorio.auditorio.model.Hierarquia;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

	public Usuario findByNif(String nif);

	public Usuario findByNifAndSenha(String nif, String senha);

	public Usuario findByNifAndSenhaAndHierarquia(String nif, String senha, Hierarquia hierarquia);
	
	
}
