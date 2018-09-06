package de.deegreeenterprise.tools.featurestoresql.loader;

import static org.slf4j.LoggerFactory.getLogger;

import org.deegree.commons.config.DeegreeWorkspace;
import org.deegree.feature.Feature;
import org.deegree.feature.persistence.FeatureStoreProvider;
import org.deegree.feature.persistence.sql.SQLFeatureStore;
import org.deegree.workspace.Workspace;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
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

    private static final Logger LOG = getLogger( GmlLoaderConfiguration.class );

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @StepScope
    @Bean
    public TransactionHandler transactionHandler( SQLFeatureStore sqlFeatureStore ) {
        return new TransactionHandler( sqlFeatureStore );
    }

    @StepScope
    @Bean
    public GmlReader gmlReader( SQLFeatureStore sqlFeatureStore,
                                @Value("#{jobParameters[pathToFile]}") String pathToFile ) {
        GmlReader gmlReader = new GmlReader( sqlFeatureStore );
        gmlReader.setResource( new PathResource( pathToFile ) );
        return gmlReader;
    }

    @StepScope
    @Bean
    public FeatureReferencesParser featureReferencesParser() {
        return new FeatureReferencesParser();
    }

    @StepScope
    @Bean
    public FeatureStoreWriter featureStoreWriter( SQLFeatureStore sqlFeatureStore ) {
        return new FeatureStoreWriter( sqlFeatureStore );
    }

    @StepScope
    @Bean
    public SQLFeatureStore sqlFeatureStore( @Value("#{jobParameters[workspaceName]}") String workspaceName,
                                            @Value("#{jobParameters[sqlFeatureStoreId]}") String sqlFeatureStoreId )
                            throws Exception {
        DeegreeWorkspace workspace = DeegreeWorkspace.getInstance( workspaceName );
        workspace.initAll();
        LOG.info( "deegree workspace directory: [" + workspace.getLocation() + "] initialized" );
        Workspace newWorkspace = workspace.getNewWorkspace();
        SQLFeatureStore featureStore = (SQLFeatureStore) newWorkspace.getResource( FeatureStoreProvider.class,
                                                                                   sqlFeatureStoreId );
        LOG.info( "SQLFeatureStore: [" + sqlFeatureStoreId + "] requested." );
        return featureStore;
    }

    @Bean
    public Step step( TransactionHandler transactionHandler, GmlReader gmlReader,
                      FeatureReferencesParser featureReferencesParser, FeatureStoreWriter featureStoreWriter ) {
        return stepBuilderFactory.get( "step" ).<Feature, Feature> chunk( 10 ).reader( gmlReader ).processor( featureReferencesParser ).writer( featureStoreWriter ).listener( transactionHandler ).build();
    }

    @Bean
    public Job job( Step step ) {
        return jobBuilderFactory.get( "job" ).incrementer( new RunIdIncrementer() ).start( step ).build();
    }

    @Bean
    public JobLauncherCommandLineRunner runner() {
        return new JobLauncherCommandLineRunner( jobLauncher, jobExplorer );
    }

}
