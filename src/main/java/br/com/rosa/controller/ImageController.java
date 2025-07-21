package br.com.rosa.controller;

import br.com.rosa.domain.TransformAndResizeImage;
import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.contract.validations.CheckItemIfAvaible;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import br.com.rosa.domain.itemContract.dto.CheckItemsDTO;
import br.com.rosa.domain.itemContract.dto.ItemsAvailableDTO;
import br.com.rosa.tool.ImgSaveAndGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("image")
@RestController
public class ImageController {

    @GetMapping("/upload/{nameFile}")
    public ResponseEntity<byte[]> getImagePoduto(@PathVariable String nameFile) throws IOException {

        var urlPath = ImgSaveAndGet.verImagem(nameFile);

        if (!Files.exists(urlPath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(urlPath);
        String contentType = Files.probeContentType(urlPath); // Ex: image/jpeg

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }

    @PostMapping("/upload")
    public ResponseEntity<Page<ItemsAvailableDTO>> saveImageProduct(@RequestParam("file") MultipartFile file) throws IOException {

        String appRoot = new File(".").getCanonicalPath(); // diretório do JAR
        String uploadDir = appRoot + File.separator + "uploads";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // cria se não existir
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok().build();
    }


}
