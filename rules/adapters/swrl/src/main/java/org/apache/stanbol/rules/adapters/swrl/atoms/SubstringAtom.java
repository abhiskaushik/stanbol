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
package org.apache.stanbol.rules.adapters.swrl.atoms;

import java.util.ArrayList;
import java.util.List;

import org.apache.stanbol.rules.adapters.AbstractAdaptableAtom;
import org.apache.stanbol.rules.adapters.swrl.ArgumentSWRLAtom;
import org.apache.stanbol.rules.adapters.swrl.HigherOrderSWRLAtom;
import org.apache.stanbol.rules.base.api.RuleAtom;
import org.apache.stanbol.rules.base.api.RuleAtomCallExeption;
import org.apache.stanbol.rules.base.api.Symbols;
import org.apache.stanbol.rules.base.api.UnavailableRuleObjectException;
import org.apache.stanbol.rules.base.api.UnsupportedTypeForExportException;
import org.apache.stanbol.rules.manager.atoms.NumericFunctionAtom;
import org.apache.stanbol.rules.manager.atoms.StringFunctionAtom;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary;

/**
 * It adapts any SumAtom to the <http://www.w3.org/2005/xpath-functions#substring> XPath function call in
 * SWRL.
 * 
 * @author anuzzolese
 * 
 */
public class SubstringAtom extends AbstractAdaptableAtom {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T adapt(RuleAtom ruleAtom) throws RuleAtomCallExeption,
                                         UnavailableRuleObjectException,
                                         UnsupportedTypeForExportException {

        String substring_result = "substrig_result" + System.currentTimeMillis();

        org.apache.stanbol.rules.manager.atoms.SubstringAtom tmp = (org.apache.stanbol.rules.manager.atoms.SubstringAtom) ruleAtom;

        StringFunctionAtom stringFunction = tmp.getStringFunctionAtom();
        NumericFunctionAtom start = tmp.getStart();
        NumericFunctionAtom length = tmp.getLength();

        OWLDataFactory factory = OWLManager.getOWLDataFactory();

        SWRLAtom stringFunctionAtom = (SWRLAtom) adapter.adaptTo(stringFunction, SWRLRule.class);
        SWRLAtom startAtom = (SWRLAtom) adapter.adaptTo(start, SWRLRule.class);
        SWRLAtom lengthAtom = (SWRLAtom) adapter.adaptTo(length, SWRLRule.class);

        SWRLDArgument retArgument = factory.getSWRLVariable(IRI.create(Symbols.variablesPrefix
                                                                       + substring_result));
        ;
        SWRLDArgument stringArgument;
        SWRLDArgument startArgument;
        SWRLDArgument lengthArgument;

        List<SWRLAtom> listOfArguments = new ArrayList<SWRLAtom>();

        if (stringFunctionAtom instanceof HigherOrderSWRLAtom) {
            stringArgument = ((HigherOrderSWRLAtom) stringFunctionAtom).getBindableArgument();

            listOfArguments.addAll(((HigherOrderSWRLAtom) stringFunctionAtom).getAtoms());
        } else if (stringFunctionAtom instanceof ArgumentSWRLAtom) {
            SWRLArgument swrlArgument = ((ArgumentSWRLAtom) stringFunctionAtom).getSwrlArgument();
            stringArgument = (SWRLDArgument) swrlArgument;
        } else {
            throw new org.apache.stanbol.rules.base.api.RuleAtomCallExeption(getClass());
        }

        if (startAtom instanceof HigherOrderSWRLAtom) {
            startArgument = ((HigherOrderSWRLAtom) startAtom).getBindableArgument();

            listOfArguments.addAll(((HigherOrderSWRLAtom) startAtom).getAtoms());
        } else if (startAtom instanceof ArgumentSWRLAtom) {
            SWRLArgument swrlArgument = ((ArgumentSWRLAtom) startAtom).getSwrlArgument();
            startArgument = (SWRLDArgument) swrlArgument;
        } else {
            throw new org.apache.stanbol.rules.base.api.RuleAtomCallExeption(getClass());
        }

        if (lengthAtom instanceof HigherOrderSWRLAtom) {
            lengthArgument = ((HigherOrderSWRLAtom) lengthAtom).getBindableArgument();

            listOfArguments.addAll(((HigherOrderSWRLAtom) lengthAtom).getAtoms());
        } else if (lengthAtom instanceof ArgumentSWRLAtom) {
            SWRLArgument swrlArgument = ((ArgumentSWRLAtom) lengthAtom).getSwrlArgument();
            lengthArgument = (SWRLDArgument) swrlArgument;
        } else {
            throw new org.apache.stanbol.rules.base.api.RuleAtomCallExeption(getClass());
        }

        List<SWRLDArgument> swrldArguments = new ArrayList<SWRLDArgument>();
        swrldArguments.add(retArgument);
        swrldArguments.add(stringArgument);
        swrldArguments.add(startArgument);
        swrldArguments.add(lengthArgument);

        return (T) factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.SUBSTRING.getIRI(), swrldArguments);

    }

}
