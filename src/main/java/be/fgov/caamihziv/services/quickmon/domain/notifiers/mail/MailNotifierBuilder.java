package be.fgov.caamihziv.services.quickmon.domain.notifiers.mail;

import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;

/**
 * Created by gs on 13.06.17.
 */
public class MailNotifierBuilder extends NotifierBuilder<MailNotifierBuilder, MailNotifier> {

    private String smtpHost;
    private Integer smtpPort;
    private String from;
    private String to;

    public MailNotifierBuilder smtpHost(String val) {
        this.smtpHost = val;
        return this;
    }

    public MailNotifierBuilder smtpPort(int val) {
        this.smtpPort = val;
        return this;
    }

    public MailNotifierBuilder from(String val) {
        this.from = val;
        return this;
    }

    public MailNotifierBuilder to(String val) {
        this.to = val;
        return this;
    }

    @Override
    public String getType() {
        return "mail";
    }

    @Override
    public MailNotifier build() {
        return new MailNotifier(this);
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
