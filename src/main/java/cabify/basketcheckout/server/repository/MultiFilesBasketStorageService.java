package cabify.basketcheckout.server.repository;

import cabify.basketcheckout.server.model.Basket;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MultiFilesBasketStorageService implements StorageService<Basket> {

    private final Logger logger = LoggerFactory.getLogger(MultiFilesBasketStorageService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Basket read(String basketId) {
        try {
            logger.debug(String.format("Reading basket %s", basketId));
            File file = new File(basketId);
            if (file.exists())
                return mapper.readValue(file, new TypeReference<Basket>() {
                });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void write(Basket basket) {
        try {
            String basketId = basket.getId();
            logger.debug(String.format("Storing basket %s", basketId));
            mapper.writeValue(new File(basketId), basket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String basketId) {
        try {
            logger.debug(String.format("Removing basket %s", basketId));
            File file = new File(basketId);
            file.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
