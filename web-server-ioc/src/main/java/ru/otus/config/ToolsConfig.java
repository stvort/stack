package ru.otus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import java.io.IOException;

@AppComponentsContainerConfig(order = 0)
public class ToolsConfig {
    private static final String TEMPLATES_DIR = "/templates/";


    @AppComponent(order = 0, name = "templateProcessor")
    public TemplateProcessor templateProcessor() throws IOException {
        return new TemplateProcessorImpl(TEMPLATES_DIR);
    }

    @AppComponent(order = 1, name = "gson")
    public Gson gson(){
        return new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

}
