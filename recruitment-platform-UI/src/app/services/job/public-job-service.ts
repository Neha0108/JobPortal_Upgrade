import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { JobStatus, JobType, PublicJobResponse } from '../../models/job';
import { environment } from '../../../env/environment';
import { ApiResponse, PageResponse } from '../../models/api-response';


export interface JobSearchParams {
  keyword?: string;
  location?: string;
  jobType?: JobType;
  status?: JobStatus;
  page?: number;
  size?: number;
}

@Injectable({ providedIn: 'root' })
export class PublicJobService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/jobs`;

  search(params: JobSearchParams): Observable<ApiResponse<PageResponse<PublicJobResponse>>> {
    let httpParams = new HttpParams();
    if (params.keyword) httpParams = httpParams.set('keyword', params.keyword);
    if (params.location) httpParams = httpParams.set('location', params.location);
    if (params.jobType) httpParams = httpParams.set('jobType', params.jobType);
    if (params.status) httpParams = httpParams.set('status', params.status);
    httpParams = httpParams.set('page', params.page ?? 0).set('size', params.size ?? 20);

    return this.http.get<ApiResponse<PageResponse<PublicJobResponse>>>(this.baseUrl, { params: httpParams });
  }

  getById(jobId: string): Observable<ApiResponse<PublicJobResponse>> {
    return this.http.get<ApiResponse<PublicJobResponse>>(`${this.baseUrl}/${jobId}`);
  }
}