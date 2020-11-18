import java.awt.*; 
public class IMF implements Comparable<IMF>{
	Point point;
	int id;
	int health;
	int carriedAt;

	public IMF(Point point, int id, int health) {
		this.point = point;
		this.id = id;
		this.carriedAt = -1;
		this.health= health;
	}
	
	public boolean notCarriedYet() {
		if(carriedAt == -1) {
			return true;
		}
		return false;
	}
	
	public void markAsCarried(int step) {
		this.carriedAt = step;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Point getPoint() {
		return point;
	}
	public void Carry() {
		this.point.setLocation(-1, -1);
	}

	public int getId() {
		return id;
	}

	public void setCarriedAt(int carriedAt) {
		this.carriedAt = carriedAt;
	}

	@Override
	public String toString() {
		return "IMF [point=" + point + ", id=" + id + ", health=" + health + ", carriedAt=" + carriedAt + "]";
	}

	@Override
	public int compareTo(IMF m) {
		// TODO Auto-generated method stub
		if(this.id==(m.id)) {
			return 0;
		}
		return 1;
	}

}
