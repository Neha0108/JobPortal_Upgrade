import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Jobdetail } from './jobdetail';

describe('Jobdetail', () => {
  let component: Jobdetail;
  let fixture: ComponentFixture<Jobdetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Jobdetail],
    }).compileComponents();

    fixture = TestBed.createComponent(Jobdetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
