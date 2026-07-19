import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { ExperienceRequest, ExperienceResponse } from '../../models/candidateprofile';
import { ApiResponse } from '../../models/api-response';


@Injectable({ providedIn: 'root' })
export class ExperienceService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate/experience`;

  add(request: ExperienceRequest): Observable<ApiResponse<ExperienceResponse>> {
    return this.http.post<ApiResponse<ExperienceResponse>>(this.baseUrl, request);
  }

  update(experienceId: string, request: ExperienceRequest): Observable<ApiResponse<ExperienceResponse>> {
    return this.http.put<ApiResponse<ExperienceResponse>>(`${this.baseUrl}/${experienceId}`, request);
  }

  delete(experienceId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${experienceId}`);
  }

  getMyExperience(): Observable<ApiResponse<ExperienceResponse[]>> {
    return this.http.get<ApiResponse<ExperienceResponse[]>>(this.baseUrl);
  }
}