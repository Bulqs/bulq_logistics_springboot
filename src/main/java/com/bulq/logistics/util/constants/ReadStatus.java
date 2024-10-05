package com.bulq.logistics.util.constants;

public enum ReadStatus {
    READ("MESSAGE NOT YET READ"),
    UNREAD("MESSAGE READ");

    private final String messageStatus;
    
    // Constructor to assign the string value to each enum constant
    ReadStatus(String messageStatus){
        this.messageStatus = messageStatus;
    }

    // Getter to access the description
    public String getDescription(){
        return messageStatus;
    }

    // Static method to map a string from the payload to the enum constant
    public static ReadStatus fromValue(String value) {
        for (ReadStatus status : ReadStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
