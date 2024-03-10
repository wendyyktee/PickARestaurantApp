import { TestBed } from '@angular/core/testing';

import { CommonErrorPopupService } from './common-error-popup.service';

describe('CommonErrorPopupService', () => {
  let service: CommonErrorPopupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommonErrorPopupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
