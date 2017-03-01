package org.rcsb.mmtf.dataholders;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.rcsb.mmtf.serialization.mp.ObjectTree;

/**
 *
 * @author Antonin Pavelka
 */
public class MmtfStructureFactory {

	public MmtfStructure create(ObjectTree tree) {
		MmtfStructure s = new MmtfStructure();
		s.setxCoordList(tree.ba("xCoordList"));
		s.setyCoordList(tree.ba("yCoordList"));
		s.setzCoordList(tree.ba("zCoordList"));
		s.setbFactorList(tree.ba("bFactorList"));
		s.setOccupancyList(tree.ba("occupancyList"));
		s.setNumAtoms(tree.i("numAtoms"));
		s.setAtomIdList(tree.ba("atomIdList"));
		s.setAltLocList(tree.ba("altLocList"));
		s.setInsCodeList(tree.ba("insCodeList"));
		s.setGroupIdList(tree.ba("groupIdList"));
		s.setGroupList(createGroupList(tree.oa("groupList")));
		s.setSequenceIndexList(tree.ba("sequenceIndexList"));
		s.setGroupTypeList(tree.ba("groupTypeList"));
		s.setChainNameList(tree.ba("chainNameList"));
		s.setChainIdList(tree.ba("chainIdList"));
		s.setNumBonds(tree.i("numBonds"));
		s.setBondAtomList(tree.ba("bondAtomList"));
		s.setBondOrderList(tree.ba("bondOrderList"));
		s.setSecStructList(tree.ba("secStructList"));
		s.setChainsPerModel(tree.ia("chainsPerModel"));
		s.setGroupsPerChain(tree.ia("groupsPerChain"));
		s.setSpaceGroup(tree.s("spaceGroup"));
		s.setUnitCell(tree.fa("unitCell"));
		s.setBioAssemblyList(createBioAssemblyList(tree.oa("bioAssemblyList")));
		s.setMmtfVersion(tree.s("mmtfVersion"));
		s.setMmtfProducer(tree.s("mmtfProducer"));
		s.setEntityList(createEntityList(tree.oa("entityList")));
		s.setStructureId(tree.s("structureId"));
		s.setrFree(tree.f("rFree"));
		s.setrWork(tree.f("rWork"));
		s.setResolution(tree.f("resolution"));
		s.setTitle(tree.s("title"));
		s.setExperimentalMethods(tree.sa("experimentalMethods"));
		s.setDepositionDate(tree.s("depositionDate"));
		s.setReleaseDate(tree.s("releaseDate"));
		s.setNumGroups(tree.i("numGroups"));
		s.setNumChains(tree.i("numChains"));
		s.setNumModels(tree.i("numModels"));
		s.setNcsOperatorList(tree.daa("ncsOperatorList"));
		return s;
	}

	private Group[] createGroupList(Object[] array) {
		Group[] ga = new Group[array.length];
		for (int i = 0; i < ga.length; i++) {
			ga[i] = createGroup(array[i]);
		}
		return ga;
	}

	private Group createGroup(Object o) {
		ObjectTree t = new ObjectTree((Hashtable<String, Object>) o);
		Group g = new Group();
		g.setGroupName(t.s("groupName"));
		g.setAtomNameList(t.sa("atomNameList"));
		g.setElementList(t.sa("elementList"));
		g.setBondOrderList(t.ia("bondOrderList"));
		g.setBondAtomList(t.ia("bondAtomList"));
		g.setFormalChargeList(t.ia("formalChargeList"));
		g.setSingleLetterCode(t.s("singleLetterCode").charAt(0));
		g.setChemCompType(t.s("chemCompType"));
		return g;
	}

	private List<BioAssemblyData> createBioAssemblyList(Object[] array) {
		List<BioAssemblyData> list = new ArrayList<>();
		for (Object o : array) {
			list.add(createBioAssemblyData(o));
		}
		return list;
	}

	private BioAssemblyData createBioAssemblyData(Object o) {
		ObjectTree t = new ObjectTree((Hashtable<String, Object>) o);
		BioAssemblyData data = new BioAssemblyData();
		data.setName(t.s("name"));
		List<BioAssemblyTransformation> ts = new ArrayList<>();
		Object[] tls = t.oa("transformList");
		for (Object tl : tls) {
			ts.add(createBioAssemblyTransformation(tl));
		}
		data.setTransformList(ts);
		return data;
	}

	private BioAssemblyTransformation createBioAssemblyTransformation(Object o) {
		ObjectTree t = new ObjectTree((Hashtable<String, Object>) o);
		BioAssemblyTransformation bat = new BioAssemblyTransformation();
		bat.setChainIndexList(t.ia("chainIndexList"));
		double[] ds = t.da("matrix");
		bat.setMatrix(ds);
		return bat;
	}

	private Entity[] createEntityList(Object[] array) {
		Entity[] list = new Entity[array.length];
		for (int i = 0; i < array.length; i++) {
			list[i] = createEntity(array[i]);
		}
		return list;
	}

	private Entity createEntity(Object o) {
		ObjectTree t = new ObjectTree((Hashtable<String, Object>) o);
		Entity e = new Entity();
		e.setChainIndexList(t.ia("chainIndexList"));
		e.setDescription(t.s("description"));
		e.setSequence(t.s("sequence"));
		e.setType(t.s("type"));
		return e;
	}

}
