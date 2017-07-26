package be.fgov.caamihziv.services.quickmon.domain.samplers.http;

import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
 * Created by gs on 15.04.17.
 */
public class HttpSampler implements Sampler<ResponseEntity<String>> {

    private final RestTemplate restTemplate;
    private final String url;
    private int timeout;
    private HttpMethod method;
    private String body;
    private Map<String, List<String>> headers;

    public HttpSampler(HttpSamplerBuilder builder) {
        url = builder.getUrl();
        this.timeout = builder.getTimeout();
        this.restTemplate = initRestTemplate(builder.getTimeout());
        this.method = builder.getMethod();
        this.body = builder.getBody();
        this.headers = builder.getHeaders();
    }

    protected RestTemplate initRestTemplate(int timeout) {


        RestTemplate restTemplate = null;
        try {
            SSLContext sslc = SSLContext.getInstance("TLS");


            TrustManager[] trustManagerArray = { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } };

            sslc.init(null, trustManagerArray, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });


            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

            factory.setReadTimeout(timeout);

            restTemplate = new RestTemplate(factory);

            restTemplate.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse response) throws IOException {
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize rest template");
        }

        return restTemplate;
    }



    @Override
    public ResponseEntity<String> sample() {
        URI urlObject = URI.create(this.url);
        HttpHeaders headers = new HttpHeaders();
        if (this.headers != null) {
            headers.putAll(headers);
        }
        RequestEntity<Object> request = new RequestEntity<>(body, headers, method, urlObject);
        return restTemplate.exchange(urlObject, method, request, String.class);
    }

    @Override
    public String getType() {
        return "http";
    }

    @Override
    public SamplerBuilder toBuilder() {
        return new HttpSamplerBuilder()
                .url(url)
                .headers(headers)
                .body(body)
                .method(method)
                .timeout(timeout);
    }

    public String getUrl() {
        return url;
    }
}
