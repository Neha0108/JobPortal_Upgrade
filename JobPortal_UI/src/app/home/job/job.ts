import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { Candidate } from '../../service/candidate';
import { JobDTO } from '../../interface/job-request';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecommendedJobs } from "../../candidate/recommended-jobs/recommended-jobs";

@Component({
  selector: 'app-job',
  imports: [CommonModule, FormsModule, RecommendedJobs],
  templateUrl: './job.html',
  styleUrl: './job.css',
})
export class Job implements OnInit {

  private service = inject(Candidate);
  private chng = inject(ChangeDetectorRef);

  jobs: JobDTO[] = [];

  keyword = '';
  location = '';
  category = '';
  workMode = '';
  employmentType = '';

  ngOnInit(): void {
    this.getAllJobs();
  }

  getAllJobs(): void {

    this.service.getAllJobs().subscribe({

      next: (data) => {

        this.jobs = data;
        this.chng.detectChanges();
        console.log('All Jobs:', data);

      },

      error: (err) => {

        console.error('Error loading jobs', err);

      }

    });

  }

  searchJobs(): void {

    this.service.getJobs({

      keyword: this.keyword,
      location: this.location,
      category: this.category,
      workMode: this.workMode,
      employmentType: this.employmentType

    }).subscribe({

      next: (data) => {

        this.jobs = data;
        console.log('Search Result:', data);

      },

      error: (err) => {

        console.error('Search failed', err);

      }

    });

  }

  clearFilters(): void {

    this.keyword = '';
    this.location = '';
    this.category = '';
    this.workMode = '';
    this.employmentType = '';

    this.getAllJobs();

  }

}