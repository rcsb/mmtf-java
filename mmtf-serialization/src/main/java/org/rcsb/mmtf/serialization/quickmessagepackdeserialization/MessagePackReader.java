package org.rcsb.mmtf.serialization.quickmessagepackdeserialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * A simple MessagePack reader. Adopted from Jmol code:
 * https://github.com/msgpack/msgpack/blob/master/spec.md with very few dependencies. In contrast to
 * original implementation, float64 is interpreted as double.
 *
 * Nuances:
 *
 * Does not implement unsigned int32 or int64 (delivers simple integers in all cases).
 *
 * Note:
 *
 * homogeneousArrays == true will deliver null for empty array.
 *
 *
 * @author Bob Hanson hansonr@stolaf.edu
 * @author Antonin Pavelka
 *
 */
public class MessagePackReader {

	private final boolean isHomo;// homogeneous arrays -- use int[] not Integer

	// these maps must be checked for the specific number of bits, in the following order:
	private final static int POSITIVEFIXINT_x80 = 0x80; //0xxxxxxx
	private final static int FIXMAP_xF0 = 0x80; //1000xxxx
//  private final static int FIXARRAY_xF0       = 0x90; //1001xxxx
	private final static int FIXSTR_xE0 = 0xa0; //101xxxxx
	private final static int NEGATIVEFIXINT_xE0 = 0xe0; //111xxxxx
	private final static int DEFINITE_xE0 = 0xc0; //110xxxxx

	private final static int NIL = 0xc0;
//  private final static int (NEVERUSED)        = 0xc1;
	private final static int FALSE = 0xc2;
	private final static int TRUE = 0xc3;
	private final static int BIN8 = 0xc4;
	private final static int BIN16 = 0xc5;
	private final static int BIN32 = 0xc6;
	private final static int EXT8 = 0xc7;
	private final static int EXT16 = 0xc8;
	private final static int EXT32 = 0xc9;
	private final static int FLOAT32 = 0xca;
	private final static int FLOAT64 = 0xcb;
	private final static int UINT8 = 0xcc;
	private final static int UINT16 = 0xcd;
	private final static int UINT32 = 0xce;
	private final static int UINT64 = 0xcf;
	private final static int INT8 = 0xd0;
	private final static int INT16 = 0xd1;
	private final static int INT32 = 0xd2;
	private final static int INT64 = 0xd3;
	private final static int FIXEXT1 = 0xd4;
	private final static int FIXEXT2 = 0xd5;
	private final static int FIXEXT4 = 0xd6;
	private final static int FIXEXT8 = 0xd7;
	private final static int FIXEXT16 = 0xd8;
	private final static int STR8 = 0xd9;
	private final static int STR16 = 0xda;
	private final static int STR32 = 0xdb;
	private final static int ARRAY16 = 0xdc;
	private final static int ARRAY32 = 0xdd;
	private final static int MAP16 = 0xde;
	private final static int MAP32 = 0xdf;

	private final DataInputStream stream;
	private final boolean isBigEndian = true;
	private final byte[] t8 = new byte[8];

	public MessagePackReader(DataInputStream dis,
		boolean isHomogeneousArrays) {
		this.isHomo = isHomogeneousArrays;
		this.stream = dis;
	}

	public Map<String, Object> readMap() throws IOException {
		return (Map<String, Object>) getNext(null, 0);
	}

