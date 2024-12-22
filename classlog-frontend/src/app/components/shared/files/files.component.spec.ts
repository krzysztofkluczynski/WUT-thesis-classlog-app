import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FilesComponent } from './files.component';
import { AxiosService } from '../../../service/axios/axios.service';
import { AuthService } from '../../../service/auth/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalNotificationHandler } from '../../../service/notification/global-notification-handler.service';
import {MockRouter, MockGlobalNotificationHandler, MockAuthService} from '../../../utils/tests/test-commons';
import { FileDto } from '../../../model/entities/file-dto';
import { ClassDto } from '../../../model/entities/class-dto';
import { UserDto } from '../../../model/entities/user-dto';

describe('FilesComponent', () => {
  let component: FilesComponent;
  let fixture: ComponentFixture<FilesComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockRouter: MockRouter;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockAuthService: MockAuthService;

  const mockClass: ClassDto = {
    id: 1,
    name: 'Sample Class',
    description: 'A test class',
    createdAt: new Date('2023-01-01'),
  };

  const mockUser: UserDto = {
    id: 1,
    name: 'John',
    surname: 'Doe',
    email: 'john.doe@example.com',
    role: { id: 2, roleName: 'Student' },
    token: '',
    createdAt: new Date('2023-01-01'),
  };

  const mockFiles: FileDto[] = [
    {
      fileId: 1,
      filePath: '/uploads/sample1.pdf',
      assignedClass: mockClass,
      uploadedBy: mockUser,
      createdAt: new Date('2023-01-02'),
    },
    {
      fileId: 2,
      filePath: '/uploads/sample2.pdf',
      assignedClass: mockClass,
      uploadedBy: mockUser,
      createdAt: new Date('2023-01-03'),
    },
  ];

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request', 'requestDownload']);
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockAuthService = new MockAuthService();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/classes/1')) {
        return Promise.resolve({ data: mockClass });
      }
      if (url.includes('/files/class/1')) {
        return Promise.resolve({ data: mockFiles });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [FilesComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch class data on initialization', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/classes/1', {});
    expect(component.classDto?.id).toEqual(mockClass.id);
  });

  it('should fetch files on initialization', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/files/class/1', {});
    expect(component.fileList[0].fileId).toEqual(mockFiles[0].fileId);
  });

  it('should toggle the upload file modal', () => {
    component.toggleUploadFileModal();
    expect(component.showUploadFileModal).toBeTrue();

    component.toggleUploadFileModal();
    expect(component.showUploadFileModal).toBeFalse();
  });

  it('should delete a file and update the file list', async () => {
    const fileIdToDelete = 1;
    mockAxiosService.request.and.returnValue(Promise.resolve({ data: 'File deleted successfully' }));

    await component.deleteFile(fileIdToDelete);

    expect(mockAxiosService.request).toHaveBeenCalledWith('DELETE', `/files/${fileIdToDelete}`, {});
    expect(component.fileList.length).toBe(1);
    expect(component.fileList.find(file => file.fileId === fileIdToDelete)).toBeUndefined();
  });

  it('should download a file successfully', async () => {
    const fileIdToDownload = 1;
    const mockBlob = new Blob(['file content'], { type: 'application/pdf' });
    mockAxiosService.requestDownload.and.returnValue(Promise.resolve({
      data: mockBlob,
      headers: { 'content-disposition': 'attachment; filename="sample1.pdf"', 'content-type': 'application/pdf' },
    }));

    spyOn(window.URL, 'createObjectURL').and.returnValue('mock-url');
    const mockAnchor = jasmine.createSpyObj('a', ['click']);
    spyOn(document, 'createElement').and.returnValue(mockAnchor);

    await component.downloadFile(fileIdToDownload);

    expect(mockAxiosService.requestDownload).toHaveBeenCalledWith(`/files/download/${fileIdToDownload}`, {});
    expect(mockAnchor.download).toBe('sample1.pdf');
    expect(mockAnchor.href).toBe('mock-url');
  });
});
