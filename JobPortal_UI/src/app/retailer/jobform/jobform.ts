import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  FormsModule
} from '@angular/forms';

import { Recruiter } from '../../service/recruiter';
import { DataService } from '../../service/data-service';

import { JobDTO } from '../../interface/job-request';
import { Skills } from '../../interface/skills';

@Component({
  selector: 'app-jobform',
  imports: [CommonModule,ReactiveFormsModule,FormsModule],
  templateUrl: './jobform.html',
  styleUrl: './jobform.css'
})
export class Jobform implements OnInit {

  @Input() editJob: JobDTO | null = null;

  @Output() saved = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private recruiterService = inject(Recruiter);
  private dataService = inject(DataService);

  loading = false;
  error = '';

  employmentTypes: string[] = [];
  categories: string[] = [];
  workModes: string[] = [];

  skills: Skills[] = [];
  selectedSkills: Skills[] = [];

  form = this.fb.group({

    jobTitle: ['', Validators.required],

    jobDescription: ['', Validators.required],

    jobLocation: ['', Validators.required],

    employmentType: ['', Validators.required],

    category: ['', Validators.required],

    workMode: ['', Validators.required],

    experienceRequired: [
      0,
      [Validators.required, Validators.min(0)]
    ],

    minSalary: [
      0,
      Validators.required
    ],

    maxSalary: [
      0,
      Validators.required
    ],

    vacancies: [
      1,
      [Validators.required, Validators.min(1)]
    ],

    benefits: [''],

    active: [true],

    expiryDate: ['', Validators.required],

    skillIds: [[] as number[]]

  });

  get isEdit(): boolean {
    return this.editJob != null;
  }

  ngOnInit(): void {

    this.loadMasterData();

    if (this.editJob) {
      this.patchForm();
    }

  }

  loadMasterData() {

    this.dataService.getSkills().subscribe(data => {

      this.skills = data;

      if (this.editJob?.skillIds) {

        this.selectedSkills =
          this.skills.filter(
            s => this.editJob!.skillIds!.includes(s.skillId)
          );

      }

    });

    this.dataService.getCategories()
      .subscribe(data => this.categories = data);

    this.dataService.getEmploymentTypes()
      .subscribe(data => this.employmentTypes = data);

    this.dataService.getWorkModes()
      .subscribe(data => this.workModes = data);

  }

  patchForm() {

    const j = this.editJob!;

    this.form.patchValue({

      jobTitle: j.jobTitle,

      jobDescription: j.jobDescription,

      jobLocation: j.jobLocation,

      employmentType: j.employmentType,

      category: j.category,

      workMode: j.workMode,

      experienceRequired: j.experienceRequired,

      minSalary: j.minSalary,

      maxSalary: j.maxSalary,

      vacancies: j.vacancies,

      benefits: j.benefits ?? '',

      active: j.active ?? true,

      expiryDate: j.expiryDate.split('T')[0],

      skillIds: j.skillIds ?? []

    });

  }

  invalid(field: string) {

    const control = this.form.get(field);

    return control?.invalid && control?.touched;

  }
  // ======================
// SKILL METHODS
// ======================

addSkill(skill: Skills) {

  if (!skill) return;

  const exists = this.selectedSkills.some(
    s => s.skillId === skill.skillId
  );

  if (exists) return;

  this.selectedSkills.push(skill);

  this.form.patchValue({
    skillIds: this.selectedSkills.map(
      s => s.skillId
    )
  });

}

removeSkill(skillId: number) {

  this.selectedSkills =
    this.selectedSkills.filter(
      s => s.skillId !== skillId
    );

  this.form.patchValue({
    skillIds: this.selectedSkills.map(
      s => s.skillId
    )
  });

}

// ======================
// SUBMIT
// ======================

submit(): void {

  if (this.form.invalid) {
    this.form.markAllAsTouched();
    return;
  }

  this.loading = true;
  this.error = '';

  const v = this.form.value;

  const payload: JobDTO = {
    jobTitle: v.jobTitle!,

    jobDescription: v.jobDescription!,

    jobLocation: v.jobLocation!,

    employmentType: v.employmentType!,

    category: v.category!,

    workMode: v.workMode!,

    experienceRequired: v.experienceRequired!,

    minSalary: v.minSalary!,

    maxSalary: v.maxSalary!,

    vacancies: v.vacancies!,

    benefits: v.benefits ?? '',

    active: v.active ?? true,

    expiryDate: new Date(
      v.expiryDate!
    ).toISOString(),

    skillIds: v.skillIds ?? [],
    jobId: 0
  };

  const request = this.isEdit

    ? this.recruiterService.updateJob(
        this.editJob!.jobId!,
        payload
      )

    : this.recruiterService.createJob(
        payload
      );

  request.subscribe({

    next: () => {

      this.loading = false;

      this.saved.emit();

    },

    error: err => {

      console.error(err);

      this.loading = false;

      this.error =
        'Something went wrong. Please try again.';

    }

  });

}
}