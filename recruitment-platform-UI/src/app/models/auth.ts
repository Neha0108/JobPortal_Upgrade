import { Role } from './role.enum';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  role: Role.RECRUITER | Role.CANDIDATE;
}


export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface ForgotPasswordRequest {
  email: string;
}


export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}


export interface VerifyEmailRequest {
  token: string;
}


export interface AuthResponse {
  userId: string;
  email: string;
  role: Role;
  accessToken: string;
  refreshToken: string;
  expiresInMs: number;
}

export interface AuthenticatedUser {
  userId: string;
  email: string;
  role: Role;
}