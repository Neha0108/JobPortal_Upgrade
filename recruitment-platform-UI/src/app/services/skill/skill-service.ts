import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { SkillRequest, SkillResponse } from '../../models/candidateprofile';
import { ApiResponse } from '../../models/api-response';


@Injectable({ providedIn: 'root' })
export class SkillService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate/skills`;

  add(request: SkillRequest): Observable<ApiResponse<SkillResponse>> {
    return this.http.post<ApiResponse<SkillResponse>>(this.baseUrl, request);
  }

  delete(skillId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${skillId}`);
  }

  getMySkills(): Observable<ApiResponse<SkillResponse[]>> {
    return this.http.get<ApiResponse<SkillResponse[]>>(this.baseUrl);
  }
}