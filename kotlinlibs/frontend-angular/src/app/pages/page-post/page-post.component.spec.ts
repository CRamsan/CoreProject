import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { Observable, of, from } from 'rxjs';
import { PagePostComponent } from './page-post.component';

describe('PagePostComponent', () => {
  let component: PagePostComponent;
  let fixture: ComponentFixture<PagePostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PagePostComponent ],
      providers: [ { provide: ActivatedRoute, useValue: activatedRoute } ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PagePostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

let activatedRoute: Partial<ActivatedRoute> = {
  paramMap: of(convertToParamMap({id: "1"}))
};
