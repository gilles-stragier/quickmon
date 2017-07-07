package be.fgov.caamihziv.services.quickmon.domain.samplers.x509;

import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.http.HttpSamplerBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by gs on 07.07.17.
 */
public class X509Sampler implements Sampler<X509CertificateDTO> {

    private String url;
    private int timeout;

    public X509Sampler(X509SamplerBuilder builder) {
        url = builder.getUrl();
        this.timeout = builder.getTimeout();
    }

    @Override
    public X509CertificateDTO sample() {
        HttpsURLConnection conn = null;
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

            URL url = new URL(this.url);
            conn = (HttpsURLConnection) url.openConnection();

            conn.connect();

            X509Certificate cert = (X509Certificate) conn.getServerCertificates()[0];

            return new X509CertificateDTO(cert);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to retrieve certificate", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public SamplerBuilder toBuilder() {
        return new X509SamplerBuilder()
                .url(url)
                .timeout(timeout);
    }

    @Override
    public String getType() {
        return "x509";
    }

}
