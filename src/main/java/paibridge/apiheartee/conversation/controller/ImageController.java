package paibridge.apiheartee.conversation.controller;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import paibridge.apiheartee.conversation.service.ImageService;

import java.io.IOException;

@RestController
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController (ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String processImage(@RequestParam(value="image")MultipartFile image) throws IOException, TesseractException {
        return this.imageService.processImage(image);
    }

}
