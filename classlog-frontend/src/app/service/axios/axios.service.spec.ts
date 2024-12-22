import { TestBed } from '@angular/core/testing';
import { AxiosService } from './axios.service';
import { AuthService } from './../auth/auth.service';
import axios, {AxiosError, AxiosResponse, InternalAxiosRequestConfig} from 'axios';

describe('AxiosService', () => {
  let service: AxiosService;
  let authService: jasmine.SpyObj<AuthService>;

  const mockToken = 'mock-token';

  // Mock AxiosResponse
  const mockResponse: AxiosResponse = {
    data: 'mock data',
    status: 200,
    statusText: 'OK',
    headers: {},
    config: {
      url: '/test',
      method: 'GET',
      headers: {},
    } as InternalAxiosRequestConfig,
    request: {},
  };

  beforeEach(() => {
    authService = jasmine.createSpyObj('AuthService', ['getAuthToken']);
    TestBed.configureTestingModule({
      providers: [
        AxiosService,
        { provide: AuthService, useValue: authService },
      ],
    });

    service = TestBed.inject(AxiosService);

    // Mock axios methods
    spyOn(axios, 'request').and.returnValue(Promise.resolve(mockResponse));
    spyOn(axios, 'post').and.returnValue(Promise.resolve(mockResponse));
    spyOn(axios, 'get').and.returnValue(Promise.resolve(mockResponse));
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('uploadFileRequest', () => {
    it('should make a POST request with FormData and authorization header', async () => {
      authService.getAuthToken.and.returnValue(mockToken);

      const formData = new FormData();
      formData.append('file', new Blob(['test file content']), 'test-file.txt');

      const result = await service.uploadFileRequest('/upload', formData);

      expect(authService.getAuthToken).toHaveBeenCalled();
      expect(axios.post).toHaveBeenCalledWith('/upload', formData, {
        headers: {
          Authorization: `Bearer ${mockToken}`,
          'Content-Type': 'multipart/form-data',
        },
      });
      expect(result).toEqual(mockResponse);
    });

    it('should make a POST request without authorization header when token is not available', async () => {
      authService.getAuthToken.and.returnValue(null);

      const formData = new FormData();
      formData.append('file', new Blob(['test file content']), 'test-file.txt');

      const result = await service.uploadFileRequest('/upload', formData);

      expect(authService.getAuthToken).toHaveBeenCalled();
      expect(axios.post).toHaveBeenCalledWith('/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      expect(result).toEqual(mockResponse);
    });
  });

  describe('requestDownload', () => {
    it('should make a GET request for downloading with authorization header', async () => {
      authService.getAuthToken.and.returnValue(mockToken);

      const result = await service.requestDownload('/download', { fileId: 1 });

      expect(authService.getAuthToken).toHaveBeenCalled();
      expect(axios.get).toHaveBeenCalledWith('/download', {
        params: { fileId: 1 },
        headers: { Authorization: `Bearer ${mockToken}` },
        responseType: 'blob',
      });
      expect(result).toEqual(mockResponse);
    });

    it('should make a GET request for downloading without authorization header when token is not available', async () => {
      authService.getAuthToken.and.returnValue(null);

      const result = await service.requestDownload('/download', { fileId: 1 });

      expect(authService.getAuthToken).toHaveBeenCalled();
      expect(axios.get).toHaveBeenCalledWith('/download', {
        params: { fileId: 1 },
        headers: {},
        responseType: 'blob',
      });
      expect(result).toEqual(mockResponse);
    });
  });
});
