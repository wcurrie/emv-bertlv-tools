package io.github.binaryfoo.controllers;

import io.github.binaryfoo.DecodedData;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class DecodedAsStringMatcher extends TypeSafeMatcher<List<DecodedData>> {

    private final String raw;

    public DecodedAsStringMatcher(String raw) {
        this.raw = raw;
    }

    public static TypeSafeMatcher<List<DecodedData>> decodedAsString(String raw) {
        return new DecodedAsStringMatcher(raw);
    }

    @Override
    protected boolean matchesSafely(List<DecodedData> decodedData) {
        return raw.equals(toString(decodedData, ""));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(raw);
    }

    @Override
    protected void describeMismatchSafely(List<DecodedData> item, Description mismatchDescription) {
        mismatchDescription.appendText(toString(item, ""));
    }

    private String toString(List<DecodedData> decoded, String indent) {
        StringBuilder b = new StringBuilder();
        for (DecodedData d : decoded) {
            b.append(indent);
            if (!"".equals(d.getRawData())) {
                b.append(d.getRawData()).append(": ");
            }
            b.append(d.getDecodedData()).append("\n");
            b.append(toString(d.getChildren(), indent + "  "));
        }
        return b.toString();
    }
}
