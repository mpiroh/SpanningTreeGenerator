package sk.upjs.ics.bakalarka;

import java.util.ArrayList;

public class State {
	public static final int MAX_CHARS = 26;
	public static final int SHIFT = 97;

	private int id;
	private long bitcode;
	private int group;
	
	private ArrayList<State> transitions[] = new ArrayList[MAX_CHARS];
	private ArrayList<State> epsilonTransitions = new ArrayList();

	public State() {
		for (int i = 0; i < transitions.length; i++) {
			transitions[i] = new ArrayList();
		}
	}

	public void addTransition(char c, State state) {
		transitions[(int) c - SHIFT].add(state);
	}

	public void pridajEpsilonTransition(State state) {
		epsilonTransitions.add(state);
	}

	//------------------------------------------------------------------------

	public ArrayList<State>[] getTransitions() {
		return transitions;
	}

	public void setTransitions(ArrayList<State>[] transitions) {
		this.transitions = transitions;
	}

	public ArrayList<State> getEpsilonTransitions() {
		return epsilonTransitions;
	}

	public void setEpsilonTransitions(ArrayList<State> epsilonTransitions) {
		this.epsilonTransitions = epsilonTransitions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public long getBitcode() {
		return bitcode;
	}

	public void setBitcode(long bitcode) {
		this.bitcode = bitcode;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (char i = 0; i < transitions.length; i++) {
			if (transitions[i].isEmpty())
				continue;
			char c = (char) (i + SHIFT);
			for (State state : transitions[i]) {
				sb.append(c + " -> ");
				sb.append(state.getId());
				sb.append("\n");
			}
		}
		for (State state : epsilonTransitions) {
			sb.append("E" + " -> ");
			sb.append(state.getId());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public String toStringWithoutEpsilonTransitions() {
		StringBuilder sb = new StringBuilder();

		for (char i = 0; i < transitions.length; i++) {
			if (transitions[i].isEmpty())
				continue;
			char c = (char) (i + SHIFT);
			for (State state : transitions[i]) {
				sb.append(c + " -> ");
				sb.append(state.getId());
				sb.append("\n");
			}
		}		
		return sb.toString();
	}
	
	public String toStringBitcodes() {
		StringBuilder sb = new StringBuilder();

		for (char i = 0; i < transitions.length; i++) {
			if (transitions[i].isEmpty())
				continue;
			char c = (char) (i + SHIFT);
			for (State state : transitions[i]) {
				sb.append(c + " -> ");
				sb.append(state.getBitcode());
				sb.append("\n");
			}
		}
		for (State state : epsilonTransitions) {
			sb.append("E" + " -> ");
			sb.append(state.getBitcode());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
