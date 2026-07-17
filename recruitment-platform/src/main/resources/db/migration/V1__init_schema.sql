-- ============================================================
-- V1__init_schema.sql
-- Initial schema for AI Recruitment Management System
-- ============================================================

CREATE TABLE users (
                       id                 UUID PRIMARY KEY,
                       email              VARCHAR(255) NOT NULL,
                       password           VARCHAR(255) NOT NULL,
                       role               VARCHAR(20)  NOT NULL,
                       is_email_verified  BOOLEAN      NOT NULL DEFAULT FALSE,
                       is_active          BOOLEAN      NOT NULL DEFAULT TRUE,
                       created_at         TIMESTAMP    NOT NULL,
                       updated_at         TIMESTAMP    NOT NULL
);
CREATE UNIQUE INDEX idx_users_email ON users (email);

CREATE TABLE candidate_profiles (
                                    id           UUID PRIMARY KEY,
                                    user_id      UUID NOT NULL UNIQUE REFERENCES users (id),
                                    full_name    VARCHAR(150) NOT NULL,
                                    phone        VARCHAR(20),
                                    headline     VARCHAR(200),
                                    summary      TEXT,
                                    created_at   TIMESTAMP NOT NULL,
                                    updated_at   TIMESTAMP NOT NULL
);

CREATE TABLE candidate_educations (
                                      id                   UUID PRIMARY KEY,
                                      candidate_profile_id UUID NOT NULL REFERENCES candidate_profiles (id) ON DELETE CASCADE,
                                      institution          VARCHAR(150) NOT NULL,
                                      degree               VARCHAR(100) NOT NULL,
                                      field_of_study       VARCHAR(100),
                                      start_date           DATE,
                                      end_date             DATE,
                                      created_at           TIMESTAMP NOT NULL,
                                      updated_at           TIMESTAMP NOT NULL
);
CREATE INDEX idx_candidate_educations_profile ON candidate_educations (candidate_profile_id);

CREATE TABLE candidate_experiences (
                                       id                   UUID PRIMARY KEY,
                                       candidate_profile_id UUID NOT NULL REFERENCES candidate_profiles (id) ON DELETE CASCADE,
                                       company_name         VARCHAR(150) NOT NULL,
                                       job_title            VARCHAR(150) NOT NULL,
                                       description          TEXT,
                                       start_date           DATE,
                                       end_date             DATE,
                                       created_at           TIMESTAMP NOT NULL,
                                       updated_at           TIMESTAMP NOT NULL
);
CREATE INDEX idx_candidate_experiences_profile ON candidate_experiences (candidate_profile_id);

CREATE TABLE candidate_skills (
                                  id                   UUID PRIMARY KEY,
                                  candidate_profile_id UUID NOT NULL REFERENCES candidate_profiles (id) ON DELETE CASCADE,
                                  skill_name           VARCHAR(100) NOT NULL,
                                  proficiency_level    VARCHAR(20),
                                  created_at           TIMESTAMP NOT NULL,
                                  updated_at           TIMESTAMP NOT NULL
);
CREATE INDEX idx_candidate_skills_profile ON candidate_skills (candidate_profile_id);

CREATE TABLE resumes (
                         id                   UUID PRIMARY KEY,
                         candidate_profile_id UUID NOT NULL REFERENCES candidate_profiles (id) ON DELETE CASCADE,
                         file_name            VARCHAR(255) NOT NULL,
                         file_path            VARCHAR(500) NOT NULL,
                         file_type            VARCHAR(100) NOT NULL,
                         file_size            BIGINT NOT NULL,
                         is_primary           BOOLEAN NOT NULL DEFAULT FALSE,
                         uploaded_at          TIMESTAMP NOT NULL,
                         created_at           TIMESTAMP NOT NULL,
                         updated_at           TIMESTAMP NOT NULL
);
CREATE INDEX idx_resumes_profile ON resumes (candidate_profile_id);

CREATE TABLE companies (
                           id           UUID PRIMARY KEY,
                           name         VARCHAR(200) NOT NULL,
                           description  TEXT,
                           website      VARCHAR(255),
                           industry     VARCHAR(100),
                           logo_url     VARCHAR(500),
                           created_at   TIMESTAMP NOT NULL,
                           updated_at   TIMESTAMP NOT NULL
);

CREATE TABLE recruiter_profiles (
                                    id           UUID PRIMARY KEY,
                                    user_id      UUID NOT NULL UNIQUE REFERENCES users (id),
                                    full_name    VARCHAR(150) NOT NULL,
                                    phone        VARCHAR(20),
                                    designation  VARCHAR(150),
                                    company_id   UUID REFERENCES companies (id),
                                    created_at   TIMESTAMP NOT NULL,
                                    updated_at   TIMESTAMP NOT NULL
);
CREATE INDEX idx_recruiter_profiles_company ON recruiter_profiles (company_id);

