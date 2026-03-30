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
}
