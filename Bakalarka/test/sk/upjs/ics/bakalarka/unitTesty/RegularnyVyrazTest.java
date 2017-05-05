package sk.upjs.ics.bakalarka.unitTesty;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;
import sk.upjs.ics.bakalarka.RegularExpression;

public class RegularnyVyrazTest {

	@Test
	public void odstranBodkyTest() {
		RegularExpression rv = new RegularExpression("a.b.c+g+fi+r*d.e");
		rv.removeDots();
		
		Assert.assertEquals(rv.getExpression(), "abc+g+fi+r*de");
	}

}
