import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { SkillService } from '../../services/skill/skill-service';
import { ProficiencyLevel, SkillResponse } from '../../models/candidateprofile';
import { EnumLabelPipe } from '../../models/enum-label-pipe';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-skills',
  imports: [CommonModule, ReactiveFormsModule, EnumLabelPipe],
  templateUrl: './skills.html',
  styleUrl: './skills.css',
})
export class Skills implements OnInit{

  private readonly fb = inject(FormBuilder);
  private readonly skillService = inject(SkillService);

  readonly ProficiencyLevel = ProficiencyLevel;
  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly skills = signal<SkillResponse[]>([]);

  readonly isSaving = signal(false);
  readonly saveError = signal<string | null>(null);
  readonly pendingDeleteId = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    skillName: ['', [Validators.required]],
    proficiencyLevel: [ProficiencyLevel.INTERMEDIATE as ProficiencyLevel | ''],
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.skillService.getMySkills().subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.skills.set(res.data ?? []);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load your skills. Please try again.');
      },
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    this.saveError.set(null);

    const raw = this.form.getRawValue();
    this.skillService
      .add({ skillName: raw.skillName, proficiencyLevel: raw.proficiencyLevel || undefined })
      .subscribe({
        next: (res) => {
          this.isSaving.set(false);
          if (res.data) this.skills.update((list) => [...list, res.data as SkillResponse]);
          this.form.reset({ skillName: '', proficiencyLevel: ProficiencyLevel.INTERMEDIATE });
        },
        error: (err) => {
          this.isSaving.set(false);
          this.saveError.set(err?.error?.message ?? 'Could not add this skill. Please try again.');
        },
      });
  }

  delete(skillId: string): void {
    this.pendingDeleteId.set(skillId);
    this.skillService.delete(skillId).subscribe({
      next: () => {
        this.pendingDeleteId.set(null);
        this.skills.update((list) => list.filter((s) => s.id !== skillId));
      },
      error: (err) => {
        this.pendingDeleteId.set(null);
        this.saveError.set(err?.error?.message ?? 'Could not remove this skill.');
      },
    });
  }
}