import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HeaderComponent} from '../../../shared/header/header.component';
import {PostDto} from '../../../../model/entities/post-dto';
import {parseDate} from '../../../../utils/date-utils';
import {CommentDto} from '../../../../model/entities/comment-dto';
import {LessonDto} from '../../../../model/entities/lesson.dto';
import {AuthService} from '../../../../service/auth/auth.service';
import {AxiosService} from '../../../../service/axios/axios.service';
import {GlobalNotificationHandler} from '../../../../service/notification/global-notification-handler.service';
import {DatePipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {getFullName} from "../../../../utils/user-utils";
import {
  AddUserToClassWindowComponent
} from "../popup-window/add-user-to-class-window/add-user-to-class-window.component";
import {
  DeleteUserFromClassWindowComponent
} from "../popup-window/delete-user-from-class-window/delete-user-from-class-window.component";
import {FormsModule} from "@angular/forms";
import {ClassDto} from "../../../../model/entities/class-dto";
import {CreateClassWindowComponent} from "../popup-window/create-class-window/create-class-window.component";
import {ShowClassCodeWindowComponent} from "../popup-window/show-class-code-window/show-class-code-window.component";
import {LessonInfoWindowComponent} from "../../../shared/lesson-info-window/lesson-info-window.component";
import {ShowMembersWindowComponent} from "../popup-window/show-members-window/show-members-window.component";
import {DeleteClassWindowComponent} from "../popup-window/delete-class-window/delete-class-window.component";

@Component({
  selector: 'app-teacher-class',
  standalone: true,
  imports: [
    HeaderComponent,
    DatePipe,
    NgForOf,
    AddUserToClassWindowComponent,
    NgIf,
    DeleteUserFromClassWindowComponent,
    FormsModule,
    NgClass,
    CreateClassWindowComponent,
    ShowClassCodeWindowComponent,
    LessonInfoWindowComponent,
    ShowMembersWindowComponent,
    DeleteClassWindowComponent
  ],
  templateUrl: './teacher-class.component.html',
  styleUrl: './teacher-class.component.css'
})
export class TeacherClassComponent implements OnInit {
  posts: PostDto[] = [];
  postCommentsMap: Map<number, CommentDto[]> = new Map(); // Map of Post ID to Comments
  expandedComments: { [postId: number]: boolean } = {}; // Track expanded state for posts
  lessons: LessonDto[] = [];
  classId: number | null = null;
  classDto: ClassDto | null = null;
  showAddUserModal: boolean = false;
  showDeleteUserModal: boolean = false;
  showClassCodeModal: boolean = false;
  showLessonModal: boolean = false;
  showMembersModal: boolean = false;
  selectedLessonId: number | null = null;
  topic: string = '';
  message: string = '';
  classIdForSelectedLesson: number | null = null;
  showDeleteClassModal: boolean = false;


  commentDrafts: { [postId: number]: string } = {};

  constructor(
    public authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute,
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

  // Fetch comments for each post and populate the map
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

  toggleAddUserModal() {
    this.showAddUserModal = !this.showAddUserModal;
  }

  toggleDeleteUserModal() {
    this.showDeleteUserModal = !this.showDeleteUserModal;
  }

  toggleShowClassCodeModal() {
    this.showClassCodeModal = !this.showClassCodeModal;
    if (!this.showClassCodeModal) {
      window.location.reload();
    }
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

    // Create the comment payload
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

        // Update comments map
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


  navigateToLessonCreation() {
    this.router.navigate([`/teacher/lessonCreator/${this.classId}`], {queryParams: {editMode: false}});
  }

  toggleLessonWindow(lessonId: number | null, classId: number | null) {
    this.selectedLessonId = lessonId;
    this.classIdForSelectedLesson = classId;
    this.showLessonModal = !this.showLessonModal;
  }

  deleteComment(comment: CommentDto) {
    this.axiosService.request('DELETE', `/comments/${comment.id}`, {}).then(
      (response: any) => {
        this.globalNotificationHandler.handleMessagewithType(`Comment deleted successfully.`, 'success');
        const postId = comment.post.id;
        const updatedComments = (this.postCommentsMap.get(postId) || []).filter(c => c.id !== comment.id);
        this.postCommentsMap.set(postId, updatedComments);
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to delete post:', error);
    });
  }

  deletePost(id: number) {
    this.axiosService.request('DELETE', `/posts/${id}`, {}).then(
      (response: any) => {
        this.globalNotificationHandler.handleMessagewithType(`Post deleted successfully.`, 'success');
        this.posts = this.posts.filter(post => post.id !== id);
        this.postCommentsMap.delete(id); // Clean up comments for the deleted post
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to delete comment:', error);
    });
  }

  toggleShowMembersModal() {
    this.showMembersModal = !this.showMembersModal;
  }

  toggleDeleteClassModal(): void {
    this.showDeleteClassModal = !this.showDeleteClassModal;
  }
}
