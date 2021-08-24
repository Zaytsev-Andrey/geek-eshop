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
    public void downloadProductPicture(@PathVariable("pictureId") Long pictureId,
                                       HttpServletResponse response) throws IOException {
        Optional<String> opt = pictureService.getPictureContentTypeById(pictureId);

        if (opt.isPresent()) {
            response.setContentType(opt.get());
            response.getOutputStream()
                    .write(pictureService.getPictureDataById(pictureId).get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}/{pictureId}")
    public String deletePicture(@PathVariable("productId") Long productID,
                                @PathVariable("pictureId") Long pictureId) {
        logger.info("Deleting picture with UUID: {}", pictureId);

        pictureService.deletePictureById(pictureId);

        return "redirect:/product/" + productID;
    }
}
