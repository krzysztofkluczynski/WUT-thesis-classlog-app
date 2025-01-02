package com.example.classlog.service;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.FileDto;
import com.example.classlog.entity.File;
import com.example.classlog.mapper.FileMapper;
import com.example.classlog.repository.FileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileService {

  private final FileRepository fileRepository;
  private final FileMapper fileMapper;

  private final String uploadDir = "uploads/"; // Directory for storing uploaded files


  public List<FileDto> getFilesByClassId(Long classId) {
    return fileRepository.findByClassEntityClass_Id(classId).stream()
        .map(fileMapper::toFileDto)
        .toList();
  }

  public FileDto getFileById(Long fileId) {
    return fileRepository.findById(fileId)
        .map(fileMapper::toFileDto)
        .orElseThrow(() -> new AppException("File not found", HttpStatus.NOT_FOUND));
  }

  public FileDto saveFile(FileDto fileDto) {
    File file = fileMapper.toEntity(fileDto);
    File savedFile = fileRepository.save(file);
    return fileMapper.toFileDto(savedFile);
  }


  public FileDto saveFile(FileDto fileDto, MultipartFile file) throws IOException {
    // Determine the directory path
    String baseDir;
    if (fileDto.getAssignedClass() != null) {
      baseDir = uploadDir + "/class/" + fileDto.getAssignedClass().getId();
    } else {
      baseDir = uploadDir + "/tasks"; // Default directory for tasks
    }

    Path directoryPath = Paths.get(baseDir);
    Files.createDirectories(directoryPath); // Ensure the directory exists

    // Construct the initial file path
    String originalFilename = file.getOriginalFilename();
    String fileExtension = "";
    int extensionIndex = originalFilename.lastIndexOf(".");
    String fileName = originalFilename;

    if (extensionIndex > 0) {
      fileExtension = originalFilename.substring(extensionIndex);
      fileName = originalFilename.substring(0, extensionIndex);
    }

    Path filePath = directoryPath.resolve(originalFilename);
    int counter = 1;

    // Check if the file already exists and modify the file name
    while (Files.exists(filePath)) {
      filePath = directoryPath.resolve(fileName + "_" + counter++ + fileExtension);
    }

    // Save the file to the filesystem
    Files.write(filePath, file.getBytes());

    // Update the fileDto with the saved file path
    fileDto.setFilePath(filePath.toString());

    // Map the DTO to entity and save to the repository
    File createdFile = fileMapper.toEntity(fileDto);
    File savedFile = fileRepository.save(createdFile);
    return fileMapper.toFileDto(savedFile);
  }


  public void deleteFileById(FileDto file) {
    try {
      // Get the file path
      Path filePath = Paths.get(file.getFilePath());

      // Delete the file from the filesystem
      if (Files.exists(filePath)) {
        Files.delete(filePath);
      } else {
        throw new AppException("File not found on the filesystem: " + file.getFilePath(),
            HttpStatus.NOT_FOUND);
      }

      // Remove the file metadata from the database
      fileRepository.deleteById(file.getFileId());
    } catch (IOException e) {
      throw new AppException("Failed to delete the file from the filesystem: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      throw new AppException("Failed to delete file record from the database: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
