package io.github.binaryfoo.controllers;

import io.github.binaryfoo.EmvTags;
import io.github.binaryfoo.tlv.Tag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NoisyTagList {

    /**
     * Tags to show collapsed by default.
     */
    public static Map<Tag, Boolean> noisyOnes() {
        Map<Tag, Boolean> map = new HashMap<>();
        for (Tag tag : Arrays.asList(EmvTags.IAC_DEFAULT, EmvTags.IAC_DENIAL, EmvTags.IAC_ONLINE, EmvTags.CDOL_1, EmvTags.CDOL_2)) {
            map.put(tag, true);
        }
        return map;
    }
}
