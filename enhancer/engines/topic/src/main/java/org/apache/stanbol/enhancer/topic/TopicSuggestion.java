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
package org.apache.stanbol.enhancer.topic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Data transfer object for the individual topic classification results.
 */
public class TopicSuggestion {

    public final String uri;

    public final List<String> paths = new ArrayList<String>();

    public final float score;

    public TopicSuggestion(String uri, List<String> paths, float score) {
        this.uri = uri;
        if (paths != null) {
            this.paths.addAll(paths);
        }
        this.score = score;
    }

    public TopicSuggestion(String uri, float score) {
        this(uri, null, score);
    }

    @Override
    public String toString() {
        return String.format("TopicSuggestion(\"%s\", [%s], %f)", uri, StringUtils.join(paths, "\", \""),
            score);
    }
}