package org.rcsb.mmtf.serialization.mp;

import java.util.Hashtable;
import java.util.Map;


/**
 * A simple MessagePack reader. See https://github.com/msgpack/msgpack/blob/master/spec.md
 * with very few dependencies.
 * 
 * Nuances: 
 * 
 *  Does not implement unsigned int32 or int64 (delivers simple integers in all cases).
 *  Does not use doubles; just floats
 *  
 * Note: 
 * 
 *  homogeneousArrays == true will deliver null for empty array.
 * 
 * 
 * Use in MMTF:
 * 
 * 
 *     BufferedInputStream bs = [whatever]
 *     
 *      GenericBinaryDocument binaryDoc =  new javajs.util.BinaryDocument();
 *   
 *      binaryDoc.setStream(bs, true);
 * 
 * 
 *     map = (new MessagePackReader(binaryDoc, true)).readMap();
 * 
 *     entities = (Object[]) map.get("entityList");
 *
 *     float[] x = (float[]) decode((byte[]) map.get("xCoordList"))
 *     
 * 
 * @author Bob Hanson hansonr@stolaf.edu
 */

public class MessagePackReader {

  private GenericBinaryDocument doc;

  private boolean isHomo;// homogeneous arrays -- use int[] not Integer

  // these maps must be checked for the specific number of bits, in the following order:
  private final static int POSITIVEFIXINT_x80 = 0x80; //0xxxxxxx
  private final static int FIXMAP_xF0         = 0x80; //1000xxxx
//  private final static int FIXARRAY_xF0       = 0x90; //1001xxxx
  private final static int FIXSTR_xE0         = 0xa0; //101xxxxx
  private final static int NEGATIVEFIXINT_xE0 = 0xe0; //111xxxxx
  private final static int DEFINITE_xE0       = 0xc0; //110xxxxx
  
  private final static int NIL          = 0xc0;
//  private final static int (NEVERUSED)        = 0xc1;
  private final static int FALSE        = 0xc2;
  private final static int TRUE         = 0xc3;
  private final static int BIN8         = 0xc4;
  private final static int BIN16        = 0xc5;
  private final static int BIN32        = 0xc6;
  private final static int EXT8         = 0xc7;
  private final static int EXT16        = 0xc8;
  private final static int EXT32        = 0xc9;
  private final static int FLOAT32      = 0xca;
  private final static int FLOAT64      = 0xcb;
  private final static int UINT8        = 0xcc;
  private final static int UINT16       = 0xcd;
  private final static int UINT32       = 0xce;
  private final static int UINT64       = 0xcf;
  private final static int INT8         = 0xd0;
  private final static int INT16        = 0xd1;
  private final static int INT32        = 0xd2;
  private final static int INT64        = 0xd3;
  private final static int FIXEXT1      = 0xd4;
  private final static int FIXEXT2      = 0xd5;
  private final static int FIXEXT4      = 0xd6;
  private final static int FIXEXT8      = 0xd7;
  private final static int FIXEXT16     = 0xd8;
  private final static int STR8         = 0xd9;
  private final static int STR16        = 0xda;
  private final static int STR32        = 0xdb;
  private final static int ARRAY16      = 0xdc;
  private final static int ARRAY32      = 0xdd;
  private final static int MAP16        = 0xde;
  private final static int MAP32        = 0xdf;

