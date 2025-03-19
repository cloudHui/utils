package utils.other.excel;

public class Title {

	private String name;

	private String type;

	private String des;

	public Title(String name, String type, String des) {
		this.name = name;
		this.type = type;
		this.des = des;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}


	@Override
	public String toString() {
		return "Title{" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				", des='" + des + '\'' +
				'}';
	}
}
