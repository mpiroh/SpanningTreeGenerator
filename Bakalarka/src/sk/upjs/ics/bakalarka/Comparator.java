package sk.upjs.ics.bakalarka;

import java.util.ArrayList;
import java.util.List;

public class Comparator {
	public boolean compare(String string1, String string2) {
		RegularExpression regExp1 = new RegularExpression(string1);
		RegularExpression regExp2 = new RegularExpression(string2);
		
		// kontrola identickosti vyrazov
		if (regExp1.getExpression().equals(regExp2.getExpression())) {
			return true;
		}
		
		// kontrola rozdielnej abecedy
		if (!regExp1.getAlphabet().equals(regExp2.getAlphabet())) {
			return false;
		}
		
		Automaton nfa = regExp1.toNFA();
		Automaton dfa = nfa.determinize();
		Automaton minAutomaton1 = dfa.minimize();
		
		nfa = regExp2.toNFA();
		dfa = nfa.determinize();
		Automaton minAutomaton2 = dfa.minimize();

		if (minAutomaton1.getStates().size() != minAutomaton2.getStates().size()) {
			return false;
		}

		minAutomaton1.resetId();
		minAutomaton2.resetId();

		// zmena id-ciek prveho automatu
		int newId = 0;
		minAutomaton1.getInitialState().setId(newId);
		int i = 0;

		while (true) {
			//int temp = newId;
			for (List<State> transitions : minAutomaton1.getStateById(i).getTransitions()) {
				for (State state : transitions) {
					if (state.getId() == -1) {
						state.setId(++newId);
					}
				}
			}
			//if (temp == newId) {
			//	break;
			//}
			i++;
			if (i > newId) {
				break;
			}
		}
		
		// zmena id-ciek prveho automatu
		newId = 0;
		minAutomaton2.getInitialState().setId(newId);
		i = 0;

		while (true) {
			//int temp = newId;
			for (List<State> transitions : minAutomaton2.getStateById(i).getTransitions()) {
				for (State stav : transitions) {
					if (stav.getId() == -1) {
						stav.setId(++newId);
					}
				}
			}
			//if (temp == newId) {
			//	break;
			//}
			i++;
			if (i > newId) {
				break;
			}
		}
		
		// porovnanie prechodov a koncovych stavov
		int size = minAutomaton1.getStates().size();
		List<Integer> list1 = new ArrayList<>();
		for (int j = 0; j < size; j++) {
			for (List<State> transitions : minAutomaton1.getStateById(j).getTransitions()) {
				for (State state : transitions) {
					list1.add(state.getId());
				}
			}
		}
		List<Integer> list2 = new ArrayList<>();
		for (int j = 0; j < size; j++) {
			for (List<State> transitions : minAutomaton2.getStateById(j).getTransitions()) {
				for (State state : transitions) {
					list2.add(state.getId());
				}
			}
		}
		
		List<Integer> finalStates1 = new ArrayList<>();
		List<Integer> finalStates2 = new ArrayList<>();
		for (State state : minAutomaton1.getFinalStates()) {
			finalStates1.add(state.getId());
		}
		for (State state : minAutomaton2.getFinalStates()) {
			finalStates2.add(state.getId());
		}
		
		return list1.equals(list2) && finalStates1.equals(finalStates2);
	}
}
