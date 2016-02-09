package io.github.binaryfoo.controllers

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.tlv.Tag
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class DecodedDataExtensionTest {

    @Test fun hexDumpWithTlv() {
        val decoded = DecodedData.fromTlv(BerTlv.newInstance(Tag.fromHex("9F1b"), "112233"), EmvTags.METADATA, "", 40, 45)

        assertThat(DecodedDataExtension.hexDumpPositionToJson(decoded), `is`("""{"position":{"start":40,"end":44},"tag":{"start":40,"end":41,"value":"9F1B"},"length":{"start":42,"end":42,"value":"3"}}"""))
    }

    @Test fun hexDumpPrimitive() {
        val decoded = DecodedData.primitive("label", "", 40, 42)

        assertThat(DecodedDataExtension.hexDumpPositionToJson(decoded), `is`("""{"position":{"start":40,"end":41}}"""))
    }
}
