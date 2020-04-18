package ru.otus.homework.services;

import java.util.List;

public class InMemoryDataWriter implements DataWriter {

    private final StringBuilder implicitData;

    public InMemoryDataWriter(StringBuilder implicitData) {
        this.implicitData = implicitData;
    }

    @Override
    public void writeData(List<String> data) {
        implicitData.setLength(0);
        data.forEach(s -> implicitData.append(s).append("\n"));
    }
}
