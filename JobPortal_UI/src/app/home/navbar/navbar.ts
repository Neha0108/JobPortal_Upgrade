import { CommonModule } from '@angular/common';
import { AfterViewInit, Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { gsap } from 'gsap';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css'],
})
export class Navbar implements AfterViewInit {
  constructor(private router: Router) {}
  ngAfterViewInit() {
    gsap.to('.cube', {
      rotateX: 360,
      rotateY: 360,
      duration: 6,
      repeat: -1,
      ease: 'linear',
    });
  }

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');

    this.router.navigate(['']);
  }
}
