import { AfterViewInit, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { gsap } from 'gsap';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class Navbar implements AfterViewInit {

  
ngAfterViewInit() {
    gsap.to('.cube', {
      rotateX: 360,
      rotateY: 360,
      duration: 6,
      repeat: -1,
      ease: 'linear'
    });
  }

}