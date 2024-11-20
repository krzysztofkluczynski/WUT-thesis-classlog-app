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
import {DatePipe, NgForOf} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {getFullName} from "../../../../utils/user-utils";
import {FormsModule} from "@angular/forms";
import {ClassDto} from "../../../../model/entities/class-dto";

@Component({
  selector: 'app-student-class',
  standalone: true,
    imports: [
        HeaderComponent,
        NgForOf,
        DatePipe,
        FormsModule
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


    this.axiosService.request('GET', `/posts`, {}).then(
      (response: { data: PostDto[] }) => {
        this.posts = response.data.map(post => ({
          ...post,
          createdAt: parseDate(post.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch posts:', error);
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
      console.error('Failed to fetch comments:', error);
    });

    this.axiosService.request('GET', `/lessons/class/${this.classId}/recent/${5}`, {}).then( //TODO: MAKE SURE THAT 5 PASSED AS A STRING WORKS THERE
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
}
