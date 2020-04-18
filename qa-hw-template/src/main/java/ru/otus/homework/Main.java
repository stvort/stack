package ru.otus.homework;

import ru.otus.homework.services.*;

/*
0. Подключить зависимости JUnit5, Mockito (AssertJ по желанию)
1. Создать структуру каталогов для тестов src/test/java
2. Отметить папку java в качестве Test Sources Root (Правой кнопкой на java -> Mark Direcory as -> Test Sources Root)
3. Создать тестовые классы
4. Реализовать указанные ниже сценарии
5. Развесить @DisplayName над классом и методами
6. Дать имена методам согласно тестовым сценариям (should*)

Сценарии:
1. Проверить, что в DataWriter#writeData приходят, корректно очищенные данные
В качестве DataReader можно использовать spy для InMemoryDataReader
В качестве DataWriter использовать mock интерфейса

2. Проверить, что если при чтении данных возникнет ошибка, то будет исключение DataReadingErrorException

3. Проверить, что InMemoryDataWriter действительно пишет данные ожидаемымы образом (каждый элемент с новой строки)
*/

public class Main {
    public static void main(String[] args) {
        StringBuilder cleaningResult = new StringBuilder();
        DataWriter writer = new InMemoryDataWriter(cleaningResult);
        DataReader reader = new InMemoryDataReader();
        DataCleaningProcess process = new DataCleaningProcess(reader, writer);
        process.execute();

        System.out.println(cleaningResult.toString());
    }
}
