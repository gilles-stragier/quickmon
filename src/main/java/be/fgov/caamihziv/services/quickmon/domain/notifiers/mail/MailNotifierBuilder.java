package be.fgov.caamihziv.services.quickmon.domain.notifiers.mail;

import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;

/**
 * Created by gs on 13.06.17.
 */
public class MailNotifierBuilder extends NotifierBuilder<MailNotifierBuilder, MailNotifier> {

    private String url;
    private String from;
    private String to;
    private String subjectPrefix;

    public MailNotifierBuilder from(String val) {
        this.from = val;
        return this;
    }

    public MailNotifierBuilder to(String val) {
        this.to = val;
        return this;
    }

    public MailNotifierBuilder url(String val) {
        this.url = val;
        return this;
    }

    public MailNotifierBuilder subjectPrefix(String val) {
        this.subjectPrefix = val;
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

    public String getUrl() {
        return url;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubjectPrefix() {
        return subjectPrefix;
    }
}
