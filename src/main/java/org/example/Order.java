package org.example;

import lombok.Data;

@Data
public class Order {
    int orderId;
    String product;
    int quantity;
}
