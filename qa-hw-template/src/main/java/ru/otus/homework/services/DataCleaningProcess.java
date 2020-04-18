package ru.otus.homework.services;

import ru.otus.homework.exceptions.DataReadingErrorException;

import java.util.List;
import java.util.stream.Collectors;

public class DataCleaningProcess {
    private final DataReader reader;
    private final DataWriter writer;


    public DataCleaningProcess(DataReader reader, DataWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void execute() {
        List<String> data = readData();
        List<String> cleanData = data.stream().filter(s -> s.contains("орошие")).collect(Collectors.toList());
        writer.writeData(cleanData);
    }

    private List<String> readData() {
        try {
            return reader.readData();
        } catch (Exception e) {
            throw new DataReadingErrorException(e);
        }
    }
}
