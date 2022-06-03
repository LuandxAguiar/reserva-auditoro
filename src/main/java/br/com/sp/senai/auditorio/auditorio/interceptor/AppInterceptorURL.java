package br.com.sp.senai.auditorio.auditorio.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.sp.senai.auditorio.auditorio.annotation.Administrador;
import br.com.sp.senai.auditorio.auditorio.annotation.Professor;
import br.com.sp.senai.auditorio.auditorio.annotation.Publico;
import br.com.sp.senai.auditorio.auditorio.model.Hierarquia;

@Component
public class AppInterceptorURL implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String uri = request.getRequestURI();
		HttpSession session = request.getSession();
		if (uri.startsWith("/erro")) {
			System.out.println("URI");
			return true;

		}

		if (handler instanceof HandlerMethod) {
			HandlerMethod metodo = (HandlerMethod) handler;
			System.out.println("Passou aqui 1 !");
			if (uri.startsWith("/")) {
				System.out.println("Passou aqui 2 !");
				System.out.println("Session 	" + session.getAttribute("nivel"));

				if (metodo.getMethodAnnotation(Administrador.class) != null) {
					System.out.println("Passou aqui 3 !");
					if (session.getAttribute("nivel") == Hierarquia.ADMIN) {
						System.out.println("Passou aqui ADMIIIIIIIIIIIINNNNN if else !");
						return true;
					}

					if (Hierarquia.ADMIN == null) {
						response.sendError(HttpStatus.UNAUTHORIZED.value());
					} else {
						response.sendError(HttpStatus.FORBIDDEN.value());
					}
					return false;

				} else if (metodo.getMethodAnnotation(Professor.class) != null) {
					System.out.println("Passou aqui 5 !");
					if (session.getAttribute("nivel") == Hierarquia.DOCENTE) {
						System.out.println("Passou aqui Docente if else !");
						return true;
					}

					if (Hierarquia.DOCENTE == null) {
						response.sendError(HttpStatus.UNAUTHORIZED.value());
					} else {
						response.sendError(HttpStatus.FORBIDDEN.value());
					}
					return false;

				}

				return true;

			} else {
				System.out.println("Passou aqui !");

				if (metodo.getMethodAnnotation(Publico.class) != null) {
					System.out.println("Tá aqui ");
					return true;
				}
				if (session.getAttribute("usuarioLogado") != null) {
					return true;
				}
				response.sendRedirect("/");
				return false;

			}

		} else {
			return true;
		}

	}
}
