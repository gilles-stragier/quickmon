package be.fgov.caamihziv.services.quickmon.domain.notifiers.mail;

import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.AbstractNotifier;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.NotifierBuilder;
import be.fgov.caamihziv.services.quickmon.domain.notifiers.logging.LoggingNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by gs on 13.06.17.
 */
public class MailNotifier extends AbstractNotifier {

    private Logger logger = LoggerFactory.getLogger(MailNotifier.class);

    private String smtpHost;
    private int smtpPort;
    private String from;
    private String to;
    private Optional<String> userName;
    private Optional<String> pwd;
    private String url;


    public MailNotifier(MailNotifierBuilder builder) {
        super(builder);
        this.url = builder.getUrl();

        URI uri = URI.create(builder.getUrl());

        Assert.isTrue("smtp".equals(uri.getScheme()), "The protocol of the url should be smtp, and not " + uri.getScheme());

        this.smtpHost = uri.getHost();
        this.smtpPort = uri.getPort();
        if (uri.getUserInfo() != null) {
            userName = Optional.ofNullable(uri.getUserInfo().split(":")[0]);
            pwd = Optional.ofNullable(uri.getUserInfo().split(":")[1]);
        } else {
            userName = Optional.empty();
            pwd = Optional.empty();
        }

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
                .url(url)
                .from(from)
                .to(to)
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

            userName.ifPresent(name -> sender.setUsername(name));
            pwd.ifPresent(pwd -> sender.setPassword(pwd));

            sender.send(message);
            logger.info("Notification mail sent for " + healthChecks.size() + " health checks");
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

    public Optional<String> getUserName() {
        return userName;
    }

    public Optional<String> getPwd() {
        return pwd;
    }
}

