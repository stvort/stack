package ru.otus.example.mongoDbDemo.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.MappingException;
import ru.otus.example.mongoDbDemo.model.Knowledge;
import ru.otus.example.mongoDbDemo.model.Student;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StudentRepository при отсутствии listener-ов в контексте ")
class StudentRepositoryWithoutListenerTest extends AbstractRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @DisplayName("должен кидать MappingException во время сохранения студента с отсутствующими в БД знаниями")
    @Test
    void shouldThrowMappingExceptionWhenSaveStudentWithNewKnowledge(){
        val student = new Student("Student #2", new Knowledge("Knowledge #3"));
        assertThatThrownBy(() -> studentRepository.save(student)).isInstanceOf(MappingException.class);
    }
}