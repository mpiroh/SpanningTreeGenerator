package sk.upjs.ics.bakalarka.unitTesty;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;
import sk.upjs.ics.bakalarka.State;

public class StavTest {

	private static final int POSUN = 97;

	@Test
	public void pridajPrechodTest() {
		State s1 = new State();
		State s2 = new State();
		
		s1.addTransition('a', s2);
		s1.addTransition('a', s1);
		
		Assert.assertEquals(s1.getTransitions()[(int)'a' - POSUN].size(), 2);
	}
	
	@Test
	public void pridajEpsilonPrechodTest() {
		State s1 = new State();
		State s2 = new State();
		
		s1.pridajEpsilonTransition(s2);
				
		Assert.assertEquals(s1.getEpsilonTransitions().size(), 1);
	}

}
