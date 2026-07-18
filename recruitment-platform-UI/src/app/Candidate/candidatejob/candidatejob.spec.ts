import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Candidatejob } from './candidatejob';

describe('Candidatejob', () => {
  let component: Candidatejob;
  let fixture: ComponentFixture<Candidatejob>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Candidatejob],
    }).compileComponents();

    fixture = TestBed.createComponent(Candidatejob);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
