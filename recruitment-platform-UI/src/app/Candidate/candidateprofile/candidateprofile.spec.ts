import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Candidateprofile } from './candidateprofile';

describe('Candidateprofile', () => {
  let component: Candidateprofile;
  let fixture: ComponentFixture<Candidateprofile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Candidateprofile],
    }).compileComponents();

    fixture = TestBed.createComponent(Candidateprofile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
