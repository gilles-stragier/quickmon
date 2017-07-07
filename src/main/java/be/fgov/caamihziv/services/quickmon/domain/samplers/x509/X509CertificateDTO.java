package be.fgov.caamihziv.services.quickmon.domain.samplers.x509;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by gs on 07.07.17.
 */
public class X509CertificateDTO {

    private X509Certificate wrapped;

    public int getVersion() {
        return wrapped.getVersion();
    }

    public BigInteger getSerialNumber() {
        return wrapped.getSerialNumber();
    }

    public String getIssuerDN() {
        return wrapped.getIssuerDN().getName();
    }

    public String getSubjectDN() {
        return wrapped.getSubjectDN().getName();
    }

    public LocalDateTime getNotBefore() {
        return LocalDateTime.ofInstant(wrapped.getNotBefore().toInstant(), ZoneId.systemDefault());
    }

    public LocalDateTime getNotAfter() {
        return LocalDateTime.ofInstant(wrapped.getNotAfter().toInstant(), ZoneId.systemDefault());
    }

    public String getSigAlgName() {
        return wrapped.getSigAlgName();
    }

    public String getSigAlgOID() {
        return wrapped.getSigAlgOID();
    }

    public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {
        return wrapped.getSubjectAlternativeNames();
    }

    public Collection<List<?>> getIssuerAlternativeNames() throws CertificateParsingException {
        return wrapped.getIssuerAlternativeNames();
    }

    public String getType() {
        return wrapped.getType();
    }

    public X509CertificateDTO(X509Certificate wrapped) {
        this.wrapped = wrapped;
    }


}
