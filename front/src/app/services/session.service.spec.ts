import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';
import { take } from 'rxjs/operators';

describe('SessionService', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    token: 'abc123',
    type: 'Bearer',
    id: 1,
    username: 'john_doe',
    firstName: 'John',
    lastName: 'Doe',
    admin: true,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have default isLogged value as false', () => {
    expect(service.isLogged).toBe(false);
  });

  it('should emit false initially from $isLogged()', (done) => {
    service.$isLogged().pipe(take(1)).subscribe(value => {
      expect(value).toBe(false);
      done();
    });
  });

  it('should update session info and isLogged on logIn()', (done) => {
    service.logIn(mockUser);

    expect(service.sessionInformation).toEqual(mockUser);
    expect(service.isLogged).toBe(true);

    service.$isLogged().pipe(take(1)).subscribe(value => {
      expect(value).toBe(true);
      done();
    });
  });

  it('should clear session info and set isLogged to false on logOut()', (done) => {
    service.logIn(mockUser); // simulate login first
    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBe(false);

    service.$isLogged().pipe(take(1)).subscribe(value => {
      expect(value).toBe(false);
      done();
    });
  });
});
