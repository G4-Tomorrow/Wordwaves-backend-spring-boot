package com.server.wordwaves.dto.request;

import java.util.List;

import com.server.wordwaves.dto.model.RecipientModel;
import com.server.wordwaves.dto.model.SenderModel;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    SenderModel sender;
    List<RecipientModel> to;
    String htmlContent;
    String subject;
}
