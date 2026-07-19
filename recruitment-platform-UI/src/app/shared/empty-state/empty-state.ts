import { Component, input } from '@angular/core';

/**
 * Usage: <app-empty-state title="No jobs yet" message="...">
 *          <button (click)="...">Post a job</button>
 *        </app-empty-state>
 * The optional action button/link goes in the default content slot.
 */
@Component({
  selector: 'app-empty-state',
  standalone: true,
  templateUrl: './empty-state.html',
  styleUrl: './empty-state.css',
})
export class EmptyState {
  readonly icon = input('◇');
  readonly title = input('Nothing here yet');
  readonly message = input('');
}
