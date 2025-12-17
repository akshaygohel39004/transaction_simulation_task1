package org.example.exceptions;

public class NotFoundException extends Exception {
    NotFoundException(String subject) {
        super("404:"+subject+"Not Found");
    }
}
