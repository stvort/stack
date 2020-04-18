package ru.otus.homework.services;

import java.util.List;

public class InMemoryDataReader implements DataReader {
    @Override
    public List<String> readData() {
        return List.of("Хорошие данные", "Так себе данные", "Еще одни хорошие данные", "Прямо плохие данные");
    }
}
