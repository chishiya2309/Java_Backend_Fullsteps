package vn.hunghaohan.service;

public interface EmailService {
    public void send(String to, String subject, String text);

    public void emailVerification(String to, String name);

    public void emailVerification(String to, String name, String secretCode);
}
