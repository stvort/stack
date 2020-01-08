package ru.otus.testing.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {
    private final IOService ioService;


    @Override
    public void readTwoDigitsAndMultiply() {
        int d1 = Integer.parseInt(ioService.readString());
        int d2 = Integer.parseInt(ioService.readString());
        multiplyAndOutResult(d1, d2);
    }

    @Override
    public void readTwoDigitsAndMultiply(String prompt) {
        ioService.out(prompt);
        readTwoDigitsAndMultiply();
    }

    @Override
    public void multiplyTwoDigits(String prompt, int d1, int d2) {
        ioService.out(prompt);
        multiplyAndOutResult(d1, d2);
    }

    @SneakyThrows
    @Override
    public void longCalculations() {
        ioService.out("Ответ на главный вопрос жизни, вселенной и всего такого");
        Thread.sleep(5000);
        ioService.out("<<42>>");
    }

    private void multiplyAndOutResult(int d1, int d2) {
        ioService.out(String.format("%d * %d = %d", d1, d2, d1 * d2));
    }
}
