package be.fgov.caamihziv.services.quickmon.intrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	super.addViewControllers(registry);

        registry.addRedirectViewController("/", "/ui");

        registry.addViewController("/ui/**").setViewName("forward:/index.html");
    }

}
