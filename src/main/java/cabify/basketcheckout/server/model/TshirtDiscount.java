package cabify.basketcheckout.server.model;

import java.util.List;

public class TshirtDiscount implements Discount {

    private final int bulkQuantity;
    private final double priceReduction;

    public TshirtDiscount(int bulkQuantity, double priceReduction) {
        this.bulkQuantity = bulkQuantity;
        this.priceReduction = priceReduction;
    }

    @Override
    public double getDiscount(List<Product> products) {
        long nTshirt = products.stream()
                .filter(p -> p == Product.TSHIRT)
                .count();

        if (nTshirt >= bulkQuantity) {
            return priceReduction * nTshirt;
        }

        return 0;
    }
}
