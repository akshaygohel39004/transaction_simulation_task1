package org.example.exceptions;

public class ThrowExcpetions {
    public static void throwUnAuthorized() throws Exception {
        throw new Exception("401:Unauthorized , Kindly do login first");
    }

    public static  void throwNotFound(String subject) throws Exception {
        throw new Exception("404:"+subject+"Not Found");
    }

    public static void general(String message) throws Exception {
        throw new Exception(message);
    }
}
