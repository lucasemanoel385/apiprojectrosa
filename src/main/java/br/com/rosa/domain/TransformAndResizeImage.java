package br.com.rosa.domain;

import br.com.rosa.infra.exceptions.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class TransformAndResizeImage {
    
    public static byte[] saveImgItem(MultipartFile file) {

        try {

            //Lê a imagem
            BufferedImage imageOrigin = ImageIO.read(file.getInputStream());

            int imageWidth = 400;
            int imageHeight = 550;

            //Redimensiona a imagem com a largura e altura desejada
            BufferedImage imgResized = resizeImage(imageOrigin, imageWidth, imageHeight);

            //Desenha a imagem com sua altura e largura definidas
            Graphics2D g = imgResized.createGraphics();
            g.drawImage(imageOrigin, 0, 0, imageWidth, imageHeight, null);
            g.dispose();

            ByteArrayOutputStream img = new ByteArrayOutputStream();
            ImageIO.write(imgResized, "png", img);

            return img.toByteArray();

        } catch (Exception e) {
            throw new ValidationException("Formatação de imagem errada");
        }
    }

    public static String takeImage(byte[] imgBlob) {
        return imgBlob != null ? Base64.getEncoder().encodeToString(imgBlob) : null;
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
