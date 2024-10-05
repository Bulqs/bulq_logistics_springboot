package com.bulq.logistics.util.constants;

public enum PickupType {
    M2A("MeToAnother"),//MeToAnother
    A2M("AnotherToMe"),//AnotherToMe
    SA2("Specific Address to Specific Address"),//Specific Address to Specific Address
    PUO("PickupOnly"); //PickupOnly

    private final String pickupType;
    
    // Constructor to assign the string value to each enum constant
    PickupType(String pickupType){
        this.pickupType = pickupType;
    }

    // Getter to access the description
    public String getDescription(){
        return pickupType;
    }

    // Static method to map a string from the payload to the enum constant
    public static PickupType fromValue(String value) {
        for (PickupType catalog : PickupType.values()) {
            if (catalog.name().equalsIgnoreCase(value)) {
                return catalog;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
