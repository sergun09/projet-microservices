import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPariComponent } from './new-pari.component';

describe('NewPariComponent', () => {
  let component: NewPariComponent;
  let fixture: ComponentFixture<NewPariComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewPariComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewPariComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
