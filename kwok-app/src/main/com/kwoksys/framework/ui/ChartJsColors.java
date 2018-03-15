/*
 * Copyright 2015 Kwoksys
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kwoksys.framework.ui;

import java.util.Arrays;
import java.util.List;

/**
 * ChartJsColors
 */
public class ChartJsColors {

    private List<String> colors = Arrays.asList(
            "#5B90BF", "#F7464A", "#a3be8c", "#D378E3", "#FDB45C", "#247C41", "#949FB1", "#6666CC", "#4D5360", "#80C8F6",
            "#d08770", "#CC6699", "#E6B02D", "#6C6C6C", "#96b5b4", "#6EE365", "#b48ead", "#ab7967", "#5AC47D");

    private int count = 0;

    public String getColor() {
        return colors.get(count);
    }

    public int incrCount() {
        if (count >= colors.size()-1) {
            count = 0;
        } else {
            count++;
        }
        return count;
    }
}
