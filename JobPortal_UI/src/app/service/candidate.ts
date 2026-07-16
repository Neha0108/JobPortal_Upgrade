import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobDTO } from '../interface/job-request';
import { Candidateprofile } from '../interface/candidateprofile';
import { Application } from '../interface/application';
import { SavedJob } from '../interface/saved-job';
import { RecruiterProfile } from '../interface/recruiter-profile';

@Injectable({
  providedIn: 'root'
})
export class Candidate {

  private http = inject(HttpClient);

  private readonly API = 'http://localhost:8080/api/candidate';


  createProfile(profile: Candidateprofile): Observable<Candidateprofile> {
    return this.http.post<Candidateprofile>(
      `${this.API}/profile`,
      profile
    );
  }

  getProfile(): Observable<Candidateprofile> {
    return this.http.get<Candidateprofile>(
      `${this.API}/profile`
    );
  }

  updateProfile(profile: Candidateprofile): Observable<Candidateprofile> {
    return this.http.put<Candidateprofile>(
      `${this.API}/profile`,
      profile
    );
  }

  // ===========================
  // Jobs
  // ===========================

  getJobs(filters?: {
    keyword?: string;
    location?: string;
    category?: string;
    workMode?: string;
    employmentType?: string;
  }): Observable<JobDTO[]> {

    let params = new HttpParams();

    if (filters?.keyword)
      params = params.set('keyword', filters.keyword);

    if (filters?.location)
      params = params.set('location', filters.location);

    if (filters?.category)
      params = params.set('category', filters.category);

    if (filters?.workMode)
      params = params.set('workMode', filters.workMode);

    if (filters?.employmentType)
      params = params.set('employmentType', filters.employmentType);

    return this.http.get<JobDTO[]>(
      `${this.API}/jobs`,
      { params }
    );
  }

  getJobById(jobId: number): Observable<JobDTO> {
    return this.http.get<JobDTO>(
      `${this.API}/jobs/${jobId}`
    );
  }

  // ===========================
  // Applications
  // ===========================

  applyJob(jobId: number): Observable<Application> {
    return this.http.post<Application>(
      `${this.API}/jobs/${jobId}/apply`,
      {}
    );
  }

  getApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(
      `${this.API}/applications`
    );
  }

  withdrawApplication(applicationId: number): Observable<string> {
    return this.http.delete(
      `${this.API}/applications/${applicationId}`,
      {
        responseType: 'text'
      }
    );
  }

  // ===========================
  // Saved Jobs
  // ===========================

  saveJob(jobId: number): Observable<SavedJob> {
    return this.http.post<SavedJob>(
      `${this.API}/saved-jobs/${jobId}`,
      {}
    );
  }

  unsaveJob(jobId: number): Observable<string> {
    return this.http.delete(
      `${this.API}/saved-jobs/${jobId}`,
      {
        responseType: 'text'
      }
    );
  }

  getSavedJobs(): Observable<SavedJob[]> {
    return this.http.get<SavedJob[]>(
      `${this.API}/saved-jobs`
    );
  }

  // ===========================
  // Recruiter
  // ===========================

  getRecruiterProfile(recruiterId: number): Observable<RecruiterProfile> {
    return this.http.get<RecruiterProfile>(
      `${this.API}/recruiters/${recruiterId}`
    );
  }

  getAllJobs() {
  return this.http.get<JobDTO[]>(`${this.API}/jobs/all`);
}

}