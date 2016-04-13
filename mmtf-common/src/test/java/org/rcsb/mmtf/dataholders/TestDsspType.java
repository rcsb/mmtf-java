package org.rcsb.mmtf.dataholders;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDsspType {
	@Test
	public void testDsspType() {		
		
		assertEquals(DsspType.dsspTypeFromString("pi Helix"), DsspType.dsspTypeFromInt(0));
		assertEquals(DsspType.PI_HELIX, DsspType.dsspTypeFromInt(0));
		
		assertEquals(DsspType.dsspTypeFromString("Bend"), DsspType.dsspTypeFromInt(1));
		assertEquals(DsspType.BEND, DsspType.dsspTypeFromInt(1));
		
		assertEquals(DsspType.dsspTypeFromString("alpha Helix"), DsspType.dsspTypeFromInt(2));
		assertEquals(DsspType.ALPHA_HELIX, DsspType.dsspTypeFromInt(2));
		
		assertEquals(DsspType.dsspTypeFromString("Extended"), DsspType.dsspTypeFromInt(3));
		assertEquals(DsspType.EXTENDED, DsspType.dsspTypeFromInt(3));
		
		assertEquals(DsspType.dsspTypeFromString("3-10 Helix"), DsspType.dsspTypeFromInt(4));
		assertEquals(DsspType.HELIX_3_10, DsspType.dsspTypeFromInt(4));
		
		assertEquals(DsspType.dsspTypeFromString("Bridge"), DsspType.dsspTypeFromInt(5));
		assertEquals(DsspType.BRIDGE, DsspType.dsspTypeFromInt(5));
		
		assertEquals(DsspType.dsspTypeFromString("Turn"), DsspType.dsspTypeFromInt(6));
		assertEquals(DsspType.TURN, DsspType.dsspTypeFromInt(6));
		
		assertEquals(DsspType.dsspTypeFromString("Coil"), DsspType.dsspTypeFromInt(7));
		assertEquals(DsspType.COIL, DsspType.dsspTypeFromInt(7));
		
		assertEquals(DsspType.dsspTypeFromString("NA"), DsspType.dsspTypeFromInt(-1));
		assertEquals(DsspType.NULL_ENTRY, DsspType.dsspTypeFromInt(-1));
		
		assertEquals(DsspType.NULL_ENTRY, DsspType.dsspTypeFromString(null));
		assertEquals(DsspType.NULL_ENTRY, DsspType.dsspTypeFromString("MMTF"));
		assertEquals(DsspType.NULL_ENTRY, DsspType.dsspTypeFromInt(100));

	}
}
