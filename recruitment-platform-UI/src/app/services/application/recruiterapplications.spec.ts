import { TestBed } from '@angular/core/testing';

import { Recruiterapplications } from './recruiterapplications';

describe('Recruiterapplications', () => {
  let service: Recruiterapplications;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Recruiterapplications);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
