package com.bulq.logistics.util.constants;

public enum Status {
    NEW("New Order"),//'completed',
    COMPLETED("Order delivered successfully"),//'completed'
    PENDING("Pending order for delivery"),//,'pending'
    CANCELLED("Order Cancelled");//'cancelled'

    private final String status;
    
    // Constructor to assign the string value to each enum constant
    Status(String status){
        this.status = status;
    }

    // Getter to access the description
    public String getDescription(){
        return status;
    }

    // Static method to map a string from the payload to the enum constant
    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
