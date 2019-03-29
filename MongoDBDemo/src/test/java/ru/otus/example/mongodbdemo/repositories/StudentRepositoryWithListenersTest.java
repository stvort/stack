package ru.otus.example.mongodbdemo.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.example.mongodbdemo.model.Knowledge;
import ru.otus.example.mongodbdemo.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StudentRepository при наличии listener-ов в контексте ")
@ComponentScan("ru.otus.example.mongodbdemo.events")
public class StudentRepositoryWithListenersTest extends AbstractRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @DisplayName("должен корректно сохранять студента с отсутствующими в БД знаниями")
    @Test
    public void shouldCorrectSaveStudentWithNewKnowledge(){
        Student student = new Student("Student #2", new Knowledge("Knowledge #3"));
        Student actual = studentRepository.save(student);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(student.getName());
    }

}
