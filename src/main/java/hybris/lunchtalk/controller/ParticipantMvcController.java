package hybris.lunchtalk.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hybris.lunchtalk.dao.IParticipantRepository;
import hybris.lunchtalk.model.Pagination;
import hybris.lunchtalk.model.Participant;

@Controller
public class ParticipantMvcController {

	private static final int BUTTONS_TO_SHOW = 3;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = { 5, 10 };
	
	@Autowired
	private IParticipantRepository participantRepository;

	//MongoDB version
	//Only used by MongoDB. Is not mandatory, only generate a sequence ID.
	// @Autowired
	//private SequenceRestController sequenceController;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/login?logout";
	}
    
	@GetMapping("/participants/list")
	public ModelAndView listParticipant(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) {

		ModelAndView modelAndView = new ModelAndView("participants/list");
		//
		// Evaluate page size. If requested parameter is null, return initial
		// page size
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		// Evaluate page. If requested parameter is null or less than 0 (to
		// prevent exception), return initial size. Otherwise, return value of
		// param. decreased by 1.
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Participant> participantList = participantRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pagination pager = new Pagination(participantList.getTotalPages(), participantList.getNumber(), BUTTONS_TO_SHOW);
		// add participant
		modelAndView.addObject("participantList", participantList);
		// evaluate page size
		modelAndView.addObject("selectedPageSize", evalPageSize);
		// add page sizes
		modelAndView.addObject("pageSizes", PAGE_SIZES);
		// add pager
		modelAndView.addObject("pager", pager);

		return modelAndView;
	}

	@GetMapping("/participants/new")
	public String newParticipant(@ModelAttribute("participant") Participant participant) {
		return "participants/new";
	}

	// if MongoDB use String id
	// if SQL database use long id
	@RequestMapping("/participants/edit/{id}")
	public String editParticipant(@PathVariable("id") long id, ModelMap model ) {
		//Participant participant = this.participantRepository.findOne(id);
		Participant participant = this.participantRepository.findById(id).get();
		model.addAttribute("participant", participant);
	    
		return "participants/edit";
	}	
	
	// if MongoDB use String id
	// if SQL database use long id
	@GetMapping("/participants/delete/{id}")
	public String deleteParticipant(@PathVariable("id") long id, RedirectAttributes attr) {
		//Participant participant = this.participantRepository.findOne(id);
		Participant participant = this.participantRepository.findById(id).get();

		this.participantRepository.deleteById(id);

		//this.participantRepository.delete(id);
		attr.addFlashAttribute("message", "Participant '" + participant.getName() + "' removed.");
		return "redirect:/participants/list";
	}

	@RequestMapping(value = "/participants/insert", method = RequestMethod.PUT)
	public String saveParticipant(@Valid @ModelAttribute("participant") Participant participant, BindingResult result,
			RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "/participants/new";
		}

		// MongoDB version
		// Only used by MongoDB. Is not mandatory, only generate a sequence ID.
		//participant.setId(String.valueOf(sequenceController.getNextSequenceId("participant")));
		this.participantRepository.save(participant);
		
		attr.addFlashAttribute("message", "Participant '" + participant.getName() + "' added.");
		return "redirect:/participants/list";
	}

	@RequestMapping(value = "/participants/update", method = RequestMethod.PUT)
	public ModelAndView updateParticipant(@Valid @ModelAttribute("participant") Participant participant, BindingResult result,
			RedirectAttributes attr) {
		if (result.hasErrors()) {
			return new ModelAndView("/participants/edit");
		}

		this.participantRepository.save(participant);

		attr.addFlashAttribute("message", "Participant '" + participant.getName() + "' updated.");
		return new ModelAndView("redirect:/participants/list");
	}
}