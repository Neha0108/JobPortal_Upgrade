import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Skills } from '../interface/skills';

@Injectable({
  providedIn: 'root',
})
export class DataService {
 
  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api/data';

  getSkills() {
    return this.http.get<Skills[]>(`${this.base}/skills`);
  }

  getCategories() {
    return this.http.get<string[]>(`${this.base}/categories`);
  }

  getEmploymentTypes() {
    return this.http.get<string[]>(`${this.base}/employment-types`);
  }

  getWorkModes() {
    return this.http.get<string[]>(`${this.base}/work-modes`);
  }
}
