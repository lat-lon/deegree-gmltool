package de.deegreeenterprise.tools.featurestoresql;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.deegreeenterprise.tools.featurestoresql.config.FeatureStoreLoaderConfigApp;
import de.deegreeenterprise.tools.featurestoresql.loader.GmlLoaderApp;

/**
 * Entry point of the command line interface of the GMLLoader.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
@SpringBootApplication
public class FeaturestoreSqlApp {

    public static void main( String[] args ) {
        if ( args.length == 0 || "--help".equals( args[0] ) || "-help".equals( args[0] )  || "-h".equals( args[0] ) ) {
            printUsage();
        } else if ( "featureStoreConfigLoader".equals( args[0] ) ) {
            FeatureStoreLoaderConfigApp.run( args );
        } else if ( "gmlLoader".equals( args[0] ) ) {
            GmlLoaderApp.run( args );
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println( "Includes tools to create SQLFeatureStore configurations and load  GML files" );
        System.out.println( "Use the keywords 'featureStoreConfigLoader' or 'gmlLoader' to differentiate between the two tools: " );
        System.out.println( "   featureStoreConfigLoader -h (Prints the usage of this tool)" );
        System.out.println( "   gmlLoader -h (Prints the usage of this tool)" );
    }

}