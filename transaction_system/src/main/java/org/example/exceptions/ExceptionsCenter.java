package org.example.exceptions;

public class ExceptionsCenter {
    public static void throwUnAuthorized() throws Exception {
        throw new UnauthorizedException();
    }

    public static  void throwNotFound(String subject) throws Exception {
        throw new NotFoundException(subject);
    }

    public static void general(String message) throws Exception {

        throw new GeneralException(message);
    }

    public static void insufficientBalance() throws Exception {
        throw new InsufficientBalanceException();
    }
}
