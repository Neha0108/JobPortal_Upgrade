import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../env/environment';
import { ApplyJobRequest, CandidateApplicationResponse } from '../../models/job';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../../models/api-response';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate`;
 
  apply(jobId: string, request?: ApplyJobRequest): Observable<ApiResponse<CandidateApplicationResponse>> {
    return this.http.post<ApiResponse<CandidateApplicationResponse>>(
      `${this.baseUrl}/jobs/${jobId}/apply`,
      request ?? {},
    );
  }
 
  getMyApplications(
    page = 0,
    size = 20,
  ): Observable<ApiResponse<PageResponse<CandidateApplicationResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<CandidateApplicationResponse>>>(
      `${this.baseUrl}/applications`,
      { params },
    );
  }
}
