package hybris.lunchtalk.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import hybris.lunchtalk.model.Participant;

@RepositoryRestResource
public interface IParticipantRepository extends JpaRepository<Participant, Long> {
	
	// select * from participant where name = :name
	@RestResource(path="by-name")
	Collection<Participant> findByName ( @Param("name") String name);
}