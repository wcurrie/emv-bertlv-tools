package io.github.binaryfoo.controllers;

import io.github.binaryfoo.EmvTags;
import io.github.binaryfoo.tlv.Tag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoisyTagList {

    public static final List<Tag> NOISY = Arrays.asList(
            EmvTags.IAC_DEFAULT,
            EmvTags.IAC_DENIAL,
            EmvTags.IAC_ONLINE,
            EmvTags.CDOL_1,
            EmvTags.CDOL_2,
            EmvTags.DDOL,
            EmvTags.PDOL,
            EmvTags.APPLICATION_USAGE_CONTROL,
            EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE,
            EmvTags.ICC_PUBLIC_KEY_CERTIFICATE,
            EmvTags.SIGNED_STATIC_APPLICATION_DATA,
            EmvTags.SIGNED_DYNAMIC_APPLICATION_DATA
    );

    /**
     * Tags to show collapsed by default.
     */
    public static Map<Tag, Boolean> noisyOnes() {
        Map<Tag, Boolean> map = new HashMap<>();
        for (Tag tag : NOISY) {
            map.put(tag, true);
        }
        return map;
    }
}
