import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { EducationRequest, EducationResponse } from '../../models/candidateprofile';
import { ApiResponse } from '../../models/api-response';


@Injectable({ providedIn: 'root' })
export class EducationService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate/education`;

  add(request: EducationRequest): Observable<ApiResponse<EducationResponse>> {
    return this.http.post<ApiResponse<EducationResponse>>(this.baseUrl, request);
  }

  update(educationId: string, request: EducationRequest): Observable<ApiResponse<EducationResponse>> {
    return this.http.put<ApiResponse<EducationResponse>>(`${this.baseUrl}/${educationId}`, request);
  }

  delete(educationId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${educationId}`);
  }

  getMyEducation(): Observable<ApiResponse<EducationResponse[]>> {
    return this.http.get<ApiResponse<EducationResponse[]>>(this.baseUrl);
  }
}