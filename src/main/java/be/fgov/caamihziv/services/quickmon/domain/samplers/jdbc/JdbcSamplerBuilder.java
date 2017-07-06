package be.fgov.caamihziv.services.quickmon.domain.samplers.jdbc;

import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by gs on 09.05.17.
 */
@JsonTypeName("jdbc")
public class JdbcSamplerBuilder extends SamplerBuilder<JdbcSamplerBuilder, JdbcSampler> {

    private String sql;

    public JdbcSamplerBuilder sql(String val) {
        this.sql = val;
        return this;
    }

    @Override
    public String getType() {
        return "jdbc";
    }

    public String getSql() {
        return sql;
    }

    @Override
    public JdbcSampler build() {
        return new JdbcSampler(this);
    }

}
