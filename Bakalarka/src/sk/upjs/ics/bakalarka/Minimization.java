package sk.upjs.ics.bakalarka;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Minimization {
	public static final int MAX_CHARS = 26;
	public static final int SHIFT = 97;

	private Map<List<Integer>, List<State>> map = new LinkedHashMap<>();

	public Automaton minimize(Automaton oldAutomaton) {
		Automaton newAutomaton = new Automaton();
		Automaton tempAutomaton = oldAutomaton;
		tempAutomaton.generateGroups();
		
		int numberOfGroups;
		if (tempAutomaton.getStates().size() == tempAutomaton.getFinalStates().size()) {
			// ak su vsetky stavy koncove
			numberOfGroups = 1;
		} else {
			numberOfGroups = 2;
		}

		for (int i = 1; i <= numberOfGroups; i++) {
			boolean dived = true;
			while (dived) {
				for (State state : tempAutomaton.getStatesByGroup(i)) {
					List<Integer> groups = new ArrayList<>();
					for (List<State> transitions : state.getTransitions()) {
						if (!transitions.isEmpty()) {
							groups.add(transitions.get(0).getGroup());
						}
					}
					
					if (map.get(groups) == null) {
						List<State> list = new ArrayList<>();
						list.add(state);
						map.put(groups, list);
					} else {
						List<State> list = map.get(groups);
						list.add(state);
						map.put(groups, list);
					}
				
					if (map.size() == 1) {
						dived = false;
					} else {
						dived = true;
					}

				}

				// ak mapa ma len jeden riadok, znamena ze sa nic nerozdelilo
				// ak mapa ma viac ako jeden riadok, znamena ze treba pridat
				// nove skupiny
				// nech velkost mapy je n, potom novych skupin bude n-1
				int numberOfNewGroups = map.size() - 1;
				for (int j = 1; j <= numberOfNewGroups; j++) {
					// prvym n-1 riadokm v mape nastavim nove skupiny
					List<State> states = (List<State>) map.values().toArray()[j];
					for (State state : states) {
						state.setGroup(numberOfGroups + 1);
					}
					numberOfGroups++;
				}

				map.clear();
			}
		}

		// z pomocneho automatu vytvorime novy automat podla skupin
		// vytvorim tolko stavov kolko mam skupin
		for (int i = 1; i <= numberOfGroups; i++) {
			State state = new State();
			state.setGroup(i);
			newAutomaton.addState(state);
		}

		// nastavime prechody podla skupin zo stareho automatu
		for (int i = 1; i <= numberOfGroups; i++) {
			State oldState = oldAutomaton.getAStateByGroup(i);
			State newState = newAutomaton.getAStateByGroup(i);
			for (int j = 0; j < MAX_CHARS; j++) {
				if (!oldState.getTransitions()[j].isEmpty()) {
					int targetGroup = oldState.getTransitions()[j].get(0).getGroup();
					State targetState = newAutomaton.getAStateByGroup(targetGroup);
					newState.addTransition((char) (j + SHIFT), targetState);
				}
			}
		}

		// nastavime pociatocny stav
		int groupOfInitialState = oldAutomaton.getInitialState().getGroup();
		newAutomaton.setInitialState(newAutomaton.getAStateByGroup(groupOfInitialState));

		// nastavime koncove stavy
		List<State> finalStates = oldAutomaton.getFinalStates();
		for (State state : finalStates) {
			int groupOfFinalStates = state.getGroup();
			State finalState = newAutomaton.getAStateByGroup(groupOfFinalStates);
			if (!newAutomaton.getFinalStates().contains(finalState)) {
				newAutomaton.addFinalState(finalState);
			}
		}

		newAutomaton.generateId();
		return newAutomaton;
	}
}
