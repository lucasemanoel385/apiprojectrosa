package br.com.rosa.tool;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImgSaveAndGet {

    private static final String folderSave = "imagensProdutos";

    public static String saveImagemInFolder(MultipartFile file) throws IOException {

        String appRoot = new File(".").getCanonicalPath(); // diretório do JAR
        String uploadDir = appRoot + File.separator + folderSave;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // cria se não existir
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public static Path verImagem(String nameFile) throws IOException {
        String appRoot = new File(".").getCanonicalPath();
        Path urlPath = Paths.get(appRoot + "/" + folderSave + "/" + nameFile);

        if (!Files.exists(urlPath)) {
            return null;
        }

        return urlPath;
    }

    public static void deleteImg(String nameFile) throws IOException {
        String appRoot = new File(".").getCanonicalPath();
        Path caminho = Paths.get(appRoot + "/" + folderSave + "/" + nameFile);

        if (Files.exists(caminho)) {
            Files.delete(caminho);
        }
    }


}
