import {ComponentFixture, TestBed} from '@angular/core/testing';
import {DeleteClassWindowComponent} from './delete-class-window.component';
import {AxiosService} from '../../../../../service/axios/axios.service';
import {GlobalNotificationHandler} from '../../../../../service/notification/global-notification-handler.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {MockGlobalNotificationHandler} from '../../../../../utils/tests/test-commons';

describe('DeleteClassWindowComponent', () => {
  let component: DeleteClassWindowComponent;
  let fixture: ComponentFixture<DeleteClassWindowComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'DELETE' && url.includes('/classes/1')) {
        return Promise.resolve({data: {}});
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [DeleteClassWindowComponent, FormsModule],
      providers: [
        {provide: AxiosService, useValue: mockAxiosService},
        {provide: GlobalNotificationHandler, useValue: mockNotificationHandler},
        {provide: Router, useValue: mockRouter},
        {provide: ActivatedRoute, useValue: {snapshot: {paramMap: {get: () => '1'}}}},
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DeleteClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should close the window when closeWindow is called', () => {
    spyOn(component.close, 'emit');

    component.closeWindow();

    expect(component.close.emit).toHaveBeenCalled();
  });

  it('should handle errors when deleting the class fails', async () => {
    mockAxiosService.request.and.returnValue(Promise.reject(new Error('Delete failed')));
    const errorSpy = spyOn(console, 'error'); // Ensure the spy is created for console.error

    await component.confirmSelection();

    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

});
