/** Mirrors dto.candidate.CandidateDashboardResponse */
export interface CandidateDashboardResponse {
  totalApplications: number;
  shortlistedCount: number;
  interviewCount: number;
  hiredCount: number;
  savedJobsCount: number;
  resumeCount: number;
}

/** Mirrors dto.candidate.CandidateProfileResponse */
export interface CandidateProfileResponse {
  id: string;
  fullName: string;
  phone: string | null;
  headline: string | null;
  summary: string | null;
  email: string;
}

/** Mirrors dto.candidate.UpdateCandidateProfileRequest */
export interface UpdateCandidateProfileRequest {
  fullName: string;
  phone?: string;
  headline?: string;
  summary?: string;
}