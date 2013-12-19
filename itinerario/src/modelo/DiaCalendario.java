package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DiaCalendario {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private CalendarioLetivo calendario;
}
