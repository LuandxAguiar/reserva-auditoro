package br.com.sp.senai.auditorio.auditorio.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.sp.senai.auditorio.auditorio.annotation.Administrador;
import br.com.sp.senai.auditorio.auditorio.annotation.Professor;
import br.com.sp.senai.auditorio.auditorio.annotation.Publico;
import br.com.sp.senai.auditorio.auditorio.model.Hierarquia;
import br.com.sp.senai.auditorio.auditorio.model.Usuario;
import br.com.sp.senai.auditorio.auditorio.rest.UsuarioRestController;

public class AppInterceptor implements HandlerInterceptor {
	private Usuario usuario;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String uri = request.getRequestURI();
		HttpSession session = request.getSession();
		if (uri.startsWith("/erro")) {
			return true;
		}

		if (handler instanceof HandlerMethod) {
			HandlerMethod metodo = (HandlerMethod) handler;

			if (uri.startsWith("/api")) {
				String token = null;
				if (metodo.getMethodAnnotation(Administrador.class) != null) {

					try {

						token = request.getHeader("Authorization");

						Algorithm algorito = Algorithm.HMAC256(UsuarioRestController.SECRET);
						JWTVerifier verifier = JWT.require(algorito).withIssuer(UsuarioRestController.EMISSOR).build();
						DecodedJWT jwt = verifier.verify(token);
						Map<String, Claim> claims = jwt.getClaims();
						System.out.println(claims);
						Hierarquia h = Hierarquia.values()[Integer.parseInt(claims.get("hierarquia").toString())];
						if(h == Hierarquia.ADMIN) {
							// entrou !
							return true;
						}
						return false;

					} catch (Exception e) {
						e.printStackTrace();
						if (token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
						} else {
							response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
						}
						return false;
					}
				} else if (metodo.getMethodAnnotation(Professor.class) != null) {
					try {

						token = request.getHeader("Authorization");

						Algorithm algorito = Algorithm.HMAC256(UsuarioRestController.SECRET);
						JWTVerifier verifier = JWT.require(algorito).withIssuer(UsuarioRestController.EMISSOR).build();
						DecodedJWT jwt = verifier.verify(token);
						Map<String, Claim> claims = jwt.getClaims();
						System.out.println(claims);
						
						Hierarquia h = Hierarquia.values()[Integer.parseInt(claims.get("hierarquia").toString())];
						if(h == Hierarquia.DOCENTE) {
							// entrou !
							return true;
						}
						
						return false;
					} catch (Exception e) {
						e.printStackTrace();
						if (token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
						} else {
							response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
						}
						return false;

					}

				} else {
					if (metodo.getMethodAnnotation(Publico.class) != null) {
						return true;
					}
					if (session.getAttribute("usuarioLogado") != null) {
						return true;
					}
					response.sendRedirect("/");
					return false;
				}

			}
			return true;
		}
		return false;
	}

}
