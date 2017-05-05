package sk.upjs.ics.bakalarka;

import java.util.Stack;

public class RegExpToNFA {
	private String vyraz;
	private Automaton automat;
	private State pociatocnyStav;
	private State aktualnyStav;
	private State koncovyStav;
	private char znak;
	private Stack<State> zaciatkyZatvoriek = new Stack<State>();
	private Stack<State> konceZatvoriek = new Stack<State>();
	private boolean vZatvorke;

	public Automaton toNFA(String vyraz) {
		this.vyraz = vyraz;
		this.automat = new Automaton();

		init();

		for (int i = 0; i < vyraz.length() - 1; i++) {
			znak = vyraz.charAt(i);
			char dalsiZnak = vyraz.charAt(i + 1);
			if (znak == '*') {
				continue;
			}
			if (znak == '+') {
				vetvenie();
				continue;
			}
			if (znak == '(') {
				zaciatokZatvorky();
				continue;
			}
			if (znak == ')') {
				if (dalsiZnak != '*') {
					koniecZatvorky();
				} else {
					koniecZatvorkySHviezdickou();
				}
				continue;
			}

			konstrukcia(dalsiZnak);
		}
		
		znak = vyraz.charAt(vyraz.length()-1);
		if (znak != '*' && znak != ')') {
			konkatenacia(znak);			
		} else if (znak == ')') {
			koniecZatvorky();
		}
		aktualnyStav.pridajEpsilonTransition(koncovyStav);

		automat.generateId();
		return automat;
	}

	public void konstrukcia(char dalsiZnak) {
		if (dalsiZnak == '*')
			hviezdicka(znak);
		else
			konkatenacia(znak);
	}

	public void konkatenacia(char znak) {
		State s1 = new State();
		automat.addState(s1);
		
		if (znak == 'E') {
			aktualnyStav.pridajEpsilonTransition(s1);
		} else {
			aktualnyStav.addTransition(znak, s1);
		}
		
		aktualnyStav = s1;
	}

	public void hviezdicka(char znak) {
		State s1 = new State();
		State s2 = new State();
		State s3 = new State();
		automat.addState(s1);
		automat.addState(s2);
		automat.addState(s3);
		aktualnyStav.pridajEpsilonTransition(s1);
		aktualnyStav.pridajEpsilonTransition(s3);
		
		if (znak == 'E') {
			s1.pridajEpsilonTransition(s2);
		} else {
			s1.addTransition(znak, s2);
		}
		
		s2.pridajEpsilonTransition(s1);
		s2.pridajEpsilonTransition(s3);
		aktualnyStav = s3;
	}

	public void vetvenie() {
		if (!vZatvorke) {
			aktualnyStav.pridajEpsilonTransition(koncovyStav);
			State s1 = new State();
			automat.addState(s1);
			pociatocnyStav.pridajEpsilonTransition(s1);
			aktualnyStav = s1;
		} else { //if (vZatvorke)
			aktualnyStav.pridajEpsilonTransition(konceZatvoriek.peek());
			State s1 = new State();
			automat.addState(s1);
			zaciatkyZatvoriek.peek().pridajEpsilonTransition(s1);
			aktualnyStav = s1;
		}
	}

	public void zaciatokZatvorky() {
		vZatvorke = true;
		State s1 = new State();
		State s2 = new State();
		automat.addState(s1);
		automat.addState(s2);
		aktualnyStav.pridajEpsilonTransition(s1);
		zaciatkyZatvoriek.push(aktualnyStav);
		konceZatvoriek.push(s2);
		aktualnyStav = s1;
	}
	
	public void koniecZatvorky() {
		aktualnyStav.pridajEpsilonTransition(konceZatvoriek.peek());
		aktualnyStav = konceZatvoriek.pop();
		
		zaciatkyZatvoriek.pop();
		if (zaciatkyZatvoriek.isEmpty()) {
			vZatvorke = false;
		}
	}
	
	public void koniecZatvorkySHviezdickou() {
		aktualnyStav.pridajEpsilonTransition(konceZatvoriek.peek());
		State s1 = new State();
		konceZatvoriek.peek().pridajEpsilonTransition(s1);
		automat.addState(s1);
		aktualnyStav = s1;
		
		zaciatkyZatvoriek.peek().pridajEpsilonTransition(konceZatvoriek.peek());
		konceZatvoriek.pop().pridajEpsilonTransition(zaciatkyZatvoriek.pop());
		if (zaciatkyZatvoriek.isEmpty()) {
			vZatvorke = false;
		}
	}

	public void init() {
		pociatocnyStav = new State();
		aktualnyStav = new State();
		koncovyStav = new State();
		pociatocnyStav.pridajEpsilonTransition(aktualnyStav);
		automat.addState(pociatocnyStav);
		automat.addState(koncovyStav);
		automat.addState(aktualnyStav);
		automat.setInitialState(pociatocnyStav);
		automat.pridajFinalState(koncovyStav);
	}
}
