package ru.otus.example.springbatch.shell;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.example.springbatch.config.AppProps;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private static final String OUTPUT_FILE_NAME = "outputFileName";
    private static final String INPUT_FILE_NAME = "inputFileName";

    private final AppProps appProps;
    private final Job importUserJob;

    private final JobLauncher jobLauncher;
    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;
    private final JobRegistry jobRegistry;


    private long lastJobInstanceId;

    //http://localhost:8080/h2-console/
    @SneakyThrows
    @ShellMethod(value = "startMigrationJob", key = "sm")
    public void startMigrationJob() {
        Long importUserJobExecutionId = jobOperator.start("importUserJob",
                INPUT_FILE_NAME + "=" + appProps.getInputFileName() + "\n" +
                        OUTPUT_FILE_NAME + "=" + appProps.getOutputFileName()
        );

/*
        JobExecution execution = jobLauncher.run(importUserJob, new JobParametersBuilder()
                .addString(INPUT_FILE_NAME, appProps.getInputFileName())
                .addString(OUTPUT_FILE_NAME, appProps.getOutputFileName())
                .toJobParameters());
*/
    }

    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getJobInstances("importUserJob", 0, 100));
    }
}
