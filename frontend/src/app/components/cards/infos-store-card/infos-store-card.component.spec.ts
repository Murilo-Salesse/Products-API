import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InfosStoreCardComponent } from './infos-store-card.component';

describe('InfosStoreCardComponent', () => {
  let component: InfosStoreCardComponent;
  let fixture: ComponentFixture<InfosStoreCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InfosStoreCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InfosStoreCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
