package be.fgov.caamihziv.services.quickmon.domain.samplers.ssh;

import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gs on 09.05.17.
 */
@JsonTypeName("ssh")
public class JschSamplerBuilder extends SamplerBuilder<JschSamplerBuilder, JschSampler> {

    private String command;

    public JschSamplerBuilder command(String val) {
        this.command = val;
        return this;
    }

    @Override
    public String getType() {
        return "ssh";
    }

    @Override
    public JschSampler build() {
        return new JschSampler(this);
    }

    public String getCommand() {
        return command;
    }
}
