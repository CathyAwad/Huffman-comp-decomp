import java.util.*;
import java.io.*;
import java.lang.*;
import java.nio.*;
import java.math.*;


public class Binary{

  private class Node implements Comparable<Node>{
    private byte b;
    private int freq;
    private Node right, left;
    private String code ;
    public Node(){}
    public Node(byte b, int freq, Node left, Node right){
      this.b = b;
      this.freq = freq;
      this.left = left;
      this.right = right;
    }
    public void setCode(String s){
      this.code = s;
    }
    public String getCode(){
      return this.code;
    }
    public int getFreq(){
      return this.freq;
    }
    public byte getB(){
      return this.b;
    }
    public void setLeft(Node n){
      this.left = n;
    }
    public void setRight(Node n){
      this.right = n;
    }
    public Node getLeft(){
      return this.left;
    }
    public Node getRight(){
      return this.right;
    }
    public boolean isLeaf(){
      if(this.right == null && this.left == null)
        return true;
      else
        return false;
    }
    @Override
    public int compareTo( Node t) {
        if(this.freq < t.freq){
            return -1;
        }else if(this.freq > t.freq){
            return 1;
        }else
            return 0;
    }
    public void print(){
    //  if(this.code != null)
        System.out.println(this.b + " : " + this.code);
    }
  }

  public HashMap<Byte,Integer> x = new HashMap();
  public static PriorityQueue<Node> q;
  public HashMap<Byte,String> code = new HashMap();
  public HashMap<String,Byte> decode = new HashMap();
  public Integer count;
  public Integer noOfFiles;

  public void readFolder(String foldername){
    File folder = new File(foldername);
    File[] listOfFiles = folder.listFiles();
    noOfFiles = listOfFiles.length;
    for (File file : listOfFiles){
      if (file.isFile()) {
        System.out.println("file "+ file.getName());
        try{
          InputStream insputStream = new FileInputStream(file);
          long length = file.length();
          byte[] bytes = new byte[(int) length];
          insputStream.read(bytes);
          insputStream.close();
          for (int i=0; i< bytes.length; i++){
            byte b = bytes[i];
            if(x.containsKey(b)){
              int a = x.get(b);
              a++;
              x.put(b, a);
            }
            else {
              x.put(b,1);
            }

          }
          System.out.println("close file "+file.getName());

          }catch(Exception e){
            System.out.println("Error is:" + e.getMessage());
          }

      }
    }
  }

  public void readBinaryFile(String filename){
    try{
      File file = new File(filename);
      InputStream insputStream = new FileInputStream(file);
      long length = file.length();
      byte[] bytes = new byte[(int) length];
      insputStream.read(bytes);
      insputStream.close();
      for (int i=0; i< bytes.length; i++){
        byte b = bytes[i];
        if(x.containsKey(b)){
          int a = x.get(b);
          a++;
          x.put(b, a);
        }
        else {
          x.put(b,1);
        }

      }


      }catch(Exception e){
        System.out.println("Error is:" + e.getMessage());
      }
  }

  public void buildQ(){
    q = new PriorityQueue<Node>();
    for(byte key : x.keySet()){
      Node n = new Node(key, x.get(key),null, null);
      q.add(n);
    }
  }

  public void encode( String s, Node head){
    if(!(head.isLeaf())){
      encode(s+"1", head.getRight());
      encode(s+"0", head.getLeft());
    }
    else {
    //  System.out.println("henaaaa");
      head.setCode(s);
      code.put(head.getB(), s);
    }
  }

  public void codeSize(){
    count = 0;
    if(x.size()!=0){
      for(Byte key:x.keySet()){
        int freq = x.get(key);
        if(code.get(key) != null){
          int len = code.get(key).length();
          count+= len * freq;
        }

      //  System.out.println("count is " + count);
      }
    }
  }

