package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class GeradorHash {
    
   public static String getChecksum(File file, String tipo) throws Exception {
   //public static String getChecksum(File file) throws Exception {
       InputStream fis =  new FileInputStream(file);
       String resultado = "";
       byte[] buffer = new byte[1024];
       //MessageDigest complete = MessageDigest.getInstance("SHA1");
       MessageDigest complete = MessageDigest.getInstance(tipo);
       int numRead;

       do {
           numRead = fis.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       } while (numRead != -1);

       fis.close();
       //return complete.digest();
       resultado = new HexBinaryAdapter().marshal(complete.digest()).toLowerCase();
       return resultado;
   }
/*
   public static String getChecksum(File file) throws Exception {
       byte[] b = createChecksum(file);
       String result = "";

       for (int i=0; i < b.length; i++) {
           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
       return result;
}*/
}
