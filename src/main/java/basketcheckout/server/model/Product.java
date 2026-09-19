package cabify.basketcheckout.server.model;

public enum Product {

    VOUCHER("Cabify Voucher", 5f),
    TSHIRT("Cabify T-Shirt", 20f),
    MUG("Cabify Coffee Mug", 7.5f);

    private String description;
    private Float price;

    Product(String description, Float price) {
        this.description = description;
        this.price = price;
    }

    public Float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

}
