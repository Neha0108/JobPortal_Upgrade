import { TestBed } from '@angular/core/testing';

import { Recruiterjobs } from './recruiterjobs';

describe('Recruiterjobs', () => {
  let service: Recruiterjobs;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Recruiterjobs);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
