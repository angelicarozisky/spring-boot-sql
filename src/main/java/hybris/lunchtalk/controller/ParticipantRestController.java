package hybris.lunchtalk.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hybris.lunchtalk.dao.IParticipantRepository;
import hybris.lunchtalk.model.Participant;

@RestController
public class ParticipantRestController {

	@Autowired
	private IParticipantRepository participantRepository;

	@RequestMapping("/participants-initial")
	Collection<Participant> participants() {
		return this.participantRepository.findAll();
	}
}