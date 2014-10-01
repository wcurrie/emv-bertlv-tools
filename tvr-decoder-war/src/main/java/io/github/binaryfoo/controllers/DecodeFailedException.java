package io.github.binaryfoo.controllers;

public class DecodeFailedException extends RuntimeException {
    public DecodeFailedException(String message) {
        super(message);
    }
}
