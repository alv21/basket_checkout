package cabify.basketcheckout.server.model;

import java.util.List;

public class VoucherDiscount implements Discount {

    private final int itemsToGetOneFree;

    public VoucherDiscount(int itemsToGetOneFree) {
        this.itemsToGetOneFree = itemsToGetOneFree;
    }

    @Override
    public double getDiscount(List<Product> products) {
        double discount = 0;

        long nVoucher = products.stream()
                .filter(p -> p == Product.VOUCHER)
                .count();

        discount = Math.floor(nVoucher / itemsToGetOneFree) * Product.VOUCHER.getPrice();

        return discount;
    }
}
