package org.rcsb.mmtf.serialization.quickmessagepackdeserialization;

import java.util.Hashtable;
import java.util.Map;

/**
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

	public String s(String s) {
		Object o = root.get(s);
		if (o == null) {
			return "";
		} else {
			return (String) root.get(s);
		}
	}

	public int i(String s) {
		Object o = root.get(s);
		if (o == null) {
			return 0;
		} else {
			return (int) root.get(s);
		}
	}

	public float f(String s) {
		Object o = root.get(s);
		if (o == null) {
			return 0;
		} else {
			return (float) root.get(s);
		}
	}

	public byte[] ba(String s) {
		Object o = root.get(s);
		if (o == null) {
			return new byte[0];
		} else {
			return (byte[]) root.get(s);
		}
	}

	public int[] ia(String s) {
		Object o = root.get(s);
		if (o == null) {
			return new int[0];
		} else {
			return (int[]) root.get(s);
		}
	}

	public float[] fa(String s) {
		return (float[]) root.get(s);
	}

	public double[] da(String s) {
		return (double[]) root.get(s);
	}

	public double[][] daa(String s) {
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

	public char c(String s) {
		Object o = root.get(s);
		if (o == null) {
			return 0;
		} else {
			return (char) root.get(s);
		}
	}

	public String[] sa(String s) {
		return (String[]) root.get(s);
	}

	public Object[] oa(String s) {
		Object o = root.get(s);
		if (o == null) {
			return new Object[0];
		} else {
			return (Object[]) root.get(s);
		}
	}

	public Hashtable l(String s) {
		Object o = root.get(s);
		if (o == null) {
			return new Hashtable();
		} else {
			return (Hashtable) root.get(s);
		}
	}
}
