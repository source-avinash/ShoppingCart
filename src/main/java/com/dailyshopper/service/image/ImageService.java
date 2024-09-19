package com.dailyshopper.service.image;

import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.model.Image;
import com.dailyshopper.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;


@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No image found with id "+ id ));
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete,
                        () -> {throw new ResourceNotFoundException("No image found with id "+ id );});
    }

    @Override
    public Image saveImage(MultipartFile file, Long productId) {
        return null;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {

        try {
            Image image = getImage(imageId);
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
