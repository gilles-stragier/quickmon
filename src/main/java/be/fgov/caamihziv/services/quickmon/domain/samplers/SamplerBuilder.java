package be.fgov.caamihziv.services.quickmon.domain.samplers;

import be.fgov.caamihziv.services.quickmon.domain.Builder;
import be.fgov.caamihziv.services.quickmon.domain.ValidationUtils;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.ssh.JschSamplerBuilder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HttpSamplerBuilder.class, name = "http"),
        @JsonSubTypes.Type(value = JschSamplerBuilder.class, name = "ssh")
})
public abstract class SamplerBuilder<T extends SamplerBuilder, U extends Sampler> implements Builder<T,U> {

    private String url;
    private int timeout;

    public SamplerBuilder() {
        this.timeout = 1000;
    }

    public String getUrl() {
        return url;
    }

    public T url(String val) {
        this.url = val;
        return (T) this;
    }

    public T timeout(int val) {
        this.timeout = val;
        return (T) this;
    }


    @Override
    public List<String> validate() {
        ArrayList<String> errors = new ArrayList<>();
        ValidationUtils.notNull(errors, url, "Please provide the sampler a url");
        return errors;
    }

    public abstract  String getType();

    public abstract U build();

    public int getTimeout() {
        return timeout;
    }
}
