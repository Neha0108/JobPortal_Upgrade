import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { ApiResponse, PageResponse } from '../../models/api-response';
import { ApplicantResponse } from '../../models/job';

@Injectable({
  providedIn: 'root',
})
export class Recruiterapplications {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/recruiter`;
 
  getApplicantsForJob(
    jobId: string,
    page = 0,
    size = 20,
  ): Observable<ApiResponse<PageResponse<ApplicantResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<ApplicantResponse>>>(
      `${this.baseUrl}/jobs/${jobId}/applicants`,
      { params },
    );
  }
 
  shortlist(applicationId: string): Observable<ApiResponse<ApplicantResponse>> {
    return this.http.patch<ApiResponse<ApplicantResponse>>(
      `${this.baseUrl}/applications/${applicationId}/shortlist`,
      {},
    );
  }
 
  reject(applicationId: string): Observable<ApiResponse<ApplicantResponse>> {
    return this.http.patch<ApiResponse<ApplicantResponse>>(
      `${this.baseUrl}/applications/${applicationId}/reject`,
      {},
    );
  }
}
