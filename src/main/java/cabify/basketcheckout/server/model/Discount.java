package cabify.basketcheckout.server.model;

import java.util.List;

public interface Discount {

    public double getDiscount(List<Product> products);

}
