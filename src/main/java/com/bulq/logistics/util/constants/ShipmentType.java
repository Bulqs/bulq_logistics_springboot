package com.bulq.logistics.util.constants;

public enum ShipmentType {
    EXPRESS("EXPRESS"),
    STANDARD("STANDARD");

    private final String shipmentType;
    
    // Constructor to assign the string value to each enum constant
    ShipmentType(String shipmentType){
        this.shipmentType = shipmentType;
    }

    // Getter to access the description
    public String getDescription(){
        return shipmentType;
    }

    // Static method to map a string from the payload to the enum constant
    public static ShipmentType fromValue(String value) {
        for (ShipmentType catalog : ShipmentType.values()) {
            if (catalog.name().equalsIgnoreCase(value)) {
                return catalog;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
