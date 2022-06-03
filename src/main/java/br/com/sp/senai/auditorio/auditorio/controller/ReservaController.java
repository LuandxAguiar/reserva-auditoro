package br.com.sp.senai.auditorio.auditorio.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

import br.com.sp.senai.auditorio.auditorio.annotation.Administrador;
import br.com.sp.senai.auditorio.auditorio.annotation.Professor;
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

	@Administrador
	@RequestMapping(value = "agendamento", method = RequestMethod.GET)
	private String formCalendario(Model model) {
		model.addAttribute("tipo", trRep.findAll());
		model.addAttribute("reserva", repository.findAll());
		return "reserva/calendario";
	}

	// metodo de salvamento da agenda

	@Administrador
	@Professor
	@RequestMapping(value = "salvareserva", method = RequestMethod.POST)
	public String salvar(Reserva reserva, String data, Periodo periodo, RedirectAttributes attr, Periodo p)
			throws Exception {

		Reserva agendamento = repository.findByDataAndPeriodo(data, periodo);

		if (agendamento != null) {

			attr.addFlashAttribute("mensagemErro", "Verificar campos");
			return "redirect:agendamento";
		}

		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Calendar dataAtual = Calendar.getInstance();
		Calendar dataSalva = Calendar.getInstance();
		dataSalva.setTime(formato.parse(data));
		dataAtual.add(Calendar.DATE, -1);

		for (Reserva res : repository.findByData(data)) {

			if (res.getPeriodo() != Periodo.TODOS) {

				return "redirect:agendamento";

			}

			else {
				if (res.getPeriodo() == Periodo.MANHA || res.getPeriodo() == Periodo.TARDE
						|| res.getPeriodo() == Periodo.NOITE) {
					repository.deleteById(res.getId());
					return "redirect:agendamento";
				}
			}
		}

		if (dataSalva.before(dataAtual)) {
			attr.addFlashAttribute("mensagemErro", "verificar data");
			System.out.println("Erro da data");
			return "redirect:agendamento";
		}

		repository.save(reserva);
		attr.addFlashAttribute("mensagemSucesso", "Sua data foi salva");
		return "redirect:agendamento";
	}

	// listar as reservas realizadas

	@Administrador
	@Professor
	@RequestMapping("listareserva/{page}")
	public String listaReserva(Model model, @PathVariable("page") int page) {
		PageRequest pageble = PageRequest.of(page - 1, 100, Sort.by(Sort.Direction.ASC, "data"));

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

		model.addAttribute("numPaginas", numPaginas);
		model.addAttribute("totalPaginas", totalPaginas);
		model.addAttribute("page", page);

		return "listas/listaReserva";
	}

	// alterar a reserva

	@Administrador
	@Professor
	@RequestMapping("alteraReserva")
	public String alteraReserva(Long id, Model model) {
		// busca a reserva pelo ID possibilitando a alteração
		Reserva reserva = repository.findById(id).get();
		model.addAttribute("r", reserva);
		return "forward:agendamento";
	}

	@Administrador
	@RequestMapping("exclueReserva")
	public String excluir(Long id) {
		// excluir a reserva pelo ID
		Reserva reserva = repository.findById(id).get();
		repository.delete(reserva);
		return "redirect:listareserva/1";
	}

	@Administrador
	@Professor
	@RequestMapping(value = "buscar", method = RequestMethod.GET)
	public String buscar(String buscar, Model model) {
		model.addAttribute("reserva", repository.buscar(buscar));

		return "listas/listarBuscar";
	}

}
