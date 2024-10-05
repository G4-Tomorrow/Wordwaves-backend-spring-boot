package com.server.wordwaves.service.implement;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.server.wordwaves.dto.model.RecipientModel;
import com.server.wordwaves.dto.model.SenderModel;
import com.server.wordwaves.dto.request.common.EmailRequest;
import com.server.wordwaves.dto.response.common.EmailResponse;
import com.server.wordwaves.entity.User;
import com.server.wordwaves.repository.httpclient.EmailClient;
import com.server.wordwaves.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
//@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImp implements EmailService {
    EmailClient emailClient;
    TemplateEngine templateEngine;

    @NonFinal
    @Value("${app.email-client.brevo-apikey}")
    String apiKey;

    @NonFinal
    @Value("${app.email-client.sender.email}")
    String senderMail;

    @NonFinal
    @Value("${app.email-client.sender.name}")
    String senderName;

    @NonFinal
    @Value("${jwt.access-signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    protected long accessTokenExpiration = 120;

    public EmailServiceImp(EmailClient emailClient, TemplateEngine templateEngine,
                           @Value("${app.email-client.brevo-apikey}") String apiKey,
                           @Value("${app.email-client.sender.email}") String senderMail,
                           @Value("${app.email-client.sender.name}") String senderName,
                           @Value("${jwt.access-signer-key}") String SIGNER_KEY) {
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
        this.apiKey = apiKey;
        this.senderMail = senderMail;
        this.senderName = senderName;
        this.SIGNER_KEY = SIGNER_KEY;

        // Thêm log để kiểm tra giá trị apiKey
        log.info("API Key: {}", this.apiKey);
    }

    private void sendEmail(User user, String token, String subject, String templateName) {
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        context.setVariable("token", token);

        String htmlContent = templateEngine.process(templateName, context);

        EmailRequest emailRequest = EmailRequest.builder()
                .sender(SenderModel.builder().email(senderMail).name(senderName).build())
                .to(List.of(RecipientModel.builder().email(user.getEmail()).build()))
                .subject(subject)
                .htmlContent(htmlContent)
                .build();

        emailClient.sendEmail(apiKey, emailRequest);
    }

    @Override
    public EmailResponse sendVerifyEmail(User user) {
        String token = generateEmailVerifyToken(user);
        sendEmail(user, token, "Xác thực tài khoản", "register-template");
        return EmailResponse.builder().messageId("Email đã được gửi thành công").build();
    }

    @Override
    public EmailResponse sendForgotPasswordEmail(User user) {
        String token = generateEmailVerifyToken(user);
        sendEmail(user, token, "Yêu cầu đặt lại mật khẩu", "forgot-password-template");
        return EmailResponse.builder().messageId("Email đã được gửi thành công").build();
    }

    public String generateEmailVerifyToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(accessTokenExpiration, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("ps", user.getPassword())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Không thể tạo token", e);
            throw new RuntimeException(e);
        }
    }
}
