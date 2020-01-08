package ru.otus.testing.example.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.PrintStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsoleContext {
    private PrintStream out = System.out;
    private InputStream in = System.in;
}
