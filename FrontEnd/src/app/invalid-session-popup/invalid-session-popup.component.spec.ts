import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvalidSessionPopupComponent } from './invalid-session-popup.component';

describe('InvalidSessionPopupComponent', () => {
  let component: InvalidSessionPopupComponent;
  let fixture: ComponentFixture<InvalidSessionPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InvalidSessionPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InvalidSessionPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
