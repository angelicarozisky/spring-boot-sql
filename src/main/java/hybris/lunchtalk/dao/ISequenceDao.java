package hybris.lunchtalk.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import hybris.lunchtalk.utility.SequenceException;

// MongoDB version
// Only used by MongoDB. Is not mandatory, only generate a sequence ID.

@RepositoryRestResource
public interface ISequenceDao {

	long getNextSequenceId(String key) throws SequenceException;
}