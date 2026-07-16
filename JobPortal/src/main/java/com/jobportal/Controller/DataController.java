package com.jobportal.Controller;

import com.jobportal.Entities.EmploymentType;
import com.jobportal.Entities.JobCategory;
import com.jobportal.Entities.Skill;
import com.jobportal.Entities.WorkMode;
import com.jobportal.Repositories.SkillRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final SkillRepository skillRepo;

    public DataController(SkillRepository skillRepo) {
        this.skillRepo = skillRepo;
    }

    @GetMapping("/skills")
    public List<Skill> getSkills() {
        return skillRepo.findAll();
    }

    @GetMapping("/categories")
    public JobCategory[] getCategories() {
        return JobCategory.values();
    }

    @GetMapping("/employment-types")
    public EmploymentType[] getEmploymentTypes() {
        return EmploymentType.values();
    }

    @GetMapping("/work-modes")
    public WorkMode[] getWorkModes() {
        return WorkMode.values();
    }
}
