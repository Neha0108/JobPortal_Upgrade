import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Recruiterprofile } from './recruiterprofile';

describe('Recruiterprofile', () => {
  let component: Recruiterprofile;
  let fixture: ComponentFixture<Recruiterprofile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Recruiterprofile],
    }).compileComponents();

    fixture = TestBed.createComponent(Recruiterprofile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
