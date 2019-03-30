/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2017 ForgeRock AS.
 */
/**
 * jon.knight@forgerock.com
 *
 * A node that requests actions from a native application. Assumes REST-based authentication only.
 */


package org.forgerock.openam.auth.nodes;

import com.google.inject.assistedinject.Assisted;
import com.sun.identity.shared.debug.Debug;

import org.forgerock.json.JsonValue;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.*;
import org.forgerock.openam.core.CoreWrapper;
import javax.inject.Inject;
import java.util.*;


@Node.Metadata(outcomeProvider = SingleOutcomeNode.OutcomeProvider.class,
        configClass = QueryParamsNode.Config.class)
public class QueryParamsNode extends SingleOutcomeNode {

    public interface Config {
//        @Attribute(order = 100)
//        default String variable() { return "queryParams"; }
        @Attribute(order = 100)
        Map<String, String> properties();
    }


    private final Config config;
    private final CoreWrapper coreWrapper;
    private final static String DEBUG_FILE = "QueryParamsNode";
    protected Debug debug = Debug.getInstance(DEBUG_FILE);

    /**
     * Guice constructor.
     * @param config The node configuration.
     * @throws NodeProcessException If there is an error reading the configuration.
     */
    @Inject
    public QueryParamsNode(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        this.coreWrapper = coreWrapper;
    }



    @Override
    public Action process(TreeContext context) {
        JsonValue newSharedState = context.sharedState.copy();
//        newSharedState.put(config.variable(), context.request.parameters.);

        Set<String> configKeys = config.properties().keySet();
        for (String key : configKeys) {

            String propertyValue = config.properties().get(key);
            String value = "";

            if (context.request.parameters.get(propertyValue) != null)
                value = context.request.parameters.get(propertyValue).get(0);

            newSharedState.put(key, value);
        }

        return goToNext().replaceSharedState(newSharedState).build();


    }

}


