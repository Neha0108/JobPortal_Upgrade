/** Mirrors dto.resume.ResumeResponse */
export interface ResumeResponse {
  id: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  primary: boolean;
  uploadedAt: string;
}

/** Mirrors dto.education.EducationRequest */
export interface EducationRequest {
  institution: string;
  degree: string;
  fieldOfStudy?: string;
  startDate?: string; // ISO date (yyyy-MM-dd)
  endDate?: string;
}

/** Mirrors dto.education.EducationResponse */
export interface EducationResponse {
  id: string;
  institution: string;
  degree: string;
  fieldOfStudy: string | null;
  startDate: string | null;
  endDate: string | null;
}

/** Mirrors dto.Experience.ExperienceRequest */
export interface ExperienceRequest {
  companyName: string;
  jobTitle: string;
  description?: string;
  startDate?: string;
  endDate?: string;
}

/** Mirrors dto.Experience.ExperienceResponse */
export interface ExperienceResponse {
  id: string;
  companyName: string;
  jobTitle: string;
  description: string | null;
  startDate: string | null;
  endDate: string | null;
}

/** Mirrors entity.ProficiencyLevel */
export enum ProficiencyLevel {
  BEGINNER = 'BEGINNER',
  INTERMEDIATE = 'INTERMEDIATE',
  ADVANCED = 'ADVANCED',
  EXPERT = 'EXPERT',
}

/** Mirrors dto.skill.SkillRequest */
export interface SkillRequest {
  skillName: string;
  proficiencyLevel?: ProficiencyLevel;
}

/** Mirrors dto.skill.SkillResponse */
export interface SkillResponse {
  id: string;
  skillName: string;
  proficiencyLevel: ProficiencyLevel | null;
}