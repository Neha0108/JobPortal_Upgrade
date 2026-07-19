import { Component, inject } from '@angular/core';
import { ToastService } from '../toast-service';

/** Mounted once in app.html, outside the router-outlet, so toasts survive navigation. */
@Component({
  selector: 'app-toast-container',
  standalone: true,
  templateUrl: './toast-container.html',
  styleUrl: './toast-container.css',
})
export class ToastContainer {
  protected readonly toastService = inject(ToastService);
}
