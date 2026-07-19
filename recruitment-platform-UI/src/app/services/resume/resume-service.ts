import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { ApiResponse } from '../../models/api-response';
import { ResumeResponse } from '../../models/resume';

@Injectable({ providedIn: 'root' })
export class ResumeService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate/resumes`;

  upload(file: File): Observable<ApiResponse<ResumeResponse>> {
    const formData = new FormData();
    formData.append('file', file);
    // Deliberately no explicit Content-Type header - the browser sets
    // multipart/form-data with the correct boundary itself; setting it
    // manually here would break that boundary and the backend's parsing.
    return this.http.post<ApiResponse<ResumeResponse>>(this.baseUrl, formData);
  }

  getMyResumes(): Observable<ApiResponse<ResumeResponse[]>> {
    return this.http.get<ApiResponse<ResumeResponse[]>>(this.baseUrl);
  }

  setPrimary(resumeId: string): Observable<ApiResponse<ResumeResponse>> {
    return this.http.patch<ApiResponse<ResumeResponse>>(`${this.baseUrl}/${resumeId}/primary`, {});
  }

  delete(resumeId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${resumeId}`);
  }

  /** Raw binary endpoint - the one place in the whole API that does NOT
   *  return an ApiResponse envelope, since it streams the actual file bytes. */
  download(resumeId: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${resumeId}/download`, { responseType: 'blob' });
  }
}
