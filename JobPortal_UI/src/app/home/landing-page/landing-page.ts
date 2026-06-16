import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

declare var VANTA: any;

@Component({
  selector: 'app-landing-page',
  imports: [CommonModule, RouterOutlet],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage implements AfterViewInit, OnDestroy {

  @ViewChild('vantaRef', { static: true }) vantaRef!: ElementRef;
  vantaEffect: any;

  ngAfterViewInit() {
    this.vantaEffect = VANTA.GLOBE({
      el: this.vantaRef.nativeElement,
      color: 0x007bff,
      backgroundColor: 0x0d1117,
      size: 0.8,       
      spacing: 18,
      points: 8,         
      maxDistance: 18
    });
  }

  ngOnDestroy() {
    if (this.vantaEffect) {
      this.vantaEffect.destroy();
    }
  }

  jobs = [
    { title: 'Frontend Developer', company: 'Google', location: 'Remote' },
    { title: 'Backend Developer', company: 'Amazon', location: 'Bangalore' },
    { title: 'UI Designer', company: 'Adobe', location: 'Delhi' }
  ];

  showLogin = false;
  openLogin() {
    this.showLogin = true;
  }

  closeLogin() {
    this.showLogin = false;
  }
}
