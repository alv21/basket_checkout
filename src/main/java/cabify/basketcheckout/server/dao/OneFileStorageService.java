package cabify.basketcheckout.server.dao;

import cabify.basketcheckout.server.model.Basket;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores all the baskets in one file (dusabled)
 */
//@Service
@Deprecated
public class OneFileStorageService implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(OneFileStorageService.class);

    private final File file = new File("baskets.json");
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Basket readBasket(String basketId) {
        try {
            List<Basket> baskets = mapper.readValue(file, new TypeReference<List<Basket>>() {
            });
            return retrieveBasket(baskets, basketId);
        } catch (IOException e) {
            logger.error("Error reading the basket", e);
            return null;
        }
    }

    @Override
    public boolean writeBasket(Basket basket) {
        List<Basket> baskets = readBaskets();
        baskets.remove(basket);
        baskets.add(basket);
        try {
            mapper.writeValue(file, baskets);
        } catch (IOException e) {
            logger.error("Error writing the basket", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean removeBasket(String basketId) {
        List<Basket> baskets = readBaskets();
        Basket basket = retrieveBasket(baskets, basketId);
        baskets.remove(basket);
        try {
            mapper.writeValue(file, baskets);
        } catch (IOException e) {
            logger.error("Error writing the basket", e);
            return false;
        }

        return true;
    }

    private Basket retrieveBasket(List<Basket> baskets, String basketId) {
        return baskets.stream()
                .filter(i -> i.getId().equals(basketId))
                .findFirst()
                .orElse(null);
    }

    private List<Basket> readBaskets() {
        List<Basket> baskets = new LinkedList<>();

        try {
            if (file.exists() && file.length() != 0)
                baskets = mapper.readValue(file, new TypeReference<List<Basket>>() {
                });
        } catch (IOException e) {
            logger.error("Error reading the baskets", e);
            throw new RuntimeException(e);
        }
        return baskets;
    }

}
