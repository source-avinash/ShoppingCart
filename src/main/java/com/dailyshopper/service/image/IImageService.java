package com.dailyshopper.service.image;
import com.dailyshopper.dto.ImageDto;
import com.dailyshopper.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImage(Long id);
    void deleteImage(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);


}
