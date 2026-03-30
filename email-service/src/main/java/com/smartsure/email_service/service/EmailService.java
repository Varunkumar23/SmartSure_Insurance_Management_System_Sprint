package com.smartsure.email_service.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String policyName) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("SmartSure Insurance - Policy Confirmation ✅");

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;'>"

                            + "<div style='max-width: 600px; margin: auto; background: white; border-radius: 10px; overflow: hidden;'>"

                            // Header
                            + "<div style='background: #0d6efd; color: white; padding: 20px; text-align: center;'>"
                            + "<h2>SmartSure Insurance</h2>"
                            + "<p>Your trusted insurance partner</p>"
                            + "</div>"

                            // Body
                            + "<div style='padding: 20px;'>"
                            + "<h3 style='color: #333;'>Policy Purchased Successfully 🎉</h3>"
                            + "<p>Dear Customer,</p>"
                            + "<p>We are delighted to inform you that your policy has been successfully activated.</p>"

                            + "<div style='background: #f1f3f5; padding: 15px; border-radius: 8px;'>"
                            + "<strong>Policy Name:</strong> " + policyName + "<br>"
                            + "<strong>Status:</strong> Active ✅"
                            + "</div>"

                            + "<p style='margin-top: 15px;'>You can now enjoy the benefits and protection offered under this policy.</p>"

                            + "<p>If you have any questions, feel free to contact our support team.</p>"

                            + "<p style='margin-top: 20px;'>Regards,<br><strong>SmartSure Team</strong></p>"
                            + "</div>"

                            // Footer
                            + "<div style='background: #f8f9fa; text-align: center; padding: 15px; font-size: 12px; color: #777;'>"
                            + "© 2026 SmartSure Insurance. All rights reserved."
                            + "</div>"

                            + "</div>"
                            + "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);

            System.out.println("🔥 Professional email sent successfully!");

        } catch (Exception e) {
            System.out.println("❌ Email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}