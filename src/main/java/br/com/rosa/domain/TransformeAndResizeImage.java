package br.com.rosa.domain;

import br.com.rosa.infra.exceptions.ValidacaoException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class TransformeAndResizeImage {
    
    public static byte[] saveImgItem(MultipartFile file) {

        try {

            //Lê a imagem
            BufferedImage imageOrigin = ImageIO.read(file.getInputStream());

            int imageWidth = 200;
            int imageHeight = 200;

            //Redimensiona a imagem com a largura e altura desejada
            BufferedImage imagemRedimensionada = resizeImage(imageOrigin, imageWidth, imageHeight);

            //Desenha a imagem com sua altura e largura definidas
            Graphics2D g = imagemRedimensionada.createGraphics();
            g.drawImage(imageOrigin, 0, 0, imageWidth, imageHeight, null);
            g.dispose();

            ByteArrayOutputStream img = new ByteArrayOutputStream();
            ImageIO.write(imagemRedimensionada, "png", img);

            return img.toByteArray();

        } catch (Exception e) {
            throw new ValidacaoException("Formatação de imagem errada");
        }
    }

    public static String takeImage(byte[] imgBlob) {
        return Base64.getEncoder().encodeToString(imgBlob);
    }

    private static BufferedImage resizeImage(BufferedImage imageOriginal, int width, int height) {
        Image resizedImage = imageOriginal.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Desenha a imagem redimensionada
        Graphics2D g = newImage.createGraphics();
        g.drawImage(resizedImage, 0, 0, null);
        g.dispose();

        return newImage;
    }

}
