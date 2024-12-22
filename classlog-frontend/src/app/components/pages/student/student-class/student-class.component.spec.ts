import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StudentClassComponent } from './student-class.component';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../service/auth/auth.service';
import { AxiosService } from '../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { PostDto } from '../../../../model/entities/post-dto';
import { CommentDto } from '../../../../model/entities/comment-dto';
import { ClassDto } from '../../../../model/entities/class-dto';
import { LessonDto } from '../../../../model/entities/lesson.dto';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { MockRouter, MockAuthService, MockGlobalNotificationHandler } from '../../../../utils/tests/test-commons';
import {UserDto} from "../../../../model/entities/user-dto";

describe('StudentClassComponent', () => {
  let component: StudentClassComponent;
  let fixture: ComponentFixture<StudentClassComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockAuthService: MockAuthService;
  let mockNotificationHandler: jasmine.SpyObj<GlobalNotificationHandler>;
  let mockRouter: MockRouter;

  const mockClass: ClassDto = {
    id: 1,
    name: 'Class A',
    description: 'A test class',
    createdAt: new Date('2023-01-01'),
  };

  const mockPosts: PostDto[] = [
    {
      id: 1,
      assignedClass: mockClass,
      user: { id: 1, name: 'Test User', surname: 'User', email: 'user@test.com', role: { id: 1, roleName: 'Student' }, token: '', createdAt: new Date() },
      title: 'Test Post',
      content: 'This is a test post',
      createdAt: new Date('2023-02-01'),
    },
  ];

  const mockComments: CommentDto[] = [
    {
      id: 1,
      content: 'Test Comment',
      createdAt: new Date('2023-02-02'),
      post: mockPosts[0] as PostDto,
      user: { id: 1, name: 'Comment User', surname: '', email: '' } as UserDto,
    },
  ];

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockAuthService = new MockAuthService();
    mockNotificationHandler = jasmine.createSpyObj('GlobalNotificationHandler', ['handleError', 'handleMessage']);
    mockRouter = new MockRouter();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'GET' && url.includes('/classes/1')) return Promise.resolve({ data: mockClass });
      if (method === 'GET' && url.includes('/posts/class/1')) return Promise.resolve({ data: mockPosts });
      if (method === 'GET' && url.includes('/comments/post/1')) return Promise.resolve({ data: mockComments });
      return Promise.reject(new Error('Unexpected URL or method'));
    });

    await TestBed.configureTestingModule({
      imports: [FormsModule, StudentClassComponent],
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

    fixture = TestBed.createComponent(StudentClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch class data on initialization', async () => {
    await component.ngOnInit();
    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/classes/1', {});
  });

  it('should fetch posts and their comments', async () => {
    await component.ngOnInit();
    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/posts/class/1', {});
    expect(component.posts.length).toBe(1);
    expect(component.posts[0].title).toBe('Test Post');

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/comments/post/1', {});
    expect(component.postCommentsMap.get(1)?.length).toBe(1);
    expect(component.postCommentsMap.get(1)![0].content).toBe('Test Comment');
  });

  it('should handle errors during fetching class data', async () => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/classes/1')) return Promise.reject(new Error('Class fetch error'));
      return Promise.resolve({});
    });

    await component.ngOnInit();
    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(jasmine.any(Error));
  });

  it('should send a post successfully', async () => {
    const newPost: PostDto = {
      id: 2,
      assignedClass: mockClass,
      user: mockAuthService.getUserWithoutToken(),
      title: 'New Post',
      content: 'New content',
      createdAt: new Date(),
    };

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'POST' && url === '/posts') return Promise.resolve({ data: newPost });
      return Promise.resolve({});
    });

    component.topic = 'New Post';
    component.message = 'New content';

    await component.sendPost();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/posts', jasmine.any(Object));
    expect(component.posts[0].title).toBe('New Post');
    expect(component.topic).toBe('');
    expect(component.message).toBe('');
  });

  it('should handle errors during sending a post', async () => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'POST' && url === '/posts') return Promise.reject(new Error('Post creation error'));
      return Promise.resolve({});
    });

    component.topic = 'New Post';
    component.message = 'New content';

    await component.sendPost();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(jasmine.any(Error));
  });

  it('should toggle comments correctly', () => {
    component.toggleComments(1);
    expect(component.expandedComments[1]).toBeTrue();
    component.toggleComments(1);
    expect(component.expandedComments[1]).toBeFalse();
  });

  it('should fetch visible comments', () => {
    component.postCommentsMap.set(1, mockComments);
    const visibleComments = component.getVisibleComments(1);
    expect(visibleComments.length).toBe(1);
    expect(visibleComments[0].content).toBe('Test Comment');
  });

  it('should send a comment successfully', async () => {
    const newComment: CommentDto = {
      id: 2,
      content: 'New Comment',
      createdAt: new Date(),
      post: mockPosts[0],
      user: mockAuthService.getUserWithoutToken(),
    };

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'POST' && url === '/comments') return Promise.resolve({ data: newComment });
      return Promise.resolve({});
    });

    component.commentDrafts[1] = 'New Comment';

    await component.sendComment(mockPosts[0]);

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/comments', jasmine.any(Object));
    expect(component.postCommentsMap.get(1)?.length).toBe(2);
  });

  it('should handle errors during sending a comment', async () => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (method === 'POST' && url === '/comments') return Promise.reject(new Error('Comment creation error'));
      return Promise.resolve({});
    });

    component.commentDrafts[1] = 'New Comment';

    await component.sendComment(mockPosts[0]);

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(jasmine.any(Error));
  });
});
