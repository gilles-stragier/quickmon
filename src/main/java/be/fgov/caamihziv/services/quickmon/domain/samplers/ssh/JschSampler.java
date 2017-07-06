package be.fgov.caamihziv.services.quickmon.domain.samplers.ssh;

import be.fgov.caamihziv.services.quickmon.domain.assertions.spel.SpelAssertionBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheck;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthCheckBuilder;
import be.fgov.caamihziv.services.quickmon.domain.healthchecks.HealthStatus;
import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import com.jcraft.jsch.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

/**
 * Created by gs on 09.05.17.
 */
public class JschSampler implements Sampler<ShellResult> {

    private String url;
    private String user;
    private String pwd;
    private String host;
    private int port;
    private String command;
    private int timeout;

    public JschSampler(JschSamplerBuilder builder) {
        this.command = builder.getCommand();
        URI uri = URI.create(builder.getUrl());
        isTrue(uri.getScheme().equals("ssh"), "Uri must have a ssh scheme");
        notNull(uri.getHost(), "Uri must have a host");
        notNull(uri.getUserInfo(), "User info is mandatory");

        this.url = builder.getUrl();
        this.timeout = builder.getTimeout();

        host = uri.getHost();
        port = uri.getPort() != -1 ? uri.getPort() : 22;
        user = uri.getUserInfo().split(":")[0];
        pwd = uri.getUserInfo().split(":")[1];

    }

    @Override
    public ShellResult sample() {
        ShellResult.Builder resultBuilder = ShellResult.newBuilder();

        try {
            JSch jSch = new JSch();
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            Session session = jSch.getSession(user, host, port);
            session.setTimeout(timeout);
            session.setConfig(config);
            session.setPassword(pwd);
            session.connect();


            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            channel.setInputStream(null);

            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            ((ChannelExec)channel).setErrStream(errStream);

            InputStream in=channel.getInputStream();

            channel.connect();

            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    resultBuilder.out(new String(tmp, 0, i), true);
                }
                if(channel.isClosed()){
                    if(in.available()>0) continue;
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }

            resultBuilder.exitStatus(channel.getExitStatus());
            resultBuilder.err(new String(errStream.toByteArray()));

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return resultBuilder.build();
    }

    @Override
    public SamplerBuilder toBuilder() {
        return new JschSamplerBuilder()
                .url(url)
                .command(command)
                .timeout(timeout)
                ;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getType() {
        return "ssh";
    }

}
