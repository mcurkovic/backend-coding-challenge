package com.demo;

import java.util.Map;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * A bean mapper designed for Spring suitable for dependency injection. Provides an implementation of {@link
 * MapperFacade} which can be injected. In addition it is "Spring aware" in that it can autodiscover any implementations
 * of {@link Mapper} or {@link Converter} that are managed beans within it's parent {@link ApplicationContext}.
 */
@Component
public class SpringConfigurableMapper extends ConfigurableMapper implements ApplicationContextAware {

    private MapperFactory factory;
    private ApplicationContext applicationContext;

    public SpringConfigurableMapper() {
        super(false); // delay the initialization
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureFactoryBuilder(
            final DefaultMapperFactory.Builder factoryBuilder) {
        // customize the factoryBuilder as needed
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(final MapperFactory p_factory) {
        this.factory = p_factory;
        // customize the factory as needed
        addAllSpringBeans(applicationContext);
    }

    @Override
    @Autowired
    public void setApplicationContext(
            final ApplicationContext p_applicationContext) {
        applicationContext = p_applicationContext;
        init();
    }

    /**
     * Adds all managed beans of type {@link Mapper} or {@link Converter} to the parent {@link MapperFactory}.
     *
     * @param applicationContext The application context to look for managed beans in.
     */
    @SuppressWarnings({"rawtypes"})
    private void addAllSpringBeans(final ApplicationContext p_applicationContext) {
        final Map<String, Converter> converters = p_applicationContext
                .getBeansOfType(Converter.class);

        for (Map.Entry<String, Converter> converter : converters.entrySet()) {
            addConverter(converter.getKey(), converter.getValue());
        }

        final Map<String, Mapper> mappers = p_applicationContext
                .getBeansOfType(Mapper.class);
        for (final Mapper mapper : mappers.values()) {
            addMapper(mapper);
        }

    }

    private void addConverter(String converterId,
            final Converter<?, ?> converter) {
        factory.getConverterFactory().registerConverter(converterId, converter);
    }

    /**
     * Add a {@link Mapper}.
     *
     * @param mapper The mapper.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addMapper(final Mapper<?, ?> mapper) {
        ClassMapBuilder<?, ?> classMapBuilder = factory.classMap(
                mapper.getAType(), mapper.getBType());

        classMapBuilder.customize((Mapper) mapper).register();
    }
}
