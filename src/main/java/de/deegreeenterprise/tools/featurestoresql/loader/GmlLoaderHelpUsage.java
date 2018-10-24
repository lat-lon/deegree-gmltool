package de.deegreeenterprise.tools.featurestoresql.loader;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class GmlLoaderHelpUsage {

    public static void printUsage() {
        System.out.println( "Usage: java -jar deegree-tools-featurestore-sql.jar GmlLoader [options]" );
        System.out.println( "Description: Imports a GML file directly into a given deegree SQLFeatureStore" );
        System.out.println();
        System.out.println( "options (all are mandatory):" );
        System.out.println( " -pathToFile=<path/to/gmlfile> - the path to the GML file to import" );
        System.out.println( " -workspaceName=<workspace_identifier> - the name of the deegree workspace used for the import. Must be located at default DEEGREE_WORKSPACE_ROOT directory" );
        System.out.println( " -sqlFeatureStoreId=<feature_store_identifier> - the ID of the SQLFeatureStore in the given workspace" );
        System.out.println();
        System.out.println( "Example:" );
        System.out.println( " java -jar deegree-tools-featurestore-sql.jar GmlLoader -pathToFile=/path/to/cadastralparcels.gml -workspaceName=inspire -sqlFeatureStoreId=cadastralparcels" );
    }

}
