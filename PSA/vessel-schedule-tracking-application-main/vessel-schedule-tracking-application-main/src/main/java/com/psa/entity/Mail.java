package com.psa.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Mail {
    private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String mailContent;
    private String contentType;

    public Mail() {
        contentType = "text/html";
    }

    public static String formatAlerts(List<Alert> vesselAlerts, Set<String> preferences) {
        //differentiate and arrange alerts for readability in emails
        String mailContent = "<ul>";
        if (preferences == null || preferences.size() == 0) {
            return null;
        }

        for (Alert alert : vesselAlerts) {
            if (preferences.contains(alert.getType())) {
                mailContent += "<li>" + alert.toString() + "</li>";
            }
        }
        mailContent += "</ul>";
        return mailContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public Date getMailSendDate() {
        return new Date();
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

}
