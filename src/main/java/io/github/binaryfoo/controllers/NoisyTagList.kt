package io.github.binaryfoo.controllers

import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.tlv.Tag

import java.util.Arrays
import java.util.HashMap

class NoisyTagList {
    companion object {

        val NOISY: List<Tag> = listOf(EmvTags.IAC_DEFAULT,
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
                EmvTags.SIGNED_DYNAMIC_APPLICATION_DATA)

        /**
         * Tags to show collapsed by default.
         */
        fun noisyOnes(): Map<Tag, Boolean> {
            val map = HashMap<Tag, Boolean>()
            for (tag in NOISY) {
                map.put(tag, true)
            }
            return map
        }
    }
}
