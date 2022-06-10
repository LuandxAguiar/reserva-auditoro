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
import br.com.sp.senai.auditorio.auditorio.annotation.Publico;
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

	@Professor
	@Administrador
	@RequestMapping(value = "agendamento", method = RequestMethod.GET)
	private String formCalendario(Model model) {
		model.addAttribute("tipo", trRep.findAll());
		model.addAttribute("reserva", repository.findAll());
		return "reserva/calendario";
	}

	// metodo de salvamento da agenda
	
	@Administrador
	@RequestMapping(value = "salvareserva", method = RequestMethod.POST)
	public String salvar(Reserva reserva, String start, Periodo periodo, RedirectAttributes attr) throws Exception {
		//confere se o agendamento e nulo 
		Reserva agendamento = repository.findByStartAndPeriodo(start, periodo);

		if (agendamento != null) {

			attr.addFlashAttribute("mensagemErro", "Verificar campos");
			return "redirect:agendamento";
		}
		
		// conferindo se já tem alguma reserva para não salvar todos 
		if (reserva.getPeriodo() == Periodo.TODOS) {
			List<Reserva> agendamentoNoPeriodo = repository.findByStart(start);
			// se tamanho for igual a 0 erro de cadastro 
			if (agendamentoNoPeriodo.size() > 0) {
				System.out.println("erro");
				attr.addFlashAttribute("mensagemErro", "Erro de Cadastro. Já tem Algum Periodo para esse dia");
				return "redirect:agendamento";
			}


		}
		if (reserva.getPeriodo() == Periodo.TODOS) {
			reserva.setBackgroundColor("#613dc1");
		}else if (reserva.getPeriodo() == Periodo.MANHA) {
			reserva.setBackgroundColor("#99e2b4");
		}else if (reserva.getPeriodo() == Periodo.TARDE) {
			reserva.setBackgroundColor("#0096c7");
		}else {
			reserva.setBackgroundColor("#353535");
		}
		// pegando a variavel data e transformando em CALENDAR 
		//format para data 
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		//CALENDAR de data ATUAL 
		Calendar dataAtual = Calendar.getInstance();
		//variavel para pegar a data que foi cadastrada 
		Calendar dataSalva = Calendar.getInstance();
		//PEGANDO A STRING DATA E TRANSFORMANDO EM CALENDAR 
		dataSalva.setTime(formato.parse(start));
		//CALENDAR - 1, pois sem isso não consegue cadastrar o dia atual 
		dataAtual.add(Calendar.DATE, -1);

		//fazendo um for para passar por todos campos da data 
		for (Reserva res : repository.findByStart(start)) {

			// repository.deleteById(res.getId());
			// impede se ha um todos salvo, outros periodos sao negados
			if (res.getStart() != null && res.getPeriodo() != Periodo.TODOS) {
				System.out.println("todos");
				
				attr.addFlashAttribute("mensagemSucesso", "Seu agendamento no Auditorio foi cadastrado");
				repository.save(reserva);
			} else if (res.getStart() != null && res.getPeriodo() == Periodo.MANHA) {
				repository.deleteById(res.getId());
				attr.addFlashAttribute("mensagemErro", "verificar data ou periodo");
				System.out.println("passouMan");
				return "redirect:agendamento";
			} else if (res.getStart() != null && res.getPeriodo() == Periodo.TARDE) {
				repository.deleteById(res.getId());
				attr.addFlashAttribute("mensagemErro", "verificar data ou periodo");
				System.out.println("passoutarde");
				return "redirect:agendamento";
			} else if (res.getStart() != null && res.getPeriodo() == Periodo.NOITE) {
				repository.deleteById(res.getId());
				attr.addFlashAttribute("mensagemErro", "verificar data ou periodo");
				System.out.println("passouNoi");
				return "redirect:agendamento";

			}
		
			// logica para não salvar todos se tiver outro periodo
			//mensagem de erro
		
			return "redirect:agendamento";
		}
		//pegando data cadastrada e atual 
		//logica para fazer com que dias que ja passaram não cadastre 
		// PEGAR A DATA E FAZER A COMPARAÇÃO 
		//PARA NÃO SALVAR DIA QUE JA PASSOU 
		// 
		if (dataSalva.before(dataAtual)) {
			attr.addFlashAttribute("mensagemErro", "verificar data");
			System.out.println("Erro da data");
			return "redirect:agendamento";
		}
		//jogar cor 
		
		
		//ADICIONA RESERVA 
		repository.save(reserva);
		attr.addFlashAttribute("mensagemSucesso", "Sua data foi salva");
		return "redirect:agendamento";
	}

	// listar as reservas realizadas

	@Administrador
	@Professor
	@RequestMapping("listareserva/{page}")
	public String listaReserva(Model model, @PathVariable("page") int page) {
		PageRequest pageble = PageRequest.of(page - 1, 8, Sort.by(Sort.Direction.ASC, "start"));

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
	
	@Publico
	@Professor
	@Administrador
	@RequestMapping(value = "evento", method = RequestMethod.GET)
	private String evento() {
		return "eventoApi/evento";
	}

}
