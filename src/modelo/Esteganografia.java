package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Esteganografia {

	 /*
            Encripta uma imagem com texto, o resultado será .png
            @param path         O caminho da imagem a ser modificada
            @param original    O nome da imagem que será modificada
            @param ext1          A extensão da imagem a ser modificada (jpg, png)
            @param stegan      O output do nome do arquivo
            @param message  O texto a ser escrito na imagem
            @param type	  integer que representa encriptação simples ou avançada
	 */
	public static boolean encode(String path, String original, String ext1, String stegan, String message)
	{
		String			file_name 	= image_path(path,original,ext1);
		BufferedImage 	image_orig	= getImage(file_name);

		BufferedImage image = user_space(image_orig);
		image = add_text(image,message);

		return(setImage(image,new File(image_path(path,stegan,"png")),"png"));
	}

	 /*
            A decriptação assume que a imagem  da qual a mensagem será extraída é do tipo .png
            @param path   O path (folder) que contém a imagem da qual a mensagem será extraída
            @param name O nome da imagem da qual a mensagem será extraída
            @param type integer que representa encriptação simples ou avançada
	 */
	public static String decode(String path, String name)
	{
		byte[] decode;
		try
		{
			//user space é necessário para decriptação
			BufferedImage image  = user_space(getImage(image_path(path,name,"png")));
			decode = decode_text(get_byte_data(image));
			return(new String(decode));
		}
        catch(Exception e)
        {
			JOptionPane.showMessageDialog(null,
				"Não existe mensagem escondida nessa imagem","Erro",
				JOptionPane.ERROR_MESSAGE);
			return "";
        }
    }

	 /*
            Retorna o path completo de uma arquivo, da forma: path\nome.ext
            @param path   O path (folder) do arquivo
            @param name O nome do arquivo
            @param ext      A extensão do arquivo
            @return Uma String que representa o path completo do arquivo
	 */
	private static String image_path(String path, String name, String ext)
	{
                String imagePath = path + "//" + name + "." + ext;
                System.out.println(imagePath);
		return imagePath;
	}

	 /*
            Método get para retornar um aruqivo imagem
            @param f O path completo da imagem
            @return Uma BufferedImage do path fornecido
            @see    Steganography.image_path
	 */
	private static BufferedImage getImage(String f)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(f);

		try
		{
			image = ImageIO.read(file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,
				"A imagem não foi lida","Erro",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

	 /*
            Método para salvar um arquivo de imagem
            @param image O arquivo imagem a ser salvo
            @param file      Arquivo no qual a imagem será salva
            @param ext      A extensão (formato) no qual será salva
            @return Retorna true se a imagem for salva
	 */
	private static boolean setImage(BufferedImage image, File file, String ext)
	{
		try
		{
			file.delete(); //deleta resources usados pelo arquivo
			ImageIO.write(image,ext,file);
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,
				"O arquivo não foi salvo","Erro",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	 /*
            Adiciona texto a imagem
            @param image A imagem na qual será adicionada a imagem
            @param text     O texto a ser escondido na imagem
            @return Retorna uma imagem com o texto escondido nela
	 */
	private static BufferedImage add_text(BufferedImage image, String text)
	{
		//converte todos os itens para byte arrays: image, message, message length
		byte img[]  = get_byte_data(image);
		byte msg[] = text.getBytes();
		byte len[]   = bit_conversion(msg.length);
		try
		{
			encode_text(img, len,  0);
			encode_text(img, msg, 32);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,
"Arquivo de destino não suporta a mensagem", "Erro",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

	 /*
            Cria uma versão user space da  BufferedImage, para editar e salvar bytes
            @param image A imagem a ser colocada no user space, remove interferências de compressão
            @return A versão user space da imagem enviada
	 */
	private static BufferedImage user_space(BufferedImage image)
	{
		//cria new_img, com os atributos da imagem
		BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D    graphics = new_img.createGraphics();
		graphics.drawRenderedImage(image, null);
		graphics.dispose(); //liberaa toda a memória alocada para essa imagem
		return new_img;
	}

	 /*
            Pega o byte array da imagem
            @param image A imagem da qual será extraído os dados em byte
            @return Retorna o byte array para a imagem enviada
            @see Raster
            @see WritableRaster
            @see DataBufferByte
	 */
	private static byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}

	 /*
            Converte integer para byte
            @param i A integer a ser convertida
            @return Retorna um array byte[4], convertendo a integer enviada para bytes
	 */
	private static byte[] bit_conversion(int i)
	{
		//integers (ints) para bytes
		//byte byte7 = (byte)((i & 0xFF00000000000000L) >>> 56);
		//byte byte6 = (byte)((i & 0x00FF000000000000L) >>> 48);
		//byte byte5 = (byte)((i & 0x0000FF0000000000L) >>> 40);
		//byte byte4 = (byte)((i & 0x000000FF00000000L) >>> 32);

		//usando só 4 bytes
		byte byte3 = (byte)((i & 0xFF000000) >>> 24); //0
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); //0
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); //0
		byte byte0 = (byte)((i & 0x000000FF)       );
		//{0,0,0,byte0} é equivalente, já que todas as trocas >=8 serão 0
		return(new byte[]{byte3,byte2,byte1,byte0});
	}

	 /*
            Encripta um array de bytes dentro de outro array de bytes, num offset fornecido
            @param image     Array de dados representando uma imagem
            @param addition Array de dados para adicionar ao array de imagem fornecido
            @param offset      O offset no array da imagem para adicionar os dados
            @return Returns data Array merge da imagem e dados
	 */
	private static byte[] encode_text(byte[] image, byte[] addition, int offset)
	{
		//verifica se dados + offset caberá na imagem
		if(addition.length + offset > image.length)
		{
			throw new IllegalArgumentException("Imagem não é grande o suficiente!");
		}
		//loop em cada byte adicionado
		for(int i=0; i<addition.length; ++i)
		{
			//loop nos 8 bits de cada byte
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset)
			{
				int b = (add >>> bit) & 1;
				image[offset] = (byte)((image[offset] & 0xFE) | b );
			}
		}
		return image;
	}

	 /*
            Recupera o texto escondido na imagem
            @param image Array de dados, representando uma imagem
            @return Array de dados, que contém o texto escondido
	 */
	private static byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		//loop nos 32 bytes de dados para determinar length do texto
		for(int i=0; i<32; ++i)
		{
			length = (length << 1) | (image[i] & 1);
		}

		byte[] result = new byte[length];

		//loop em cada byte do texto
		for(int b=0; b<result.length; ++b )
		{
			//loop em cada bit dos bytes do texto
			for(int i=0; i<8; ++i, ++offset)
			{
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));

			}
		}
		return result;
	}
}
