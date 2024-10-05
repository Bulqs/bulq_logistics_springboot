package com.bulq.logistics.util.constants;

public enum ComplaintType {
    OPEN("Complaint Open"),//Specific Address to Specific Adress
    PENDING("Complaint pending resolution"),//Another to Me
    RESOLVED("Complaint resolved"),//Me to Another
    CLOSED("Complaint closed");//Pick Up Only

    private final String complaintType;
    
    // Constructor to assign the string value to each enum constant
    ComplaintType(String complaintType){
        this.complaintType = complaintType;
    }

    // Getter to access the description
    public String getDescription(){
        return complaintType;
    }

    // Static method to map a string from the payload to the enum constant
    public static ComplaintType fromValue(String value) {
        for (ComplaintType complaint : ComplaintType.values()) {
            if (complaint.name().equalsIgnoreCase(value)) {
                return complaint;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
