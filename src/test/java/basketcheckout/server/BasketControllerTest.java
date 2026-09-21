package basketcheckout.server;

import basketcheckout.server.repository.StorageService;
import basketcheckout.server.model.Basket;
import basketcheckout.server.model.Product;
import basketcheckout.server.web.BasketController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.UUID;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BasketController.class, secure = false)
public class BasketControllerTest {

    @Autowired
    BasketController basketController;

    @Autowired
    StorageService<Basket> basketStorageService;

    @Test
    public void shouldAddAnewBasket() {
        String basketId = basketController.newBasket();
        try {
            UUID uuid = UUID.fromString(basketId);
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("Basket id " + basketId + " not valid", exception);
        }

        Basket basket = basketStorageService.read(basketId);
        Assert.isTrue(basket.getId().equals(basketId), String.format("Basket id %s doesn't match with %s", basketId, basket.getId()));

        basketController.removeBasket(basketId);
    }

    @Test
    public void shouldRetrieveTheRightTotal() throws InterruptedException {
        String basketId = basketController.newBasket();

        basketController.addProduct(Product.TSHIRT.toString(), basketId);
        basketController.addProduct(Product.MUG.toString(), basketId);
        basketController.addProduct(Product.TSHIRT.toString(), basketId);
        basketController.addProduct(Product.TSHIRT.toString(), basketId);
        basketController.addProduct(Product.VOUCHER.toString(), basketId);
        basketController.addProduct(Product.TSHIRT.toString(), basketId);
        basketController.addProduct(Product.VOUCHER.toString(), basketId);

        double total = basketController.retrieveTotal(basketId);
        Assert.isTrue(total == Product.MUG.getPrice()
                + 4 * Product.TSHIRT.getPrice() -4
                + 1 * Product.VOUCHER.getPrice(), "total " + total + " is not correct");

        basketController.removeBasket(basketId);
    }

    @Test
    public void shouldRemoveBasket() {
        String basketId = basketController.newBasket();
        basketController.removeBasket(basketId);

        Basket basket = basketStorageService.read(basketId);
        Assert.isNull(basket, "Basket should have been removed");
    }

}
