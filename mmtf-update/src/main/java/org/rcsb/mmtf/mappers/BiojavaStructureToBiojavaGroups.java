package org.rcsb.mmtf.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;

import scala.Tuple2;

/**
 * Extracts all of the groups from a given Biojava structure
 * @author Anthony Bradley
 *
 */
public class BiojavaStructureToBiojavaGroups implements PairFlatMapFunction<Tuple2<String,Structure>, String, Group>{

	private static final long serialVersionUID = 9211500299985679809L;

	@Override
	public Iterable<Tuple2<String, Group>> call(Tuple2<String, Structure> t) throws Exception {
		// The list to return
		List<Tuple2<String, Group>> outList = new ArrayList<Tuple2<String, Group>>();
		for(int modelNr=0;  modelNr<t._2.nrModels(); modelNr++){
			// Function to get all the fragments - and return in a tuple
			for(Chain c: t._2.getChains(modelNr)){
				List<Group> myGroups = c.getAtomGroups();
				// Now loop through these groups and add them to the outputlist
				for(Group g: myGroups){
					// Now generate the unique id
					String uniqId = modelNr+"_"+c.getChainID()+"_"+g.getResidueNumber();
					outList.add(new Tuple2<String, Group>(uniqId, g));
				}
			}
		}
		return outList;
	}
}