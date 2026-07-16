import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentApplications } from './recent-applications';

describe('RecentApplications', () => {
  let component: RecentApplications;
  let fixture: ComponentFixture<RecentApplications>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecentApplications],
    }).compileComponents();

    fixture = TestBed.createComponent(RecentApplications);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
