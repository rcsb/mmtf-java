package org.rcsb.mmtf.dataholders;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.rcsb.mmtf.serialization.quickmessagepackdeserialization.ObjectTree;

/**
 * This class constructs MmtfStructure objects from tree-like structure
 * containing the data. The general types of the data are converted to proper
 * type by this class.
 *
 * A solution using reflection, such as Jackson, is more elegant, but also
 * slower.
 *
 * @author Antonin Pavelka
 */
public class MmtfStructureFactory {

	public MmtfStructure create(ObjectTree tree) {
		MmtfStructure s = new MmtfStructure();
		s.setxCoordList(tree.getByteArray("xCoordList"));
		s.setyCoordList(tree.getByteArray("yCoordList"));
		s.setzCoordList(tree.getByteArray("zCoordList"));
		s.setbFactorList(tree.getByteArray("bFactorList"));
		s.setOccupancyList(tree.getByteArray("occupancyList"));
		s.setNumAtoms(tree.getInt("numAtoms"));
		s.setAtomIdList(tree.getByteArray("atomIdList"));
		s.setAltLocList(tree.getByteArray("altLocList"));
		s.setInsCodeList(tree.getByteArray("insCodeList"));
		s.setGroupIdList(tree.getByteArray("groupIdList"));
		s.setGroupList(createGroupList(tree.getObjectArray("groupList")));
		s.setSequenceIndexList(tree.getByteArray("sequenceIndexList"));
		s.setGroupTypeList(tree.getByteArray("groupTypeList"));
		s.setChainNameList(tree.getByteArray("chainNameList"));
		s.setChainIdList(tree.getByteArray("chainIdList"));
		s.setNumBonds(tree.getInt("numBonds"));
		s.setBondAtomList(tree.getByteArray("bondAtomList"));
		s.setBondOrderList(tree.getByteArray("bondOrderList"));
		s.setSecStructList(tree.getByteArray("secStructList"));
		s.setChainsPerModel(tree.getIntArray("chainsPerModel"));
		s.setGroupsPerChain(tree.getIntArray("groupsPerChain"));
		s.setSpaceGroup(tree.getString("spaceGroup"));
		s.setUnitCell(tree.getFloatArray("unitCell"));
		s.setBioAssemblyList(createBioAssemblyList(tree.getObjectArray("bioAssemblyList")));
		s.setMmtfVersion(tree.getString("mmtfVersion"));
		s.setMmtfProducer(tree.getString("mmtfProducer"));
		s.setEntityList(createEntityList(tree.getObjectArray("entityList")));
		s.setStructureId(tree.getString("structureId"));
		s.setrFree(tree.getFloat("rFree"));
		s.setrWork(tree.getFloat("rWork"));
		s.setResolution(tree.getFloat("resolution"));
		s.setTitle(tree.getString("title"));
		s.setExperimentalMethods(tree.getStringArray("experimentalMethods"));
		s.setDepositionDate(tree.getString("depositionDate"));
		s.setReleaseDate(tree.getString("releaseDate"));
		s.setNumGroups(tree.getInt("numGroups"));
		s.setNumChains(tree.getInt("numChains"));
		s.setNumModels(tree.getInt("numModels"));
		s.setNcsOperatorList(tree.getDoubleArray2d("ncsOperatorList"));
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
		g.setGroupName(t.getString("groupName"));
		g.setAtomNameList(t.getStringArray("atomNameList"));
		g.setElementList(t.getStringArray("elementList"));
		g.setBondOrderList(t.getIntArray("bondOrderList"));
		g.setBondAtomList(t.getIntArray("bondAtomList"));
		g.setFormalChargeList(t.getIntArray("formalChargeList"));
		g.setSingleLetterCode(t.getString("singleLetterCode").charAt(0));
		g.setChemCompType(t.getString("chemCompType"));
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
		data.setName(t.getString("name"));
		List<BioAssemblyTransformation> ts = new ArrayList<>();
		Object[] tls = t.getObjectArray("transformList");
		for (Object tl : tls) {
			ts.add(createBioAssemblyTransformation(tl));
		}
		data.setTransformList(ts);
		return data;
	}

	private BioAssemblyTransformation createBioAssemblyTransformation(Object o) {
		ObjectTree t = new ObjectTree((Hashtable<String, Object>) o);
		BioAssemblyTransformation bat = new BioAssemblyTransformation();
		bat.setChainIndexList(t.getIntArray("chainIndexList"));
		double[] ds = t.getDoubleArray("matrix");
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
		e.setChainIndexList(t.getIntArray("chainIndexList"));
		e.setDescription(t.getString("description"));
		e.setSequence(t.getString("sequence"));
		e.setType(t.getString("type"));
		return e;
	}

}
