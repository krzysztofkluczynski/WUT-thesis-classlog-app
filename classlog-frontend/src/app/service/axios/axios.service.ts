import { Injectable } from '@angular/core';
import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import { AuthService } from './../auth/auth.service';
import { UserDto } from "../../model/entities/user-dto";
import { GlobalNotificationHandler } from "../notification/global-notification-handler.service";

@Injectable({
  providedIn: 'root'
})
export class AxiosService {
  private isRefreshing = false;
  private failedRequestsQueue: Array<() => void> = [];

  constructor(private authService: AuthService, private globalNotificationHandler: GlobalNotificationHandler) {
    axios.defaults.baseURL = "http://localhost:8080";
    axios.defaults.headers.post['Content-Type'] = 'application/json';

    // Add Axios interceptor
    axios.interceptors.response.use(
      (response) => response, // Return response if successful
      async (error) => {
        const originalRequest = error.config;

        // Check if the error is related to an expired token
        if (
          error.response &&
          error.response.status === 401 &&
          error.response.data.error &&
          error.response.data.error.includes("Token has expired") &&
          !originalRequest._isRetryAttempt // Ensure we retry only once
        ) {
          originalRequest._isRetryAttempt = true; // Mark this request as retried
          return this.handleTokenExpiration(originalRequest);
        }

        // If the error is not related to token expiration or retry already attempted, reject it
        return Promise.reject(error);
      }
    );
  }

  async request(method: string, url: string, data: any): Promise<any> {
    const headers: any = {};

    const token = this.authService.getAuthToken();
    if (token) {
      headers["Authorization"] = "Bearer " + token;
    }

    return axios({
      method,
      url,
      data,
      headers,
    });
  }

  async uploadFileRequest(url: string, formData: FormData): Promise<any> {
    const headers: any = {};

    const token = this.authService.getAuthToken();
    if (token) {
      headers["Authorization"] = "Bearer " + token;
    }

    headers["Content-Type"] = "multipart/form-data";

    return axios.post(url, formData, { headers });
  }

  async requestDownload(url: string, params: any): Promise<any> {
    const headers: any = {};

    const token = this.authService.getAuthToken();
    if (token) {
      headers["Authorization"] = "Bearer " + token;
    }

    return axios.get(url, {
      params,
      headers,
      responseType: "blob",
    });
  }

  private async handleTokenExpiration(originalRequest: AxiosRequestConfig): Promise<AxiosResponse> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;

      try {
        const user = this.authService.getUser();
        if (!user) {
          throw new Error("No user information available to refresh the token");
        }

        // Send request to refresh token
        const response = await this.request('POST', '/refresh-token', user);
        const refreshedUser: UserDto = response.data;
        this.authService.setUser(refreshedUser);

        // Upewnij się, że nagłówki istnieją
        if (!originalRequest.headers) {
          originalRequest.headers = {}; // Inicjalizuj nagłówki jako pusty obiekt
        }

        // Retry original request with new token
        originalRequest.headers["Authorization"] = "Bearer " + refreshedUser.token;
        return axios(originalRequest);
      } catch (error) {
        // Notify user and clear session if token refresh fails
        this.globalNotificationHandler.handleMessage("Could not refresh token. Please log in again.");
        this.authService.setUser(null);
        return Promise.reject(error); // Reject the request
      } finally {
        this.isRefreshing = false;
      }
    }

    // Kolejkowanie żądań w czasie odświeżania tokena
    return new Promise((resolve, reject) => {
      this.failedRequestsQueue.push(() => {
        axios(originalRequest)
          .then(resolve)
          .catch(reject);
      });
    });
  }

}
