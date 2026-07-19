import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';

/**
 * Two modes:
 *  - variant="spinner" (default): small centered spinner + label, for quick loads
 *    (profile forms, dashboards).
 *  - variant="skeleton": animated placeholder rows, for lists (job listings,
 *    applicant tables) where a spinner would leave a jarring blank gap.
 */
@Component({
  selector: 'app-loading-state',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loading-state.html',
  styleUrl: './loading-state.css',
})
export class LoadingState {
  readonly variant = input<'spinner' | 'skeleton'>('spinner');
  readonly label = input('Loading…');
  readonly rows = input(3);
  protected readonly rowArray = (n: number) => Array.from({ length: n });
}
