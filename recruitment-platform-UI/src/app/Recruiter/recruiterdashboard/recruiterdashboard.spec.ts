import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Recruiterdashboard } from './recruiterdashboard';

describe('Recruiterdashboard', () => {
  let component: Recruiterdashboard;
  let fixture: ComponentFixture<Recruiterdashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Recruiterdashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(Recruiterdashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
