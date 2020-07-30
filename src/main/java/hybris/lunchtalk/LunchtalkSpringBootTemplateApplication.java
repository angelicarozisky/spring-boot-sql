package hybris.lunchtalk;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import hybris.lunchtalk.controller.SequenceRestController;
import hybris.lunchtalk.dao.IParticipantRepository;
import hybris.lunchtalk.model.Participant;
import hybris.lunchtalk.utility.RandomString;

@SpringBootApplication
public class LunchtalkSpringBootTemplateApplication{

	// MongoDB version
	// Only used by MongoDB. Is not mandatory, only generate a sequence ID.
	// @Autowired
	// private SequenceRestController sequenceController;

	public static void main(String[] args) {

		SpringApplication.run(LunchtalkSpringBootTemplateApplication.class, args);
	}

	// Spring Boot Issue - https://github.com/spring-projects/spring-boot/issues/11911
    // The request was rejected because the URL contained a potentially malicious String ";"   
    // https://stackoverflow.com/questions/48580584/stricthttpfirewall-in-spring-security-4-2-vs-spring-mvc-matrixvariable
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        
        return firewall;
    }   

// SQL version    
	@Bean
	CommandLineRunner runner (IParticipantRepository rr) {
		return args -> {
			Arrays.asList("Ken,Marcelo,Daniel,Andreas,Ivan,Christian,Arthur,Sabrina,Barbara,Andrea,Dennis,a001,b001,c001,d001,e001,f001,g001,h001,i001".split(",")).forEach(n -> rr.save(new Participant(n)));

			rr.findAll().forEach( System.out::println );
			rr.findByName("Ken").forEach(System.out::println);
		};
	}

// MongoDB version
	/*
	 * @Bean CommandLineRunner runner (IParticipantRepository rr) { return args -> {
	 * rr.deleteAll();
	 * 
	 * Participant p1 = new Participant();
	 * 
	 * p1.setId(String.valueOf(sequenceController.getNextSequenceId("participant")))
	 * ; p1.setName("Ken"); rr.save(p1);
	 * 
	 * Participant p2 = new Participant();
	 * 
	 * p2.setId(String.valueOf(sequenceController.getNextSequenceId("participant")))
	 * ; p2.setName("Marcelo"); rr.save(p2);
	 * 
	 * 
	 * RandomString randomStr = new RandomString(8);
	 * 
	 * for(int i=0; i < 50; i++ ) { Participant participant = new Participant();
	 * 
	 * participant.setId(String.valueOf(sequenceController.getNextSequenceId(
	 * "participant"))); participant.setName(randomStr.nextString());
	 * 
	 * rr.save(participant); }
	 * 
	 * rr.findAll().forEach( System.out::println );
	 * rr.findByName("Ken").forEach(System.out::println); }; }
	 */	
	@Bean
	HealthIndicator hybrisIndicator () {
		return ()-> Health.status("Lunch Talk OK!").build();
	}
}