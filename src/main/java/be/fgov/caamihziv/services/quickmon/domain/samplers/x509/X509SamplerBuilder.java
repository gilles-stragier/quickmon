package be.fgov.caamihziv.services.quickmon.domain.samplers.x509;

import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSampler;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeName("x509")
public class X509SamplerBuilder extends SamplerBuilder<X509SamplerBuilder, X509Sampler> {

    @Override
    public String getType() {
        return "x509";
    }

    @Override
    public X509Sampler build() {
        return new X509Sampler(this);
    }
}
