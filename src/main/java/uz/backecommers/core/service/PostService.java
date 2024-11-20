package uz.backecommers.core.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.backecommers.core.dto.PostAddRequest;
import uz.backecommers.core.dto.PostEditRequest;
import uz.backecommers.core.entity.Post;
import uz.backecommers.identety.dto.ApiResponse;

public interface PostService {
    ApiResponse createPost(PostAddRequest post, HttpServletRequest request);

    ApiResponse updatePost(PostEditRequest post, HttpServletRequest request);
    ApiResponse deletePost(Long id, HttpServletRequest request);

    ApiResponse getPost(Long id);
    ApiResponse getAllPosts();
}
