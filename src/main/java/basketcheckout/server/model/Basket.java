package cabify.basketcheckout.server.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Basket implements Serializable {

    private String id;
    private List<Product> products;

    public Basket() {
    }

    public Basket(String id, List<Product> products) {
        this.id = id;
        this.products = products;
    }

    public Basket(String id) {
        this(id, Collections.emptyList());
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Basket basket = (Basket) o;
        return id.equals(basket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
