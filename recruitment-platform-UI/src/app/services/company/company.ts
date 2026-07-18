import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ApiResponse } from '../../models/api-response';
import { environment } from '../../../env/environment';
import { CompanyRequest, CompanyResponse } from '../../models/recruiter';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/recruiter/company`;
 
  /** 404s (via ApiResponse envelope, not a thrown error the interceptor swallows) when the recruiter has no company yet — callers should treat that as "show the create form", not a hard failure. */
  getMyCompany(): Observable<ApiResponse<CompanyResponse>> {
    return this.http.get<ApiResponse<CompanyResponse>>(this.baseUrl);
  }
 
  createMyCompany(request: CompanyRequest): Observable<ApiResponse<CompanyResponse>> {
    return this.http.post<ApiResponse<CompanyResponse>>(this.baseUrl, request);
  }
 
  updateMyCompany(request: CompanyRequest): Observable<ApiResponse<CompanyResponse>> {
    return this.http.put<ApiResponse<CompanyResponse>>(this.baseUrl, request);
  }
}
