package sk.upjs.ics.bakalarka;

import java.util.List;

public class Main {
	public static final int POSUN = 97;
	
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		RegularExpression rv = new RegularExpression("(aa+b)(a+b)*+ab(ab*a)*");
		RegExpToNFA p = new RegExpToNFA();
		Determinization pp = new Determinization();
		Minimization min = new Minimization();
		
		Automaton n = p.toNFA(rv.getExpression());
		Automaton d = pp.toDFA(n);
		Automaton m = min.minimize(d);
		
		System.out.println(m);
		System.out.println((System.currentTimeMillis() - time) + " ms");
		
		/*long time = System.currentTimeMillis();
		Porovnavac p = new Porovnavac();
		System.out.println(p.compare("a(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*",
									 "a(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*(a+b)*"));
		System.out.println("Time: " + (System.currentTimeMillis()-time));*/
		
	}
}
