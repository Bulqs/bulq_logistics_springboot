package com.bulq.logistics.util.constants;

public enum ServiceType {
    SAS("Specific Address to Specific Address"),//Specific Address to Specific Adress
    A2M("Another to me"),//Another to Me
    M2A("Me to Another"),//Me to Another
    PUO("Pick Up Only");//Pick Up Only

    private final String serviceType;
    
    // Constructor to assign the string value to each enum constant
    ServiceType(String serviceType){
        this.serviceType = serviceType;
    }

    // Getter to access the description
    public String getDescription(){
        return serviceType;
    }

    // Static method to map a string from the payload to the enum constant
    public static ServiceType fromValue(String value) {
        for (ServiceType catalog : ServiceType.values()) {
            if (catalog.name().equalsIgnoreCase(value)) {
                return catalog;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
