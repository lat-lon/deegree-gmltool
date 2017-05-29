package de.weichand.deegree;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.namespace.QName;

/**
 * Parser for property names from a file.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class PropertyNameParser {

    /**
     * Parses the property names from the passed file. The properties are parsed line by line. empty lines are ignored.
     *
     * @param pathToFile
     * @return
     */
    public List<QName> parsePropertiesWithPrimitiveHref( String pathToFile ) {
        Path path = Paths.get( pathToFile );
        return parsePropertiesWithPrimitiveHref( path );
    }

    /**
     * Parses the property names from the passed file. The properties are parsed line by line. empty lines are ignored.
     * 
     * @param pathToFile
     * @return
     */
    public List<QName> parsePropertiesWithPrimitiveHref( URI pathToFile ) {
        Path path = Paths.get( pathToFile );
        return parsePropertiesWithPrimitiveHref( path );
    }

    private List<QName> parsePropertiesWithPrimitiveHref( Path path ) {
        try (Stream<String> stream = Files.lines( path )) {
            ArrayList<QName> properties = new ArrayList<>();
            List<String> list = stream.collect( Collectors.toList() );
            parseList( properties, list );
            return properties;
        } catch ( NoSuchFileException e ) {
            System.out.println( "Referenced listOfPropertiesWithPrimitiveHref cannot be found and is ignored! " );
        } catch ( IOException i ) {
            System.out.println( "Referenced listOfPropertiesWithPrimitiveHref cannot be parsed and is ignored! Exception: "
                                + i.getMessage() );
        }
        return null;
    }

    private void parseList( ArrayList<QName> properties, List<String> list ) {
        for ( String entry : list ) {
            try {
                QName qName = QName.valueOf( entry );
                properties.add( qName );
            } catch ( IllegalArgumentException e ) {
                System.out.println( "One line of referenced listOfPropertiesWithPrimitiveHref cannot be parsed and is ignored: "
                                    + entry );
            }
        }
    }

}