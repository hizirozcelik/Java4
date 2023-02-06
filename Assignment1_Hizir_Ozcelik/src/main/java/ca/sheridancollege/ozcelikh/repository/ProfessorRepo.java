package ca.sheridancollege.ozcelikh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ozcelikh.beans.Professor;

public interface ProfessorRepo extends JpaRepository<Professor, Long>{

}