  public MessagePackReader(GenericBinaryDocument binaryDoc, boolean isHomogeneousArrays) {
    isHomo = isHomogeneousArrays;
    doc = binaryDoc;
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> readMap() throws Exception {
    return (Map<String, Object>) getNext(null, 0);
  }
  
  public Object getNext(Object array, int pt) throws Exception {
    int b = doc.readByte() & 0xFF;
    int be0 = b & 0xE0;
    if ((b & POSITIVEFIXINT_x80) == 0) {
      if (array != null) {
        ((int[]) array)[pt] = b;
        return null;
      }
      return Integer.valueOf(b);
    }
    switch (be0) {
    case NEGATIVEFIXINT_xE0:
      b = BC.intToSignedInt(b | 0xFFFFFF00);
      if (array != null) {
        ((int[]) array)[pt] = b;
        return null;
      }
      return Integer.valueOf(b);
    case FIXSTR_xE0: {
      String s = doc.readString(b & 0x1F);
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
        int n = doc.readUInt8();
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(n) };
      }
      case EXT16: {
        int n = doc.readUnsignedShort();
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(n) };
      }
      case EXT32: {
        int n = doc.readInt(); // should be unsigned int
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(n) };
      }
      case FIXEXT1:
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(1) };
      case FIXEXT2:
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(2) };
      case FIXEXT4:
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(4) };
      case FIXEXT8:
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(8) };
      case FIXEXT16:
        return new Object[] { Integer.valueOf(doc.readUInt8()),
            doc.readBytes(16) };
      case ARRAY16:
        return getArray(doc.readUnsignedShort());
      case ARRAY32:
        return getArray(doc.readInt());
      case MAP16:
        return getMap(doc.readUnsignedShort());
      case MAP32:
        return getMap(doc.readInt());

        // binary arrays:

      case BIN8:
        return doc.readBytes(doc.readUInt8());
      case BIN16:
        return doc.readBytes(doc.readUnsignedShort());
      case BIN32:
        return doc.readBytes(doc.readInt());
      }
      if (array == null) {
        switch (b) {
        case FLOAT32:
          return Float.valueOf(doc.readFloat());
        case FLOAT64:
          return doc.readDouble();
        case UINT8:
          return Integer.valueOf(doc.readUInt8());
        case UINT16:
          return Integer.valueOf(doc.readUnsignedShort());
        case UINT32:
          return Integer.valueOf(doc.readInt()); // should be unsigned int
        case UINT64:
          return Long.valueOf(doc.readLong()); // should be unsigned long; incompatible with JavaScript!
        case INT8:
          return Integer.valueOf(doc.readByte());
        case INT16:
          return Integer.valueOf(doc.readShort());
        case INT32:
          return Integer.valueOf(doc.readInt()); // should be Unsigned Int here
        case INT64:
          return Long.valueOf(doc.readLong());
        case STR8:
          return doc.readString(doc.readUInt8());
        case STR16:
          return doc.readString(doc.readShort());
        case STR32:
          return doc.readString(doc.readInt());
        }
      } else {
        switch (b) {
        case FLOAT32:
          ((float[]) array)[pt] = doc.readFloat();
          break;
        case FLOAT64:
          ((double[]) array)[pt] = doc.readDouble();
          break;
        case UINT8:
          ((int[]) array)[pt] = doc.readUInt8();
          break;
        case UINT16:
          ((int[]) array)[pt] = doc.readUnsignedShort();
          break;
        case UINT32:
          ((int[]) array)[pt] =  doc.readInt(); // should be unsigned int
          break;
        case UINT64:
          ((int[]) array)[pt] =  (int) doc.readLong(); // should be unsigned long; incompatible with JavaScript!
          break;
        case INT8:
          ((int[]) array)[pt] =  doc.readByte();
          break;
        case INT16:
          ((int[]) array)[pt] = doc.readShort();
          break;
        case INT32:
          ((int[]) array)[pt] =  doc.readInt(); // should be Unsigned Int here
          break;
        case INT64:
          ((int[]) array)[pt] =  (int) doc.readLong();
          break;
        case STR8:
          ((String[]) array)[pt] = doc.readString(doc.readUInt8());
          break;
        case STR16:
          ((String[]) array)[pt] = doc.readString(doc.readShort());
          break;
        case STR32:
          ((String[]) array)[pt] = doc.readString(doc.readInt());
          break;
        }
      }
    }
    return null;
  }

  private Object getArray(int n) throws Exception {
    if (isHomo) {
      if (n == 0)
        return null;
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
        for (int i = 1; i < n; i++)
          o[i] = getNext(null, 0);
        return o;
      }
      for (int i = 1; i < n; i++)
        getNext(v, i);
      return v;
    }
    Object[] o = new Object[n];
    for (int i = 0; i < n; i++)
      o[i] = getNext(null, 0);
    return o;
  }

  private Object getMap(int n) throws Exception {
    Map<String, Object> map = new Hashtable<String, Object>();
    for (int i = 0; i < n; i++) {
      String key = getNext(null, 0).toString();
      //Logger.info(key);

      Object value = getNext(null, 0);
      if (value == null) {
        //Logger.info("null value for " + key);
      } else {
        map.put(key, value);
      }
    }
    return map;
  }
}
