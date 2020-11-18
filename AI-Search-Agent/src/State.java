import java.util.*;
import java.awt.*;

public class State {
	Point position ;
	State parent;
	/* 
	 * HashMap is in form of <key = IMF member, value = Integer>
	 * The values represents: 
	 		0 for no action taken on that member
			1 for member is currently carried
			2 for this member is carried before and currently at submarine
	 * to check that this member can be taken the value should be == 0
	*/

	HashMap<IMF, Integer> memberStates;
	int gn; //the path cost function until this state; needs the gn of the parent node to be calculated
	int hn; // independent of the parent; needs to be calculated wrt the goal state
	
	public State(Point position,  State parent, HashMap<IMF,Integer> cm) {
		super();
		this.position = position;
		this.parent = parent;
		this.memberStates = cm;
		this.gn = 0;
		this.hn=0;
	}
	public State(Point position) {
		super();
		this.position = position;
		this.parent = null;
		this.memberStates = new HashMap<>();
		this.gn = 0;
		this.hn=0;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((memberStates == null) ? 0 : memberStates.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		State other = (State) obj;
		if (memberStates == null) {
			if (other.memberStates != null)
				return false;
		} else if (!memberStates.equals(other.memberStates))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
	public State getParent() {
		return parent;
	}

	public void setParent(State parent) {
		this.parent = parent;
	}
	
	public void setGn(int gn) {
		this.gn=gn;
	}
	
	public int getGn() {
		return this.parent.gn + this.gn;
	}
	
	public int HowManyCurrentlyCarried() {
		int counter = 0;
		for(Map.Entry<IMF, Integer> set : this.memberStates.entrySet()) {
			if(set.getValue()==1) {
				counter+=1;
			}

		}
		
		return counter;
	}
	
	public int HowManyDropped() {
		int counter = 0;
		for(Map.Entry<IMF, Integer> set : this.memberStates.entrySet()) {
			if(set.getValue()==2) {
				counter+=1;
			}

		}
		
		return counter;
	}
	
	
	public boolean isMemberHere(IMF member) {
		boolean atThisPosition  = ((this.position.getX() == member.point.getX()) && (this.position.getY() == member.point.getY()));
		boolean notCarriedYet = member.notCarriedYet();
		return (atThisPosition && notCarriedYet);
	}
	
	public boolean atSubmarine(Point s) {
		return ((this.position.getX() == s.getX()) && (this.position.getY() == s.getY()));
	}

	@Override
	public String toString() {
		return "State [position=" + position + ", carried=" + HowManyCurrentlyCarried() + ", dropped=" + HowManyDropped() + ", parent=" + parent
				+ "]";
	}
	
	
	
}

