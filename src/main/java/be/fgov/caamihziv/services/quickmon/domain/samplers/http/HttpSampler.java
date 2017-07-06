package be.fgov.caamihziv.services.quickmon.domain.samplers.http;

import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by gs on 15.04.17.
 */
public class HttpSampler implements Sampler<ResponseEntity<String>> {

    private final RestTemplate restTemplate;
    private final String url;
    private int timeout;

    public HttpSampler(HttpSamplerBuilder builder) {
        url = builder.getUrl();
        this.timeout = builder.getTimeout();
        this.restTemplate = initRestTemplate(builder.getTimeout());
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
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity;
    }

    @Override
    public String getType() {
        return "http";
    }

    @Override
    public SamplerBuilder toBuilder() {
        return new HttpSamplerBuilder().url(url).timeout(timeout);
    }

    public String getUrl() {
        return url;
    }
}
