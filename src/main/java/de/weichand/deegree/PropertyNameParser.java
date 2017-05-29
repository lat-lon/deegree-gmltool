package de.weichand.deegree;

import javax.xml.namespace.QName;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Parser for property names from a file.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class PropertyNameParser {

    /**
     * Parses the property names from the passed file. The properties are parsed line by line. empty lines are ignored.
     * @param pathToFile
     * @return
     */
    public static List<QName> parsePropertiesWithPrimitiveHref( String pathToFile ) {
        try (Stream<String> stream = Files.lines( Paths.get( pathToFile ) )) {
            ArrayList<QName> properties = new ArrayList<>();
            List<String> list = stream.collect( Collectors.toList() );
            parseList( properties, list );
            return properties;
        } catch ( Exception e ) {
            System.out.println( "Referenced listOfPropertiesWithPrimitiveHref cannot be parsed and is ignored!" );
            return null;
        }
    }

    private static void parseList( ArrayList<QName> properties, List<String> list ) {
        for ( String entry : list ) {
            parseEntry( properties, entry );
        }
    }

    private static void parseEntry( ArrayList<QName> properties, String entry ) {
        String[] splitEntry = entry.split( "\",\"" );
        if ( splitEntry.length == 2 ) {
            String namespaceUri = splitEntry[0].replace( "\"", "" );
            String localPart = splitEntry[1].replace( "\"", "" );
            QName qName = new QName( namespaceUri, localPart );
            properties.add( qName );
        } else
            System.out.println( "One line of referenced listOfPropertiesWithPrimitiveHref cannot be parsed and is ignored: "
                                + entry );
    }

}