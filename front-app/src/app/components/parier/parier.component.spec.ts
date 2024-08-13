import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParierComponent } from './parier.component';

describe('ParierComponent', () => {
  let component: ParierComponent;
  let fixture: ComponentFixture<ParierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParierComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
