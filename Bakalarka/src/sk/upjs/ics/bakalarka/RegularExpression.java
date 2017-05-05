package sk.upjs.ics.bakalarka;

import java.util.HashSet;
import java.util.Set;

public class RegularExpression {
	private String expression;

	public RegularExpression(String expression) {
		this.expression = expression;
		removeDots();
	}
	
	public Automaton toNFA() {
		RegExpToNFA regExpToNFA = new RegExpToNFA();
		Automaton automaton = regExpToNFA.toNFA(expression);
		return automaton;
	}

	public void removeDots() {
		StringBuilder newExp = new StringBuilder();
		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);
			if (c != '.')
				newExp.append(c);
		}
		this.expression = newExp.toString();
	}
	
	public Set<Character> getAlphabet() {
		Set<Character> alphabet = new HashSet<>();
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) != 'E') {
				alphabet.add(expression.charAt(i));
			}
		}
		return alphabet;
	}
		
	//------------------------------------------------------------------------
	public String getExpression() {
		return expression;
	}
}
