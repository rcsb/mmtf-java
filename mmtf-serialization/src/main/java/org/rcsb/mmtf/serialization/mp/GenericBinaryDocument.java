package org.rcsb.mmtf.serialization.mp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Map;



public interface GenericBinaryDocument {

  GenericBinaryDocument setStream(BufferedInputStream bis, boolean isBigEndian);

  void setStreamData(DataInputStream dataInputStream, boolean isBigEndian);

  long getPosition();

  SB getAllDataFiles(String binaryFileList, String firstFile);

  void getAllDataMapped(String replace, String string, Map<String, String> fileData);

  int swapBytesI(int nx);

  short swapBytesS(short s);

  void seek(long i);

  byte readByte() throws Exception;

  int readUInt8() throws Exception;

  int readInt() throws Exception;

  int readIntLE() throws Exception;

  long readLong() throws Exception;

  float readFloat() throws Exception;

  double readDouble() throws Exception;

  short readShort() throws Exception;

  int readUnsignedShort() throws Exception;

  String readString(int i) throws Exception;

  int readByteArray(byte[] b, int off, int len) throws Exception;

  byte[] readBytes(int n) throws Exception;

  void close();

  void setOutputChannel(GenericOutputChannel out);

  InputStream getInputStream();

}
