package com.avion.billing_system.controller;

import com.avion.billing_system.dto.FoodItemRequest;

import com.avion.billing_system.entity.Employee;
import com.avion.billing_system.entity.FoodItem;
import com.avion.billing_system.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FoodItemController {

    private final FoodItemService foodItemService;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        String filename = foodItemService.saveImageOnly(image);
        return ResponseEntity.ok().body("{\"filename\": \"" + filename + "\"}");
    }

    @PostMapping
    public ResponseEntity<FoodItem> createFoodItem(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        FoodItem saved = foodItemService.save(name,price,image);
        return ResponseEntity.ok(saved);
    }


    @GetMapping
    public List<FoodItem> getAll() {
        return foodItemService.getAll();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        foodItemService.delete(id);
    }


    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        Resource image = foodItemService.loadImage(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.IMAGE_JPEG)

                .body(image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodItem> updateFoodItem(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        FoodItem updated = foodItemService.update(id, name, price, image);
        return ResponseEntity.ok(updated);
    }

//    @GetMapping("/user")
//    public ResponseEntity<FoodItem> viewEmployeeController(@RequestBody TestRequest testRequest){
//        FoodItem emp = foodItemService.viewEmployee(testRequest);
//        return ResponseEntity.ok(emp);
//    }

}
