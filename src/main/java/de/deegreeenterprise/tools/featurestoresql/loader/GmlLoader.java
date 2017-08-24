package de.deegreeenterprise.tools.featurestoresql.loader;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Entry point of the command line interface of the GMLLoader.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
@SpringBootApplication
public class GmlLoader {

    private static final Logger LOG = getLogger( GmlLoader.class );

    public static void main( String[] args )
                            throws Exception {
        ConfigurableApplicationContext springContext = SpringApplication.run( GmlLoaderConfiguration.class, args );
        try {
            JobLauncher jobLauncher = springContext.getBean( "jobLauncher", JobLauncher.class );
            Job job = springContext.getBean( "job", Job.class );
            JobParameters jobParameters = createjobParameters( args );
            LOG.info( "Starting job {} with following parameters {}", job.getName(), jobParameters.toString() );
            JobExecution jobExecution = jobLauncher.run( job, jobParameters );
            LOG.info( "Finished job {} with parameters {} with following status {}, exit status {} and batch status {}",
                      job.getName(), jobParameters.toString(), jobExecution.getStatus().toString(),
                      jobExecution.getExitStatus().toString(), jobExecution.getStatus().getBatchStatus().toString() );
        } catch ( JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                                | JobParametersInvalidException e ) {
            LOG.error( "An error occurred during executing the job.", e );
        }
    }

    private static JobParameters createjobParameters( String[] args ) {
        return new JobParametersBuilder().addString( "pathToFile", args[0] ).addString( "workspaceName", args[1] ).addString( "sqlFeatureStoreId", args[2] ).addDate( "startDate", new Date() ).toJobParameters();
    }
}