package org.example.exceptions;

public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
        super("401:Unauthorized , Kindly do login first");
    }
}
