package de.deegreeenterprise.tools.featurestoresql.config;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class FeatureStoreConfigUsagePrinter {

    public static void printUsage() {
        System.out.println( "Usage: java -jar deegree-tools-featurestore-sql.jar FeatureStoreConfigLoader -schemaUrl=<url-or-path/to/file> [options]" );
        System.out.println();
        System.out.println( "options:" );
        System.out.println( " -format={deegree|ddl|all}" );
        System.out.println( " -srid=<epsg_code>" );
        System.out.println( " -idtype={int|uuid}" );
        System.out.println( " -mapping={relational|blob}" );
        System.out.println( " -dialect={postgis|oracle}" );
        System.out.println( " -listOfPropertiesWithPrimitiveHref=<path/to/file>" );
        System.out.println();
        System.out.println( "The option listOfPropertiesWithPrimitiveHref references a file listing properties which are written with primitive instead of feature mappings (see deegree-webservices documentation and README of this tool for further information):" );
        System.out.println( "---------- begin file ----------" );
        System.out.println( "# lines beginning with an # are ignored" );
        System.out.println( "# property with namespace binding" );
        System.out.println( "{http://inspire.ec.europa.eu/schemas/ps/4.0}designation" );
        System.out.println( "# property without namespace binding" );
        System.out.println( "designation" );
        System.out.println( "# empty lines are ignored" );
        System.out.println();
        System.out.println( "# leading and trailing white spaces are ignored" );
        System.out.println( "---------- end file ----------" );
    }

}
