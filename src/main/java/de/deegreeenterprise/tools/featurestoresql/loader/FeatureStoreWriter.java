package de.deegreeenterprise.tools.featurestoresql.loader;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.deegree.feature.Feature;
import org.deegree.feature.FeatureCollection;
import org.deegree.feature.GenericFeatureCollection;
import org.deegree.feature.persistence.sql.SQLFeatureStore;
import org.deegree.feature.persistence.sql.SQLFeatureStoreTransaction;
import org.deegree.protocol.wfs.transaction.action.IDGenMode;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.Assert;

/**
 * Inserts Feature in the SQLFeatureStore.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class FeatureStoreWriter implements ItemWriter<Feature> {

    private static final Logger LOG = getLogger( GmlLoaderConfiguration.class );

    private SQLFeatureStore sqlFeatureStore;

    private final IDGenMode idGenMode;

    /**
     * @param sqlFeatureStore
     *            SQLFeatureStore to insert the features, never <code>null</code>
     * @param idGenMode
     *            the id generation mode to use for insert, never <code>null</code>
     */
    public FeatureStoreWriter( SQLFeatureStore sqlFeatureStore, IDGenMode idGenMode ) {
        Assert.notNull( sqlFeatureStore, "sqlFeatureStore  must not be null" );
        Assert.notNull( idGenMode, "idGenMode  must not be null" );
        this.sqlFeatureStore = sqlFeatureStore;
        this.idGenMode = idGenMode;
    }

    @Override
    public void write( List features )
                            throws Exception {

        FeatureCollection featureCollection = new GenericFeatureCollection();
        for ( Object feature : features ) {
            Feature featureToAdd = (Feature) feature;
            LOG.info( "Add feature to write " + ( (Feature) feature ).getId() );
            featureCollection.add( featureToAdd );
        }
        LOG.info( "Try to write " + featureCollection.size() + " features" );
        SQLFeatureStoreTransaction transaction = (SQLFeatureStoreTransaction) sqlFeatureStore.getTransaction();
        transaction.performInsert( featureCollection, idGenMode );
        LOG.info( "Insert performed." );
    }

}