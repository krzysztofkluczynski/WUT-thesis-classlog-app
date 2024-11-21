import { Injectable } from '@angular/core';
import axios from "axios";
import { AuthService } from './../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor(private authService: AuthService) {
    axios.defaults.baseURL = "http://localhost:8080";
    axios.defaults.headers.post['Content-Type'] = 'application/json';
  }

  request(method: string, url: string, data: any): Promise<any> | any {
    let headers: any = {};

    const token = this.authService.getAuthToken();
    if (token !== null) {
      headers = {"Authorization": "Bearer " + token};
    }

    return axios({
      method: method,
      url: url,
      data: data,
      headers: headers
    });
  }

  uploadFileRequest(url: string, formData: FormData): Promise<any> {
    const headers: any = {};

    // Add Authorization header if token exists
    const token = this.authService.getAuthToken();
    if (token !== null) {
      headers["Authorization"] = "Bearer " + token;
    }

    headers["Content-Type"] = "multipart/form-data";

    // Do not manually set Content-Type for FormData; let Axios handle it
    return axios.post(url, formData, { headers });
  }

  requestDownload(url: string, params: any): Promise<any> {
    const headers: any = {};

    const token = this.authService.getAuthToken();
    if (token !== null) {
      headers["Authorization"] = "Bearer " + token;
    }

    return axios.get(url, {
      params, // Pass parameters like fileId
      headers,
      responseType: "blob", // Ensure the response is treated as binary data
    });
  }
}
