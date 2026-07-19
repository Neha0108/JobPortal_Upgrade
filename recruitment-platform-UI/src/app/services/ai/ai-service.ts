import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { ApiResponse } from '../../models/api-response';
import { AiAnalysisResponse } from '../../models/ai-analysis';


@Injectable({ providedIn: 'root' })

export class AiService {
  
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate/ai`;

  extractSkills(resumeId: string): Observable<ApiResponse<AiAnalysisResponse>> {
    return this.http.post<ApiResponse<AiAnalysisResponse>>(
      `${this.baseUrl}/resumes/${resumeId}/skills`,
      {},
    );
  }

  generateSummary(resumeId: string): Observable<ApiResponse<AiAnalysisResponse>> {
    return this.http.post<ApiResponse<AiAnalysisResponse>>(
      `${this.baseUrl}/resumes/${resumeId}/summary`,
      {},
    );
  }

  matchScore(resumeId: string, jobId: string): Observable<ApiResponse<AiAnalysisResponse>> {
    return this.http.post<ApiResponse<AiAnalysisResponse>>(
      `${this.baseUrl}/resumes/${resumeId}/jobs/${jobId}/match-score`,
      {},
    );
  }

  missingSkills(resumeId: string, jobId: string): Observable<ApiResponse<AiAnalysisResponse>> {
    return this.http.post<ApiResponse<AiAnalysisResponse>>(
      `${this.baseUrl}/resumes/${resumeId}/jobs/${jobId}/missing-skills`,
      {},
    );
  }
}