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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.framework.util.StringUtils;

/**
 * Pop3
 */
public class Pop3 {

    private static final Logger LOGGER = Logger.getLogger(Pop3.class.getName());
    
    private ActionMessages errors = new ActionMessages();

    public List<EmailMessage> receive(PopConnection conn) throws Exception {
        String protocol = conn.isSslEnabled() ? "pop3s" : "pop3";

        Properties props = new Properties();
        props.put("mail." + protocol + ".host", conn.getHost());
        props.put("mail." + protocol + ".port", conn.getPort());
        props.put("mail." + protocol + ".timeout", "10000");

        if (conn.isSslEnabled()) {
            props.put("mail.pop3s.ssl.enable", "true");
            props.put("mail.pop3s.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.pop3s.socketFactory.fallback", "false");
            props.put("mail.pop3s.socketFactory.port", conn.getPort());

            // Accept self-sign certificate
            props.put("mail.pop3s.ssl.checkserveridentity", "false");
            props.put("mail.pop3s.ssl.trust", conn.getHost());
        }

        // Setup authentication, get session
        Session session = Session.getInstance(props, new MailAuthenticator(conn.getUsername(), conn.getPassword()));

        List<EmailMessage> emailMessages = new ArrayList<>();
        Folder folder = null;
        Store store = null;

        try {
            // Get store
            store = session.getStore(protocol);
            store.connect();

            // Get folder
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // Get messages
            int messageCount = folder.getMessageCount();
            LOGGER.log(Level.INFO, "Retrieving up to {0} messages. Number of messages in issue mailbox: {1}",
                    new Object[] {conn.getMessagesLimit(), messageCount});
            
            for (int i=1; (i<=messageCount && i<=conn.getMessagesLimit()); i++) {
                Message message = folder.getMessage(i);

                if (conn.isDeleteFlag()) {
                    message.setFlag(Flags.Flag.DELETED, true);
                }

                try {
                    EmailMessage emailMessage = new EmailMessage();
                    emailMessage.setFromField(IssueEmailParser.parseEmailAddress(message.getFrom()[0].toString()));
                    emailMessage.setSubjectField(StringUtils.replaceNull(message.getSubject()));
                    emailMessage.setBodyField(IssueEmailParser.parseEmailBody(getBodyContent(message).toString().trim()));
                    
                    Address[] ccList = message.getRecipients(RecipientType.CC);
                    if (ccList != null) {
                        for (Address ccAddress : ccList) {
                            emailMessage.getCcField().add(IssueEmailParser.parseEmailAddress(ccAddress.toString()));
                        }
                    }
                    
                    if (ignoreSender(conn.getSenderIgnoreList().split("\n"), emailMessage.getFromField())) {
                        LOGGER.info(LogConfigManager.EMAIL_PREFIX + " Ignored issue email from: " + emailMessage.getFromField());
                    } else {
                        emailMessages.add(emailMessage);
                        LOGGER.info(LogConfigManager.EMAIL_PREFIX + " Received issue email from: " + emailMessage.getFromField());
                    }
                } catch (Exception e) {
                    errors.add("fetchMail", new ActionMessage("admin.config.email.pop.error", e.getClass(), e.getMessage()));
                    LOGGER.log(Level.WARNING, "Failed to receive email " + i, e);
                }
            }
        } catch (Exception e) {
            errors.add("fetchMail", new ActionMessage("admin.config.email.pop.error", e.getClass(), e.getMessage()));
            LOGGER.log(Level.SEVERE, "Failed to receive emails", e);

        } finally {
            // Close connection, and delete messages marked DELETED
            try {
                if (folder != null && folder.isOpen()) {
                    folder.close(true);
                }
            } catch (Exception e) {
                errors.add("fetchMail", new ActionMessage("admin.config.email.pop.error", e.getClass(), e.getMessage()));
                LOGGER.log(Level.SEVERE, "Failed to close inbox folder", e);
                // Empty the list, since the messages may not have been deleted when the API failed to close the folder.
                emailMessages.clear();
            }
            if (store != null) {
                store.close();
            }
        }
        return emailMessages;
    }

    public static boolean ignoreSender(String[] senderList, String from) {
        for (String ignoreSender : senderList) {
            if (ignoreSender.trim().equalsIgnoreCase(from)) {
                return true;
            }
        }
        return false;
    }

    public static StringBuilder getBodyContent(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        StringBuilder bodyText = new StringBuilder();

        if (content instanceof MimeMultipart) {
            MimeMultipart mp = (MimeMultipart) content;

            for (int j = 0; j < mp.getCount(); j++) {
                BodyPart bodyPart = mp.getBodyPart(j);
                String disposition = bodyPart.getDisposition();

                if (BodyPart.ATTACHMENT.equals(disposition)) {
                    continue;
                }
                String text = getText(bodyPart);
                if (text != null && bodyText.toString().isEmpty()) {
                    bodyText.append(text);
                }
            }
        } else {
            bodyText.append(StringUtils.replaceNull(content));
        }
        return bodyText;
    }

    /**
     * Return the primary text content of the message. From "JAVAMAIL API FAQ" sample.
     * http://www.oracle.com/technetwork/java/faq-135477.html
     */
    private static String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            return (String)p.getContent();
        }

        if (p.isMimeType("multipart/alternative")) {
            // Prefer plain text over html text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/html")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                } else if (bp.isMimeType("text/plain")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }

    public ActionMessages getErrors() {
        return errors;
    }
}
