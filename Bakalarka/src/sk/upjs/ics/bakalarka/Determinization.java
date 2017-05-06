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
	public static final int MAX_CHARS = 26;
	public static final int SHIFT = 97;
	private Queue<State> queue = new LinkedList<>();
	private Queue<State> epsilonQueue = new LinkedList<>();
	private List<Character> alphabet;
	private Map<State, List<State>> epsilonTransitions;
	private List<State> anticyclicalList = new ArrayList<>();

	public Automaton toDFA(Automaton oldAutomaton) {
		this.alphabet = getAlphabetFromAutomaton(oldAutomaton);
		this.epsilonTransitions = getEpsilonTransitionsFromAutomaton(oldAutomaton);
		oldAutomaton.generateBitcodes();

		Automaton newAutomaton = new Automaton();
		
		State initialState = new State();
		long initialBitcode = oldAutomaton.getInitialState().getBitcode();
		
		epsilonQueue.add(oldAutomaton.getInitialState());
		while(!epsilonQueue.isEmpty()) {
			State e = epsilonQueue.poll();
			if (!e.getEpsilonTransitions().isEmpty()) {
				for (State epsilonState : e.getEpsilonTransitions()) {
					if (!anticyclicalList.contains(epsilonState)) {
						initialBitcode = initialBitcode | epsilonState.getBitcode();
						epsilonQueue.add(epsilonState);
						anticyclicalList.add(epsilonState);
					}
				}
			}
		}
		anticyclicalList.clear();
		
		initialState.setBitcode(initialBitcode);
		newAutomaton.addState(initialState);
		newAutomaton.setInitialState(initialState);
		
		queue.add(initialState);
		
		while(!queue.isEmpty()) {
			State state = queue.poll();
			long bitcode = state.getBitcode();
			
			for (char c : alphabet) {
				long i = 0;
				long targetBitcode = 0;

				//zistim cielovy bitkod zatial bez eplsionovych prechodov
				while (Math.pow(2, i) <= bitcode) {
					long bit = (bitcode & (1L << i));
					if (bit != 0) {
						for (State sss : oldAutomaton.getStateByBitcode((long) (Math.pow(2, i)))
								.getTransitions()[((int)c)-SHIFT]) {
							targetBitcode = targetBitcode | sss.getBitcode();
							
							if (epsilonTransitions.get(sss) != null) {
								epsilonQueue.add(sss);
								while(!epsilonQueue.isEmpty()) {
									State e = epsilonQueue.poll();
									if (epsilonTransitions.get(e) != null) {
										for (State epsilonState : epsilonTransitions.get(e)) {
											if (!anticyclicalList.contains(epsilonState)) {
												targetBitcode = targetBitcode | epsilonState.getBitcode();
												epsilonQueue.add(epsilonState);
												anticyclicalList.add(epsilonState);
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
				
				boolean stavAlreadyExists = false;
				
				//zistim ci taky stav uz existuje (podla provnania bitkodov)
				//ak existuje, tak pridam prechod
				for (State ks : newAutomaton.getStates()) {
					if (ks.getBitcode() == targetBitcode) {
						stavAlreadyExists = true;
						//stav.pridajPrechod((char)(i + POSUN), ks);
						newAutomaton.addTransition(state, c, ks);
						break;
					}
				}
				
				//ak neexistuje, vyrobim novy stav, pridam prechod a pridam novy stav do automatu, 
				if (!stavAlreadyExists) {
					State novyStav = new State();
					novyStav.setBitcode(targetBitcode);
					state.addTransition(c, novyStav);
					newAutomaton.addState(novyStav);
					queue.add(novyStav);
				}
				anticyclicalList.clear();
			}
		}
		//
		//nastavenie kocnovych stavov
		List<Long> finalStatesBitcodes = new ArrayList<>();
		for (State s : oldAutomaton.getFinalStates()) {
			finalStatesBitcodes.add(s.getBitcode());
		}
		for (State s : newAutomaton.getStates()) {
			for (long bitcode : finalStatesBitcodes) {
				if ((s.getBitcode() & bitcode) != 0) {
					newAutomaton.addFinalState(s);
					break;
				}
			}
		}
		
		newAutomaton.generateId();
		return newAutomaton;
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
	
	private List<Character> getAlphabetFromAutomaton(Automaton automaton) {
		Set<Character> alphabetSet = new HashSet<>();
		List<Character> alphabet = new ArrayList<>();
		List<State> states = automaton.getStates();
		
		for (State s : states) {
			for (int i = 0; i < s.getTransitions().length; i++) {
				if (!s.getTransitions()[i].isEmpty()) {
					alphabetSet.add((char) (i + SHIFT));
				}
			}
		}
		alphabet.addAll(alphabetSet);
		
		return alphabet;
	}
	
	private Map<State, List<State>> getEpsilonTransitionsFromAutomaton(Automaton automaton) {
		Map<State, List<State>> epsilons = new HashMap<>();
		List<State> states = automaton.getStates();
		
		for (State s : states) {
			if (!s.getEpsilonTransitions().isEmpty()) {
				epsilons.put(s, s.getEpsilonTransitions());
			}
		}
		
		return epsilons;
	}
}
