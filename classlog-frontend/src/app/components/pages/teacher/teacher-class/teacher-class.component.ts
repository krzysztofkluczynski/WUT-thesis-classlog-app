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

@Component({
  selector: 'app-teacher-class',
  standalone: true,
  imports: [
    HeaderComponent
  ],
  templateUrl: './teacher-class.component.html',
  styleUrl: './teacher-class.component.css'
})
export class TeacherClassComponent implements OnInit {
  posts: PostDto[] = [];
  comments: CommentDto[] = [];
  lessons: LessonDto[] = [];
  classId: number | null = null;

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('classId'));

    this.axiosService.request('GET', `/posts`, {}).then(
      (response: { data: PostDto[] }) => {
        this.posts = response.data.map(post => ({
          ...post,
          createdAt: parseDate(post.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch lessons:', error);
    });

    this.axiosService.request('GET', `/comments`, {}).then(
      (response: { data: CommentDto[] }) => {
        this.comments = response.data.map(comment => ({
          ...comment,
          createdAt: parseDate(comment.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch lessons:', error);
    });

    this.axiosService.request('GET', `/lessons/class/${this.classId}/recent/5`, {}).then( //TODO: MAKE SURE THAT 5 PASSED AS A STRING WORKS THERE
      (response: { data: LessonDto[] }) => {
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
}
