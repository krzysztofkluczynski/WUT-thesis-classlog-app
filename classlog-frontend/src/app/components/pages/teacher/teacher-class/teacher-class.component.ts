import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderComponent } from '../../../shared/header/header.component';
import { PostDto } from '../../../../model/entities/post-dto';
import { parseDate } from '../../../../utils/date-utils';
import { CommentDto } from '../../../../model/entities/comment-dto';
import { LessonDto } from '../../../../model/entities/lesson.dto';
import { AuthService } from '../../../../service/auth/auth.service';
import { AxiosService } from '../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { DatePipe, NgForOf } from "@angular/common";
import { getFullName } from "../../../../utils/user-utils";

@Component({
  selector: 'app-teacher-class',
  standalone: true,
  imports: [
    HeaderComponent,
    DatePipe,
    NgForOf
  ],
  templateUrl: './teacher-class.component.html',
  styleUrl: './teacher-class.component.css'
})
export class TeacherClassComponent implements OnInit {
  posts: PostDto[] = [];
  postCommentsMap: Map<number, CommentDto[]> = new Map(); // Map of Post ID to Comments
  lessons: LessonDto[] = [];
  classId: number | null = null;

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('id'));

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
}
