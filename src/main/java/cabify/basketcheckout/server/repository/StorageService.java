package cabify.basketcheckout.server.service;

import cabify.basketcheckout.server.model.Basket;

public interface StorageService {

    public Basket readBasket(String basketId);

    public boolean writeBasket(Basket basket);

    public boolean removeBasket(String basketId);
}
