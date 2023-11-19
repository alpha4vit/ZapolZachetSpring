package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.props.MailProperties;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @SneakyThrows
    public void sendFile(String filePath){
        File file = new File(filePath);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject("Успеваемость");
        helper.setFrom(mailProperties.getUsername());
        helper.setTo("gurinovi4rm@gmail.com");
        helper.setText("Успеваемость на "+ new Date(System.currentTimeMillis()));
        helper.addAttachment("file.xls", file);
        mailSender.send(message);
    }
}
