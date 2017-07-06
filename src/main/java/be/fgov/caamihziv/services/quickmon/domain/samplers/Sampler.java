package be.fgov.caamihziv.services.quickmon.domain.samplers;

/**
 * Created by gs on 24.04.17.
 */
public interface Sampler<T> {

    T sample();
    SamplerBuilder toBuilder();
    String getType();

}
