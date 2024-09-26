package com.dailyshopper.service.image;

import com.dailyshopper.dto.ImageDto;
import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.model.Image;
import com.dailyshopper.model.Product;
import com.dailyshopper.repository.ImageRepository;
import com.dailyshopper.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

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
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {

            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownload = "/api/v1/images/download/";
                String downloadUrl = buildDownload + image.getFileName();
                image.setDownloadurl(downloadUrl);
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadurl(buildDownload + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setImageUrl(savedImage.getDownloadurl());
                savedImageDto.add(imageDto);


            } catch(IOException |  SQLException e){
               throw new RuntimeException(e.getMessage());
            }
        }

        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {

        Image image = getImage(imageId);

        try {

            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
