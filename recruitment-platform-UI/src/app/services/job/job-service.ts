import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../env/environment';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../../models/api-response';
import { SavedJobResponse } from '../../models/job';
@Injectable({
  providedIn: 'root',
})
export class JobService {
  
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/candidate/saved-jobs`;
 
  save(jobId: string): Observable<ApiResponse<SavedJobResponse>> {
    return this.http.post<ApiResponse<SavedJobResponse>>(`${this.baseUrl}/${jobId}`, {});
  }
 
  unsave(jobId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${jobId}`);
  }
 
  getMySavedJobs(page = 0, size = 20): Observable<ApiResponse<PageResponse<SavedJobResponse>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<SavedJobResponse>>>(this.baseUrl, { params });
  }
}
