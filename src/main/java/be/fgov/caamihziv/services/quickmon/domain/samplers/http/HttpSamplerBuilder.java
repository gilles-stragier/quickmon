package be.fgov.caamihziv.services.quickmon.domain.samplers.http;

import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gs on 24.04.17.
 */
@JsonTypeName("http")
public class HttpSamplerBuilder extends SamplerBuilder<HttpSamplerBuilder, HttpSampler> {

    private HttpMethod method;
    private String body;
    private Map<String,List<String>> headers;

    public HttpSamplerBuilder() {
        super();
        this.method = HttpMethod.GET;
        this.headers = new HashMap<>();
    }

    public HttpSamplerBuilder method(HttpMethod val) {
        this.method = val;
        return this;
    }

    public HttpSamplerBuilder body(String val) {
        this.body = val;
        return this;
    }

    public HttpSamplerBuilder headers(Map<String, List<String>> val) {
        this.headers = val;
        return this;
    }


    @Override
    public String getType() {
        return "http";
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }


    @Override
    public HttpSampler build() {
        return new HttpSampler(this);
    }
}
