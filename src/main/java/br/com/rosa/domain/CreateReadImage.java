package br.com.rosa.domain;

import br.com.rosa.infra.exceptions.ValidacaoException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class CreateReadImage {

    private static final String destinationFolder =
            System.getProperty("user.dir") + "\\src\\main\\resources\\assetProducts\\";
            //"C:\\Users\\lucas\\Documents\\tudo de programação\\ProjetoRosa\\rosa\\src\\main\\java\\assetItem\\";

    public static String updateImgItem(MultipartFile file) {

        var nameImage = file.getOriginalFilename().toLowerCase();
        var urlImg = destinationFolder + nameImage;

        if(Files.exists(Paths.get(urlImg))) {
            return nameImage;
        }
        return saveImgItem(file);

    }

    public static String saveImgItem(MultipartFile file) {

        //Dados da pasta e nome do arquivo
        var nameImage = file.getOriginalFilename().toLowerCase();
        var urlImg = destinationFolder + nameImage;

        try {

            //Cria a imagem na pastida destinada
            Files.write(Paths.get(urlImg), file.getBytes());

            //Lê a imagem
            BufferedImage imageOrigin = ImageIO.read(new File(urlImg));

            int imageWidth = 200;
            int imageHeight = 200;

            Files.delete(Paths.get(urlImg));

            //Redimensiona a imagem com a largura e altura desejada
            BufferedImage imagemRedimensionada = resizeImage(imageOrigin, imageWidth, imageHeight);

            //Desenha a imagem com sua altura e largura definidas
            Graphics2D g = imagemRedimensionada.createGraphics();
            g.drawImage(imageOrigin, 0, 0, imageWidth, imageHeight, null);
            g.dispose();

            //Salva a imagem redimensionada
            ImageIO.write(imagemRedimensionada, "png", new File(urlImg));
        } catch (Exception e) {
            throw new ValidacaoException("Formatação de imagem errada");

        }

        return nameImage;
    }

    public static String takeImage(String urlImg) {
        //Pega o caminho da pasta que contem o arquivo de cada item
        Path imgUrl = Paths.get(destinationFolder + urlImg.toLowerCase());
        byte[] img = null;
        String base64Image = null;
        try {
            //Pega o arquivo e tranforma em bytes
            img = Files.readAllBytes(imgUrl);

            //Transformamos o byte em String
            base64Image = Base64.getEncoder().encodeToString(img);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return base64Image;
    }

    private static BufferedImage resizeImage(BufferedImage imageOriginal, int width, int height) {
        Image resizedImage = imageOriginal.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Desenha a imagem redimensionada
        Graphics2D g = newImage.createGraphics();
        g.drawImage(resizedImage, 0, 0, null);
        g.dispose();

        return newImage;
    }

    public static void deleteImg(String url) {
        try {
            Files.delete(Paths.get(destinationFolder + url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
