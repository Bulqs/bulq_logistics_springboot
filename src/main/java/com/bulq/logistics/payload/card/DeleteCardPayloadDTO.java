package com.bulq.logistics.payload.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCardPayloadDTO {
    
    @Schema(description = "input card id", defaultValue = "1")
    private Long cardId;
}
