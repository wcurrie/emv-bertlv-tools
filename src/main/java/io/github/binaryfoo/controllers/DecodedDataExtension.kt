package io.github.binaryfoo.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.tlv.BerTlv

class DecodedDataExtension {

    companion object {
        @JvmStatic fun hexDumpPositionToJson(d: DecodedData): String {
            val mapper = ObjectMapper()
            val position = if (d.tlv == null) {
                mapOf("position" to toStartAndEnd(d.positionInHexDump))
            } else {
                mapOf(
                    "position" to toStartAndEnd(d.positionInHexDump),
                    "tag" to toStartAndEnd(d.tagPositionInHexDump!!, (d.tlv as BerTlv).tag.hexString),
                    "length" to toStartAndEnd(d.lengthPositionInHexDump!!, (d.tlv as BerTlv).length)
                )
            }
            try {
                return mapper.writeValueAsString(position)
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
        }

        fun toStartAndEnd(range: ClosedRange<Int>): Map<String, Int> {
            return mapOf("start" to range.start, "end" to range.endInclusive)
        }

        fun toStartAndEnd(range: ClosedRange<Int>, value: Any): Map<String, Any> {
            return mapOf("start" to range.start, "end" to range.endInclusive, "value" to value.toString())
        }
    }
}
