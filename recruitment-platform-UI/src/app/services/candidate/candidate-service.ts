import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { CandidateDashboardResponse, CandidateProfileResponse, UpdateCandidateProfileRequest } from '../../models/candidate';
import { ApiResponse } from '../../models/api-response';

@Injectable({ providedIn: 'root' })
export class CandidateService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate`;

  getMyDashboard(): Observable<ApiResponse<CandidateDashboardResponse>> {
    return this.http.get<ApiResponse<CandidateDashboardResponse>>(`${this.baseUrl}/dashboard`);
  }
  
  getMyProfile(): Observable<ApiResponse<CandidateProfileResponse>> {
    return this.http.get<ApiResponse<CandidateProfileResponse>>(`${this.baseUrl}/profile`);
  }
 
  updateMyProfile(request: UpdateCandidateProfileRequest): Observable<ApiResponse<CandidateProfileResponse>> {
    return this.http.put<ApiResponse<CandidateProfileResponse>>(`${this.baseUrl}/profile`, request);
  }
}