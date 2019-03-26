package cabify.basketcheckout.server;

import cabify.basketcheckout.server.repository.StorageService;
import cabify.basketcheckout.server.model.Basket;
import cabify.basketcheckout.server.model.DiscountsAvailable;
import cabify.basketcheckout.server.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BasketController {

    private final Logger logger = LoggerFactory.getLogger(BasketController.class);

    private final StorageService storageService;
    private final DiscountsAvailable discountsAvailable;

    public BasketController(StorageService storageService, DiscountsAvailable discountsAvailable) {
        this.storageService = storageService;
        this.discountsAvailable = discountsAvailable;
    }

    /**
     * Adds a basket and returns its ID. Returns null if something went wrong
     */
    @RequestMapping("/new")
    public synchronized String newBasket() {
        String basketId = UUID.randomUUID().toString();
        Basket basket = new Basket(basketId);

        if (!storageService.writeBasket(new Basket(basketId)))
            return null;

        logger.info(String.format("New basket %s created", basketId));
        return basketId;
    }

    /**
     * Adds the product to the basket and returns true for success
     */
    @RequestMapping("/add/{productCode}/{basketId}")
    public synchronized boolean addProduct(@PathVariable String productCode, @PathVariable String basketId) {
        Product product = Product.valueOf(productCode);
        if (product == null) {
            logger.error(String.format("Product %s not found", productCode));
            return false;
        }

        Basket basket = storageService.readBasket(basketId);
        if (basket == null) {
            logger.error(String.format("Basket %s not found", basketId));
            return false;
        }
        basket.addProduct(product);

        if (!storageService.writeBasket(basket))
            return false;

        logger.info(String.format("Product %s added to basket %s", productCode, basketId));
        return true;
    }

    /**
     * Returns the sum of the products prices in the basket
     */
    @RequestMapping("/total/{basketId}")
    public synchronized double retrieveTotal(@PathVariable String basketId) {
        Basket basket = storageService.readBasket(basketId);
        if (basket == null) {
            logger.error(String.format("Basket %s not found", basketId));
            return 0;
        }

        double total = basket.getProducts()
                .stream()
                .mapToDouble(p -> p.getPrice())
                .sum();

        double discount = discountsAvailable.getDiscounts().stream()
                .mapToDouble(d -> d.getDiscount(basket.getProducts()))
                .sum();

        double finalTotal = total - discount;

        logger.info(String.format("Total %s retrieved for basket %s", finalTotal, basketId));
        return finalTotal;
    }

    /**
     * Removes the basket and returns true for success
     */
    @RequestMapping("/remove/{basketId}")
    public synchronized boolean renmoveBasket(@PathVariable String basketId) {
        if (!storageService.removeBasket(basketId))
            return false;

        logger.info(String.format("Basket %s removed", basketId));
        return true;
    }


}