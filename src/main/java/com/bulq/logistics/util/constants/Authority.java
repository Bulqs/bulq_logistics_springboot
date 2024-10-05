package com.bulq.logistics.util.constants;

public enum Authority {
    USER, // can update, delete self object and read anything
    ADMIN, // can update, read, delete object any object
    BUSINESS
}