	private Object getNext(Object array, int pt)
		throws IOException {
		int b = readByte() & 0xFF;
		int be0 = b & 0xE0;
		if ((b & POSITIVEFIXINT_x80) == 0) {
			if (array != null) {
				((int[]) array)[pt] = b;
				return null;
			}
			return b;
		}
		switch (be0) {
			case NEGATIVEFIXINT_xE0:
				b = b | 0xFFFFFF00;
				if (array != null) {
					((int[]) array)[pt] = b;
					return null;
				}
				return b;
			case FIXSTR_xE0: {
				String s = readString(b & 0x1F);
				if (array != null) {
					((String[]) array)[pt] = s;
					return null;
				}
				return s;
			}
			case FIXMAP_xF0:
				return ((b & 0xF0) == FIXMAP_xF0 ? getMap(b & 0x0F) : getArray(b & 0x0F));
			case DEFINITE_xE0:
				switch (b) {
					case NIL:
						return null;
					case FALSE:
						return Boolean.FALSE;
					case TRUE:
						return Boolean.TRUE;
					case EXT8: {
						int n = readUInt8();
						return new Object[]{readUInt8(), readBytes(n)};
					}
					case EXT16: {
						int n = readUnsignedShort();
						return new Object[]{readUInt8(), readBytes(n)};
					}
					case EXT32: {
						int n = readInt(); // should be unsigned int
						return new Object[]{readUInt8(), readBytes(n)};
					}
					case FIXEXT1:
						return new Object[]{readUInt8(), readBytes(1)};
					case FIXEXT2:
						return new Object[]{readUInt8(), readBytes(2)};
					case FIXEXT4:
						return new Object[]{readUInt8(), readBytes(4)};
					case FIXEXT8:
						return new Object[]{readUInt8(), readBytes(8)};
					case FIXEXT16:
						return new Object[]{readUInt8(), readBytes(16)};
					case ARRAY16:
						return getArray(readUnsignedShort());
					case ARRAY32:
						return getArray(readInt());
					case MAP16:
						return getMap(readUnsignedShort());
					case MAP32:
						return getMap(readInt());

					// binary arrays:
					case BIN8:
						return readBytes(readUInt8());
					case BIN16:
						return readBytes(readUnsignedShort());
					case BIN32:
						return readBytes(readInt());
				}
				if (array == null) {
					switch (b) {
						case FLOAT32:
							return readFloat();
						case FLOAT64:
							return readDouble();
						case UINT8:
							return readUInt8();
						case UINT16:
							return readUnsignedShort();
						case UINT32:
							return readInt(); // should be unsigned int
						case UINT64:
							return readLong(); // should be unsigned long; incompatible with JavaScript!
						case INT8:
							return readByte();
						case INT16:
							return readShort();
						case INT32:
							return readInt(); // should be Unsigned Int here
						case INT64:
							return readLong();
						case STR8:
							return readString(readUInt8());
						case STR16:
							return readString(readShort());
						case STR32:
							return readString(readInt());
					}
				} else {
					switch (b) {
						case FLOAT32:
							((float[]) array)[pt] = readFloat();
							break;
						case FLOAT64:
							((double[]) array)[pt] = readDouble();
							break;
						case UINT8:
							((int[]) array)[pt] = readUInt8();
							break;
						case UINT16:
							((int[]) array)[pt] = readUnsignedShort();
							break;
						case UINT32:
							((int[]) array)[pt] = readInt(); // should be unsigned int
							break;
						case UINT64:
							((int[]) array)[pt] = (int) readLong(); // should be unsigned long; incompatible with JavaScript!
							break;
						case INT8:
							((int[]) array)[pt] = readByte();
							break;
						case INT16:
							((int[]) array)[pt] = readShort();
							break;
						case INT32:
							((int[]) array)[pt] = readInt(); // should be Unsigned Int here
							break;
						case INT64:
							((int[]) array)[pt] = (int) readLong();
							break;
						case STR8:
							((String[]) array)[pt] = readString(readUInt8());
							break;
						case STR16:
							((String[]) array)[pt] = readString(readShort());
							break;
						case STR32:
							((String[]) array)[pt] = readString(readInt());
							break;
					}
				}
		}
		return null;
	}

	private Object getArray(int n) throws IOException {
		if (isHomo) {
			if (n == 0) {
				return null;
			}
			Object v = getNext(null, 0);
			if (v instanceof Integer) {
				int[] a = new int[n];
				a[0] = ((Integer) v).intValue();
				v = a;
			} else if (v instanceof Float) {
				float[] a = new float[n];
				a[0] = ((Float) v).floatValue();
				v = a;
			} else if (v instanceof Double) {
				double[] a = new double[n];
				a[0] = ((Double) v).doubleValue();
				v = a;
			} else if (v instanceof String) {
				String[] a = new String[n];
				a[0] = (String) v;
				v = a;
			} else {
				Object[] o = new Object[n];
				o[0] = v;
				for (int i = 1; i < n; i++) {
					o[i] = getNext(null, 0);
				}
				return o;
			}
			for (int i = 1; i < n; i++) {
				getNext(v, i);
			}
			return v;
		}
		Object[] o = new Object[n];
		for (int i = 0; i < n; i++) {
			o[i] = getNext(null, 0);
		}
		return o;
	}

	private Object getMap(int n) throws IOException {
		Map<String, Object> map = new Hashtable<String, Object>();
		for (int i = 0; i < n; i++) {
			String key = getNext(null, 0).toString();
			Object value = getNext(null, 0);
			if (value == null) {
			} else {
				map.put(key, value);
			}
		}
		return map;
	}

