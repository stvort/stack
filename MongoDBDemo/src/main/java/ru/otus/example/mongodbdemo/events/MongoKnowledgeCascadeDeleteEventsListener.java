package ru.otus.example.mongodbdemo.events;

import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.example.mongodbdemo.model.Knowledge;
import ru.otus.example.mongodbdemo.repositories.StudentRepository;

@Component
public class MongoKnowledgeCascadeDeleteEventsListener extends AbstractMongoEventListener<Knowledge> {


    private final StudentRepository studentRepository;

    public MongoKnowledgeCascadeDeleteEventsListener(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Knowledge> event) {
        super.onAfterDelete(event);
        Document source = event.getSource();
        String id = source.get("_id").toString();
        studentRepository.removeExperienceArrayElementsById(id);
    }
}
