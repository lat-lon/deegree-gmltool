package de.deegreeenterprise.tools.featurestoresql.loader;

import org.deegree.feature.Feature;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * Reader....
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class GmlReader implements ItemReader<Feature> {

    @Override
    public Feature read()
                            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

}
