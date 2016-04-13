package org.rcsb.mmtf.decoder;

import org.rcsb.mmtf.api.DecodedDataInterface;

public class DummyApiImpl implements DecodedDataInterface {

	
	public final int numAtoms = 1;
	public final int numGroups = 1;
	public final int atomsPerGroup = 1;
	public final int bondsPerGroup = 1;
	public final int interGroupBonds = 1;
	public final int numChains = 1;
	public final int numModels = 1;

	
	@Override
	public float[] getxCoords() {
		return new float[numAtoms];
	}

	@Override
	public float[] getyCoords() {
		return new float[numAtoms];
	}

	@Override
	public float[] getzCoords() {
		return new float[numAtoms];
	}

	@Override
	public float[] getbFactors() {
		return new float[numAtoms];
	}

	@Override
	public float[] getOccupancies() {
		return new float[numAtoms];
	}

	@Override
	public int[] getAtomIds() {
		return new int[numAtoms];
	}

	@Override
	public char[] getAltLocIds() {
		return new char[numAtoms];
	}

	@Override
	public char[] getInsCodes() {
		return new char[numGroups];
	}

	@Override
	public int[] getGroupIds() {
		return new int[numGroups];
	}

	@Override
	public String getGroupName(int groupInd) {
		return "NAME";
	}

	@Override
	public int getNumAtomsInGroup(int groupInd) {
		return atomsPerGroup;
	}

	@Override
	public String[] getGroupAtomNames(int groupInd) {
		return new String[atomsPerGroup];
	}

	@Override
	public String[] getGroupElementNames(int groupInd) {
		return new String[atomsPerGroup];
	}

	@Override
	public int[] getGroupBondOrders(int groupInd) {
		return new int[bondsPerGroup];
	}

	@Override
	public int[] getGroupBondIndices(int groupInd) {
		return new int[bondsPerGroup*2];
	}

	@Override
	public int[] getGroupAtomCharges(int groupInd) {
		return new int[atomsPerGroup];
	}

	@Override
	public char getGroupSingleLetterCode(int groupInd) {
		return 0;
	}

	@Override
	public String getGroupChemCompType(int groupInd) {
		return "CHEM";
	}

	@Override
	public int[] getGroupTypeIndices() {
		return new int[numGroups];
	}

	@Override
	public int[] getGroupSequenceIndices() {
		return new int[numGroups];
	}

	@Override
	public String[] getChainIds() {
		return new String[numChains];
	}

	@Override
	public String[] getChainNames() {
		return new String[numChains];
	}

	@Override
	public int[] getChainsPerModel() {
		
		return new int[] {numChains};
	}

	@Override
	public int[] getGroupsPerChain() {
		return new int[] {numGroups};
	}

	@Override
	public String getSpaceGroup() {
		return "SPACE";
	}

	@Override
	public float[] getUnitCell() {
		return new float[6];
	}

	@Override
	public int getNumBioassemblies() {
		return 1;
	}

	@Override
	public int getNumTransInBioassembly(int bioassemblyIndex) {
		return 1;
	}

	@Override
	public int[] getChainIndexListForTransform(int bioassemblyIndex, int transformationIndex) {
		return new int[1];
	}

	@Override
	public double[] getMatrixForTransform(int bioassemblyIndex, int transformationIndex) {
		return new double[1];
	}

	@Override
	public int[] getInterGroupBondIndices() {
		return new int[interGroupBonds*2];

	}

	@Override
	public int[] getInterGroupBondOrders() {
		return new int[interGroupBonds];
	}

	@Override
	public String getMmtfVersion() {
		return "VERS";
	}

	@Override
	public String getMmtfProducer() {
		return "PROD";
	}

	@Override
	public int getNumEntities() {
		return 1;
	}

	@Override
	public String getEntityDescription(int entityInd) {
		return "DESC";
	}

	@Override
	public String getEntityType(int entityInd) {
		return "TYPE";
	}

	@Override
	public int[] getEntityChainIndexList(int entityInd) {
		return new int[] {0};
	}

	@Override
	public String getEntitySequence(int entityInd) {
		return "SEQ";
	}

	@Override
	public String getStructureId() {
		return "1EG1";
	}

	@Override
	public int getNumModels() {
		return 1;
	}

	@Override
	public int getNumBonds() {
		return bondsPerGroup*numGroups+interGroupBonds;
	}

	@Override
	public int getNumChains() {
		return numChains;
	}

	@Override
	public int getNumGroups() {
		return numGroups;
	}

	@Override
	public int getNumAtoms() {
		return numAtoms;
	}

	@Override
	public float getRfree() {
		return 1.0f;

	}

	@Override
	public float getRwork() {
		return 1.0f;

	}

	@Override
	public float getResolution() {
		return 1.0f;
	}

	@Override
	public String getTitle() {
		return "NA";
	}

	@Override
	public String[] getExperimentalMethods() {
		return new String[] {"NA"};

	}

	@Override
	public String getDepositionDate() {
		return "NA";

	}

	@Override
	public String getReleaseDate() {
		return "NA";
	}

	@Override
	public int[] getSecStructList() {
		return new int[numGroups];
	}

}
