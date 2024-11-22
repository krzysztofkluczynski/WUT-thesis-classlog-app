import {Component, OnInit} from '@angular/core';
import {UserDto} from "../../../../model/entities/user-dto";
import {parseDate} from "../../../../utils/date-utils";
import {AuthService} from "../../../../service/auth/auth.service";
import {AxiosService} from "../../../../service/axios/axios.service";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {PostDto} from "../../../../model/entities/post-dto";
import {CommentDto} from "../../../../model/entities/comment-dto";
import {LessonDto} from "../../../../model/entities/lesson.dto";
import {HeaderComponent} from "../../../shared/header/header.component";
import {DatePipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {getFullName} from "../../../../utils/user-utils";
import {FormsModule} from "@angular/forms";
import {ClassDto} from "../../../../model/entities/class-dto";
import {LessonInfoWindowComponent} from "../../../shared/lesson-info-window/lesson-info-window.component";

@Component({
  selector: 'app-student-class',
  standalone: true,
    imports: [
        HeaderComponent,
        NgForOf,
        DatePipe,
        FormsModule,
        NgIf,
        NgClass,
        LessonInfoWindowComponent
    ],
  templateUrl: './student-class.component.html',
  styleUrl: './student-class.component.css'
})
export class StudentClassComponent implements OnInit {
  posts: PostDto[] = [];
  comments: CommentDto[] = [];
  lessons: LessonDto[] = [];
  classId: number | null = null;
  classDto: ClassDto | null = null;
  topic: string = '';
  message: string = '';
  postCommentsMap: Map<number, CommentDto[]> = new Map(); // Map of Post ID to Comments
  expandedComments: { [postId: number]: boolean } = {}; // Track expanded state for posts
  commentDrafts: { [postId: number]: string } = {};
  showLessonModal: boolean = false;
  selectedLessonId: number | null = null;
  classIdForSelectedLesson: number | null = null;




  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {
  }

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('id'));

    // Fetch Class
    this.axiosService.request('GET', `/classes/${this.classId}`, {}).then(
      (response: { data: ClassDto }) => {
        this.classDto = {
          ...response.data,
          createdAt: parseDate(response.data.createdAt), // Parse the createdAt field
        };
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch class data:', error);
    });


    // Fetch Posts
    this.axiosService.request('GET', `/posts/class/${this.classId}`, {}).then(
      (response: { data: PostDto[] }) => {
        this.posts = response.data.map(post => ({
          ...post,
          createdAt: parseDate(post.createdAt)
        }));
        this.fetchCommentsForPosts();
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch posts:', error);
    });

    // Fetch Lessons
    this.axiosService.request('GET', `/lessons/class/${this.classId}/recent/${5}`, {}).then(
      (response: { data: LessonDto[] }) => {
        console.log(response.data);
        this.lessons = response.data.map(lesson => ({
          ...lesson,
          lessonDate: parseDate(lesson.lessonDate)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch lessons:', error);
    });
  }

  private fetchCommentsForPosts(): void {
    this.posts.forEach(post => {
      this.axiosService.request('GET', `/comments/post/${post.id}`, {}).then(
        (response: { data: CommentDto[] }) => {
          const comments = response.data.map(comment => ({
            ...comment,
            createdAt: parseDate(comment.createdAt)
          }));
          this.postCommentsMap.set(post.id, comments);
        }
      ).catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error(`Failed to fetch comments for post ${post.id}:`, error);
      });
    });
  }

  protected readonly getFullName = getFullName;

  navigateToFiles() {
    this.router.navigate([`/files/${this.classId}`]);
  }

  sendPost(): void {
    if (!this.topic.trim() || !this.message.trim()) {
      console.log('Both fields are required!');
      this.globalNotificationHandler.handleMessage('Both fields are required!');
      return;
    }

    const postPayload = {
      assignedClass: this.classDto,
      user: this.authService.getUserWithoutToken(),
      title: this.topic,
      content: this.message,
    };

    this.axiosService
      .request('POST', '/posts', postPayload)
      .then((response: { data: PostDto }) => {
        const createdPost: PostDto = response.data;

        this.posts.unshift(createdPost);
        console.log('Post created successfully:', createdPost);

        this.topic = '';
        this.message = '';
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to create post:', error.response?.data || error);
      });

    this.topic = '';
    this.message = '';
  }

  toggleComments(postId: number): void {
    this.expandedComments[postId] = !this.expandedComments[postId];
  }

  getVisibleComments(postId: number): CommentDto[] {
    const comments = this.postCommentsMap.get(postId) || [];
    return this.expandedComments[postId] ? comments : comments.slice(0, 2);
  }

  sendComment(post: PostDto): void {
    const commentContent = this.commentDrafts[post.id]?.trim();
    if (!commentContent) {
      console.log('Comment cannot be empty.');
      this.globalNotificationHandler.handleMessage('Comment cannot be empty.');
      return;
    }

    const commentPayload = {
      post: post,
      user: this.authService.getUserWithoutToken(),
      content: commentContent,
    };

    this.axiosService
      .request('POST', `/comments`, commentPayload)
      .then((response: { data: CommentDto }) => {
        const createdComment: CommentDto = response.data;
        console.log('Created comment:', createdComment);

        if (!this.postCommentsMap.has(post.id)) {
          this.postCommentsMap.set(post.id, []);
        }
        this.postCommentsMap.get(post.id)!.push(createdComment);

        console.log('Comment created successfully:', createdComment);

        this.commentDrafts[post.id] = '';
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to create comment:', error.response?.data || error);
      });
  }

  toggleLessonWindow(lessonId: number | null, classId: number | null) {
    this.selectedLessonId = lessonId;
    this.classIdForSelectedLesson = classId;
    this.showLessonModal = !this.showLessonModal;
  }
}
