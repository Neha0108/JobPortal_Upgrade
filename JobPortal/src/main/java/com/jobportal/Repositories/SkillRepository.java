package com.jobportal.Repositories;

import com.jobportal.Entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findBySkillNameIn(Set<String> skillNames);
}
