import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UploadFilePopupComponent } from './upload-file-popup.component';
import { FormsModule } from '@angular/forms';
import { AxiosService } from '../../../../service/axios/axios.service';
import { AuthService } from '../../../../service/auth/auth.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { EventEmitter } from '@angular/core';
import { ClassDto } from '../../../../model/entities/class-dto';
import { FileDto } from '../../../../model/entities/file-dto';
import {UserDto} from "../../../../model/entities/user-dto";

describe('UploadFilePopupComponent', () => {
  let component: UploadFilePopupComponent;
  let fixture: ComponentFixture<UploadFilePopupComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockGlobalNotificationHandler: jasmine.SpyObj<GlobalNotificationHandler>;

  const mockClassDto: ClassDto = {
    id: 1,
    name: 'Test Class',
    description: 'This is a test class',
    createdAt: new Date(),
  };

  const mockUserDto: UserDto = {
    id: 1,
    name: 'John Doe',
    email: 'john@example.com',
    surname: 'Doe',
    role: { id: 2, roleName: 'Student' },
    token: 'mock-token',
    createdAt: new Date(),
  };

  const mockFileDto: FileDto = {
    fileId: 1,
    filePath: '/uploads/test-file.pdf',
    assignedClass: mockClassDto,
    uploadedBy: mockUserDto,
    createdAt: new Date(),
  };

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['uploadFileRequest']);
    mockAuthService = jasmine.createSpyObj('AuthService', ['getUserWithoutToken']);
    mockGlobalNotificationHandler = jasmine.createSpyObj('GlobalNotificationHandler', ['handleMessage', 'handleError']);

    await TestBed.configureTestingModule({
      imports: [FormsModule, UploadFilePopupComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: GlobalNotificationHandler, useValue: mockGlobalNotificationHandler },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UploadFilePopupComponent);
    component = fixture.componentInstance;

    // Default inputs
    component.classDto = mockClassDto;
    component.close = new EventEmitter<void>();
    component.fileAdded = new EventEmitter<FileDto>();

    // Spy on outputs
    spyOn(component.close, 'emit').and.callThrough();
    spyOn(component.fileAdded, 'emit').and.callThrough();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('closeWindow', () => {
    it('should emit close event when called', () => {
      component.closeWindow();
      expect(component.close.emit).toHaveBeenCalled();
    });
  });

  describe('onFileSelected', () => {
    it('should set selectedFile to the file from the event', () => {
      const mockFile = new File(['file content'], 'test-file.pdf', { type: 'application/pdf' });
      const event = { target: { files: [mockFile] } };

      component.onFileSelected(event);

      expect(component.selectedFile).toBe(mockFile);
    });
  });

  describe('confirmSelection', () => {
    it('should show error message if no file is selected', () => {
      component.selectedFile = null;

      component.confirmSelection();

      expect(mockGlobalNotificationHandler.handleMessage).toHaveBeenCalledWith('File or class data is missing!');
    });

    it('should show error message if classDto is not provided', () => {
      component.selectedFile = new File(['file content'], 'test-file.pdf', { type: 'application/pdf' });
      component.classDto = null;

      component.confirmSelection();

      expect(mockGlobalNotificationHandler.handleMessage).toHaveBeenCalledWith('Class data is missing!');
    });

    it('should show error message if file size exceeds 20MB', () => {
      const largeFile = new File([new ArrayBuffer(21 * 1024 * 1024)], 'large-file.pdf', { type: 'application/pdf' }); // 21MB
      component.selectedFile = largeFile;

      component.confirmSelection();

      expect(mockGlobalNotificationHandler.handleMessage).toHaveBeenCalledWith(
        'File size exceeds 20MB. Please select a smaller file.'
      );
    });

    // it('should upload file and emit fileAdded event on success', async () => {
    //   const mockFile = new File(['file content'], 'test-file.pdf', { type: 'application/pdf' });
    //   component.selectedFile = mockFile;
    //   mockAuthService.getUserWithoutToken.and.returnValue(mockUserDto);
    //   mockAxiosService.uploadFileRequest.and.returnValue(Promise.resolve({ data: mockFileDto }));
    //
    //   await component.confirmSelection();
    //
    //   expect(mockAxiosService.uploadFileRequest).toHaveBeenCalledWith('/files/upload', jasmine.any(FormData));
    //   expect(mockGlobalNotificationHandler.handleMessage).toHaveBeenCalledWith('File uploaded successfully');
    //   expect(component.fileAdded.emit).toHaveBeenCalledWith(mockFileDto);
    // });
    //
    // it('should handle upload error', async () => {
    //   const mockFile = new File(['file content'], 'test-file.pdf', { type: 'application/pdf' });
    //   component.selectedFile = mockFile;
    //   mockAuthService.getUserWithoutToken.and.returnValue(mockUserDto);
    //   const mockError = new Error('Upload failed');
    //   mockAxiosService.uploadFileRequest.and.returnValue(Promise.reject(mockError));
    //
    //   await component.confirmSelection();
    //
    //   expect(mockGlobalNotificationHandler.handleError).toHaveBeenCalledWith(mockError);
    // });
  });
});
