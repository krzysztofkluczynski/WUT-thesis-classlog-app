import { TestBed } from '@angular/core/testing';

import { ClassService } from './class-service.service';

describe('ClassServiceService', () => {
  let service: ClassService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClassService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
