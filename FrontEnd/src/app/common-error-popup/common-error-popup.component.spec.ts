import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommonErrorPopupComponent } from './common-error-popup.component';

describe('CommonErrorPopupComponent', () => {
  let component: CommonErrorPopupComponent;
  let fixture: ComponentFixture<CommonErrorPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommonErrorPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommonErrorPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
