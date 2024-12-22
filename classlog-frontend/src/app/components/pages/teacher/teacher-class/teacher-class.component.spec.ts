import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeacherClassComponent } from './teacher-class.component';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { AuthService } from '../../../../service/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { MockRouter, MockAuthService, MockGlobalNotificationHandler } from '../../../../utils/tests/test-commons';
import { PostDto } from '../../../../model/entities/post-dto';
import { ClassDto } from '../../../../model/entities/class-dto';
import {CommentDto} from "../../../../model/entities/comment-dto";
import {UserDto} from "../../../../model/entities/user-dto";
import {AxiosService} from "../../../../service/axios/axios.service";

describe('TeacherClassComponent', () => {
  let component: TeacherClassComponent;
  let fixture: ComponentFixture<TeacherClassComponent>;
  let mockRouter: MockRouter;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockAuthService: MockAuthService;
  let mockNotificationHandler: jasmine.SpyObj<GlobalNotificationHandler>;

  const mockClass: ClassDto = {
    id: 1,
    name: 'Class A',
    description: 'A sample class',
    createdAt: new Date('2023-01-01'),
  };

  const mockPosts: PostDto[] = [
    {
      id: 1,
      assignedClass: mockClass,
      user: {
        id: 1,
        name: 'Teacher',
        surname: 'Test',
        email: 'teacher@test.com',
        role: { id: 1, roleName: 'Teacher' },
        token: '',
        createdAt: new Date('2023-01-01'),
      },
      title: 'Post 1',
      content: 'Content of post 1',
      createdAt: new Date('2023-02-01'),
    },
  ];

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockNotificationHandler = jasmine.createSpyObj('GlobalNotificationHandler', ['handleError']);
    mockAuthService = new MockAuthService();

    // Mock the AxiosService behavior
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'GET' && url.includes('/classes')) {
        return Promise.resolve({ data: mockClass });
      }
      if (method === 'GET' && url.includes('/posts/class')) {
        return Promise.resolve({ data: mockPosts });
      }
      return Promise.reject(new Error('Unexpected URL or Method'));
    });

    await TestBed.configureTestingModule({
      imports: [FormsModule, TeacherClassComponent],
      declarations: [],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: AuthService, useValue: mockAuthService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: AxiosService, useValue: mockAxiosService },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '1' } } },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TeacherClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch class data and handle errors correctly', async () => {
    // Arrange
    const error = new Error('Failed to fetch class');
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'GET' && url.includes('/classes/1')) {
        return Promise.reject(error);
      }
      return Promise.resolve({ data: {} });
    });

    // Act
    await component.ngOnInit();
    fixture.detectChanges();

    // Assert
    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/classes/1', {});
  });

  it('should fetch posts successfully', async () => {
    // Act
    await component.ngOnInit();
    fixture.detectChanges();

    // Assert
    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/posts/class/1', {});
    expect(component.posts.length).toBe(1);
    expect(component.posts[0].title).toBe('Post 1');
  });

  it('should handle error when fetching posts', async () => {
    // Arrange
    const error = new Error('Failed to fetch posts');
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'GET' && url.includes('/posts/class/1')) {
        return Promise.reject(error);
      }
      return Promise.resolve({ data: {} });
    });

    // Act
    await component.ngOnInit();
    fixture.detectChanges();

    // Assert
    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/posts/class/1', {});
  });

  it('should send a new post successfully', async () => {
    // Arrange
    const newPost: PostDto = {
      id: 2,
      assignedClass: mockClass,
      user: mockAuthService.getUser(),
      title: 'New Post',
      content: 'New content',
      createdAt: new Date(),
    };
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'POST' && url === '/posts') {
        return Promise.resolve({ data: newPost });
      }
      return Promise.resolve({ data: {} });
    });

    component.topic = 'New Post';
    component.message = 'New content';

    // Act
    await component.sendPost();
    fixture.detectChanges();

    // Assert
    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/posts', jasmine.any(Object));
    expect(component.posts[0].title).toBe('New Post');
  });

  it('should delete a post successfully', async () => {
    // Act
    await component.deletePost(1);
    fixture.detectChanges();

    // Assert
    expect(mockAxiosService.request).toHaveBeenCalledWith('DELETE', '/posts/1', {});
  });
});
