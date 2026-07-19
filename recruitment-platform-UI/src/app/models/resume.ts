/** Mirrors dto.resume.ResumeResponse */
export interface ResumeResponse {
  id: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  primary: boolean;
  uploadedAt: string;
}

/** Matches backend FileStorageProperties defaults (application.properties) —
 *  used for client-side pre-validation before hitting the upload endpoint.
 *  The server (Apache Tika, content-based) remains the actual authority. */
export const RESUME_MAX_SIZE_BYTES = 5 * 1024 * 1024;
export const RESUME_ALLOWED_TYPES = [
  'application/pdf',
  'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
];
