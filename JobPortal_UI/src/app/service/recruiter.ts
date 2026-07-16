import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { JobDTO } from '../interface/job-request';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class Recruiter {
  
  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api/recruiter';

  getMyJobs(): Observable<JobDTO[]> {
    return this.http.get<JobDTO[]>(`${this.base}/jobs`);
  }
 
  getJobById(id: number): Observable<JobDTO> {
    return this.http.get<JobDTO>(`${this.base}/jobs/${id}`);
  }
 
  createJob(payload: JobDTO): Observable<JobDTO> {
    return this.http.post<JobDTO>(`${this.base}/jobs`, payload);
  }
 
  updateJob(id: number, payload: JobDTO): Observable<JobDTO> {
    return this.http.put<JobDTO>(`${this.base}/jobs/${id}`, payload);
  }
 
  deleteJob(id: number): Observable<string> {
    return this.http.delete(`${this.base}/jobs/${id}`, { responseType: 'text' });
  }
}
