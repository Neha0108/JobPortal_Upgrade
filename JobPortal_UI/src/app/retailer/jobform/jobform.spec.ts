import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Jobform } from './jobform';

describe('Jobform', () => {
  let component: Jobform;
  let fixture: ComponentFixture<Jobform>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Jobform],
    }).compileComponents();

    fixture = TestBed.createComponent(Jobform);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
