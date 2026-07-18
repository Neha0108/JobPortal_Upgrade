/** Mirrors entity.JobType */
export enum JobType {
  FULL_TIME = 'FULL_TIME',
  PART_TIME = 'PART_TIME',
  CONTRACT = 'CONTRACT',
  INTERNSHIP = 'INTERNSHIP',
}

/** Mirrors entity.JobStatus */
export enum JobStatus {
  DRAFT = 'DRAFT',
  OPEN = 'OPEN',
  CLOSED = 'CLOSED',
}

/** Mirrors entity.ApplicationStatus */
export enum ApplicationStatus {
  APPLIED = 'APPLIED',
  SHORTLISTED = 'SHORTLISTED',
  INTERVIEW = 'INTERVIEW',
  REJECTED = 'REJECTED',
  HIRED = 'HIRED',
}

/** Mirrors dto.job.JobRequest — used for both create and update. */
export interface JobRequest {
  title: string;
  description: string;
  requirements?: string;
  location?: string;
  jobType: JobType;
  minSalary?: number;
  maxSalary?: number;
}

/** Mirrors dto.job.JobResponse — recruiter-facing, includes applicant count. */
export interface JobResponse {
  id: string;
  title: string;
  description: string;
  requirements: string | null;
  location: string | null;
  jobType: JobType;
  minSalary: number | null;
  maxSalary: number | null;
  status: JobStatus;
  companyId: string;
  companyName: string;
  recruiterProfileId: string;
  recruiterName: string;
  applicantCount: number;
  createdAt: string;
}

/** Mirrors dto.job.PublicJobResponse — deliberately excludes recruiter/applicant internals. */
export interface PublicJobResponse {
  id: string;
  title: string;
  description: string;
  requirements: string | null;
  location: string | null;
  jobType: JobType;
  minSalary: number | null;
  maxSalary: number | null;
  status: JobStatus;
  companyId: string;
  companyName: string;
  createdAt: string;
}

/** Mirrors dto.application.ApplyJobRequest */
export interface ApplyJobRequest {
  resumeId?: string;
}

/** Mirrors dto.application.CandidateApplicationResponse */
export interface CandidateApplicationResponse {
  applicationId: string;
  jobId: string;
  jobTitle: string;
  companyName: string;
  resumeId: string | null;
  resumeFileName: string | null;
  status: ApplicationStatus;
  appliedAt: string;
}

/** Mirrors dto.application.ApplicantResponse */
export interface ApplicantResponse {
  applicationId: string;
  candidateProfileId: string;
  candidateName: string;
  candidateEmail: string;
  resumeId: string | null;
  resumeFileName: string | null;
  status: ApplicationStatus;
  appliedAt: string;
}

/** Mirrors dto.savedjob.SavedJobResponse */
export interface SavedJobResponse {
  savedJobId: string;
  jobId: string;
  jobTitle: string;
  companyName: string;
  location: string | null;
  jobType: JobType;
  jobStatus: JobStatus;
  savedAt: string;
}