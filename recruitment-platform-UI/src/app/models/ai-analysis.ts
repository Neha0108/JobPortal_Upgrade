/** Mirrors entity.AnalysisType */
export enum AnalysisType {
  SKILL_EXTRACTION = 'SKILL_EXTRACTION',
  JOB_MATCH_SCORE = 'JOB_MATCH_SCORE',
  SKILL_SUMMARY = 'SKILL_SUMMARY',
  MISSING_SKILLS = 'MISSING_SKILLS',
}


export interface AiAnalysisResponse {
  id: string;
  analysisType: AnalysisType;
  resultJson: string;
  matchScore: number | null;
  createdAt: string;
}

export interface SkillExtractionResult {
  skills: string[];
}

export interface SkillSummaryResult {
  summary: string;
}

export interface JobMatchScoreResult {
  matchScore: number;
  reasoning: string;
}

export interface MissingSkillsResult {
  missingSkills: string[];
}

/**
 * Parses `resultJson` into its expected shape for the given analysis type.
 * Returns null on malformed JSON (LLM output is never 100% guaranteed
 * well-formed) so callers can show a fallback rather than crash.
 */
export function parseAnalysisResult<T>(response: AiAnalysisResponse): T | null {
  try {
    return JSON.parse(response.resultJson) as T;
  } catch {
    return null;
  }
}