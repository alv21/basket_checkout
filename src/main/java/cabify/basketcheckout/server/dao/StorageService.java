package cabify.basketcheckout.server.dao;

import cabify.basketcheckout.server.model.Basket;

import java.io.IOException;

public interface StorageService {

    public Basket readBasket(String basketId);

    public boolean writeBasket(Basket basket);

    public boolean removeBasket(String basketId);
}
