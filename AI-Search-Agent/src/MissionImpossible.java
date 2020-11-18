import java.util.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class MissionImpossible extends GeneralSearch {
	static int minGridSize=5;
	static int maxGridSize=5;
	static int minNumberOfMembers=6;
	static int maxNumberOfMembers=6;
	static int minCarried=1;
	static HashSet<IMF> members;
	static int minHealth=1;
	static int maxHealth=99;
	static int m;
	static int n;
	static int ex;
	static int ey;
	static Point submarine;
    static State initialState;
    static State goalState;
    static int c;
    static int carried = 0;
    static int timeStep=0;
    static int atSubmarine=0;

public static boolean ThereIsMemberHere(int x, int y) {
	for(IMF imf : members) {
		if(imf.point.x == x && imf.point.y ==y) {
			return true;
		}
	}
	return false;
	
}
 static void genGrid() {
	  members = new HashSet<>();
	  m = (int) (Math.random() * (maxGridSize - minGridSize + 1) + minGridSize);
	  n = (int) (Math.random() * (maxGridSize - minGridSize + 1) + minGridSize);
	  ex = (int) (Math.random() * (m ));
	  ey = (int) (Math.random() * (n ));
	 int sx ;
	 int sy ;
	 do {
		  sx = (int) (Math.random() * (m ));
		  sy = (int) (Math.random() * (n ));
	 }
	 while (ex==sx && ey== sy);

	 submarine = new Point(sx,sy);
	 int numberOfMembers = (int) (Math.random() * (maxNumberOfMembers - minNumberOfMembers + 1) + minNumberOfMembers);
//	 c = (int) (Math.random() * (maxNumberOfMembers - minCarried + 2) + minCarried);
	 c = 6;
	 HashMap<IMF, Integer> memberStates = new HashMap<IMF, Integer>();
	 
	 for (int i=0; i<numberOfMembers; i++ ) {
		 int x;
		 int y;
		 do {
			  x = (int) (Math.random() * (m ));
			  y = (int) (Math.random() * (m ));
		 }
		 while ((x==sx && y== sy) || (x==ex && y== ey) || ThereIsMemberHere(x,y));
		 
		 int health = (int) (Math.random() * (maxHealth - minHealth + 2) + minHealth);		 
		 Point p=new Point(x,y);
		 IMF member = new IMF(p, i, health);
		 members.add(member);
		 memberStates.put(member,2);
	 }
	 initialState = new State( new Point(ex,ey));
	 goalState = new State(new Point(sx,sy),  null, memberStates);
	 	 
	 visualize(m,n,ex,ey,sx,sy,members);
		 
	 
 }
 
 
 private static void visualize(int m, int n, int ex, int ey, int sx, int sy, HashSet<IMF> members) {
	// TODO Auto-generated method stub
	 for(int i=0;i<m;i++) {
		 System.out.print("||");
		 for(int j=0;j<n;j++) {
			 if(i==ex && j==ey) {
				 System.out.print("    E   ||");
			 }
			 else if(i==sx && j==sy) {
				 System.out.print("    S   ||");
			 }
			 else {
				 boolean flag = false;
				 for(IMF member : members) {
					 if(member.getPoint().getX()==i && member.getPoint().getY()==j) {
						 System.out.print("IMF("+String.format("%03d",member.getHealth())+")||");
						 flag = true;
						 break;
					 }
				 }
				 if(!flag) {
					 System.out.print("   --   ||");
				 }

			 }
		 }
		 System.out.println();

	 }
	
}

 
 
private static HashSet<State> MoveOperator(HashSet<State> visited, State currentState){
	HashSet<State> possibleNextState = new HashSet<State>(); 
	int x = (int) currentState.position.getX();
	int y = (int) currentState.position.getY();
	HashMap<IMF, Integer> memberStates_1 = new HashMap<IMF, Integer>();
	HashMap<IMF, Integer> memberStates_2 = new HashMap<IMF, Integer>();
	HashMap<IMF, Integer> memberStates_3 = new HashMap<IMF, Integer>();
	HashMap<IMF, Integer> memberStates_4 = new HashMap<IMF, Integer>();

	for(Map.Entry<IMF, Integer> set : currentState.memberStates.entrySet()) {
		memberStates_1.put(set.getKey(),set.getValue());
		memberStates_2.put(set.getKey(),set.getValue());
		memberStates_3.put(set.getKey(),set.getValue());
		memberStates_4.put(set.getKey(),set.getValue());

	}
	
			
	// check which from the possible 4 states inside the grid
	// x+1 , y
	if(x+1<m) {
			State downState = new State(new Point(x+1,y),  currentState, memberStates_1);
				possibleNextState.add(downState);
	}
	// x-1 , y
	if(x-1 >=0) {
			State upState = new State(new Point(x-1,y), currentState, memberStates_2);
				possibleNextState.add(upState);
	}

	// x , y+1
	if(y+1 <n ) {
			State rightState = new State(new Point(x,y+1),  currentState, memberStates_3);
				possibleNextState.add(rightState);
	}
	// x,  y-1
	if(y-1 >= 0) {
			State leftState = new State(new Point(x,y-1), currentState, memberStates_4);
				possibleNextState.add(leftState);
	}
	
	return possibleNextState;
	
}

private static State CarryOperator(HashSet<State> visited, State currentState) {
	// TODO Auto-generated method stub 
	// if you found a member here, and if you can carry him, 
	// (we make sure this member is not carried before in the isMemberHere() method),
	//then go to new state by applying carry operator on him
	for(IMF member : members) {
		if(currentState.isMemberHere(member) && !currentState.atSubmarine(submarine)) {
			if(currentState.HowManyCurrentlyCarried() < c) {
				
				HashMap<IMF, Integer> memberStates = new HashMap<IMF, Integer>();

				for(Map.Entry<IMF, Integer> set : currentState.memberStates.entrySet()) {
					memberStates.put(set.getKey(),set.getValue());
				}
				
				memberStates.put(member, 1);
				State stateAfterCarry = new State(currentState.position, currentState, memberStates);
				return stateAfterCarry;
			}
		}
	}
	
	
	return null;
}


private static State DropOperator(HashSet<State> visited, State currentState) {
	// TODO Auto-generated method stub
	
	// if you are at submarine, and you carry members, drop all of them.
	if(currentState.atSubmarine(submarine)) {
		if( currentState.HowManyCurrentlyCarried() >0) {
			HashMap<IMF, Integer> memberStates = new HashMap<IMF, Integer>();
			
			for(Map.Entry<IMF, Integer> set : currentState.memberStates.entrySet()) {
				if(set.getValue()==1) {
					memberStates.put(set.getKey(),2);
				}
				else {
					memberStates.put(set.getKey(),set.getValue());
				}
			}
			

			State stateAfterDrop = new State(currentState.position, currentState, memberStates);
			return stateAfterDrop;
		}
	}
	return null;
}

private static HashSet<State> ApplyOperators(HashSet<State> visited, State currentState){
	
	HashSet<State> nextStates = MoveOperator(visited, currentState);
	State nextStates_2 = CarryOperator(visited, currentState);
	State nextStates_3 = DropOperator(visited, currentState);
	if(nextStates_2 != null) {
		nextStates.add(nextStates_2);
	}
	if(nextStates_3 != null) {
		nextStates.add(nextStates_3);
	}

	return nextStates;
	
}



static boolean goalTest(State state) {
	if (state.position.equals(goalState.position) ) {
		if(state.memberStates.equals(goalState.memberStates)) { 
			//compare the elements of the two hash-sets, two IMF members are equal if they acquire
			// the same id.
			return true;
		}
	}
	return false;
}




static String solve(String grid, String strategy, boolean visualize) {
	// solve for BFS for now
    
	
	HashSet<State> visited = new HashSet<State>(); 
    LinkedList<State> queue = new LinkedList<State>(); 
    queue.add(initialState);
    visited.add(initialState);
    
    while(!queue.isEmpty()) {
    	State currentState = queue.pop();
//    	System.out.println(currentState);
//    	visited.add(currentState);
    	timeStep++;
//    	if(timeStep%1000==0)
//    		System.out.println(timeStep);
    	
    	
    	// check if we are at the goal state
    	if(goalTest(currentState)) {
    		System.out.println("-----------------------------------------------");
    		goalState.setParent(currentState);
    		State trace = goalState;
    		System.out.println(trace);
    		while(trace.parent != null) {
    			System.out.println(trace.parent);
    			trace = trace.parent;
    		}
    		System.out.println("expanded nodes = "+timeStep);
    		System.out.println( "GOOD BYE");
    		System.out.println(members);
    		return "";
    	}

    	// get all next states that can come after the current state
    	// using the operators [MOVE, CARRY, DROP]
    	HashSet<State> nextStates = ApplyOperators(visited, currentState);
    	for(State s: nextStates) {
    		if(!visited.contains(s)) {
        		queue.add(s);
        		visited.add(s);
    		}
    	}
    	nextStates = null;
    	
    }
    
    System.out.println( "NO SOLUTION FOUND");
    return "";
	 
 }
 
 
 public static void main(String[] args) {
	genGrid();
	Instant start = Instant.now();
	solve("grid", "hh", false);

	Instant end = Instant.now();

	Duration interval = Duration.between(start, end);

	System.out.println("Execution time in seconds: " +
							interval.getSeconds());
}
}
