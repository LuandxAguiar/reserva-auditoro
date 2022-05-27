package br.com.sp.senai.auditorio.auditorio.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.threeten.bp.format.DateTimeFormatter;

import com.google.api.client.util.DateTime;

import br.com.sp.senai.auditorio.auditorio.model.Periodo;
import br.com.sp.senai.auditorio.auditorio.model.Reserva;
import br.com.sp.senai.auditorio.auditorio.repository.ReservaRepository;
import br.com.sp.senai.auditorio.auditorio.repository.TipoReservaRepository;

@Controller
public class ReservaController {

	@Autowired
	private ReservaRepository repository;
	@Autowired
	private TipoReservaRepository trRep;

	LocalDate dataAtual = LocalDate.now();

	@RequestMapping(value = "agendamento", method = RequestMethod.GET)
	private String formCalendario(Model model) {
		model.addAttribute("tipo", trRep.findAll());
		model.addAttribute("reserva", repository.findAll());
		return "reserva/calendario";
	}

	// metodo de salvamento da agenda
	@RequestMapping(value = "salvareserva", method = RequestMethod.POST)
	public String salvar(Reserva reserva, String data, Periodo periodo, RedirectAttributes attr) throws Exception {
		
			Reserva agendamento = repository.findByDataAndPeriodo(data, periodo);
				System.out.println(data);
			if (agendamento != null) {

				return "redirect:agendamento";

			}
			
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa      " + data);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 
	   
	        
			LocalDateTime dataSalva = LocalDateTime.parse(data);
			LocalDateTime dataAtual = LocalDateTime.now();
			
			if(dataSalva.isBefore(dataAtual)) {
				
			}
				
			
			repository.save(reserva);
			
		
			System.out.println("erro");
			return "redirect:agendamento";
		

	
	}

	// listar as reservas realizadas
	@RequestMapping("listareserva/{page}")
	public String listaReserva(Model model, @PathVariable("page") int page) {
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "data"));

		Page<Reserva> pagina = repository.findAll(pageble);

		model.addAttribute("reserva", pagina.getContent());

		int totalPaginas = pagina.getTotalPages();
		// vetor para lista
		List<Integer> numPaginas = new ArrayList<Integer>();
		// prenchendo as lista

		for (int i = 1; i <= totalPaginas; i++) {
			numPaginas.add(i);
		}
		// fazendo a model para valores serem add

		model.addAttribute("numPagina", numPaginas);
		model.addAttribute("totalPages", totalPaginas);
		model.addAttribute("pagAtual", page);

		return "listas/listaReserva";
	}

	// alterar a reserva
	@RequestMapping("alteraReserva")
	public String alteraReserva(Long id, Model model) {
		// busca a reserva pelo ID possibilitando a alteração
		Reserva reserva = repository.findById(id).get();
		model.addAttribute("r", reserva);
		return "forward:calendario";
	}

	@RequestMapping("exclueReserva")
	public String excluir(Long id) {
		// excluir a reserva pelo ID
		Reserva reserva = repository.findById(id).get();
		repository.delete(reserva);
		return "redirect:listareserva/1";
	}

	@RequestMapping("buscar")
	public String buscar(String buscar, Model model) {
		model.addAttribute("reserva", repository.buscar(buscar));
		return "reserva/calendario";
	}

}
