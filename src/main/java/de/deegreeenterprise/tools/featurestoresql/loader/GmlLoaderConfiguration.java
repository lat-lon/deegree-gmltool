package de.deegreeenterprise.tools.featurestoresql.loader;

import org.deegree.feature.Feature;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

/**
 * Configuration of the GMLLoader.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
@Configuration
@EnableBatchProcessing
public class GmlLoaderConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @StepScope
    @Bean
    public GmlReader gmlReader( @Value("#{jobParameters[pathToFile]}") String pathToFile ) {
        GmlReader gmlReader = new GmlReader();
        gmlReader.setResource( new PathResource( pathToFile ) );
        return gmlReader;
    }

    @Bean
    public FeatureStoreWriter featureStoreWriter() {
        return new FeatureStoreWriter();
    }

    @Bean
    public Step step( GmlReader gmlReader, FeatureStoreWriter featureStoreWriter ) {
        return stepBuilderFactory.get( "step" ).<Feature, Feature> chunk( 10 ).reader( gmlReader ).writer( featureStoreWriter ).build();
    }

    @Bean
    public Job job( Step step )
                            throws Exception {
        return jobBuilderFactory.get( "job" ).incrementer( new RunIdIncrementer() ).start( step ).build();
    }
}
