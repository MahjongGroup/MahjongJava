package test.system;

/**
 * 対局者を表すクラス。idは全対局の中で唯一のものである。
 *  
 * @author kohei
 *
 */
public class Player {
	private final int id;
	private final String name;
	private final boolean isMan;
	
	public Player(int id, String name, boolean isMan){
		this.id = id;
		this.name = name;
		this.isMan = isMan;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMan() {
		return isMan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", isMan=" + isMan + "]";
	}
	
	
	
}
