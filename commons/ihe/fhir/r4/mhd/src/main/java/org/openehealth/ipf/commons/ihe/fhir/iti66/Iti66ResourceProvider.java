/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti66;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Resource Provider for MHD (ITI-66)
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti66ResourceProvider extends AbstractPlainProvider {

    @SuppressWarnings("unused")
    @Search(type = DocumentManifest.class)
    public IBundleProvider documentManifestSearch(
            @RequiredParam(name = DocumentManifest.SP_PATIENT, chainWhitelist = {"", Patient.SP_IDENTIFIER}) ReferenceParam patient,
            @OptionalParam(name = DocumentManifest.SP_CREATED) DateRangeParam created,
            @OptionalParam(name = DocumentManifest.SP_AGENT, chainWhitelist = { Practitioner.SP_FAMILY, Practitioner.SP_GIVEN }) ReferenceAndListParam author,
            @OptionalParam(name = DocumentManifest.SP_TYPE) TokenOrListParam type,
            @OptionalParam(name = DocumentManifest.SP_SOURCE) TokenOrListParam source,
            @OptionalParam(name = DocumentManifest.SP_STATUS) TokenOrListParam status,
            // Extension to ITI-66
            @OptionalParam(name = IAnyResource.SP_RES_ID) TokenParam resourceId,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {


        Iti66SearchParameters searchParameters = Iti66SearchParameters.builder()
                .created(created)
                .type(type)
                .source(source)
                .status(status)
                ._id(resourceId)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .fhirContext(getFhirContext())
                .build();

        searchParameters.setAuthor(author);

        String chain = patient.getChain();
        if (Patient.SP_IDENTIFIER.equals(chain)) {
            searchParameters.setPatientIdentifier(patient.toTokenParam(getFhirContext()));
        } else if (chain == null || chain.isEmpty()) {
            searchParameters.setPatientReference(patient);
        }

        // Run down the route
        return requestBundleProvider(null, searchParameters, httpServletRequest, httpServletResponse);
    }

    /**
     * Handles DocumentManifest Retrieve. This is not an actual part of the ITI-66 specification, but in the
     * context of restful FHIR IHE transaction it makes sense to be able to retrieve a DocumentManifest by
     * its resource ID.
     *
     * @param id resource ID
     * @param httpServletRequest servlet request
     * @param httpServletResponse servlet response
     * @return {@link DocumentManifest} resource
     */
    @SuppressWarnings("unused")
    @Read(version = true, type = DocumentManifest.class)
    public DocumentManifest documentManifestRetrieve(
            @IdParam IdType id,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (id == null) throw new InvalidRequestException("Must provide ID with READ request");
        // Run down the route
        return requestResource(id, DocumentManifest.class, httpServletRequest, httpServletResponse);
    }

}
