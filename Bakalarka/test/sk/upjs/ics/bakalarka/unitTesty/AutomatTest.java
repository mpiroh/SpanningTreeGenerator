package sk.upjs.ics.bakalarka.unitTesty;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;
import sk.upjs.ics.bakalarka.Automaton;
import sk.upjs.ics.bakalarka.State;

public class AutomatTest {

	@Test
	public void pridajStavTest() {
		Automaton automat = new Automaton();
		State s1 = new State();
		State s2 = new State();
		State s3 = new State();
		
		automat.addState(s1);
		automat.addState(s2);
		automat.addState(s3);
		
		Assert.assertEquals(automat.getStates().size(), 3);
	}

}
