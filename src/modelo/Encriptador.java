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
}