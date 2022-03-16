package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.service.PictureService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/picture")
public class PictureController {

    private final Logger logger = LoggerFactory.getLogger(PictureController.class);

    private PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/{pictureId}")
    public void downloadPicture(@PathVariable("pictureId") UUID pictureId,
                                HttpServletResponse response) throws IOException {
        logger.info("Downloading picture with id='{}'", pictureId);
        Optional<String> opt = pictureService.getPictureContentTypeById(pictureId);

        if (opt.isPresent()) {
            response.setContentType(opt.get());
            response.getOutputStream()
                    .write(pictureService.downloadPictureById(pictureId));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}/{pictureId}")
    public String deletePicture(@PathVariable("productId") UUID productID,
                                @PathVariable("pictureId") UUID pictureId) {
        logger.info("Deleting picture with id='{}'", pictureId);
        pictureService.deletePictureById(pictureId);
        return "redirect:/product/" + productID;
    }
}
