import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { ActivatedRoute } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { expect } from '@jest/globals';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('123')
      }
    }
  };

  const mockSessionApiService = {
    detail: jest.fn(),
    delete: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn()
  };

  const mockTeacherService = {
    detail: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  
  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'desc',
    date: new Date(),
    teacher_id: 42,
    users: [1, 2]
  };
  
  const mockTeacher = { id: 42, name: 'John' };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();
    
  mockSessionApiService.detail.mockReturnValue(of(mockSession));
  mockTeacherService.detail.mockReturnValue(of(mockTeacher));

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call fetchSession on ngOnInit', () => {
    const spy = jest.spyOn<any, any>(component, 'fetchSession');
    component.ngOnInit();
    expect(spy).toHaveBeenCalled();
  });

  it('should go back when back() is called', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });

  it('should call delete and navigate after deletion', () => {
    mockSessionApiService.delete.mockReturnValue(of({}));
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('123');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call participate and refresh session', () => {
    mockSessionApiService.participate.mockReturnValue(of({}));
    const spy = jest.spyOn<any, any>(component, 'fetchSession');
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('123', '1');
    expect(spy).toHaveBeenCalled();
  });

  it('should call unParticipate and refresh session', () => {
    mockSessionApiService.unParticipate.mockReturnValue(of({}));
    const spy = jest.spyOn<any, any>(component, 'fetchSession');
    component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('123', '1');
    expect(spy).toHaveBeenCalled();
  });

  it('should fetch session and teacher details correctly', () => {

    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(of(mockTeacher));

    component['fetchSession']();

    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
    expect(mockTeacherService.detail).toHaveBeenCalledWith('42');

    // On vérifie les effets secondaires (mise à jour des props)
    expect(component.session).toEqual(mockSession);
    expect(component.isParticipate).toBe(true);
  });
});
