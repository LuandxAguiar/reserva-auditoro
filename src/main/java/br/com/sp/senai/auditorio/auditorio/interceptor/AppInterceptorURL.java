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
			if (uri.startsWith("/")) {
				System.out.println("Session 	" + session.getAttribute("nivel"));
			
				if (metodo.getMethodAnnotation(Administrador.class) != null && session.getAttribute("nivel") == Hierarquia.ADMIN) {
					return true;

				} else if (metodo.getMethodAnnotation(Professor.class) != null && session.getAttribute("nivel") == Hierarquia.DOCENTE) {
					return true;
				} else if (metodo.getMethodAnnotation(Publico.class) != null) {
					return true;
				}
				
				
				response.sendError(HttpStatus.FORBIDDEN.value(), "Proibido");
				return false;
			
			} else {

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
