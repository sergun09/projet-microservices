import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalConsulterNotificationComponent } from './modal-consulter-notification.component';

describe('ModalConsulterNotificationComponent', () => {
  let component: ModalConsulterNotificationComponent;
  let fixture: ComponentFixture<ModalConsulterNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalConsulterNotificationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalConsulterNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
