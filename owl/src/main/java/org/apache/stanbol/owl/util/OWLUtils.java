/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.stanbol.owl.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * A set of utility methods for the manipulation of OWL API objects.
 */
public class OWLUtils {

    /**
     * If the ontology is named, this method will return its logical ID, otherwise it will return the location
     * it was retrieved from (which is still unique).
     * 
     * @param o
     * @return
     */
    public static IRI getIdentifyingIRI(OWLOntology o) {
        String originalIri;
        if (o.isAnonymous()) {
            originalIri = o.getOWLOntologyManager().getOntologyDocumentIRI(o).toString();
        } else {
            originalIri = o.getOntologyID().getOntologyIRI().toString();
        }
        while (originalIri.endsWith("#") || originalIri.endsWith("?"))
            originalIri = originalIri.substring(0, originalIri.length() - 1);
        // try {
        // if (originalIri.endsWith("#")) originalIri = originalIri.substring(0,
        // originalIri.length() - 1) + URLEncoder.encode("#", "UTF-8");
        // else if (originalIri.endsWith("?")) originalIri = originalIri.substring(0,
        // originalIri.length() - 1)
        // + URLEncoder.encode("?", "UTF-8");
        // } catch (UnsupportedEncodingException e) {
        // // That cannot be.
        // }
        return IRI.create(originalIri);
    }
}