  public Node buildTree(){
    Node head = new Node();
    while(q.size() > 1){
      Node left = q.poll();
      Node right = q.poll();
      Byte b = new Byte("0");
      head = new Node(b, left.getFreq() + right.getFreq(), left, right);
      //System.out.println(head.getFreq());
      q.add(head);
    }
    return head;
  }

  public void Traverse(Node head){
    if(head == null){
      return;
    }
    Traverse(head.getLeft());
    head.print();
    Traverse(head.getRight());
  }

  public int mapSize(){
    String s = new String();
    count = 0;
    for(Byte key: code.keySet()){
      String a = key.toString();
       s+= a + code.get(key);
       count++;
       count += code.get(key).getBytes().length;

    }

    return count;
  }

  private static String hexToAscii(String hexStr) {
      StringBuilder output = new StringBuilder("");

      for (int i = 0; i < hexStr.length(); i += 2) {
          String str = hexStr.substring(i, i + 2);
          output.append((char) Integer.parseInt(str, 16));
      }

      return output.toString();
  }

  public void buildMap(String tree){
    //System.out.println(tree);
    while(tree.length() > 0){
      String c = tree.substring(0,2);
      int lens = c.length();
      byte[] data = new byte[lens / 2];
      for (int i = 0; i < lens; i += 2) {
          data[i / 2] = (byte) ((Character.digit(c.charAt(i), 16) << 4)
                               + Character.digit(c.charAt(i+1), 16));
      }
      //byte[] b = c.getBytes();
      Byte by = new Byte(data[0]);
      //System.out.println("byte" +b[0] + b[1]);
      String l = tree.substring(2,10);
      int len = Integer.valueOf(l,16);
      //System.out.println(len);
      len = len*2;
      len +=10;

      String k = tree.substring(10, len);
      //System.out.println(k);
      String bin = new String();

      while (k.length()>0){
        String s = k.substring(0,2);
        bin += hexToAscii(s);
        k=k.substring(2);
      }
      //System.out.println(bin+ ": " +by.toString());
      decode.put(bin, by);
      tree = tree.substring(len);

    }


    for(String key : decode.keySet()){
      System.out.println(key + " : " + decode.get(key));
    }

  }

