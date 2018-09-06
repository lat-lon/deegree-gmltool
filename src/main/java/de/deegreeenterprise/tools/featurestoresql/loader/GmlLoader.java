package de.deegreeenterprise.tools.featurestoresql.loader;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the command line interface of the GMLLoader.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
@SpringBootApplication
public class GmlLoader {

    public static void main( String[] args ) {
        SpringApplication app = new SpringApplication( GmlLoaderConfiguration.class );
        app.setBannerMode( Banner.Mode.OFF );

        if ( args.length == 0 || "--help".equals( args[0] ) || "-".equals( args[0] ) ) {
            printHelp();
        } else if ( args.length != 3 ) {
            printUnexpectedNumberOfParameters( args );
            printHelp();
        } else {
            app.run( args );
        }
    }

    private static void printHelp() {
        System.out.println( "Import of GML to deegree WFS" );
        System.out.println( "Expected parameters (in this order!):" );
        System.out.println( "   <pathToFile> - the path to the GML file to import" );
        System.out.println( "   <workspaceName> - the name of the deegree workspace used for the import. Must be located at DEEGREE_ROOT" );
        System.out.println( "   <sqlFeatureStoreId> - the id of the SQLFeatureStore in the workspace" );
        System.out.println();
        System.out.println( "Example:" );
        System.out.println( "pathToFile=/path/to/cadastralparcels.gml workspaceName=inspire sqlFeatureStoreId=cadastralparcels" );
    }

    private static void printUnexpectedNumberOfParameters( String[] args ) {
        System.out.println( "Number of arguments is invalid, must be exactly three but was " + args.length );
        System.out.println();
    }

}