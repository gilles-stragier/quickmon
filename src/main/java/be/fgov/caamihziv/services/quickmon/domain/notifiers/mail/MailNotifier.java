package be.fgov.caamihziv.services.quickmon.domain.notifiers.mail;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.AbstractNotifier;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by gs on 13.06.17.
 */
public class MailNotifier extends AbstractNotifier {

    private String smtpHost;
    private int smtpPort;
    private String from;
    private String to;


    public MailNotifier(MailNotifierBuilder builder) {
        super(builder);
        this.smtpHost = builder.getSmtpHost();
        this.smtpPort = builder.getSmtpPort();
        this.from = builder.getFrom();
        this.to = builder.getTo();
    }

    @Override
    public MailNotifierBuilder toBuilder() {
        return new MailNotifierBuilder()
                .name(getName())
                .tags(getTags())
                .statuses(getStatuses())
                .period(getPeriod())
                ;
    }

    @Override
    public void doRun(Collection<HealthCheck> healthChecks) {
        if (!healthChecks.isEmpty()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setSubject("Quickmon Alerts : " + healthChecks.size() + " failing checks");
            message.setTo(to);

            message.setText(buildMailContent(healthChecks));

            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(smtpHost);
            sender.setPort(smtpPort);
            sender.send(message);
        }
    }

    private String buildMailContent(Collection<HealthCheck> healthChecks) {
        return healthChecks.stream()
                .map(hc -> String.format("%s (%s) - %s", hc.getName(), hc.getLastStatus().getHealth(), hc.getLastStatus().getMessage()))
                .collect(Collectors.joining("\n"));
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public String getFrom() {
        return from;
    }
}

