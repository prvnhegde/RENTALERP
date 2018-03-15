/*
 * Copyright 2016 Kwoksys
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kwoksys.framework.connections.mail;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.framework.util.StringUtils;

/**
 * SMTP class
 */
public class SmtpService {

    private static final Logger LOGGER = Logger.getLogger(SmtpService.class.getName());

    private SmtpConnection conn;
    
    public SmtpService() {
        conn = new SmtpConnection();
    }

    public SmtpService(SmtpConnection conn) {
        this.conn = conn;
    }

    public ActionMessages send(EmailMessage message) throws Exception {
        // Lots of email provider require pop-authenticated.
        Properties properties = new Properties();
        properties.put("mail.smtp.host", conn.getHost());
        properties.put("mail.smtp.port", conn.getPort());
        properties.put("mail.smtp.auth", String.valueOf(conn.isAuthRequired()));
        properties.put("mail.smtp.timeout", "4000");
        properties.put("mail.smtp.starttls.enable", String.valueOf(conn.getStarttls()));

        // Accept self-sign certificate
        if (conn.getStarttls()) {
            properties.put("mail.smtp.ssl.checkserveridentity", "false");
            properties.put("mail.smtp.ssl.trust", conn.getHost());
        }

        // Convert from our EmailMessage class to MimeMessage class
        MimeMessage msg = new MimeMessage(Session.getInstance(properties,
                new MailAuthenticator(conn.getUsername(), conn.getPassword())));
        msg.addHeader("Content-Type", "charset=UTF-8");
        msg.setSentDate(new Date());

        msg.setFrom(message.getFromField().isEmpty() ? null : new InternetAddress(message.getFromField()));

        for (String to : message.getToField()) {
            if (emailAddressAllowed(to)) {
                msg.addRecipients(Message.RecipientType.TO, to);
            }
        }

        for (String cc : message.getCcField()) {
            if (emailAddressAllowed(cc)) {
                msg.addRecipients(Message.RecipientType.CC, cc);
            }
        }

        msg.setSubject(StringUtils.encodeBase64Codec(message.getSubjectField()));

        msg.setText(message.getBodyField(), ConfigManager.system.getCharacterEncoding());

        ActionMessages errors = new ActionMessages();

        // Make sure we have some kind of recepients before trying to send.
        if (msg.getAllRecipients() != null) {
            try {
                Transport.send(msg);
                LOGGER.info(LogConfigManager.EMAIL_PREFIX + " Successfully sent an email message to: " + joinList(msg.getAllRecipients(), ","));

            } catch (javax.mail.AuthenticationFailedException e) {
                errors.add("sendMail", new ActionMessage("admin.config.email.error.authentication"));
                LOGGER.severe("Failed to send mail: Authentication error. Please make sure username and password are correct.");

            } catch (Exception e) {
                errors.add("sendMail", new ActionMessage("admin.config.email.error", e.getClass(), e.getMessage()));
                LOGGER.log(Level.SEVERE, "Failed to send mail to: " + joinList(msg.getAllRecipients(), ","), e);
            }
        }
        return errors;
    }

    /**
     * Checks whether an email is allowed to be sent out.
     * Only recipient matching pre-defined domains are allowed to go out.
     *
     * @return ..
     */
    public static boolean emailAddressAllowed(String receipient) {
        if (ConfigManager.email.getAllowedDomainLowercaseOptions().isEmpty()) {
            return true;
        }
        
        LOGGER.info(LogConfigManager.EMAIL_PREFIX + " Check recipient " + receipient + " is allowed in domain list. " + ConfigManager.email.getAllowedDomainLowercaseOptions());
        
        for (String allowedDomain : ConfigManager.email.getAllowedDomainLowercaseOptions()) {
            if (receipient.toLowerCase().endsWith(allowedDomain)) {
                return true;
            }
        }
        LOGGER.info(LogConfigManager.EMAIL_PREFIX + " Recipient " + receipient + " was blocked because it's not in allowed domain list.");
        return false;
    }

    /**
     * Returns whether an email message was sent from the application, by matching SMTP From Address and Message From 
     * Address.
     * @return
     */
    public static boolean isMessageOriginatedFromApp(String smtpFrom, String messageFrom) {
        return IssueEmailParser.parseEmailAddress(smtpFrom).equalsIgnoreCase(messageFrom);
    }
        
    public static void testIsMessageOriginatedFromApp() {
        System.out.println(isMessageOriginatedFromApp("Kwoksys <support@kwoksys.com>", "Support@kwoksys.com"));
        System.out.println(isMessageOriginatedFromApp("Kwoksys < Support@kwoksys.com> ", "support@kwoksys.com"));
        System.out.println(isMessageOriginatedFromApp("support@kwoksys.com ", "Support@kwoksys.com"));
        System.out.println(isMessageOriginatedFromApp("support@Kwoksys.com", "support@kwoksys.com"));
    }

    /**
     * Joins a list of email addresses into a string.
     * @param addresses
     * @param token
     * @return
     */
    public static String joinList(Address[] addresses, String token) {
        StringBuilder result = new StringBuilder();
        for (Address address : addresses) {
            if (result.length() != 0) {
                result.append(token).append(" ");
            }
            result.append(address.toString());
        }
        return result.toString();
    }
}