CREATE TABLE jobs (
                      id                    UUID PRIMARY KEY,
                      recruiter_profile_id  UUID NOT NULL REFERENCES recruiter_profiles (id),
                      company_id            UUID NOT NULL REFERENCES companies (id),
                      title                 VARCHAR(200) NOT NULL,
                      description           TEXT NOT NULL,
                      requirements          TEXT,
                      location              VARCHAR(150),
                      job_type              VARCHAR(20) NOT NULL,
                      min_salary            NUMERIC(12,2),
                      max_salary            NUMERIC(12,2),
                      status                VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                      deleted_at            TIMESTAMP,
                      created_at            TIMESTAMP NOT NULL,
                      updated_at            TIMESTAMP NOT NULL
);
CREATE INDEX idx_jobs_title ON jobs (title);
CREATE INDEX idx_jobs_location ON jobs (location);
CREATE INDEX idx_jobs_status ON jobs (status);
CREATE INDEX idx_jobs_recruiter ON jobs (recruiter_profile_id);
CREATE INDEX idx_jobs_company ON jobs (company_id);

CREATE TABLE job_applications (
                                  id                    UUID PRIMARY KEY,
                                  job_id                UUID NOT NULL REFERENCES jobs (id),
                                  candidate_profile_id  UUID NOT NULL REFERENCES candidate_profiles (id),
                                  resume_id             UUID NOT NULL REFERENCES resumes (id),
                                  status                VARCHAR(20) NOT NULL DEFAULT 'APPLIED',
                                  created_at            TIMESTAMP NOT NULL,
                                  updated_at            TIMESTAMP NOT NULL,
                                  CONSTRAINT uq_job_candidate UNIQUE (job_id, candidate_profile_id)
);
CREATE INDEX idx_job_applications_job ON job_applications (job_id);
CREATE INDEX idx_job_applications_candidate ON job_applications (candidate_profile_id);

CREATE TABLE saved_jobs (
                            id                    UUID PRIMARY KEY,
                            candidate_profile_id  UUID NOT NULL REFERENCES candidate_profiles (id),
                            job_id                UUID NOT NULL REFERENCES jobs (id),
                            created_at            TIMESTAMP NOT NULL,
                            updated_at            TIMESTAMP NOT NULL,
                            CONSTRAINT uq_candidate_saved_job UNIQUE (candidate_profile_id, job_id)
);

CREATE TABLE ai_analysis_results (
                                     id                    UUID PRIMARY KEY,
                                     resume_id             UUID REFERENCES resumes (id),
                                     job_id                UUID REFERENCES jobs (id),
                                     candidate_profile_id  UUID NOT NULL REFERENCES candidate_profiles (id),
                                     analysis_type         VARCHAR(30) NOT NULL,
                                     result_json           JSONB NOT NULL,
                                     match_score           NUMERIC(5,2),
                                     created_at            TIMESTAMP NOT NULL,
                                     updated_at            TIMESTAMP NOT NULL
);
CREATE INDEX idx_ai_results_candidate ON ai_analysis_results (candidate_profile_id);
CREATE INDEX idx_ai_results_job ON ai_analysis_results (job_id);

CREATE TABLE password_reset_tokens (
                                       id          UUID PRIMARY KEY,
                                       user_id     UUID NOT NULL REFERENCES users (id),
                                       token       VARCHAR(255) NOT NULL UNIQUE,
                                       expires_at  TIMESTAMP NOT NULL,
                                       used        BOOLEAN NOT NULL DEFAULT FALSE,
                                       created_at  TIMESTAMP NOT NULL,
                                       updated_at  TIMESTAMP NOT NULL
);

CREATE TABLE email_verification_tokens (
                                           id          UUID PRIMARY KEY,
                                           user_id     UUID NOT NULL REFERENCES users (id),
                                           token       VARCHAR(255) NOT NULL UNIQUE,
                                           expires_at  TIMESTAMP NOT NULL,
                                           used        BOOLEAN NOT NULL DEFAULT FALSE,
                                           created_at  TIMESTAMP NOT NULL,
                                           updated_at  TIMESTAMP NOT NULL
);

CREATE TABLE refresh_tokens (
                                id          UUID PRIMARY KEY,
                                user_id     UUID NOT NULL REFERENCES users (id),
                                token_hash  VARCHAR(255) NOT NULL UNIQUE,
                                expires_at  TIMESTAMP NOT NULL,
                                revoked     BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at  TIMESTAMP NOT NULL,
                                updated_at  TIMESTAMP NOT NULL
);