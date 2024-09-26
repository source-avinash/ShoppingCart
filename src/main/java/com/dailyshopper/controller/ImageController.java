package com.dailyshopper.controller;


import com.dailyshopper.dto.ImageDto;
import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.model.Image;
import com.dailyshopper.response.ApiResponse;
import com.dailyshopper.service.image.IImageService;
import com.dailyshopper.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {



    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload Successful" , imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed" , e.getMessage()));
        }

    }


    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId ) throws SQLException {

        Image image = imageService.getImage(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage()
                .getBytes(1,(int) image
                        .getImage().length()));
        return ResponseEntity.ok().contentType(MediaType
                        .parseMediaType(image
                                .getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= \"" +image.getFileName()+ "\"")
                .body(resource);
    }


    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) throws SQLException {

        try {
            Image image = imageService.getImage(imageId);
            if(image != null){
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("Update Successful" , null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update Failed" , null));
    }


    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) throws SQLException {

        try {
            Image image = imageService.getImage(imageId);
            if(image != null){
                imageService.deleteImage(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete Successful" , null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed" , null));
    }
}