	private byte readByte() throws IOException {
		return ioReadByte();
	}

	private byte ioReadByte() throws IOException {
		byte b = stream.readByte();
		return b;
	}

	private int readUInt8() throws IOException {
		int b = stream.readUnsignedByte();
		return b;
	}

	private byte[] readBytes(int n) throws IOException {
		byte[] b = new byte[n];
		readByteArray(b, 0, n);
		return b;
	}

	private int readByteArray(byte[] b, int off, int len) throws IOException {
		int n = ioRead(b, off, len);
		return n;
	}

	private int ioRead(byte[] b, int off, int len) throws IOException {
		int m = 0;
		while (len > 0) {
			int n = stream.read(b, off, len);
			m += n;
			if (n >= len) {
				break;
			}
			off += n;
			len -= n;
		}
		return m;
	}

	private String readString(int nChar) throws IOException {
		byte[] temp = new byte[nChar];
		int n = readByteArray(temp, 0, nChar);
		return new String(temp, 0, n, "UTF-8");
	}

	private short readShort() throws IOException {
		return (isBigEndian ? ioReadShort()
			: (short) ((ioReadByte() & 0xff)
			| (ioReadByte() & 0xff) << 8));
	}

	private short ioReadShort() throws IOException {
		return stream.readShort();
	}

	private int readInt() throws IOException {
		return (isBigEndian ? ioReadInt() : readLEInt());
	}

	private int ioReadInt() throws IOException {
		int i = stream.readInt();
		return i;
	}

	private int readUnsignedShort() throws IOException {
		int a = (ioReadByte() & 0xff);
		int b = (ioReadByte() & 0xff);
		return (isBigEndian ? (a << 8) + b : (b << 8) + a);
	}

	private long readLong() throws IOException {
		return (isBigEndian ? ioReadLong()
			: ((((long) ioReadByte()) & 0xff)
			| (((long) ioReadByte()) & 0xff) << 8
			| (((long) ioReadByte()) & 0xff) << 16
			| (((long) ioReadByte()) & 0xff) << 24
			| (((long) ioReadByte()) & 0xff) << 32
			| (((long) ioReadByte()) & 0xff) << 40
			| (((long) ioReadByte()) & 0xff) << 48
			| (((long) ioReadByte()) & 0xff) << 54));
	}

	private long ioReadLong() throws IOException {
		long b = stream.readLong();
		return b;
	}

	private int readLEInt() throws IOException {
		ioRead(t8, 0, 4);
		return bytesToInt(t8, 0, false);
	}

	private float readFloat() throws IOException {
		return intToFloat(readInt());
	}

	private double readDouble() throws IOException {
		// reading the float equivalent here in JavaScript
		return (isBigEndian ? ioReadDouble() : Double.longBitsToDouble(readLELong()));
	}

	private double ioReadDouble() throws IOException {
		double d = stream.readDouble();
		return d;
	}

	private long readLELong() throws IOException {
		return ((((long) ioReadByte()) & 0xff)
			| (((long) ioReadByte()) & 0xff) << 8
			| (((long) ioReadByte()) & 0xff) << 16
			| (((long) ioReadByte()) & 0xff) << 24
			| (((long) ioReadByte()) & 0xff) << 32
			| (((long) ioReadByte()) & 0xff) << 40
			| (((long) ioReadByte()) & 0xff) << 48
			| (((long) ioReadByte()) & 0xff) << 56);
	}

	private static int bytesToInt(byte[] bytes, int j, boolean isBigEndian) {
		return (isBigEndian ? (bytes[j + 3] & 0xff) | (bytes[j + 2] & 0xff) << 8
			| (bytes[j + 1] & 0xff) << 16 | (bytes[j] & 0xff) << 24
			: (bytes[j++] & 0xff) | (bytes[j++] & 0xff) << 8
			| (bytes[j++] & 0xff) << 16 | (bytes[j++] & 0xff) << 24);
	}

	private static float intToFloat(int x) {
		/**
		 * see http://en.wikipedia.org/wiki/Binary32
		 *
		 * [sign] [8 bits power] [23 bits fraction] 0x80000000 0x7F800000 0x7FFFFF
		 *
		 * (untested)
		 *
		 */
		return Float.intBitsToFloat(x);
	}

}
