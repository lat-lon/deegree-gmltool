package de.deegreeenterprise.tools.featurestoresql.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Checks if all references can be resolved.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class FeatureReferenceChecker {

    /**
     * Checks if for all references a featureId exists.
     * 
     * @param featureIds
     *            list of featureIds, may be empty but never <code>null</code>
     * @param references
     *            list of references, may be empty but never <code>null</code>
     * @return the result of the check, never <code>null</code>
     */
    public FeatureReferenceCheckResult checkReferences( List<String> featureIds, List<String> references ) {
        List<String> unresolvableReferences = collectUnresolvableReferences( ensureNotNull( featureIds ),
                                                                             ensureNotNull( references ) );
        return new FeatureReferenceCheckResult( unresolvableReferences );
    }

    private List<String> collectUnresolvableReferences( List<String> featureIds, List<String> references ) {
        List<String> unresolvableReferences = new ArrayList<>();
        if ( featureIds != null && references != null ) {
            for ( String reference : references ) {
                String referenceId = parseReference( reference );
                if ( !featureIds.contains( referenceId ) )
                    unresolvableReferences.add( referenceId );
            }
        }
        return unresolvableReferences;
    }

    private String parseReference( String reference ) {
        if ( reference.startsWith( "#" ) )
            return reference.substring( 1 );
        return reference;
    }

    private List<String> ensureNotNull( List<String> list ) {
        return list == null ? Collections.emptyList() : list;
    }

}
