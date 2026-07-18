import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Jobapplicants } from './jobapplicants';

describe('Jobapplicants', () => {
  let component: Jobapplicants;
  let fixture: ComponentFixture<Jobapplicants>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Jobapplicants],
    }).compileComponents();

    fixture = TestBed.createComponent(Jobapplicants);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
