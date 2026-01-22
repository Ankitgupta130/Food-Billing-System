package com.avion.billing_system.service;

import com.avion.billing_system.entity.FoodItem;
import com.avion.billing_system.repository.FoodItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepo;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String FOOD_ITEM_CACHE_KEY = "ALL_FOOD_ITEMS";

    public FoodItem save(String name, double price, MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get("D:/billing-system/uploads/");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        FoodItem item = new FoodItem();
        item.setName(name);
        item.setPrice(price);
        item.setImageUrl("/uploads/" + fileName);

        redisTemplate.delete(FOOD_ITEM_CACHE_KEY);
        return foodItemRepo.save(item);
    }



    public List<FoodItem> getAll() {
        Object cachedData = redisTemplate.opsForValue().get(FOOD_ITEM_CACHE_KEY);

        if (cachedData !=null ){
            System.out.println("Returning Food Items from redis cache");
            return objectMapper.convertValue(
                    cachedData,
                    new com.fasterxml.jackson.core.type.TypeReference<List<FoodItem>>() {}
            );
        }

        List<FoodItem> all = foodItemRepo.findAll();
        redisTemplate.opsForValue().set(FOOD_ITEM_CACHE_KEY, all, Duration.ofDays(1));

        return all;
    }

    public void delete(Long id) {
        foodItemRepo.deleteById(id);

        redisTemplate.delete(FOOD_ITEM_CACHE_KEY);
    }

    public Resource loadImage(String filename) {
        try {
            Path imagePath = Paths.get("D:/billing-system/uploads/").resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Image not found: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load image: " + filename, e);
        }
    }

    public String saveImageOnly(MultipartFile image) throws IOException {
        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path path = Paths.get("D:/billing-system/uploads/" + filename);


        return filename;
    }




    public FoodItem update(Long id, String name, double price, MultipartFile image) throws IOException {
        FoodItem existing = foodItemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        existing.setName(name);
        existing.setPrice(price);


        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImageOnly(image); // your existing image save method
            existing.setImageUrl("/uploads/"+imageUrl);
        }

        redisTemplate.delete(FOOD_ITEM_CACHE_KEY);

        return foodItemRepo.save(existing);
    }

//    public FoodItem viewEmployee(TestRequest testRequest) {
//        Employee emp = new Employee();
//        emp.setId(90L);
//        emp.setName("Shubham Bedke");
//        emp.setEmail("shumbham@gmail.com");
//        emp.setPhone("9326611780");
//        log.info(emp.toString());
//
//        Optional<FoodItem> emp1 = foodItemRepo.findById(testRequest.getId());
//        return emp1.orElseThrow(null);

//    }



}

