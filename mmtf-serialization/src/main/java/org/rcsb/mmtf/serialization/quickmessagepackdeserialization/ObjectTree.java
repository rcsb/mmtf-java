package org.rcsb.mmtf.serialization.quickmessagepackdeserialization;

import java.util.Hashtable;
import java.util.Map;

/**
 * Utility class represeting an object whose attributes does not have types yet.
 * The methods of the instance of this class can be called to retrieve the
 * attributes in correct type specified by their name. Only several primitive
 * types and their arrays are supported, application specific types are resolved
 * elsewhere.
 *
 * @author Antonin Pavelka
 */
public class ObjectTree {

	private Map<String, Object> root;

	public ObjectTree(Map<String, Object> m) {
		root = m;
	}

	public ObjectTree(Hashtable<String, Object> t) {
		root = t;
	}

	public Object get(String s) {
		return root.get(s);
	}

	public String getString(String s) {
		Object o = root.get(s);
		if (o == null) {
			return null;
		} else {
			return (String) root.get(s);
		}
	}

	public int getInt(String s) {
		Object o = root.get(s);
		if (o == null) {
			return 0;
		} else {
			return (int) root.get(s);
		}
	}

	public float getFloat(String s) {
		Object o = root.get(s);
		if (o == null) {
			return 0;
		} else {
			return (float) root.get(s);
		}
	}

	public byte[] getByteArray(String s) {
		Object o = root.get(s);
		if (o == null) {
			return new byte[0];
		} else {
			return (byte[]) root.get(s);
		}
	}

	public int[] getIntArray(String s) {
		Object o = root.get(s);
		if (o == null) {
			return new int[0];
		} else {
			return (int[]) root.get(s);
		}
	}

	public float[] getFloatArray(String s) {
		return (float[]) root.get(s);
	}

	public double[] getDoubleArray(String s) {
		return (double[]) root.get(s);
	}

	public double[][] getDoubleArray2d(String s) {
		Object o = root.get(s);
		double[][] aa;
		if (o == null) {
			aa = new double[0][0];
		} else {
			Object[] a = (Object[]) o;
			aa = new double[a.length][];
			for (int i = 0; i < a.length; i++) {
				aa[i] = (double[]) a[i];
			}
		}
		return aa;
	}

	public char getChar(String s) {
		Object o = root.get(s);
		if (o == null) {
			return 0;
		} else {
			return (char) root.get(s);
		}
	}

	public String[] getStringArray(String s) {
		return (String[]) root.get(s);
	}

	public Object[] getObjectArray(String s) {
		Object o = root.get(s);
		if (o == null) {
			return new Object[0];
		} else {
			return (Object[]) root.get(s);
		}
	}
}
