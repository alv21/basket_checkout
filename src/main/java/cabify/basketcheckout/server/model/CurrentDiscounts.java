package cabify.basketcheckout.server.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CurrentDiscounts implements DiscountsAvailable {

    @Override
    public List<Discount> getDiscounts() {
        return Arrays.asList(
                new TshirtDiscount(3, 1),
                new VoucherDiscount(2)
        );
    }

}
