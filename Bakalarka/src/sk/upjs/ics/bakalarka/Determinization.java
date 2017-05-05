package sk.upjs.ics.bakalarka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Determinization {
	public static final int MAX_ZNAKOV = 26;
	public static final int POSUN = 97;
	private Queue<State> rad = new LinkedList<>();
	private Queue<State> epsilonRad = new LinkedList<>();
	private List<Character> abeceda;
	private Map<State, List<State>> epsilonPrechody;
	private List<State> anticyklickyZoznam = new ArrayList<>();

	public Automaton toDFA(Automaton oldAutomat) {
		this.abeceda = zistiAbecedu(oldAutomat);
		this.epsilonPrechody = nacitajEpsilonPrechody(oldAutomat);
		oldAutomat.generateBitcodes();

		Automaton newAutomat = new Automaton();
		
		State pociatocnyStav = new State();
		long pociatocnyBitKod = oldAutomat.getInitialState().getBitcode();
		
		epsilonRad.add(oldAutomat.getInitialState());
		while(!epsilonRad.isEmpty()) {
			State e = epsilonRad.poll();
			if (!e.getEpsilonTransitions().isEmpty()) {
				for (State epsilonStav : e.getEpsilonTransitions()) {
					if (!anticyklickyZoznam.contains(epsilonStav)) {
						pociatocnyBitKod = pociatocnyBitKod | epsilonStav.getBitcode();
						epsilonRad.add(epsilonStav);
						anticyklickyZoznam.add(epsilonStav);
					}
				}
			}
		}
		anticyklickyZoznam.clear();
		
		pociatocnyStav.setBitcode(pociatocnyBitKod);
		newAutomat.addState(pociatocnyStav);
		newAutomat.setInitialState(pociatocnyStav);
		
		rad.add(pociatocnyStav);
		
		while(!rad.isEmpty()) {
			State stav = rad.poll();
			long bitKod = stav.getBitcode();
			
			for (char znak : abeceda) {
				long i = 0;
				long cielovyBitKod = 0;

				//zistim cielovy bitkod zatial bez eplsionovych prechodov
				while (Math.pow(2, i) <= bitKod) {
					long bit = (bitKod & (1L << i));
					if (bit != 0) {
						for (State sss : oldAutomat.getStateByBitcode((long) (Math.pow(2, i)))
								.getTransitions()[((int)znak)-POSUN]) {
							cielovyBitKod = cielovyBitKod | sss.getBitcode();
							
							if (epsilonPrechody.get(sss) != null) {
								epsilonRad.add(sss);
								while(!epsilonRad.isEmpty()) {
									State e = epsilonRad.poll();
									if (epsilonPrechody.get(e) != null) {
										for (State epsilonStav : epsilonPrechody.get(e)) {
											if (!anticyklickyZoznam.contains(epsilonStav)) {
												cielovyBitKod = cielovyBitKod | epsilonStav.getBitcode();
												epsilonRad.add(epsilonStav);
												anticyklickyZoznam.add(epsilonStav);
											}
										}
									}
								}
							}
						}
					} else { //if (bit == 0)
						//
					}
					i++;
				}				
				
				boolean stavUzExistuje = false;
				
				//zistim ci taky stav uz existuje (podla provnania bitkodov)
				//ak existuje, tak pridam prechod
				for (State ks : newAutomat.getStates()) {
					if (ks.getBitcode() == cielovyBitKod) {
						stavUzExistuje = true;
						//stav.pridajPrechod((char)(i + POSUN), ks);
						newAutomat.addTransition(stav, znak, ks);
						break;
					}
				}
				
				//ak neexistuje, vyrobim novy stav, pridam prechod a pridam novy stav do automatu, 
				if (!stavUzExistuje) {
					State novyStav = new State();
					novyStav.setBitcode(cielovyBitKod);
					stav.addTransition(znak, novyStav);
					newAutomat.addState(novyStav);
					rad.add(novyStav);
				}
				anticyklickyZoznam.clear();
			}
		}
		
		//nastavenie kocnovych stavov
		List<Long> koncoveStavyBitKody = new ArrayList<>();
		for (State s : oldAutomat.getFinalStates()) {
			koncoveStavyBitKody.add(s.getBitcode());
		}
		for (State s : newAutomat.getStates()) {
			for (long bitKod : koncoveStavyBitKody) {
				if ((s.getBitcode() & bitKod) != 0) {
					newAutomat.pridajFinalState(s);
					break;
				}
			}
		}
		
		newAutomat.generateId();
		return newAutomat;
	}
	
	/*public long skontrolujBitKod(Long bitKod) {
		long novyBitKod = bitKod;
		int i = 0;
		while(bitKod > 0) {
			if ((bitKod % 10 != 0) && (bitKod % 10 != 1)) {
				long hh = (bitKod % 10)*((long)(Math.pow(10, i)));
				novyBitKod = novyBitKod - (bitKod % 10)*((long)(Math.pow(10, i))) + (long)(Math.pow(10, i));
			}
			bitKod = bitKod / 10;
			i++;
		}
		return novyBitKod;
	}*/
	
	private List<Character> zistiAbecedu(Automaton automat) {
		Set<Character> mnozinaAbeceda = new HashSet<>();
		List<Character> abeceda = new ArrayList<>();
		List<State> stavy = automat.getStates();
		
		for (State s : stavy) {
			for (int i = 0; i < s.getTransitions().length; i++) {
				if (!s.getTransitions()[i].isEmpty()) {
					mnozinaAbeceda.add((char) (i + POSUN));
				}
			}
		}
		abeceda.addAll(mnozinaAbeceda);
		
		return abeceda;
	}
	
	private Map<State, List<State>> nacitajEpsilonPrechody(Automaton automat) {
		Map<State, List<State>> epsilony = new HashMap<>();
		List<State> stavy = automat.getStates();
		
		for (State s : stavy) {
			if (!s.getEpsilonTransitions().isEmpty()) {
				epsilony.put(s, s.getEpsilonTransitions());
			}
		}
		
		return epsilony;
	}
}
