import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Recruiterlayout } from './recruiterlayout';

describe('Recruiterlayout', () => {
  let component: Recruiterlayout;
  let fixture: ComponentFixture<Recruiterlayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Recruiterlayout],
    }).compileComponents();

    fixture = TestBed.createComponent(Recruiterlayout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
