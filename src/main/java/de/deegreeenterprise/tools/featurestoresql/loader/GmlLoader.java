package de.deegreeenterprise.tools.featurestoresql.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the command line interface of the GMLLoader.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
@SpringBootApplication
public class GmlLoader {

    public static void main( String[] args )
                            throws Exception {
        SpringApplication.exit( SpringApplication.run( GmlLoaderConfiguration.class, args ) );
    }

}