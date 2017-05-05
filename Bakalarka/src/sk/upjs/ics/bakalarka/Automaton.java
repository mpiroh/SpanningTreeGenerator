package sk.upjs.ics.bakalarka;

import java.util.ArrayList;
import java.util.List;

public class Automaton {
	private List<State> states = new ArrayList<State>();
	private State initialState;
	private List<State> finalStates = new ArrayList<State>();
	
	public void addState(State state) {
		states.add(state);
	}
	
	public void pridajFinalState(State state) {
		finalStates.add(state);
	}
	
	public void generateId() {
		int id = 0;
		for (State state : states) {
			state.setId(id);
			id++;
		}
	}
	
	public void resetId() {
		for (State state : states) {
			state.setId(-1);
		}
	}
	
	/*public void vyrobBitKody() {
		long bitKod = 1;
		for (Stav stav : stavy) {
			stav.setBitKod(bitKod);
			bitKod = bitKod * 10;
		}
	}*/
	public void generateBitcodes() {
		int i = 0;
		for (State state : states) {
			state.setBitcode((long) (Math.pow(2, i)));
			i++;
		}
	}
	
	public void addTransition(State s1, char c, State s2) {
		for (State state : states) {
			if (state == s1) {
				state.addTransition(c, s2);
				return;
			}
		}
	}
	
	/*public Stav getStavPodlaBitKodu(long bitKod) {
		for (Stav stav : stavy) {
			if (stav.getBitKod() == bitKod) {
				return stav;
			}
		}
		return null;
	}*/
	public State getStateByBitcode(long bitcode) {
		for (State state : states) {
			if (state.getBitcode() == bitcode) {
				return state;
			}
		}
		return null;
	}
	
	public List<State> getStatesByGroup(int group) {
		List<State> statesByGroup = new ArrayList<>();
		for (State state : states) {
			if (state.getGroup() == group) {
				statesByGroup.add(state);
			}
		}
		return statesByGroup;
	}
	
	public void generateGroups() {
		for (State state : states) {
			if (finalStates.contains(state)) {
				state.setGroup(1);
			} else {
				state.setGroup(2);
			}
		}
	}
	
	public State getAStateByGroup(int group) {
		for (State state : states) {
			if (state.getGroup() == group) {
				return state;
			}
		}
		return null;
	}
	
	public State getStateById(int id) {
		for (State state: states) {
			if (state.getId() == id) {
				return state;
			}
		}
		return null;
	}
	
	public Automaton determinize() {
		Determinization determinization = new Determinization();
		return determinization.toDFA(this);
	}
	
	public Automaton minimize() {
		Minimization minimization = new Minimization();
		return minimization.minimize(this);
	}
	
	//------------------------------------------------------------------------
	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public List<State> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(List<State> finalStates) {
		this.finalStates = finalStates;
	}
	
	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (State state : states) {
			sb.append("State " + state.getId() + ": ");
			if (state == initialState)
				sb.append("(initial) ");
			if (finalStates.contains(state))
				sb.append("(final) ");
			sb.append("\n");
			
			sb.append(state);
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public String toStringBitcodes() {
		StringBuilder sb = new StringBuilder();
		for (State state : states) {
			sb.append("State " + state.getBitcode() + ": ");
			if (state == initialState)
				sb.append("(initial) ");
			if (finalStates.contains(state))
				sb.append("(final) ");
			sb.append("\n");
			
			sb.append(state.toStringBitcodes());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	/*public String toStringBezE() {
		StringBuilder sb = new StringBuilder();
		for (Stav stav : stavy) {
			sb.append("Stav " + stav.getId() + ": ");
			if (stav == pociatocnyStav)
				sb.append("(poèiatoèný) ");
			if (koncoveStavy.contains(stav))
				sb.append("(koncový) ");
			sb.append("\n");
			
			sb.append(stav.toStringBezE());
			sb.append("\n");
		}
		
		return sb.toString();
	}*/
	
}
