package ru.otus.example.mongoDbDemo.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.example.mongoDbDemo.model.Knowledge;
import ru.otus.example.mongoDbDemo.repositories.StudentRepository;

@Component
@RequiredArgsConstructor
public class MongoKnowledgeCascadeDeleteEventsListener extends AbstractMongoEventListener<Knowledge> {

    private final StudentRepository studentRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Knowledge> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val id = source.get("_id").toString();
        studentRepository.removeExperienceArrayElementsById(id);
    }
}
