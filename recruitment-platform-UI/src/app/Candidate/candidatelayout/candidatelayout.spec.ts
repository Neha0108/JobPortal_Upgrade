import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Candidatelayout } from './candidatelayout';

describe('Candidatelayout', () => {
  let component: Candidatelayout;
  let fixture: ComponentFixture<Candidatelayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Candidatelayout],
    }).compileComponents();

    fixture = TestBed.createComponent(Candidatelayout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
