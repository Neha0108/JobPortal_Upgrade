import { Injectable } from '@angular/core';
import { AuthResponse, AuthenticatedUser } from '../../models/auth';

interface StoredSession {
  accessToken: string;
  refreshToken: string;
  accessTokenExpiresAt: number; // epoch ms
  user: AuthenticatedUser;
}

const STORAGE_KEY = 'recruitai.session';

/**
 * Single source of truth for persisted auth state. Kept separate from
 * AuthService so the interceptor can read/write tokens without pulling
 * in AuthService's HttpClient dependency (would create a circular DI
 * chain: interceptor -> AuthService -> HttpClient -> interceptor).
 */
@Injectable({ providedIn: 'root' })
export class TokenStorageService {
  private session: StoredSession | null = this.readFromStorage();

  save(response: AuthResponse): void {
    this.session = {
      accessToken: response.accessToken,
      refreshToken: response.refreshToken,
      accessTokenExpiresAt: Date.now() + response.expiresInMs,
      user: {
        userId: response.userId,
        email: response.email,
        role: response.role,
      },
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(this.session));
  }

  clear(): void {
    this.session = null;
    localStorage.removeItem(STORAGE_KEY);
  }

  getAccessToken(): string | null {
    return this.session?.accessToken ?? null;
  }

  getRefreshToken(): string | null {
    return this.session?.refreshToken ?? null;
  }

  getUser(): AuthenticatedUser | null {
    return this.session?.user ?? null;
  }

  /** True once we're within 10s of expiry — refresh proactively rather than racing the server's clock. */
  isAccessTokenExpired(): boolean {
    if (!this.session) return true;
    return Date.now() >= this.session.accessTokenExpiresAt - 10_000;
  }

  hasSession(): boolean {
    return this.session !== null;
  }

  private readFromStorage(): StoredSession | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as StoredSession;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}