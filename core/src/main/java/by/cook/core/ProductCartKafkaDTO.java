package by.cook.core;

import java.util.UUID;

public class ProductCartKafkaDTO {
    private UUID id;
    private UUID productId;
    private String productName;
    private Long price;
    private String details;
    private String imageURL;
    private UUID cartId;
    private Long quantity;
    public ProductCartKafkaDTO(){}
    public ProductCartKafkaDTO(UUID id, UUID productId, String productName, Long price, String details,
                               String imageURL, UUID cartId, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.details = details;
        this.imageURL = imageURL;
        this.cartId = cartId;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
