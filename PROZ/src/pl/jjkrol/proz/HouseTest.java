package pl.jjkrol.proz;

import static org.junit.Assert.*;

import org.junit.Test;

public class HouseTest {

	@Test
	public void testHouse() {
		House testHouse = new House("Dom", "Adres");
		assertEquals(testHouse.getAddress(), "Adres");
		assertEquals(testHouse.getName(), "Dom");	
	}

}
