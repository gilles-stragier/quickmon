package be.fgov.caamihziv.services.quickmon.domain.samplers.http;

import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeName("http")
public class HttpSamplerBuilder extends SamplerBuilder<HttpSamplerBuilder, HttpSampler> {

    @Override
    public String getType() {
        return "http";
    }

    @Override
    public HttpSampler build() {
        return new HttpSampler(this);
    }
}
