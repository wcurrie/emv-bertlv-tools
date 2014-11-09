package io.github.binaryfoo.controllers

import io.github.binaryfoo.DecodedData
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

public class DecodedAsStringMatcher(private val raw: String) : TypeSafeMatcher<List<DecodedData>>() {

    override fun matchesSafely(decodedData: List<DecodedData>): Boolean {
        return raw == toString(decodedData, "")
    }

    override fun describeTo(description: Description) {
        description.appendText(raw)
    }

    override fun describeMismatchSafely(item: List<DecodedData>, mismatchDescription: Description) {
        mismatchDescription.appendText(toString(item, ""))
    }

    private fun toString(decoded: List<DecodedData>, indent: String): String {
        val b = StringBuilder()
        for (d in decoded) {
            b.append(indent)
            if ("" != d.rawData) {
                b.append(d.rawData).append(": ")
            }
            b.append(d.getDecodedData()).append("\n")
            b.append(toString(d.children, indent + "  "))
        }
        return b.toString()
    }

    class object {

        public fun decodedAsString(raw: String): TypeSafeMatcher<List<DecodedData>> {
            return DecodedAsStringMatcher(raw)
        }
    }
}
