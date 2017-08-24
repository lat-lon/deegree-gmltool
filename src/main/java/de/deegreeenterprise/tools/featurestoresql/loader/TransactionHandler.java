package de.deegreeenterprise.tools.featurestoresql.loader;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

import org.deegree.feature.persistence.FeatureStoreException;
import org.deegree.feature.persistence.sql.SQLFeatureStore;
import org.deegree.feature.persistence.sql.SQLFeatureStoreTransaction;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.util.Assert;

/**
 * Acquires a transaction before step and commit or rollback the transaction after.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class TransactionHandler implements StepExecutionListener {

    private static final Logger LOG = getLogger( TransactionHandler.class );

    private SQLFeatureStore sqlFeatureStore;

    private SQLFeatureStoreTransaction featureStoreTransaction;

    /**
     *
     * @param sqlFeatureStore
     *            used for transactions, never <code>null</code>
     */
    public TransactionHandler( SQLFeatureStore sqlFeatureStore ) {
        Assert.notNull( sqlFeatureStore, "sqlFeatureStore  must not be null" );
        this.sqlFeatureStore = sqlFeatureStore;
    }

    @Override
    public void beforeStep( StepExecution stepExecution ) {
        try {
            this.featureStoreTransaction = (SQLFeatureStoreTransaction) this.sqlFeatureStore.acquireTransaction();
            LOG.info( "Acquired transaction." );
        } catch ( Exception e ) {
            throw new RuntimeException( "Transaction could not acquired!", e );
        }

    }

    @Override
    public ExitStatus afterStep( StepExecution stepExecution ) {
        if ( featureStoreTransaction != null )
            return commitOrRollback( stepExecution, featureStoreTransaction );
        return FAILED;

    }

    private ExitStatus commitOrRollback( StepExecution stepExecution, SQLFeatureStoreTransaction transaction ) {
        try {
            ExitStatus exitStatus = stepExecution.getExitStatus();
            if ( COMPLETED.equals( exitStatus ) ) {
                LOG.info( "Commit transaction." );
                transaction.commit();
            } else {
                LOG.info( "Rollback transaction." );
                transaction.rollback();
            }
            return exitStatus;
        } catch ( FeatureStoreException e ) {
            LOG.error( "Could not commit/rollback the transaction.", e );
            return FAILED;
        }
    }

}
