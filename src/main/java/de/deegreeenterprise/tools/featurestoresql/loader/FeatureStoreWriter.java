package de.deegreeenterprise.tools.featurestoresql.loader;

import org.deegree.feature.Feature;
import org.deegree.feature.FeatureCollection;
import org.deegree.feature.GenericFeatureCollection;
import org.deegree.feature.persistence.sql.SQLFeatureStore;
import org.deegree.feature.persistence.sql.SQLFeatureStoreTransaction;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.Assert;

import java.util.List;

import static org.deegree.protocol.wfs.transaction.action.IDGenMode.USE_EXISTING;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Inserts Feature in the SQLFeatureStore.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class FeatureStoreWriter implements ItemWriter<Feature> {

    private static final Logger LOG = getLogger( GmlLoaderConfiguration.class );

    private SQLFeatureStore sqlFeatureStore;

    /**
     * @param sqlFeatureStore
     *                         SQLFeatureStore to insert the features, never <code>null</code>
     */
    public FeatureStoreWriter( SQLFeatureStore sqlFeatureStore ) {
        Assert.notNull( sqlFeatureStore, "sqlFeatureStore  must not be null" );
        this.sqlFeatureStore = sqlFeatureStore;
    }

    @Override
    public void write( List features )
                            throws Exception {

        FeatureCollection featureCollection = new GenericFeatureCollection();
        for ( Object feature : features ) {
            Feature featureToAdd = (Feature) feature;
            LOG.info( "Adding feature with GML ID '"+featureToAdd.getId()+"' of type '"+featureToAdd.getType().getName()+"' to chunk" );
            featureCollection.add( featureToAdd );
        }
        LOG.info( "Trying to write " + featureCollection.size() + " features" );
        SQLFeatureStoreTransaction transaction = (SQLFeatureStoreTransaction) sqlFeatureStore.getTransaction();
        transaction.performInsert( featureCollection, USE_EXISTING );
        LOG.info( "Insert performed." );
    }

}