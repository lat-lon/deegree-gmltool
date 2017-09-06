package de.deegreeenterprise.tools.featurestoresql.loader;

import java.util.List;

/**
 * Encapsulates the result of the {@link FeatureReferenceChecker}
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class FeatureReferenceCheckResult {

    private final List<String> unresolvableReferences;

    /**
     * @param unresolvableReferences
     *            a list of unresolvable reference, may be empty but never <code>null</code>
     */
    public FeatureReferenceCheckResult( List<String> unresolvableReferences ) {
        this.unresolvableReferences = unresolvableReferences;
    }

    /**
     * @return <code>true</code> if the {@link FeatureReferenceChecker} did not found any unresolvable references,
     *         <code>false</code> otherwise
     */
    public boolean isValid() {
        return unresolvableReferences.size() == 0;
    }

    /**
     * @return the detected unresolvable references, may be empty but never <code>null</code>
     */
    public List<String> getUnresolvableReferences() {
        return unresolvableReferences;
    }

}