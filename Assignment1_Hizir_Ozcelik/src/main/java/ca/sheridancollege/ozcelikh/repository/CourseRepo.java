package ca.sheridancollege.ozcelikh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ozcelikh.beans.Course;

public interface CourseRepo extends JpaRepository<Course, Long>{


}
