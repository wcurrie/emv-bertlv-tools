package io.github.binaryfoo.controllers

import io.github.binaryfoo.DecodedData
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import io.github.binaryfoo.toSimpleString

class DecodedAsStringMatcher(private val raw: String) : TypeSafeMatcher<List<DecodedData>>() {

    override fun matchesSafely(decodedData: List<DecodedData>): Boolean {
        return raw == toString(decodedData)
    }

    override fun describeTo(description: Description) {
        description.appendText(raw)
    }

    override fun describeMismatchSafely(item: List<DecodedData>, mismatchDescription: Description) {
        mismatchDescription.appendText(toString(item))
    }

    private fun toString(decoded: List<DecodedData>): String = decoded.toSimpleString("")

    companion  object {
        @JvmStatic fun decodedAsString(raw: String): TypeSafeMatcher<List<DecodedData>> = DecodedAsStringMatcher(raw)
    }
}
