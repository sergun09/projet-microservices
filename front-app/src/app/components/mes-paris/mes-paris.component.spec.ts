import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MesParisComponent } from './mes-paris.component';

describe('MesParisComponent', () => {
  let component: MesParisComponent;
  let fixture: ComponentFixture<MesParisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MesParisComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MesParisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
