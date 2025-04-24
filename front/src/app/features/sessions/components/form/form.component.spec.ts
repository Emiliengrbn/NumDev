import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect, jest } from '@jest/globals';

import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockTeacherService = {
    all: jest.fn(() => of([]))
  };

  const mockRouter = {
    navigate: jest.fn(),
    url: '/sessions/create'
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn()
      }
    }
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockSessionApiService = {
    detail: jest.fn(() => of({
      id: 1,
      name: 'Test Session',
      date: new Date(),
      teacher_id: 1,
      description: 'Description',
      users: []
    })),
    create: jest.fn(() => of({})),
    update: jest.fn(() => of({}))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form in create mode', () => {
    component.ngOnInit();
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.valid).toBe(false);
  });

  it('should initialize form in update mode', () => {
    mockRouter.url = '/sessions/update/1';
    mockActivatedRoute.snapshot.paramMap.get = jest.fn().mockReturnValue('1');

    component.ngOnInit();

    expect(component.onUpdate).toBe(true);
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  it('should redirect if not admin', () => {
    mockSessionService.sessionInformation.admin = false;

    component.ngOnInit();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should call create on submit in create mode', () => {
    component.onUpdate = false;
    component.sessionForm = component['fb'].group({
      name: ['Test'],
      date: ['2025-04-10'],
      teacher_id: [1],
      description: ['Test description']
    });

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call update on submit in update mode', () => {
    component.onUpdate = true;
    component['id'] = '1';
    component.sessionForm = component['fb'].group({
      name: ['Test'],
      date: ['2025-04-10'],
      teacher_id: [1],
      description: ['Test description']
    });

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', expect.anything());
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
