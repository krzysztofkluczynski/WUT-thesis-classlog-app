import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { ClassDto } from "../../../../model/entities/class-dto";
import { AxiosService } from "../../../../service/axios/axios.service";
import { AuthService } from "../../../../service/auth/auth.service";
import { Router } from "@angular/router";
import { GlobalNotificationHandler } from "../../../../service/notification/global-notification-handler.service";
import {FileDto} from "../../../../model/entities/file-dto";
import {parseDate} from "../../../../utils/date-utils";

@Component({
  selector: 'app-upload-file-popup',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './upload-file-popup.component.html',
  styleUrls: ['./upload-file-popup.component.css']
})
export class UploadFilePopupComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  @Input() classDto: ClassDto | null = null;
  @Output() fileAdded = new EventEmitter<FileDto>();

  selectedFile: File | null = null;

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {}

  closeWindow() {
    this.close.emit();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  confirmSelection() {
    if (!this.selectedFile) {
      this.globalNotificationHandler.handleMessage('File or class data is missing!');
      return;
    }

    if (!this.classDto) {
      this.globalNotificationHandler.handleMessage('Class data is missing!');
      return;
    }

    const maxSizeInBytes = 20 * 1024 * 1024; // 20MB
    if (this.selectedFile.size > maxSizeInBytes) {
      this.globalNotificationHandler.handleMessage('File size exceeds 20MB. Please select a smaller file.');
      return;
    }

    const fileDto = {
      uploadedBy: this.authService.getUserWithoutToken(),
      assignedClass: this.classDto
    };

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('fileDto', new Blob([JSON.stringify(fileDto)], { type: 'application/json' })); // Add the JSON as a Blob

    // Make the request
    this.axiosService.uploadFileRequest('/files/upload', formData)
      .then((response: { data: FileDto }) => {
        const responseData = response.data;

        const newFileDto = {
          ...responseData,
          createdAt: parseDate(responseData.createdAt),
        };

        console.log('File uploaded successfully:', newFileDto);
        this.globalNotificationHandler.handleMessage("File uploaded successfully");

        this.fileAdded.emit(newFileDto);
      })
      .catch((error) => {
        console.error('Failed to upload file:', error);
        this.globalNotificationHandler.handleError(error);
      })
      .finally(() => {
        this.closeWindow();
      });
  }

}
