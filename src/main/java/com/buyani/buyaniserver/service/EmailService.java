package com.buyani.buyaniserver.service;
import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.buyani.buyaniserver.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {
  @Autowired
  private JavaMailSender mailSender;

  @Value("${base.url}")
  public String baseUrl;

  @Qualifier("email")
  @Autowired
  private TemplateEngine htmlTemplateEngine;

  @Value("${mail.server.name}")
  private String serverName;

  @Value("${mail.server.from}")
  private String serverFrom;

  public static final String USER_NAME         = "name";
  public static final String USER_WEBSITE_URL  = "websiteUrl";
  public static final String USER_REDIRECTION  = "redirection";
  public static final String USER_EMAIL        = "email";

  // ── Existing (unchanged) ─────────────────────────────────────────────────
  public ResponseEntity<Object> sendForgotPasswordEmail(User user, Locale locale, String code) throws MessagingException, IOException {
    log.info("Sending Forgot Password Email - begin");
    log.info("WEBSITE_URL: {}", baseUrl);
    log.info("Locale Variable: {}", locale);

    final Context           ctx          = new Context(locale);
    final MimeMessage       mimeMessage  = this.mailSender.createMimeMessage();
    final MimeMessageHelper msg          = new MimeMessageHelper(mimeMessage, "UTF-8");
    String emailSubject = "Hi, " + " " + user.getFirstName() + " " + user.getLastName() + ", you've requested for a password reset";

    String name             = user.getFirstName() + " " + user.getLastName();
    String createPasswordUrl = baseUrl + "/new-password/" + code;

    ctx.setVariable(USER_NAME, name);
    ctx.setVariable(USER_REDIRECTION, createPasswordUrl);

    msg.setSubject(emailSubject);
    msg.setFrom(serverName + " <" + serverFrom + ">");
    msg.setTo(user.getEmail());

    final String htmlContent = this.htmlTemplateEngine.process("forgot-password-template", ctx);
    msg.setText(htmlContent, true);

    HttpStatus status = null;
    try {
      log.info("Sending Email to User with userId {}...", user.getUserId());
      this.mailSender.send(mimeMessage);
      status = HttpStatus.OK;
      log.info("Email sent successfully!");
    } catch (Exception ex) {
      log.info("Failed to send email: " + ex.getMessage());
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    log.info("Sending Forgot Password Email - end");
    return new ResponseEntity<>(status);
  }

  // ── Existing (unchanged) ─────────────────────────────────────────────────
  public ResponseEntity<Object> createPassword(User user, Locale locale, String code) throws MessagingException, IOException {
    log.info("Sending Create Password Email - begin");

    final Context           ctx          = new Context(locale);
    final MimeMessage       mimeMessage  = this.mailSender.createMimeMessage();
    final MimeMessageHelper msg          = new MimeMessageHelper(mimeMessage, "UTF-8");
    String emailSubject = "Welcome to Parmassist,  " + " " + user.getFirstName() + " " + user.getLastName() + "!";

    String name             = user.getFirstName() + " " + user.getLastName();
    String createPasswordUrl = baseUrl + "/new-password/" + code;
    String email            = user.getEmail();

    ctx.setVariable(USER_NAME, name);
    ctx.setVariable("username", email);
    ctx.setVariable(USER_REDIRECTION, createPasswordUrl);

    msg.setSubject(emailSubject);
    msg.setFrom(serverName + " <" + serverFrom + ">");
    msg.setTo(user.getEmail());

    final String htmlContent = this.htmlTemplateEngine.process("create-password-template", ctx);
    msg.setText(htmlContent, true);

    HttpStatus status = null;
    try {
      log.info("Sending Email to User with userId {}...", user.getUserId());
      this.mailSender.send(mimeMessage);
      status = HttpStatus.OK;
      log.info("Email sent successfully!");
    } catch (Exception ex) {
      log.info("Failed to send email: " + ex.getMessage());
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    log.info("Sending Create Password Email - end");
    return new ResponseEntity<>(status);
  }

  // ── NEW: Send 6-digit email verification code ─────────────────────────────
  public void sendEmailVerification(String toEmail, String firstName, String code) throws MessagingException {
    log.info("Sending Email Verification - begin");

    final Context           ctx         = new Context(Locale.getDefault());
    final MimeMessage       mimeMessage = this.mailSender.createMimeMessage();
    final MimeMessageHelper msg         = new MimeMessageHelper(mimeMessage, "UTF-8");

    ctx.setVariable("name", firstName);
    ctx.setVariable("code", code);

    msg.setSubject("BuyAni — Your Email Verification Code");
    msg.setFrom(serverName + " <" + serverFrom + ">");
    msg.setTo(toEmail);

    final String htmlContent = this.htmlTemplateEngine.process("email-verification-template", ctx);
    msg.setText(htmlContent, true);

    try {
      log.info("Sending verification email to {}...", toEmail);
      this.mailSender.send(mimeMessage);
      log.info("Verification email sent successfully!");
    } catch (Exception ex) {
      log.error("Failed to send verification email: {}", ex.getMessage());
      throw new MessagingException("Failed to send verification email", ex);
    }

    log.info("Sending Email Verification - end");
  }
}
