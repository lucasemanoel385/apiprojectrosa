package br.com.rosa.controller;

import br.com.rosa.domain.item.RepositoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@RequestMapping("image")
@RestController
public class ImageController {

    @Autowired
    private RepositoryItem repositoryItem;

    @GetMapping("/view/{cod}")
    public ResponseEntity<byte[]> getImageByCodItem(@PathVariable Long cod) throws IOException {

        byte[] imageBytes = repositoryItem.getImgByCod(cod);
        if (imageBytes == null) {
            return ResponseEntity.ok().body(null);
        }
        InputStream is = new ByteArrayInputStream(imageBytes);
        String contentType = URLConnection.guessContentTypeFromStream(is);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }


}
