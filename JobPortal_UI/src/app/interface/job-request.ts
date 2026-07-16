export interface JobDTO {
  jobId: number;
  companyName?: string;
  skillNames?: string[];
  createdAt?: string;

  jobTitle: string;
  jobDescription: string;
  jobLocation: string;

  employmentType: string;
  category: string;
  workMode: string;

  experienceRequired: number;
  minSalary: number;
  maxSalary: number;
  vacancies: number;

  benefits?: string;
  active?: boolean;
  expiryDate: string;

  skillIds?: number[];
}