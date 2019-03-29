package ru.otus.example.mongodbdemo.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.example.mongodbdemo.model.Knowledge;
import ru.otus.example.mongodbdemo.model.Student;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("KnowledgeRepository при наличии listener-ов в контексте ")
@ComponentScan("ru.otus.example.mongodbdemo.events")
public class KnowledgeRepositoryWithListenerTest extends AbstractRepositoryTest {

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @DisplayName("при удалении Knowledge должен удалить его из опыта студента")
    @Test
    public void shouldRemoveKnowledgeFromStudentExperienceWhenRemovingKnowledge() {

        // Загрузка студента и его пе
        List<Student> students = studentRepository.findAll();
        Student student = students.get(0);
        List<Knowledge> experience = student.getExperience();
        Knowledge firstKnowledge = experience.get(0);

        knowledgeRepository.delete(firstKnowledge);

        int expectedExperienceArrayLength = experience.size() - 1;
        long actualExperienceArrayLength = studentRepository.getExperienceArrayLengthByStudentId(student.getId());
        assertThat(actualExperienceArrayLength).isEqualTo(expectedExperienceArrayLength);

        Optional<Student> actualStudentOptional = studentRepository.findById(student.getId());
        assertThat(actualStudentOptional.get().getExperience().size()).isEqualTo(expectedExperienceArrayLength);

    }
}
