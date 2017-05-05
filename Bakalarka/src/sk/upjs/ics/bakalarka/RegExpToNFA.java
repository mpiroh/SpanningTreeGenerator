package sk.upjs.ics.bakalarka;

import java.util.Stack;

public class RegExpToNFA {
	private String expression;
	private Automaton automaton;
	private State initialState;
	private State currentState;
	private State finalState;
	private char c;
	private Stack<State> bracketsBegins = new Stack<State>();
	private Stack<State> bracketsEnds = new Stack<State>();
	private boolean inBracket;

	public Automaton toNFA(String expression) {
		this.expression = expression;
		this.automaton = new Automaton();

		init();

		for (int i = 0; i < expression.length() - 1; i++) {
			c = expression.charAt(i);
			char nextChar = expression.charAt(i + 1);
			if (c == '*') {
				continue;
			}
			if (c == '+') {
				union();
				continue;
			}
			if (c == '(') {
				bracketBegin();
				continue;
			}
			if (c == ')') {
				if (nextChar != '*') {
					beacketEnd();
				} else {
					bracketEndWithKleene();
				}
				continue;
			}

			construction(nextChar);
		}
		
		c = expression.charAt(expression.length()-1);
		if (c != '*' && c != ')') {
			concatenation(c);			
		} else if (c == ')') {
			beacketEnd();
		}
		currentState.addEpsilonTransition(finalState);

		automaton.generateId();
		return automaton;
	}

	public void construction(char nextChar) {
		if (nextChar == '*')
			kleene(c);
		else
			concatenation(c);
	}

	public void concatenation(char c) {
		State state = new State();
		automaton.addState(state);
		
		if (c == 'E') {
			currentState.addEpsilonTransition(state);
		} else {
			currentState.addTransition(c, state);
		}
		
		currentState = state;
	}

	public void kleene(char c) {
		State s1 = new State();
		State s2 = new State();
		State s3 = new State();
		automaton.addState(s1);
		automaton.addState(s2);
		automaton.addState(s3);
		currentState.addEpsilonTransition(s1);
		currentState.addEpsilonTransition(s3);
		
		if (c == 'E') {
			s1.addEpsilonTransition(s2);
		} else {
			s1.addTransition(c, s2);
		}
		
		s2.addEpsilonTransition(s1);
		s2.addEpsilonTransition(s3);
		currentState = s3;
	}

	public void union() {
		if (!inBracket) {
			currentState.addEpsilonTransition(finalState);
			State s1 = new State();
			automaton.addState(s1);
			initialState.addEpsilonTransition(s1);
			currentState = s1;
		} else { //if (inBracket)
			currentState.addEpsilonTransition(bracketsEnds.peek());
			State s1 = new State();
			automaton.addState(s1);
			bracketsBegins.peek().addEpsilonTransition(s1);
			currentState = s1;
		}
	}

	public void bracketBegin() {
		inBracket = true;
		State s1 = new State();
		State s2 = new State();
		automaton.addState(s1);
		automaton.addState(s2);
		currentState.addEpsilonTransition(s1);
		bracketsBegins.push(currentState);
		bracketsEnds.push(s2);
		currentState = s1;
	}
	
	public void beacketEnd() {
		currentState.addEpsilonTransition(bracketsEnds.peek());
		currentState = bracketsEnds.pop();
		
		bracketsBegins.pop();
		if (bracketsBegins.isEmpty()) {
			inBracket = false;
		}
	}
	
	public void bracketEndWithKleene() {
		currentState.addEpsilonTransition(bracketsEnds.peek());
		State state = new State();
		bracketsEnds.peek().addEpsilonTransition(state);
		automaton.addState(state);
		currentState = state;
		
		bracketsBegins.peek().addEpsilonTransition(bracketsEnds.peek());
		bracketsEnds.pop().addEpsilonTransition(bracketsBegins.pop());
		if (bracketsBegins.isEmpty()) {
			inBracket = false;
		}
	}

	public void init() {
		initialState = new State();
		currentState = new State();
		finalState = new State();
		initialState.addEpsilonTransition(currentState);
		automaton.addState(initialState);
		automaton.addState(finalState);
		automaton.addState(currentState);
		automaton.setInitialState(initialState);
		automaton.addFinalState(finalState);
	}
}
