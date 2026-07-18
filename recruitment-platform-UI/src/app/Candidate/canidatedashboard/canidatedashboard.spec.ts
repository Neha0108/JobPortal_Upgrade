import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Canidatedashboard } from './canidatedashboard';

describe('Canidatedashboard', () => {
  let component: Canidatedashboard;
  let fixture: ComponentFixture<Canidatedashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Canidatedashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(Canidatedashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
