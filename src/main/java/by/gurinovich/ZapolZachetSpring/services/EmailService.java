package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.props.MailProperties;
import freemarker.template.Configuration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final Configuration configuration;
    private final UserService userService;

    @SneakyThrows
    public void sendFile(String filePath){
        String email = userService.getAuthenticatedUser().getEmail();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                MimeMessage message = mailSender.createMimeMessage();
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setSubject("Успеваемость");
                    helper.setFrom(mailProperties.getUsername());
                    helper.setTo(email);
                    helper.setText("Успеваемость на "+ new Date(System.currentTimeMillis()));
                    helper.addAttachment("file.xls", file);
                    mailSender.send(message);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        th.start();
    }

    @SneakyThrows
    public void sendRegistrationEmailMessage(User user, Properties properties){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                MimeMessage message = mailSender.createMimeMessage();
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
                    helper.setFrom(mailProperties.getUsername());
                    helper.setTo(user.getEmail());
                    helper.setSubject("Thank you for registration, dear" + user.getUsername());
                    String emailContent = getEmailMessageForRegistration(user);
                    helper.setText(emailContent, true);
                    mailSender.send(message);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        th.start();
    }

    @SneakyThrows
    public void sendPasswordResetMessage(User user, Properties properties){
        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                MimeMessage message = mailSender.createMimeMessage();
                try {
                    System.out.println("start");
                    MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
                    helper.setFrom(mailProperties.getUsername());
                    helper.setTo(user.getEmail());
                    helper.setSubject("Reset your password");
                    String emailContent = getEmailMessageForPasswordReset(user);
                    helper.setText(emailContent, true);
                    mailSender.send(message);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        th2.start();
    }

    @SneakyThrows
    public String getEmailMessageForRegistration(User user){
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("code", user.getConfirmationCode());
        configuration.getTemplate("auth_v2/registration.flth")
                .process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @SneakyThrows
    public String getEmailMessageForPasswordReset(User user){
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("uuid", user.getUUID());
        model.put("link", mailProperties.getResetLink());
        configuration.getTemplate("auth_v2/password_reset.flth")
                .process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
