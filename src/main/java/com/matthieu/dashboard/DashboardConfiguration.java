package com.matthieu.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.JspPropertyGroupDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.util.descriptor.web.JspConfigDescriptorImpl;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroup;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroupDescriptorImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.js.ajax.AjaxUrlBasedViewResolver;
import org.springframework.js.ajax.tiles3.AjaxTilesView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;


@SpringBootApplication( exclude = { SecurityAutoConfiguration.class, ThymeleafAutoConfiguration.class, ErrorMvcAutoConfiguration.class } )
@MapperScan( value = DashboardConfiguration.PACKAGE_DAO )
public class DashboardConfiguration extends WebMvcConfigurerAdapter{

	public static final String   PACKAGE_DAO               = "com.matthieu.dashboard.dao";
	
	public static final String   PACKAGE_CONTRAINTE_ENTITE = "ccom.matthieu.dashboard.entite";
	
	@Configuration
    static class TilesConfiguration {

        @Bean
        public AjaxUrlBasedViewResolver ajaxUrlBasedViewResolver() {
            final AjaxUrlBasedViewResolver resolver = new AjaxUrlBasedViewResolver();
            resolver.setOrder( 0 );
            resolver.setViewClass( AjaxTilesView.class );
            return resolver;
        }

        @Bean
        public UrlBasedViewResolver urlBasedViewResolver() {
            final UrlBasedViewResolver resolver = new UrlBasedViewResolver();
            resolver.setOrder( 1 );
            resolver.setViewClass( TilesView.class );
            return resolver;
        }

        @Bean
        public TilesConfigurer tilesConfigurer() {
            final TilesConfigurer configurer = new TilesConfigurer();
            configurer.setCheckRefresh( true );
            configurer.setDefinitions( "WEB-INF/tiles.xml" );
            return configurer;
        }

    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        final TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.addContextCustomizers( new TomcatContextCustomizer() {

            @Override
            public void customize( final Context context ) {
                final Collection< JspPropertyGroupDescriptor > jspPropertyGroups = new ArrayList<>();
                final Collection< TaglibDescriptor > taglibs = new ArrayList<>();

                final JspPropertyGroup group = new JspPropertyGroup();
                group.addUrlPattern( "*.jsp" );
                group.setPageEncoding( "UTF-8" );

                final JspPropertyGroupDescriptor descriptor = new JspPropertyGroupDescriptorImpl( group );
                jspPropertyGroups.add( descriptor );

                final JspConfigDescriptor jspConfigDescriptor = new JspConfigDescriptorImpl( jspPropertyGroups, taglibs );
                context.setJspConfigDescriptor( jspConfigDescriptor );
            }
        } );
        return factory;
    }
    
    @Bean
    public SqlSessionFactory sqlSessionFactory( final DataSource dataSource ) throws Exception {
        final SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setFailFast( true );
        ssfb.setDataSource( dataSource );

        final Reflections ref = new Reflections( new ConfigurationBuilder().setScanners( new SubTypesScanner( false ), new ResourcesScanner() )
                .setUrls( ClasspathHelper.forPackage( DashboardConfiguration.PACKAGE_CONTRAINTE_ENTITE ) )
                .filterInputsBy( new FilterBuilder().include( FilterBuilder.prefix( DashboardConfiguration.PACKAGE_CONTRAINTE_ENTITE ) ) ) );
        final Set< Class< ? > > entites = ref.getSubTypesOf( Object.class ).stream().filter( clazz -> !clazz.isEnum() ).collect( Collectors.toSet() );
        ssfb.setTypeAliases( entites.toArray( new Class< ? >[entites.size()] ) );

        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        ssfb.setMapperLocations( resolver.getResources( "classpath*:mapper/**/*Mapper.xml" ) );

        final SqlSessionFactory ssf = ssfb.getObject();

        ssf.getConfiguration().setCacheEnabled( true );
        ssf.getConfiguration().setLocalCacheScope( LocalCacheScope.STATEMENT );

        return ssf;
    }

    @Bean
    @ConfigurationProperties( prefix = "spring.datasource" )
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager( this.dataSource() );
    }

}
