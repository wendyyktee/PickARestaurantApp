import { TestBed } from '@angular/core/testing';

import { InvalidSessionPopupService } from './invalid-session-popup.service';

describe('InvalidSessionPopupService', () => {
  let service: InvalidSessionPopupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvalidSessionPopupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
