package cabify.basketcheckout.server.repository;

import cabify.basketcheckout.server.model.Basket;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OneFilePerBasketStorageService implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(OneFilePerBasketStorageService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Basket readBasket(String basketId) {
        File file = new File(basketId);
        try {
            logger.debug(String.format("Reading basket %s", basketId));
            if (file.exists())
                return mapper.readValue(file, new TypeReference<Basket>() {
                });
        } catch (Exception e) {
            logger.error("Error reading the basket " + basketId, e);
        }

        return null;
    }

    @Override
    public boolean writeBasket(Basket basket) {
        String basketId = "";
        try {
            basketId = basket.getId();
            logger.debug(String.format("Storing basket %s", basketId));
            mapper.writeValue(new File(basketId), basket);

            return true;
        } catch (Exception e) {
            logger.error("Error writing the basket " + basketId, e);
        }

        return false;
    }

    @Override
    public boolean removeBasket(String basketId) {
        try {
            logger.debug(String.format("Removing basket %s", basketId));
            File file = new File(basketId);
            file.delete();

            return true;
        } catch (Exception e) {
            logger.error("Error removing the basket " + basketId, e);
        }

        return false;
    }
}
