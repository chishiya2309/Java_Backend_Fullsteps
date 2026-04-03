package vn.hunghaohan.service.impl;

import brevo.ApiClient;
import brevo.ApiException;
import brevo.Configuration;
import brevoApi.TransactionalEmailsApi;
import brevoModel.CreateSmtpEmail;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.hunghaohan.config.BrevoProperties;
import vn.hunghaohan.service.EmailService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BREVO-EMAIL-SERVICE")
public class BrevoEmailService implements EmailService {

    private static final String DEFAULT_FROM_NAME = "Backend Service";

    private final BrevoProperties brevoProperties;

    /**
     * Send email by brevo
     * @param to sendEmail to someone
     * @param subject email subject
     * @param text plain text content
     */
    @Override
    public void send(String to, String subject, String text) {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        apiClient.setApiKey(Objects.requireNonNull(brevoProperties.getApiKey(), "Brevo API key is required"));

        TransactionalEmailsApi transactionalEmailsApi = new TransactionalEmailsApi(apiClient);

        SendSmtpEmailTo recipient = new SendSmtpEmailTo().email(to);
        SendSmtpEmailSender sender = new SendSmtpEmailSender()
                .email(Objects.requireNonNull(brevoProperties.getFromEmail(), "Brevo from-email is required"))
                .name(Objects.requireNonNullElse(brevoProperties.getFromName(), DEFAULT_FROM_NAME));

        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail()
                .sender(sender)
                .to(List.of(recipient))
                .subject(subject)
                .textContent(text);

        try {
            CreateSmtpEmail response = transactionalEmailsApi.sendTransacEmail(sendSmtpEmail);
            log.info("Email sent successfully. messageId={}", response.getMessageId());
        } catch (ApiException exception) {
            log.error("Failed to send email via Brevo. status={}, body={}", exception.getCode(), exception.getResponseBody(), exception);
            throw new RuntimeException("Failed to send email via Brevo", exception);
        }
    }

    /**
     * Email verification by Brevo
     */
    @Override
    public void emailVerification(String to, String name) {
        emailVerification(to, name, UUID.randomUUID().toString());
    }

    @Override
    public void emailVerification(String to, String name, String secretCode) {
        log.info("Email verification started for email: {}", to);

        // Khởi tạo cấu hình API client
        ApiClient apiClient = Configuration.getDefaultApiClient();
        apiClient.setApiKey(Objects.requireNonNull(brevoProperties.getApiKey(),"Brevo API key is required"));

        TransactionalEmailsApi transactionalEmailsApi = new TransactionalEmailsApi(apiClient);

        // 2. Cấu hình người nhận
        SendSmtpEmailTo recipient = new SendSmtpEmailTo().email(to).name(name);
        String verificationToken = String.format("?secretCode=%s", secretCode);

        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("verify_link", Objects.requireNonNull(brevoProperties.getVerificationLink()) + verificationToken); // Khớp với {{ params.verify_link }}
        params.put("firstName", name);

        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail()
                .to(List.of(recipient))
                .templateId(Objects.requireNonNull(brevoProperties.getTemplateIdEmailVerification(), "Brevo verification template ID is required"))
                .params(params);

        try {
            CreateSmtpEmail response = transactionalEmailsApi.sendTransacEmail(sendSmtpEmail);
            log.info("Verification email sent successfully. messageId={}", response.getMessageId());
        } catch (ApiException exception) {
            log.error("Failed to send verification email via Brevo. status={}, body={}",
                    exception.getCode(), exception.getResponseBody(), exception);
            throw new RuntimeException("Failed to send verification email via Brevo", exception);
        }
    }
}
