import { Component, input, output } from '@angular/core';

/**
 * Usage: <app-error-state [message]="loadError()" (retry)="loadData()" />
 * Reuses the existing .form-error styling (already used inline across every
 * component) so this doesn't introduce a visually different "second" error style.
 */
@Component({
  selector: 'app-error-state',
  standalone: true,
  templateUrl: './error-state.html',
  styleUrl: './error-state.css',
})
export class ErrorState {
  readonly message = input('Something went wrong. Please try again.');
  readonly retry = output<void>();
}
