import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../env/environment';
import { Observable } from 'rxjs';
import { JobRequest, JobResponse, JobStatus } from '../../models/job';
import { ApiResponse, PageResponse } from '../../models/api-response';

@Injectable({
  providedIn: 'root',
})
export class Recruiterjobs {
  
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/recruiter/jobs`;
 
  createJob(request: JobRequest): Observable<ApiResponse<JobResponse>> {
    return this.http.post<ApiResponse<JobResponse>>(this.baseUrl, request);
  }
 
  updateJob(jobId: string, request: JobRequest): Observable<ApiResponse<JobResponse>> {
    return this.http.put<ApiResponse<JobResponse>>(`${this.baseUrl}/${jobId}`, request);
  }
 
  updateJobStatus(jobId: string, status: JobStatus): Observable<ApiResponse<JobResponse>> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<ApiResponse<JobResponse>>(`${this.baseUrl}/${jobId}/status`, null, { params });
  }
 
  deleteJob(jobId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${jobId}`);
  }
 
  getMyJob(jobId: string): Observable<ApiResponse<JobResponse>> {
    return this.http.get<ApiResponse<JobResponse>>(`${this.baseUrl}/${jobId}`);
  }
 
  getMyJobs(page = 0, size = 20): Observable<ApiResponse<PageResponse<JobResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<JobResponse>>>(this.baseUrl, { params });
  }
}
