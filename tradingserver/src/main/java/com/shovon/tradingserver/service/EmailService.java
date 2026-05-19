package com.shovon.tradingserver.service;


import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  public void sendOtpEmail(String email, String otp, String otpType) throws Exception {
        ClassPathResource resource = new ClassPathResource("static/otp_template.html");

        String htmlContent = new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        htmlContent = htmlContent.replace("TradeForge_otp", otp);
        htmlContent = htmlContent.replace("TradeForge_otp2", otpType);

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("Your TradeForge OTP");
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

}
