package com.dailyshopper.service.image;
import com.dailyshopper.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    Image getImage(Long id);
    void deleteImage(Long id);
    Image saveImage(MultipartFile file, Long imageId);
    void updateImage(MultipartFile file, Long imageId);


}
