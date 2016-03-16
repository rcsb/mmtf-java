package org.rcsb.mmtf.decoder;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;


public class TestDecoderUtils {
  
  private static final int NUM_EXAMPLES = 100;
  private static final int MAX_CHARS_PER_CHAIN = 4;
  
  private DecoderUtils decoderUtils = new DecoderUtils();
  private Random randGenerator = new Random();

  @Test
  public void getChainIdTest() {
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    List<String> testList = new ArrayList<>();
    //Loop through and add one, two, three and four character strings
    for(int i = 0; i < NUM_EXAMPLES; i++){
      int numChars = randGenerator.nextInt(MAX_CHARS_PER_CHAIN);
      if(numChars==0){
        numChars = 1;
      }
      int numBlank = MAX_CHARS_PER_CHAIN - numChars;
      StringBuilder stringBuild = new StringBuilder();
      for(int j=0; j< numChars; j++){
        char c = (char)(randGenerator.nextInt(26) + 'a');
        stringBuild.append(c);
        bos.write(c);
      }
      for(int j=0; j< numBlank; j++){
        
        bos.write((byte) 0);
        
      }     
      testList.add(stringBuild.toString());
    }
    int counter = 0;
    byte[] testByteArr = bos.toByteArray();
    for(String testChainId : testList){
      assertEquals(testChainId, decoderUtils.getChainId(testByteArr, counter));
      counter += 1;
    }
    
  }
  
  @Test
  public void bytesToIntsTests() throws IOException {
    
    // The input byte array of one byte integers
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int[] testIntArr = new int[NUM_EXAMPLES];
    // Get the int stream of random numbers
    for(int i = 0; i < NUM_EXAMPLES; i++){
      int currInt = randGenerator.nextInt();
      bos.write(ByteBuffer.allocate(4).putInt(currInt).array());
      testIntArr[i] = currInt;
    }
    // Now check they are the same
    assertArrayEquals(testIntArr, decoderUtils.bytesToInts(bos.toByteArray()));

    
  }
  
  @Test
  public void bytesToByteIntsTests() throws IOException {
    
    // The input byte array of one byte integers
    byte[] inputByteArr = new byte[NUM_EXAMPLES];
    int[] testIntArr = new int[NUM_EXAMPLES];
    // Get the int stream of random numbers
    for(int i = 0; i < NUM_EXAMPLES; i++){
      int currInt = randGenerator.nextInt(Byte.MAX_VALUE);
      inputByteArr[i] = (byte) currInt;
      testIntArr[i] = currInt;
    }
    assertArrayEquals(testIntArr, decoderUtils.bytesToByteInts(inputByteArr));

    
  }
}
