package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encriptador {
  public static void processadorDeArquivos(int cipherMode, String pass, File inputFile, File outputFile) {
      try {
          byte[] key = pass.getBytes("UTF-8");
          MessageDigest sha = MessageDigest.getInstance("SHA-1");
          key = sha.digest(key);
          key = Arrays.copyOf(key, 16);
          Key secretKey = new SecretKeySpec(key, "AES");
          Cipher cipher = Cipher.getInstance("AES");
          cipher.init(cipherMode, secretKey);
          
          FileInputStream inputStream = new FileInputStream(inputFile);
          byte[] inputBytes = new byte[(int) inputFile.length()];
          inputStream.read(inputBytes);
          
          byte[] outputBytes = cipher.doFinal(inputBytes);
          
          try (FileOutputStream fop = new FileOutputStream(outputFile)) {

			// se o arquivo não existir, ele é criado
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			// escreve o conteúdo em bytes
			fop.write(outputBytes);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
      } catch (NoSuchPaddingException | NoSuchAlgorithmException 
                     | InvalidKeyException | BadPaddingException
	             | IllegalBlockSizeException | IOException e) {
          e.printStackTrace();
      }
  }
  //retirar essa parte
  /*
  public static void main(String[] args) {
  	String key = "This is a secret";
        String homeFldr = System.getProperty("user.home");
        File desktop = new File(homeFldr, "Desktop");
	File inputFile = new File(desktop, "text.png");
	File encryptedFile = new File(desktop, "text.encrypted");
	File decryptedFile = new File(desktop, "decrypted-text.png");
		
	try {
	     Encriptador.processadorDeArquivos(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile);
	     Encriptador.processadorDeArquivos(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
	     System.out.println("Sucess");
	 } catch (Exception ex) {
	     System.out.println(ex.getMessage());
             ex.printStackTrace();
	 }
  }
  */
}