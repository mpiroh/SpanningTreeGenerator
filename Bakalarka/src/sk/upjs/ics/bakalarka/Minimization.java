package sk.upjs.ics.bakalarka;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Minimization {
	public static final int MAX_ZNAKOV = 26;
	public static final int POSUN = 97;

	private Map<List<Integer>, List<State>> mapa = new LinkedHashMap<>();

	public Automaton minimize(Automaton oldAutomat) {
		Automaton newAutomat = new Automaton();
		Automaton pomocnyAutomat = oldAutomat;
		pomocnyAutomat.generateGroups();
		
		int pocetSkupin;
		if (pomocnyAutomat.getStates().size() == pomocnyAutomat.getFinalStates().size()) {
			// ak su vsetky stavy koncove
			pocetSkupin = 1;
		} else {
			pocetSkupin = 2;
		}

		for (int i = 1; i <= pocetSkupin; i++) {
			boolean rozdeliloSa = true;
			while (rozdeliloSa) {
				for (State stav : pomocnyAutomat.getStatesByGroup(i)) {
					List<Integer> skupiny = new ArrayList<>();
					for (List<State> prechody : stav.getTransitions()) {
						if (!prechody.isEmpty()) {
							skupiny.add(prechody.get(0).getGroup());
						}
					}
					
					if (mapa.get(skupiny) == null) {
						List<State> list = new ArrayList<>();
						list.add(stav);
						mapa.put(skupiny, list);
					} else {
						List<State> list = mapa.get(skupiny);
						list.add(stav);
						mapa.put(skupiny, list);
					}
				
					if (mapa.size() == 1) {
						rozdeliloSa = false;
					} else {
						rozdeliloSa = true;
					}

				}

				// ak mapa ma len jeden riadok, znamena ze sa nic nerozdelilo
				// ak mapa ma viac ako jeden riadok, znamena ze treba pridat
				// nove skupiny
				// nech velkost mapy je n, potom novych skupin bude n-1
				int pocetNovychSkupin = mapa.size() - 1;
				for (int j = 1; j <= pocetNovychSkupin; j++) {
					// prvym n-1 riadokm v mape nastavim nove skupiny
					List<State> stavy = (List<State>) mapa.values().toArray()[j];
					for (State s : stavy) {
						s.setGroup(pocetSkupin + 1);
					}
					pocetSkupin++;
				}

				mapa.clear();
			}
		}

		// z pomocneho automatu vytvorime novy automat podla skupin
		// vytvorim tolko stavov kolko mam skupin
		for (int i = 1; i <= pocetSkupin; i++) {
			State s = new State();
			s.setGroup(i);
			newAutomat.addState(s);
		}

		// nastavime prechody podla skupin zo stareho automatu
		for (int i = 1; i <= pocetSkupin; i++) {
			State oldStav = oldAutomat.getAStateByGroup(i);
			State newStav = newAutomat.getAStateByGroup(i);
			for (int j = 0; j < MAX_ZNAKOV; j++) {
				if (!oldStav.getTransitions()[j].isEmpty()) {
					int cielovaSkupina = oldStav.getTransitions()[j].get(0).getGroup();
					State cielovyStav = newAutomat.getAStateByGroup(cielovaSkupina);
					newStav.addTransition((char) (j + POSUN), cielovyStav);
				}
			}
		}

		// nastavime pociatocny stav
		int skupinaPociatocnehoStavu = oldAutomat.getInitialState().getGroup();
		newAutomat.setInitialState(newAutomat.getAStateByGroup(skupinaPociatocnehoStavu));

		// nastavime koncove stavy
		List<State> koncoveStavy = oldAutomat.getFinalStates();
		for (State stav : koncoveStavy) {
			int skupinaKoncovychStavov = stav.getGroup();
			State koncovyStav = newAutomat.getAStateByGroup(skupinaKoncovychStavov);
			if (!newAutomat.getFinalStates().contains(koncovyStav)) {
				newAutomat.pridajFinalState(koncovyStav);
			}
		}

		newAutomat.generateId();
		return newAutomat;
	}
}
