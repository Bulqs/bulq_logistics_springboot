package com.bulq.logistics.util.constants;

public enum CancelDelivery {
    TRANSITION("Package currently in ttransit"),
    CANCEL("Delivery cancelled");

    private final String status;
    
    // Constructor to assign the string value to each enum constant
    CancelDelivery(String status){
        this.status = status;
    }

    // Getter to access the description
    public String getDescription(){
        return status;
    }

    // Static method to map a string from the payload to the enum constant
    public static CancelDelivery fromValue(String status) {
        for (CancelDelivery cancelStatus : CancelDelivery.values()) {
            if (cancelStatus.name().equalsIgnoreCase(status)) {
                return cancelStatus;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + status);
    }
}
