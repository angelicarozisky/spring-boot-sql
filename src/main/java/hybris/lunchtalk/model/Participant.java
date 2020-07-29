package hybris.lunchtalk.model;

//SQL version
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Participant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 2, max = 100)
	@Column(nullable = false, length = 100)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Participant [id=" + id + ", name=" + name + "]";
	}

	public Participant(String name) {
		this.name = name;
	}

	public Participant() {

	}
}

//MongoDB version
/*
 * import org.springframework.data.annotation.Id; import
 * org.springframework.data.mongodb.core.mapping.Document;
 * 
 * @Document(collection = "participant") public class Participant {
 * 
 * @Id private String id;
 * 
 * private String name;
 * 
 * public String getId() { return id; }
 * 
 * public void setId(String id) { this.id = id; }
 * 
 * public String getName() { return name; }
 * 
 * public void setName(String name) { this.name = name; }
 * 
 * @Override public String toString() { return String.format(
 * "Participant[id=%s, name='%s']", id, name); }
 * 
 * public Participant(String name) { this.name = name; }
 * 
 * public Participant() {
 * 
 * } }
 */