package basketcheckout.server.model;

public enum Product {

    VOUCHER("MyCompany Voucher", 5f),
    TSHIRT("MyCompany T-Shirt", 20f),
    MUG("MyCompany Coffee Mug", 7.5f);

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
