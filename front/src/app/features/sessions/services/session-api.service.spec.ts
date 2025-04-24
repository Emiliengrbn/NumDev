import { TestBed } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { expect, jest } from '@jest/globals';

jest.mock('@angular/common/http');

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpClientMock: jest.Mocked<HttpClient>;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'Description',
    date: new Date(),
    teacher_id: 10,
    users: [1, 2, 3],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    httpClientMock = {
      get: jest.fn(),
      post: jest.fn(),
      delete: jest.fn(),
      put: jest.fn()
    } as unknown as jest.Mocked<HttpClient>;

    TestBed.configureTestingModule({
      providers: [
        SessionApiService,
        { provide: HttpClient, useValue: httpClientMock }
      ]
    });

    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    httpClientMock.get.mockReturnValue(of([mockSession]));
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual([mockSession]);
    });
    expect(httpClientMock.get).toHaveBeenCalledWith('api/session');
  });

  it('should fetch session detail', () => {
    const id = '1';
    httpClientMock.get.mockReturnValue(of(mockSession));
    service.detail(id).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    expect(httpClientMock.get).toHaveBeenCalledWith(`api/session/${id}`);
  });

  it('should delete a session', () => {
    const id = '1';
    httpClientMock.delete.mockReturnValue(of({}));
    service.delete(id).subscribe((res) => {
      expect(res).toEqual({});
    });
    expect(httpClientMock.delete).toHaveBeenCalledWith(`api/session/${id}`);
  });

  it('should create a session', () => {
    httpClientMock.post.mockReturnValue(of(mockSession));
    service.create(mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    expect(httpClientMock.post).toHaveBeenCalledWith('api/session', mockSession);
  });

  it('should update a session', () => {
    const id = '1';
    httpClientMock.put.mockReturnValue(of(mockSession));
    service.update(id, mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    expect(httpClientMock.put).toHaveBeenCalledWith(`api/session/${id}`, mockSession);
  });

  it('should participate in a session', () => {
    const sessionId = '1';
    const userId = '2';
    httpClientMock.post.mockReturnValue(of(undefined));
    service.participate(sessionId, userId).subscribe((res) => {
      expect(res).toBeUndefined();
    });
    expect(httpClientMock.post).toHaveBeenCalledWith(`api/session/${sessionId}/participate/${userId}`, null);
  });

  it('should unParticipate from a session', () => {
    const sessionId = '1';
    const userId = '2';
    httpClientMock.delete.mockReturnValue(of(undefined));
    service.unParticipate(sessionId, userId).subscribe((res) => {
      expect(res).toBeUndefined();
    });
    expect(httpClientMock.delete).toHaveBeenCalledWith(`api/session/${sessionId}/participate/${userId}`);
  });
});
