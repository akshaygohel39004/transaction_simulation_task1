package org.example.exceptions;

public class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException(){
        super("Insufficient Balance");
    }
}
