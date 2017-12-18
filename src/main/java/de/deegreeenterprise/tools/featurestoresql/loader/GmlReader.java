package de.deegreeenterprise.tools.featurestoresql.loader;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.deegree.commons.xml.stax.XMLStreamReaderWrapper;
import org.deegree.feature.Feature;
import org.deegree.feature.persistence.sql.SQLFeatureStore;
import org.deegree.feature.types.AppSchema;
import org.deegree.gml.GMLInputFactory;
import org.deegree.gml.GMLStreamReader;
import org.deegree.gml.GMLVersion;
import org.deegree.gml.feature.StreamFeatureCollection;
import org.slf4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.core.io.Resource;

/**
 * Reads a GML 3.2 resource.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class GmlReader extends AbstractItemStreamItemReader<Feature> implements
                                                                    ResourceAwareItemReaderItemStream<Feature> {

    private static final Logger LOG = getLogger( GmlReader.class );

    private final SQLFeatureStore sqlFeatureStore;

    private Resource resource;

    private InputStream inputStream;

    private XMLStreamReader xmlStreamReader;

    private StreamFeatureCollection featureStream;

    private int noOfFeaturesRead = 0;

    /**
     * @param sqlFeatureStore
     *            the {@link SQLFeatureStore} used for insert, may be <code>null</code>
     */
    public GmlReader( SQLFeatureStore sqlFeatureStore ) {
        this.sqlFeatureStore = sqlFeatureStore;
    }

    @Override
    public void setResource( Resource resource ) {
        this.resource = resource;
    }

    @Override
    public Feature read()
                            throws Exception {
        if ( this.featureStream == null ) {
            return null;
        }
        Feature feature = this.featureStream.read();
        if ( feature != null )
            LOG.info( "Read feature with id " + feature.getId() + " (number " + ++noOfFeaturesRead + ") " );
        return feature;
    }

    @Override
    public void open( ExecutionContext executionContext ) {
        super.open( executionContext );
        if ( this.resource == null )
            throw new IllegalStateException( "Input resource must not be null." );
        if ( !this.resource.exists() )
            throw new IllegalStateException( "Input resource must exist." );
        if ( !this.resource.isReadable() )
            throw new IllegalStateException( "Input resource must be readable." );

        openFeatureStream();
    }

    @Override
    public void close() {
        super.close();
        try {
            if ( this.featureStream != null ) {
                this.featureStream.close();
            }
            if ( this.xmlStreamReader != null ) {
                this.xmlStreamReader.close();
            }
            if ( this.inputStream != null ) {
                this.inputStream.close();
            }
        } catch ( Exception var2 ) {
            throw new ItemStreamException( "Error while closing item reader", var2 );
        } finally {
            this.featureStream = null;
            this.xmlStreamReader = null;
            this.inputStream = null;
        }

    }

    private void openFeatureStream() {
        try {
            this.inputStream = this.resource.getInputStream();
            GMLVersion version = GMLVersion.GML_32;
            this.xmlStreamReader = XMLInputFactory.newFactory().createXMLStreamReader( this.inputStream );
            XMLStreamReaderWrapper xmlStream = new XMLStreamReaderWrapper( xmlStreamReader, null );
            GMLStreamReader gmlStreamReader = GMLInputFactory.createGMLStreamReader( version, xmlStream );
            gmlStreamReader.setApplicationSchema( findSchema() );
            this.featureStream = gmlStreamReader.readFeatureCollectionStream();
        } catch ( Exception e ) {
            throw new ItemStreamException( "Failed to initialize the reader", e );
        }
    }

    private AppSchema findSchema() {
        if ( sqlFeatureStore != null )
            return sqlFeatureStore.getSchema();
        return null;
    }

}