package ru.otus.example.mongodbdemo.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.example.mongodbdemo.model.Knowledge;
import ru.otus.example.mongodbdemo.model.Teacher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TeacherRepository должен ")
public class TeacherRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @DisplayName(" возвращать корректный список знаний преподавателя")
    @Test
    public void shouldReturnCorrectKnowledgeList(){
        List<Teacher> teachers = teacherRepository.findAll();
        Teacher teacher = teachers.get(0);
        List<Knowledge> experience = teacher.getExperience();
        assertThat(experience).isNotNull().hasSize(3);

        List<Knowledge> actualExperience = teacherRepository.getTeacherExperienceById(teacher.getId());
        assertThat(actualExperience).containsExactlyInAnyOrder(experience.toArray(new Knowledge[experience.size()]));

    }
}
