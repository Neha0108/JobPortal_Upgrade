import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruiterJob } from './recruiter-job';

describe('RecruiterJob', () => {
  let component: RecruiterJob;
  let fixture: ComponentFixture<RecruiterJob>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecruiterJob],
    }).compileComponents();

    fixture = TestBed.createComponent(RecruiterJob);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
