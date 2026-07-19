import { Injectable, signal } from '@angular/core';

export type ToastVariant = 'success' | 'error' | 'info';

export interface ToastMessage {
  id: number;
  variant: ToastVariant;
  text: string;
}

/**
 * App-wide toast notifications. Rendered once via <app-toast-container />
 * in app.html, so any service or component can call this without needing
 * to know where in the DOM the notification actually appears.
 */
@Injectable({ providedIn: 'root' })
export class ToastService {
  private nextId = 0;
  private readonly _toasts = signal<ToastMessage[]>([]);
  readonly toasts = this._toasts.asReadonly();

  success(text: string, durationMs = 4000): void {
    this.push('success', text, durationMs);
  }

  error(text: string, durationMs = 6000): void {
    // Errors stay up longer than success/info — more likely the person
    // needs a moment to actually read what went wrong.
    this.push('error', text, durationMs);
  }

  info(text: string, durationMs = 4000): void {
    this.push('info', text, durationMs);
  }

  dismiss(id: number): void {
    this._toasts.update((list) => list.filter((t) => t.id !== id));
  }

  private push(variant: ToastVariant, text: string, durationMs: number): void {
    const id = this.nextId++;
    this._toasts.update((list) => [...list, { id, variant, text }]);
    setTimeout(() => this.dismiss(id), durationMs);
  }
}
