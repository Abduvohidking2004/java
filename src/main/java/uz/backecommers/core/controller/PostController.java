package uz.backecommers.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.backecommers.core.dto.PostAddRequest;
import uz.backecommers.core.dto.PostEditRequest;
import uz.backecommers.core.service.impl.PostServiceImpl;
import uz.backecommers.identety.dto.ApiResponse;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostServiceImpl postService;

    @PostMapping("/add")
    public HttpEntity<ApiResponse> addPost(@RequestBody PostAddRequest request, HttpServletRequest http){
        ApiResponse apiResponse = postService.createPost(request, http);
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 400).body(apiResponse);
    }
    @PutMapping("/edit")
    public HttpEntity<ApiResponse> editPost(@RequestBody PostEditRequest request, HttpServletRequest http){
        ApiResponse apiResponse = postService.updatePost(request, http);
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 400).body(apiResponse);
    }
    @DeleteMapping("/delete/{id}")
    public HttpEntity<ApiResponse> deletePost(@PathVariable Long id, HttpServletRequest http){
        ApiResponse apiResponse = postService.deletePost(id, http);
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 400).body(apiResponse);
    }
    @GetMapping("/get/{id}")
    public HttpEntity<ApiResponse> getPost(@PathVariable Long id){
        ApiResponse apiResponse = postService.getPost(id);
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 400).body(apiResponse);
    }
    @GetMapping("")
    public HttpEntity<ApiResponse> getAllPosts(){
        ApiResponse apiResponse = postService.getAllPosts();
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 400).body(apiResponse);
    }
}
