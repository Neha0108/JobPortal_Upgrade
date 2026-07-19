import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { HttpErrorResponse } from '@angular/common/http';
import { CompanyService } from '../../services/company/company';
import { CompanyResponse } from '../../models/recruiter';

@Component({
  selector: 'app-company',
  imports: [ReactiveFormsModule],
  templateUrl: './company.html',
  styleUrl: './company.css',
})
export class Company implements OnInit{

  private readonly fb = inject(FormBuilder);
  private readonly companyService = inject(CompanyService);

  readonly isLoading = signal(true);
  readonly isSaving = signal(false);
  readonly loadError = signal<string | null>(null);
  readonly saveError = signal<string | null>(null);
  readonly saveSuccess = signal(false);
  /** null once loading finishes and no company exists yet — form then acts as a create form. */
  readonly company = signal<CompanyResponse | null>(null);

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(200)]],
    description: [''],
    website: ['', [Validators.maxLength(255)]],
    industry: ['', [Validators.maxLength(100)]],
    logoUrl: ['', [Validators.maxLength(500)]],
  });

  ngOnInit(): void {
    this.loadCompany();
  }

  loadCompany(): void {
    this.isLoading.set(true);
    this.loadError.set(null);

    this.companyService.getMyCompany().subscribe({
      next: (res) => {
        this.isLoading.set(false);
        const data = res.data ?? null;
        this.company.set(data);
        if (data) {
          this.form.patchValue({
            name: data.name,
            description: data.description ?? '',
            website: data.website ?? '',
            industry: data.industry ?? '',
            logoUrl: data.logoUrl ?? '',
          });
        }
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading.set(false);
        if (err.status === 404) {
          // No company yet — leave `company` as null so the template renders the create form.
          this.company.set(null);
          return;
        }
        this.loadError.set(err?.error?.message ?? 'Could not load your company. Please try again.');
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
    this.saveSuccess.set(false);

    const raw = this.form.getRawValue();
    const request = {
      name: raw.name,
      description: raw.description || undefined,
      website: raw.website || undefined,
      industry: raw.industry || undefined,
      logoUrl: raw.logoUrl || undefined,
    };

    const request$ = this.company()
      ? this.companyService.updateMyCompany(request)
      : this.companyService.createMyCompany(request);

    request$.subscribe({
      next: (res) => {
        this.isSaving.set(false);
        this.saveSuccess.set(true);
        if (res.data) this.company.set(res.data);
      },
      error: (err) => {
        this.isSaving.set(false);
        this.saveError.set(err?.error?.message ?? 'Could not save your company. Please try again.');
      },
    });
  }
}