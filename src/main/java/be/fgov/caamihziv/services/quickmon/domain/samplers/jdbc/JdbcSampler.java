package be.fgov.caamihziv.services.quickmon.domain.samplers.jdbc;

import be.fgov.caamihziv.services.quickmon.domain.samplers.Sampler;
import be.fgov.caamihziv.services.quickmon.domain.samplers.SamplerBuilder;
import be.fgov.caamihziv.services.quickmon.domain.samplers.ssh.ShellResult;
import com.jcraft.jsch.*;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

/**
 * Created by gs on 09.05.17.
 */
public class JdbcSampler implements Sampler<List<Map<String, Object>>> {

    private String sql;
    private String url;
    private int timeout;
    private JdbcTemplate jdbcTemplate;

    public JdbcSampler(JdbcSamplerBuilder builder) {
        this.sql = builder.getSql();
        this.url = builder.getUrl();
        this.timeout = builder.getTimeout();

        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource(
                getDriver(builder),
                builder.getUrl()
        );

        this.jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        this.jdbcTemplate.setQueryTimeout(builder.getTimeout());
    }

    private static Driver getDriver(JdbcSamplerBuilder builder){
        try {
            return DriverManager.getDriver(builder.getUrl());
        } catch (SQLException e) {
            throw new IllegalArgumentException("Unable to locate driver");
        }
    }

    @Override
    public List<Map<String, Object>> sample() {
        JdbcTemplate temp = new JdbcTemplate();
        List<Map<String, Object>> result = temp.query(sql, new ColumnMapRowMapper());
        return result;
    }

    @Override
    public SamplerBuilder toBuilder() {
        return new JdbcSamplerBuilder()
                .url(url)
                .sql(sql)
                ;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getType() {
        return "sql";
    }

}
