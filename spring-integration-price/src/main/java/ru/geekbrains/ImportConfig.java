package ru.geekbrains;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.dsl.JpaUpdatingOutboundEndpointSpec;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.messaging.MessageHandler;
import ru.geekbrains.dto.CsvProduct;
import ru.geekbrains.persist.Brand;
import ru.geekbrains.persist.Category;
import ru.geekbrains.persist.Picture;
import ru.geekbrains.persist.Product;
import ru.geekbrains.service.PictureService;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ImportConfig {

    private static final Logger logger = LoggerFactory.getLogger(ImportConfig.class);

    @Value("${source.directory.path}")
    private String sourcePath;

    @Value("${dest.directory.path}")
    private String destPath;

    private EntityManagerFactory entityManagerFactory;

    private PictureService pictureService;

    @Autowired
    public ImportConfig(EntityManagerFactory entityManagerFactory, PictureService pictureService) {
        this.entityManagerFactory = entityManagerFactory;
        this.pictureService = pictureService;
    }

    @Bean
    public MessageSource<File> sourceDirectory() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(sourcePath));
        messageSource.setAutoCreateDirectory(true);

        return messageSource;
    }

    @Bean
    public MessageHandler destDirectory() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(destPath));
        handler.setExpectReply(false);
        handler.setDeleteSourceFiles(true);

        return handler;
    }

    @Bean
    public JpaUpdatingOutboundEndpointSpec jpaPersistHandler() {
        return Jpa.outboundAdapter(entityManagerFactory)
                .entityClass(Product.class)
                .persistMode(PersistMode.MERGE);
    }

    @Bean
    public IntegrationFlow filePriceImportFlow() {
        return IntegrationFlows.from(sourceDirectory(), conf -> conf.poller(Pollers.fixedDelay(2000)))
                .filter(msg -> ((File) msg).getName().endsWith(".csv"))
                .transform(this::getCsvProductsFromFile)
                .split()
                .transform(this::getProductFromCsvProduct)
                .handle(jpaPersistHandler(), ConsumerEndpointSpec::transactional)
                .get();
    }

    private List<CsvProduct> getCsvProductsFromFile(File file) {
        List<CsvProduct> csvProducts = null;
        try {
            csvProducts = new CsvToBeanBuilder<CsvProduct>(new FileReader(file))
                    .withType(CsvProduct.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        }

        return csvProducts;
    }

    private Product getProductFromCsvProduct(CsvProduct csvProduct) {
        Product product = new Product(
                csvProduct.getId(),
                csvProduct.getTitle(),
                csvProduct.getCost(),
                csvProduct.getDescription(),
                new Category(csvProduct.getCsvCategory().getId(), csvProduct.getCsvCategory().getTitle()),
                new Brand(csvProduct.getCsvBrand().getId(), csvProduct.getCsvBrand().getTitle())
        );
        List<Picture> pictures = createPictures(product, csvProduct.getPictureNames());
        pictures.forEach(product::addPicture);
        return product;
    }

    private List<Picture> createPictures(Product product, List<String> fileName) {
        List<Picture> pictures = new ArrayList<>();
        fileName.forEach(f -> {
            Picture picture = null;
            try {
                Path path = Path.of(sourcePath, f);
                byte[] pictureFile = Files.readAllBytes(path);
                String storageUUID = pictureService.uploadPicture(pictureFile);
                picture = new Picture(f, Files.probeContentType(path), storageUUID);
            } catch (IOException e) {
                logger.error("File '{}' not found", f);
            }
            pictures.add(picture);
        });
        return pictures;
    }
}
