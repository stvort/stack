package ru.otus.homework.exceptions;

public class DataReadingErrorException extends RuntimeException {
    public DataReadingErrorException(Throwable cause) {
        super(cause);
    }
}
