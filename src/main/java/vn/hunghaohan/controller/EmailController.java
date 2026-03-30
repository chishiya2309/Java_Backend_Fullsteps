package vn.hunghaohan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.hunghaohan.service.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-CONTROLLER")
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/send")
    public void send(@RequestParam String to, @RequestParam String subject, @RequestParam String content) {
        log.info("Sending email to {}", to);
        emailService.send(to, subject, content);
        log.info("Email sent");
    }
}
