import java.sql.Date;

public class Table {
	private Integer id;
	private String name;
	private String email;
	private Date birth;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Table(Integer id, String name, String email, Date birth) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.birth = birth;
	}

	public Table() {
		super();
	}

	@Override
	public String toString() {
		return "Table [id=" + id + ", name=" + name + ", email=" + email + ", birth=" + birth + "]";
	}

}
