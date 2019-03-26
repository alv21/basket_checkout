package cabify.basketcheckout.server.web;

import cabify.basketcheckout.server.model.Basket;
import cabify.basketcheckout.server.model.DiscountsAvailable;
import cabify.basketcheckout.server.model.Product;
import cabify.basketcheckout.server.repository.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BasketController {

    private final Logger logger = LoggerFactory.getLogger(BasketController.class);

    private final StorageService<Basket> storageService;
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
        try {
            storageService.write(new Basket(basketId));
        } catch (Exception e) {
            logger.error("Error creating new basket", e);
            return null;
        }

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

        Basket basket;
        try {
            basket = storageService.read(basketId);
        } catch (Exception e) {
            logger.error(String.format("Error reading the basket %s", basketId));
            return false;
        }

        if (basket == null) {
            logger.error(String.format("Basket %s not found", basketId));
            return false;
        }
        basket.addProduct(product);

        try {
            storageService.write(basket);
        } catch (Exception e) {
            logger.error(String.format("Error writing the basket %s", basketId));
            return false;
        }

        logger.info(String.format("Product %s added to basket %s", productCode, basketId));
        return true;
    }

    /**
     * Returns the sum of the products prices in the basket
     */
    @RequestMapping("/total/{basketId}")
    public synchronized double retrieveTotal(@PathVariable String basketId) {
        Basket basket;
        try {
            basket = storageService.read(basketId);
        } catch (Exception e) {
            logger.error(String.format("Error reading the basket %s", basketId));
            return -1;
        }

        if (basket == null) {
            logger.error(String.format("Basket %s not found", basketId));
            return -1;
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
        try {
            storageService.remove(basketId);
        } catch (Exception e) {
            logger.error(String.format("Error removing the basket %s", basketId));
            return false;
        }

        logger.info(String.format("Basket %s removed", basketId));
        return true;
    }


}