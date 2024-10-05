package com.bulq.logistics.util.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetailsWelcome {
    private String recipient;
    private String body;
    private String subject;
    private String firstName;
}
