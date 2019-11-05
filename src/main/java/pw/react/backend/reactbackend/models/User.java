package pw.react.backend.reactbackend.models;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

	private static final long serialVersionUID = -2343243243242432341L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "login", nullable = false)
	private String login;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	public User(String login, String firstName, String lastName, LocalDate dateOfBirth, boolean isActive) {
		this.firstName=firstName;
		this.lastName=lastName;
		this.login=login;
		this.dateOfBirth=dateOfBirth;
		this.isActive=isActive;
	}

	public void setAll(int id, String login, String firstName, String lastName, LocalDate LocalDateOfBirth, boolean isActive) {
		setId(id);
		setLogin(login);
		setFirstName(firstName);
		setLastName(lastName);
		setDateOfBirth(LocalDateOfBirth);
		setIsActive(isActive);
	}

	public void setAll(String login, String firstName, String lastName, LocalDate LocalDateOfBirth, boolean isActive) {
		setLogin(login);
		setFirstName(firstName);
		setLastName(lastName);
		setDateOfBirth(LocalDateOfBirth);
		setIsActive(isActive);
	}

	public User() {}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(LocalDate DateOfBirth) {
		this.dateOfBirth = DateOfBirth;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "{"+
				"\"login\":"+"\"marti3\","+
				"\"firstName\":"+"\"marti3\","+
				"\"lastName\":"+"\"marti3\","+
				"\"dateOfBirth\":"+"\"1998-01-01\","+
				"\"active\":"+"false"+
				"}";
	}
}