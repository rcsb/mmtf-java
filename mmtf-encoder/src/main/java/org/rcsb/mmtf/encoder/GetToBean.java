package org.rcsb.mmtf.encoder;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;

/**
 * Class to take a generic data structure (mmtfDecodedDataInterface) and encode to an mmtf bean.
 * @author Anthony Bradley
 *
 */
public class GetToBean {
	
	private MmtfBean mmtfBean;
	
	public GetToBean(MmtfDecodedDataInterface mmtfDecodedDataInterface) {
		
		// DO THE ENCODING LOGIC HERE -> CONVERTING TO AN MMTF BEAN
	}
	
	public MmtfBean getMmtfBean() {
		return mmtfBean;
	}

}
