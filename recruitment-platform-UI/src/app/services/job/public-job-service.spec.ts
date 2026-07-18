import { TestBed } from '@angular/core/testing';

import { PublicJobService } from './public-job-service';

describe('PublicJobService', () => {
  let service: PublicJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicJobService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
