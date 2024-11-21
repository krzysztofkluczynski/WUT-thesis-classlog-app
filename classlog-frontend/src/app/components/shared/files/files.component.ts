import { Component } from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {
  DeleteUserFromClassWindowComponent
} from "../../pages/teacher/popup-window/delete-user-from-class-window/delete-user-from-class-window.component";
import {UploadFilePopupComponent} from "./upload-file-popup/upload-file-popup.component";
import {ClassDto} from "../../../model/entities/class-dto";
import {AxiosService} from "../../../service/axios/axios.service";
import {AuthService} from "../../../service/auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../service/notification/global-notification-handler.service";
import {ClassService} from "../../../service/class-service/class-service.service";
import {FileDto} from "../../../model/entities/file-dto";
import {parseDate} from "../../../utils/date-utils";
import {
  CreateClassWindowComponent
} from "../../pages/teacher/popup-window/create-class-window/create-class-window.component";

@Component({
  selector: 'app-files',
  standalone: true,
  imports: [
    HeaderComponent,
    DatePipe,
    NgIf,
    ReactiveFormsModule,
    DeleteUserFromClassWindowComponent,
    UploadFilePopupComponent,
    NgForOf,
    CreateClassWindowComponent
  ],
  templateUrl: './files.component.html',
  styleUrl: './files.component.css'
})
export class FilesComponent {
  showUploadFileModal: boolean = false;
  classDto: ClassDto | null = null;
  fileList: FileDto[] = []; // Add property for the list of files
  classId: number | null = null;

  constructor(
    private axiosService: AxiosService,
    public authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private globalNotificationHandler: GlobalNotificationHandler,
  ) {}

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('classId'));
    console.log('Class ID:', this.classId);

    // Fetch Class
    this.axiosService.request('GET', `/classes/${this.classId}`, {}).then(
      (response: { data: ClassDto }) => {
        this.classDto = {
          ...response.data,
          createdAt: parseDate(response.data.createdAt), // Parse the createdAt field
        };
        console.log('Received class data:', this.classDto);
        this.fetchFiles(); // Fetch files for the current class
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch class data:', error);
    });



  }

  toggleUploadFileModal() {
    this.showUploadFileModal = !this.showUploadFileModal;
  }

  fetchFiles() {
    console.log('Fetching files for class:', this.classDto);
      this.axiosService.request('GET', `/files/class/${this.classDto?.id}`, {}).then(
        (response: { data: FileDto[] }) => {
          // Map over the response and parse the dates
          this.fileList = response.data.map(file => ({
            ...file,
            createdAt: parseDate(file.createdAt) // Format the createdAt field
          }));
          console.log('Fetched files with parsed dates:', this.fileList);
        }
      ).catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch files:', error);
      });
  }


  downloadFile(fileId: number): void {
    this.axiosService
      .requestDownload(`/files/download/${fileId}`, {})
      .then((response) => {
        // Create a Blob from the response data
        const blob = new Blob([response.data], { type: response.headers["content-type"] });

        // Extract filename from Content-Disposition header
        const contentDisposition = response.headers["content-disposition"];
        const fileName = contentDisposition
          ? contentDisposition.split("filename=")[1]?.replace(/"/g, "") // Extract filename
          : `downloaded_${fileId}`; // Fallback filename

        // Create a download link and trigger download
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = fileName;
        a.click();

        // Release memory by revoking the object URL
        window.URL.revokeObjectURL(url);
        console.log("File downloaded successfully.");
      })
      .catch((error) => {
        console.error("Failed to download file:", error);
        this.globalNotificationHandler.handleError("Failed to download file.");
      });
  }

  deleteFile(id: number) {
    this.axiosService.request('DELETE', `/files/${id}`, {})
      .then((response: { data: string }) => {
      this.globalNotificationHandler.handleMessage(response.data);
      this.fileList = this.fileList.filter(file => file.fileId !== id);
    }).catch((error: any) => {
      console.error('Failed to delete file');
      this.globalNotificationHandler.handleError('Failed to delete file');
    });
  }

  getFileName(filePath: string): string {
    // Normalize the path to handle both '/' and '\' as separators
    const normalizedPath = filePath.replace(/\\/g, '/');
    const fileName = normalizedPath.split('/').pop() || filePath;

    // Truncate the file name if it exceeds 50 characters
    return fileName.length > 40 ? fileName.slice(0, 37) + '...' : fileName;
  }

  onFileAdded(file: FileDto) {
    this.fileList.push(file);
  }
}
