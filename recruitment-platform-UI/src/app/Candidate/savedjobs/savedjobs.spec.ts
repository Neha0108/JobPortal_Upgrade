import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Savedjobs } from './savedjobs';

describe('Savedjobs', () => {
  let component: Savedjobs;
  let fixture: ComponentFixture<Savedjobs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Savedjobs],
    }).compileComponents();

    fixture = TestBed.createComponent(Savedjobs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
