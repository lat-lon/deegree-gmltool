package de.deegreeenterprise.tools.featurestoresql.loader;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class GmlLoaderHelpUsage {

    public static void printUsage() {
        System.out.println( "Import of GML to deegree WFS" );
        System.out.println( "Options (mandatory):" );
        System.out.println( "   -pathToFile=/path/tp/file - the path to the GML file to import" );
        System.out.println( "   -workspaceName=inspire - the name of the deegree workspace used for the import. Must be located at DEEGREE_ROOT" );
        System.out.println( "   -sqlFeatureStoreId=cadastralparcels - the id of the SQLFeatureStore in the workspace" );
        System.out.println();
        System.out.println( "Example:" );
        System.out.println( "-pathToFile=/path/to/cadastralparcels.gml -workspaceName=inspire -sqlFeatureStoreId=cadastralparcels" );
    }

}
