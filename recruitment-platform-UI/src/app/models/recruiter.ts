/** Mirrors dto.recruiter.RecruiterDashboardResponse */
export interface RecruiterDashboardResponse {
  totalJobsPosted: number;
  openJobs: number;
  draftJobs: number;
  closedJobs: number;
  totalApplicants: number;
  shortlistedCount: number;
  hiredCount: number;
}

/** Mirrors dto.recruiter.RecruiterProfileResponse */
export interface RecruiterProfileResponse {
  id: string;
  fullName: string;
  phone: string | null;
  designation: string | null;
  email: string;
  companyId: string | null;
  companyName: string | null;
}

/** Mirrors dto.recruiter.UpdateRecruiterProfileRequest */
export interface UpdateRecruiterProfileRequest {
  fullName: string;
  phone?: string;
  designation?: string;
}

/** Mirrors dto.recruiter.CompanyResponse */
export interface CompanyResponse {
  id: string;
  name: string;
  description: string | null;
  website: string | null;
  industry: string | null;
  logoUrl: string | null;
}

/** Mirrors dto.recruiter.CompanyRequest — used for both create (POST) and update (PUT). */
export interface CompanyRequest {
  name: string;
  description?: string;
  website?: string;
  industry?: string;
  logoUrl?: string;
}