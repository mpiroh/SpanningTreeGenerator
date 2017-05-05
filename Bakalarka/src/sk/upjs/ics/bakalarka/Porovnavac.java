package sk.upjs.ics.bakalarka;

import java.util.ArrayList;
import java.util.List;

public class Porovnavac {
	public boolean compare(String vyraz1, String vyraz2) {
		RegularExpression rv1 = new RegularExpression(vyraz1);
		RegularExpression rv2 = new RegularExpression(vyraz2);
		
		// kontrola identickosti vyrazov
		if (rv1.getExpression().equals(rv2.getExpression())) {
			return true;
		}
		
		// kontrola rozdielnej abecedy
		if (!rv1.getAlphabet().equals(rv2.getAlphabet())) {
			return false;
		}
		
		Automaton n = rv1.toNFA();
		Automaton d = n.determinize();
		Automaton automat1 = d.minimize();
		
		n = rv2.toNFA();
		d = n.determinize();
		Automaton automat2 = d.minimize();

		if (automat1.getStates().size() != automat2.getStates().size()) {
			return false;
		}

		automat1.resetId();
		automat2.resetId();

		// zmena id-ciek prveho automatu
		int newId = 0;
		automat1.getInitialState().setId(newId);
		int i = 0;

		while (true) {
			//int temp = newId;
			for (List<State> prechody : automat1.getStateById(i).getTransitions()) {
				for (State stav : prechody) {
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
		
		// zmena id-ciek prveho automatu
		newId = 0;
		automat2.getInitialState().setId(newId);
		i = 0;

		while (true) {
			//int temp = newId;
			for (List<State> prechody : automat2.getStateById(i).getTransitions()) {
				for (State stav : prechody) {
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
		int size = automat1.getStates().size();
		List<Integer> list1 = new ArrayList<>();
		for (int j = 0; j < size; j++) {
			for (List<State> prechody : automat1.getStateById(j).getTransitions()) {
				for (State stav : prechody) {
					list1.add(stav.getId());
				}
			}
		}
		List<Integer> list2 = new ArrayList<>();
		for (int j = 0; j < size; j++) {
			for (List<State> prechody : automat2.getStateById(j).getTransitions()) {
				for (State stav : prechody) {
					list2.add(stav.getId());
				}
			}
		}
		
		List<Integer> finalStates1 = new ArrayList<>();
		List<Integer> finalStates2 = new ArrayList<>();
		for (State stav : automat1.getFinalStates()) {
			finalStates1.add(stav.getId());
		}
		for (State stav : automat2.getFinalStates()) {
			finalStates2.add(stav.getId());
		}
		
		return list1.equals(list2) && finalStates1.equals(finalStates2);
	}
}
