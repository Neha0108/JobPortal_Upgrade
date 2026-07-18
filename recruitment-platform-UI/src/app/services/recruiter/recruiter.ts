import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RecruiterDashboardResponse, RecruiterProfileResponse, UpdateRecruiterProfileRequest } from '../../models/recruiter';
import { ApiResponse } from '../../models/api-response';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';

@Injectable({
  providedIn: 'root',
})
export class RecruiterService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/recruiter`;

  getMyDashboard(): Observable<ApiResponse<RecruiterDashboardResponse>> {
    return this.http.get<ApiResponse<RecruiterDashboardResponse>>(`${this.baseUrl}/dashboard`);
  }
  
  getMyProfile(): Observable<ApiResponse<RecruiterProfileResponse>> {
    return this.http.get<ApiResponse<RecruiterProfileResponse>>(`${this.baseUrl}/profile`);
  }
 
  updateMyProfile(request: UpdateRecruiterProfileRequest): Observable<ApiResponse<RecruiterProfileResponse>> {
    return this.http.put<ApiResponse<RecruiterProfileResponse>>(`${this.baseUrl}/profile`, request);
  }
}