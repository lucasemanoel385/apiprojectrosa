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
            if (file == null) {
                return null;
            }

            // Lê a imagem original
            BufferedImage imageOrigin = ImageIO.read(file.getInputStream());

            if (imageOrigin == null) {
                throw new ValidationException("Formato de imagem inválido");
            }

            // Obtém largura e altura originais
            int originalWidth = imageOrigin.getWidth();
            int originalHeight = imageOrigin.getHeight();

            int targetWidth = 400;
            int targetHeight = 520;

            // Calcula a largura/altura proporcionalmente e pega o menor valor entre o height e o width
            double ratio = Math.min((double) targetWidth / originalWidth, (double) targetHeight / originalHeight);
            int newWidth = (int) (originalWidth * ratio);
            int newHeight = (int) (originalHeight * ratio);

            // Redimensiona mantendo a proporção
            BufferedImage imgResized = resizeImage(imageOrigin, newWidth, newHeight);

            // Cria uma nova imagem no tamanho desejado e centraliza a imagem redimensionada
            BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = finalImage.createGraphics();
            g.setColor(Color.WHITE); // Fundo branco
            g.fillRect(0, 0, targetWidth, targetHeight);
            g.drawImage(imgResized, (targetWidth - newWidth) / 2, (targetHeight - newHeight) / 2, null);
            g.dispose();

            // Converte para byte array
            ByteArrayOutputStream img = new ByteArrayOutputStream();
            ImageIO.write(finalImage, "png", img);

            return img.toByteArray();

        } catch (Exception e) {
            throw new ValidationException("Erro ao processar a imagem: " + e.getMessage());
        }
    }

    public static String takeImage(byte[] imgBlob) {
        return imgBlob != null ? Base64.getEncoder().encodeToString(imgBlob) : null;
    }

    private static BufferedImage resizeImage(BufferedImage imageOriginal, int width, int height) {
        // Scale_Smooth da um resultado amis suave ao reduzir o tamanho da imagem
        Image resizedImage = imageOriginal.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        // Interpolacao e melhor quando se redimensiona a imagem
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(resizedImage, 0, 0, null);
        g.dispose();
        return newImage;
    }
}
