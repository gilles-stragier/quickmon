package be.fgov.caamihziv.services.quickmon.domain.samplers.x509;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by gs on 07.07.17.
 */
public class X509SamplerTest {
    @Test
    public void sample() throws Exception {
        X509Sampler sampler = new X509SamplerBuilder()
                .url("https://github.com")
                .build();

        X509CertificateDTO sample = sampler.sample();
        Assert.assertEquals("SHA256withRSA", sample.getSigAlgName());
    }

}