  public void compressFolder(String foldername){
    int i,j,k;
    String out = new String();
    String name = foldername + "binary";
    File folder = new File(foldername);
    File[] listOfFiles = folder.listFiles();
    noOfFiles = listOfFiles.length;
    int sizes[] = new int[noOfFiles];
    int count =0;
    int index =0;
    try (
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(name));
        ){
          byte size[] = ByteBuffer.allocate(4).putInt(noOfFiles).array();
          outputStream.write(size);
          for(File file : listOfFiles){
            int fileNameLength = file.getName().length();
            byte b2[] = ByteBuffer.allocate(4).putInt(fileNameLength).array();
            outputStream.write(b2);
            byte b3[] = file.getName().getBytes();
            outputStream.write(b3);
          }

          int maps = mapSize();
          byte bytes2[] = ByteBuffer.allocate(4).putInt(maps + 4*code.size()).array();
          outputStream.write(bytes2);

          for(byte key: code.keySet()){
            outputStream.write(key);
            byte b2[] = ByteBuffer.allocate(4).putInt(code.get(key).length()).array();
            outputStream.write(b2);
            String st = code.get(key);
            byte b3[] = st.getBytes();
            outputStream.write(b3);
          }

          for (File file : listOfFiles){
            if (file.isFile()) {
              try{
                InputStream insputStream = new FileInputStream(file);
                long length = file.length();
                byte[] bytes = new byte[(int) length];
                insputStream.read(bytes);
                insputStream.close();
                System.out.println("length"+bytes.length);
                for( i=0; i<bytes.length; i++){
                  byte c = bytes[i];
                  if(code.containsKey(c)){
                    out+=code.get(c);
                    if(out.length()%8 == 0 && out.length()!= 0){
                    //  System.out.println(out);
                      int sLen = out.length();

                      //System.out.println("string length "+ sLen);
                      byte[] toPrint = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
                      //System.out.println("bytes length "+toPrint.length);
                      char d;
                      for( j = 0; j < sLen; j++ ){
                          if( (d = out.charAt(j)) == '1' )
                              toPrint[j / Byte.SIZE] = (byte) (toPrint[j / Byte.SIZE] | (0x80 >>> (j % Byte.SIZE)));
                          else if ( d != '0' )
                              throw new IllegalArgumentException();
                      }
                      for( k=0; k<toPrint.length; k++){
                        //System.out.println("hrna?"+toPrint[k]);
                        outputStream.write(toPrint[k]);
                      //  System.out.println("eshta");
                      }
                      //System.out.println("hna????");
                          sizes[index] +=sLen;
                          out = "";
                    }

                  }

                 else {
                    System.out.println(c);
                    System.out.println("errorrrrrrr");
                    System.exit(1);
                  }
                //  System.out.println("i "+i);
                }
                if(out.length()!= 0){
                  int sLen = out.length();
                  byte[] toPrint = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
                  char d;
                  for(  i = 0; i < sLen; i++ ){
                      if( (d = out.charAt(i)) == '1' )
                          toPrint[i / Byte.SIZE] = (byte) (toPrint[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
                      else if ( d != '0' )
                          throw new IllegalArgumentException();
                  }
                  for( i=0;i<toPrint.length; i++){
                    outputStream.write(toPrint[i]);
                  }
                  sizes[index] += sLen;
                }






              }catch(Exception e){
                System.out.println("Error is foldercompress:" + e.getMessage());
              }


            }
            index++;


          }

          index=0;
          for(File file : listOfFiles){
            byte b4[] = ByteBuffer.allocate(4).putInt(sizes[index]).array();
            index++;
            outputStream.write(b4);
          }




        }catch (IOException e) {
         System.out.println("folder not found");
        }
  }

  public void compress(String filename){
    int i,j,k;
    String out = new String();
    String name = filename + "binary";
    try (
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(name));
        ){
          byte size[] = ByteBuffer.allocate(4).putInt(count).array();
          outputStream.write(size);
          int maps = mapSize();
          byte bytes2[] = ByteBuffer.allocate(4).putInt(maps + 4*code.size()).array();
          outputStream.write(bytes2);

          for(byte key: code.keySet()){
            outputStream.write(key);
            byte b2[] = ByteBuffer.allocate(4).putInt(code.get(key).length()).array();
            outputStream.write(b2);
            String st = code.get(key);
            byte b3[] = st.getBytes();
            outputStream.write(b3);
          }

          try{
            File file = new File(filename);
            InputStream insputStream = new FileInputStream(file);
            long length = file.length();
            byte[] bytes = new byte[(int) length];
            insputStream.read(bytes);
            insputStream.close();
            for( i=0; i<bytes.length; i++){
              byte c = bytes[i];
              if(code.containsKey(c)){
                out+=code.get(c);
                if(out.length()%8 == 0 && out.length()!= 0){
                //  System.out.println(out);
                  int sLen = out.length();

                  //System.out.println("string length "+ sLen);
                  byte[] toPrint = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
                  //System.out.println("bytes length "+toPrint.length);
                  char d;
                  for( j = 0; j < sLen; j++ ){
                      if( (d = out.charAt(j)) == '1' )
                          toPrint[j / Byte.SIZE] = (byte) (toPrint[j / Byte.SIZE] | (0x80 >>> (j % Byte.SIZE)));
                      else if ( d != '0' )
                          throw new IllegalArgumentException();
                  }
                  for( k=0; k<toPrint.length; k++){
                  //  System.out.println("hrna?"+toPrint[k]);
                    outputStream.write(toPrint[k]);
                  }
                      out = "";
                }

              }

             else {
                System.out.println(c);
                System.out.println("errorrrrrrr");
                System.exit(1);
              }
            }
            if(out.length()!= 0){
              int sLen = out.length();
              byte[] toPrint = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
              char d;
              for(  i = 0; i < sLen; i++ ){
                  if( (d = out.charAt(i)) == '1' )
                      toPrint[i / Byte.SIZE] = (byte) (toPrint[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
                  else if ( d != '0' )
                      throw new IllegalArgumentException();
              }
              for( i=0;i<toPrint.length; i++){
                outputStream.write(toPrint[i]);
              }
            }
            }catch(Exception e){
              System.out.println("Error is:" + e.getMessage());
            }
        }catch (IOException e) {
         System.out.println("file not found");
        }

  }

  public void decompressFolder(String name, String nameout){
    try{
      File file = new File(name);
      InputStream insputStream = new FileInputStream(file);
      long length = file.length();
      byte[] bytes = new byte[(int) length];
      insputStream.read(bytes);
      insputStream.close();
      int counter =0;
      StringBuilder sx = new StringBuilder(8);
      //extract no of files
      for(counter = 0; counter<4; counter++){
        sx.append(String.format("%02x", bytes[counter]));
      }
      String s11 = sx.toString();
      noOfFiles = Integer.valueOf(s11,16);
      //extract files names
      String fileNames[] = new String[noOfFiles];
      int sizes[] = new int[noOfFiles];
      int read=0;
      for(int i=0; i<noOfFiles; i++){
        StringBuilder s2 = new StringBuilder(8);
        for(int j=0; j<4; j++){
          s2.append(String.format("%02x", bytes[counter]));
          counter++;
          read++;
        }
        String s22 = s2.toString();
        int toRead = Integer.valueOf(s22,16);
        byte nam[] = new byte[toRead];
        for(int j=0; j<toRead; j++){
          nam[j] = bytes[counter];
          counter++;
          read++;
        }
        String fileNameRead = new String(nam);
        fileNames[i] = fileNameRead;
        System.out.println(fileNameRead);
      }
      int ind=0;
      for(int i = bytes.length - (4*noOfFiles); i<bytes.length; i+=4){
        System.out.println("hena");
        StringBuilder s3 = new StringBuilder(8);
        for(int j=i; j<i+4; j++){
          s3.append(String.format("%02x", bytes[j]));
        }
        String s33 = s3.toString();
        System.out.println(s33);
        int fileseize = Integer.valueOf(s33,16);
        sizes[ind] = fileseize;
        ind++;
      }

      for(int i=0; i<noOfFiles; i++){
        System.out.println(fileNames[i] + ": "+ sizes[i]);
      }

      StringBuilder siz = new StringBuilder(8);
      for(counter = 4+read; counter < 8+read; counter++ ){
        siz.append(String.format("%02x", bytes[counter]));
      }
      String siz2 = siz.toString();
      int mapsize = Integer.valueOf(siz2,16);
      StringBuilder map = new StringBuilder(2*mapsize);
      for(counter = 8+read; counter < 8+read+mapsize; counter++){
        map.append(String.format("%02x", bytes[counter]));
      }
      String tree = map.toString();
      System.out.println(tree);
      buildMap(tree);
      System.out.println("tree done");
      System.out.println(noOfFiles);
      for(int y=0; y<noOfFiles; y++){
        String extensionRemoved = fileNames[y].split("\\.")[0];
        System.out.println("ufd"+extensionRemoved);
        String extension = fileNames[y].split("\\.")[1];
        System.out.println("ddd"+extension);
        String namenew = extensionRemoved + "outfolder." + extension;
        System.out.println(namenew);
        try (
            OutputStream r = new BufferedOutputStream(new FileOutputStream("filesout/"+namenew));
            ){
              String s1 = new String();
              int z = counter + (sizes[y]/8);

              //System.out.println("counter is "+counter+"limit " +z);
              while(counter < z && counter<bytes.length-4*noOfFiles){
                byte b1 = bytes[counter];
                counter++;
                s1 += String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
                //System.out.println(s1);
                int j=1;
                String s = new String();
                while(s1.length() > j ){
                  int i = 0;
                   j = 1;
                   s = s1.substring(i,j);
                  while(!decode.containsKey(s)){
                    j++;
                    if(j == s1.length()){
                      byte b2 = bytes[counter];
                      counter++;
                      s1 += String.format("%8s", Integer.toBinaryString(b2 & 0xFF)).replace(' ', '0');
                    }
                    s = s1.substring(i,j);
                  }
                  r.write(decode.get(s));
                  s1 = s1.substring(j);

                }
              }
              if(sizes[y]%8 !=0 && counter<bytes.length -4*noOfFiles){
                int remaining = sizes[y]%8;
                int till = 8-remaining;
                //counter++;
                //System.out.println("1");
                byte left = bytes[counter];
                String h = String.format("%8s", Integer.toBinaryString(left & 0xFF)).replace(' ', '0');
                //System.out.println("2");
                s1 += h;
                s1 = s1.substring(0, s1.length()-till);
                while(s1.length() > 0){
                  int i = 0;
                  int j = 1;
                  String s = s1.substring(i,j);
                  while(!decode.containsKey(s)){
                    j++;
                    s = s1.substring(i,j);
                  }
                  r.write(decode.get(s));
                  s1 = s1.substring(j);
                }
              }

              r.close();

            }catch(Exception e){
              System.out.println("Error is hena :" + e.getMessage());
            }
      }


    }catch(Exception e){
      System.out.println("Error is hena 2:" + e.getMessage());
    }
  }

  public void decompress(String filename, String name2){

    try{
      File file = new File(name2);
      InputStream insputStream = new FileInputStream(file);
      long length = file.length();
      byte[] bytes = new byte[(int) length];
      insputStream.read(bytes);
      insputStream.close();
      int counter = 0;
      StringBuilder len = new StringBuilder(8);
      for(counter = 0; counter<4; counter++){
        len.append(String.format("%02x", bytes[counter]));
      }
      String len2 = len.toString();
      count = Integer.valueOf(len2,16);
      StringBuilder siz = new StringBuilder(8);
      for(counter = 4; counter<8; counter++){
        siz.append(String.format("%02x", bytes[counter]));
      }
      String siz2 = siz.toString();
      int mapsize = Integer.valueOf(siz2,16);
      StringBuilder map = new StringBuilder(2*mapsize);
      for(counter = 8; counter<(mapsize+8); counter++){
        map.append(String.format("%02x", bytes[counter]));
      }
      String tree = map.toString();
      buildMap(tree);

      try (
          OutputStream r = new BufferedOutputStream(new FileOutputStream(filename));
          ){

            String s1 = new String();
            while(counter < bytes.length){
              byte b1 = bytes[counter];
              counter++;
              s1 += String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
              int j=1;
              String s = new String();
              while(s1.length() > j ){
                int i = 0;
                 j = 1;
                 s = s1.substring(i,j);
                while(!decode.containsKey(s)){
                  j++;
                  if(j == s1.length()){
                    byte b2 = bytes[counter];
                    counter++;
                    s1 += String.format("%8s", Integer.toBinaryString(b2 & 0xFF)).replace(' ', '0');
                  }
                  //System.out.println(s1.length() +" h j " +j);

                  s = s1.substring(i,j);
                }
                r.write(decode.get(s));
                //System.out.println(s1.length() +"  j " +j);
                s1 = s1.substring(j);

              }
            }
            if(count%8 !=0){
              int remaining = count%8;
              int till = 8-remaining;
              byte left = bytes[mapsize+8+(count/8)];
              String h = String.format("%8s", Integer.toBinaryString(left & 0xFF)).replace(' ', '0');
              s1 += h;
              s1 = s1.substring(0, s1.length()-till);
              while(s1.length() > 0){
                int i = 0;
                int j = 1;
                String s = s1.substring(i,j);
                while(!decode.containsKey(s)){
                  j++;
                  s = s1.substring(i,j);
                }
                r.write(decode.get(s));
                s1 = s1.substring(j);
              }
            }
            r.close();

          }catch(Exception e){
            System.out.println("Error is:" + e.getMessage());
          }





    }catch(Exception e){
      System.out.println("Error is:" + e.getMessage());
    }

  }


  public static void main(String [] args){
    try{
        Binary obj = new Binary ();
        obj.run (args);
    }
    catch (Exception e){
        e.printStackTrace ();
    }
  }
  public boolean check(String name1, String name2){
    long len1 = 0,len2 = 0;
    try{
      File file = new File(name1);
      InputStream insputStream = new FileInputStream(file);
      len1 = file.length();
    }catch(Exception e){
      System.out.println("Error is:" + e.getMessage());
    }
    /*try{
      File file = new File(name2);
      InputStream insputStream = new FileInputStream(file);
      len2 = file.length();
    }catch(Exception e){
      System.out.println("Error is:" + e.getMessage());
    }*/
    len2 = count/8 + mapSize() + 4 * code.size();
    if(len2 > len1)
      return false;
    else return true;
  }


  public void run (String[] args) throws Exception{

    if(args.length != 2){
      System.out.println("arguments size error" + args.length);
      System.exit(1);
    }
    String filename = args[0];
    String choice = args[1];
    if(choice.equals("1")){
      long startTime = System.nanoTime();
      readBinaryFile(filename);
      buildQ();
      Node head = buildTree();
      if(x.size()==1){
        System.out.println("ana hena");
        head.setCode("0");
        code.put(head.getB(), "0");
      }
      else
        encode("", head);

        for(Byte b: x.keySet()){
          System.out.println(b.toString() + ": "+ x.get(b));
        }
        for(Byte b: code.keySet()){
          System.out.println(b.toString() + ": "+ code.get(b));
        }
        codeSize();
        boolean compressed = check(filename, filename+"binary");
        if(!compressed){
          System.out.println("file can't be compressed");
          System.out.println("For a set of symbols with a uniform probability distribution and a number of members which is a power of two, Huffman coding is equivalent to simple binary block encoding, e.g., ASCII coding. This reflects the fact that compression is not possible with such an input, no matter what the compression method, i.e., doing nothing to the data is the optimal thing to do.");
          File file = new File(filename + "binary");
          file.delete();
        }
        else
          compress(filename);

        long elapsedTime = System.nanoTime() - startTime;

          System.out.println("Total execution time in millis: "
                  + elapsedTime/1000000);


    }
    else if(choice.equals("2")){
      long startTime = System.nanoTime();
      String extensionRemoved = filename.split("\\.")[0];
      String extension = filename.split("\\.")[1];
      String name = extensionRemoved + "out." + extension;
      System.out.println(name);

      decompress(name, filename+"binary");
      long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Total execution time in millis: "
                + elapsedTime/1000000);
    }
    else if(choice.equals("3")){
      long startTime = System.nanoTime();
      readFolder(filename);
      buildQ();
      Node head = buildTree();
      if(x.size()==1){
        System.out.println("ana hena");
        head.setCode("0");
        code.put(head.getB(), "0");
      }
      else
        encode("", head);

        for(Byte b: x.keySet()){
          System.out.println(b.toString() + ": "+ x.get(b));
        }
        for(Byte b: code.keySet()){
          System.out.println(b.toString() + ": "+ code.get(b));
        }
        codeSize();
        compressFolder(filename);



      long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Total execution time in millis: "
                + elapsedTime/1000000);
    }
    else if(choice.equals("4")){
      long startTime = System.nanoTime();

      decompressFolder("filesbinary", "decompfold/");
      long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Total execution time in millis: "
                + elapsedTime/1000000);

    }
    }

}
