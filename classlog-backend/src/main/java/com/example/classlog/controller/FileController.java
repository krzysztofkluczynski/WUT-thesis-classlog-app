package com.example.classlog.controller;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.FileDto;
import com.example.classlog.service.FileService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

  private final FileService fileService;
  private final String uploadDir = "uploads/"; // Directory for storing uploaded files

  @PostMapping("/upload")
  public ResponseEntity<FileDto> uploadFile(
      @RequestParam("file") MultipartFile file, // Handles the file part
      @RequestPart("fileDto") FileDto fileDto   // Handles the JSON part
  ) throws IOException {

    FileDto createdFile = fileService.saveFile(fileDto, file);

    return ResponseEntity.ok(createdFile);
  }

  @GetMapping("/class/{classId}")
  public List<FileDto> getFilesByClassId(@PathVariable Long classId) {
    return fileService.getFilesByClassId(classId);
  }

  @GetMapping("/{fileId}")
  public FileDto getFileById(@PathVariable Long fileId) {
    return fileService.getFileById(fileId);
  }

  @DeleteMapping("/{fileId}")
  public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
    try {
      FileDto file = fileService.getFileById(fileId);
      fileService.deleteFileById(file);
      return ResponseEntity.ok("File deleted successfully.");
    } catch (AppException e) {
      // Handle custom exceptions
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    } catch (Exception e) {
      // Handle unexpected exceptions
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while deleting the file.");
    }
  }


  @GetMapping("/download/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {
    // Retrieve file metadata
    FileDto fileMetadata = fileService.getFileById(fileId);
    Path filePath = Paths.get(fileMetadata.getFilePath());

    // Check if the file exists
    Resource resource = new UrlResource(filePath.toUri());
    if (!resource.exists()) {
      throw new AppException("File not found", HttpStatus.NOT_FOUND);
    }

    long maxSizeInBytes = 20 * 1024 * 1024; // 20MB
    long fileSize = Files.size(filePath); // Get the file size in bytes
    if (fileSize > maxSizeInBytes) {
      throw new AppException("File size exceeds the 20MB limit. Download not allowed.",
          HttpStatus.BAD_REQUEST);
    }

    String contentType = Files.probeContentType(filePath); // Automatically detect MIME type
    if (contentType == null) {
      contentType = "application/octet-stream"; // Fallback content type
    }

    // Set headers for file download
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType)) // Use detected content type
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
        .body(resource);
  }
}
