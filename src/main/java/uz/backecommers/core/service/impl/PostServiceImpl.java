package uz.backecommers.core.service.impl;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backecommers.Base.BaseService;
import uz.backecommers.core.dto.PostAddRequest;
import uz.backecommers.core.dto.PostEditRequest;
import uz.backecommers.core.entity.Post;
import uz.backecommers.core.repository.PostRepository;
import uz.backecommers.core.service.PostService;
import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.identety.entity.Users;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BaseService baseService;
    @Override
    public ApiResponse createPost(PostAddRequest post, HttpServletRequest request) {
        Users currentUser = baseService.getCurrentUser();
        Post newPost = new Post();
        newPost.setUser(currentUser);
        newPost.setTitle(post.getTitle());
        newPost.setDescription(post.getDescription());
        Post save = postRepository.save(newPost);
        return new ApiResponse(true, "Post created successfully", save);
    }

    @Override
    public ApiResponse updatePost(PostEditRequest post, HttpServletRequest request) {

        Optional<Post> existingPostOpt = postRepository.findById(post.getId());
        if (existingPostOpt.isEmpty()) {
            return new ApiResponse(false, "Post not found", null);
        }

        Post existingPost = existingPostOpt.get();
        Users currentUser = baseService.getCurrentUser();

        if (!existingPost.getUser().equals(currentUser)) {
            return new ApiResponse(false, "You can only edit your own posts", null);
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setDescription(post.getDescription());
        postRepository.save(existingPost);

        return new ApiResponse(true, "Post updated successfully", existingPost);

    }

    @Override
    public ApiResponse deletePost(Long id, HttpServletRequest request) {
        Optional<Post> existingPostOpt = postRepository.findById(id);
        if (existingPostOpt.isEmpty()) {
            return new ApiResponse(false, "Post not found", null);
        }

        Post existingPost = existingPostOpt.get();
        Users currentUser = baseService.getCurrentUser();

        if (!existingPost.getUser().equals(currentUser)) {
            return new ApiResponse(false, "You can only delete your own posts", null);
        }

        postRepository.delete(existingPost);

        return new ApiResponse(true, "Post deleted successfully", null);
    }

    @Override
    public ApiResponse getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return new ApiResponse(false, "Post not found", null);
        }

        return new ApiResponse(true, "Post found", post.get());
    }

    @Override
    public ApiResponse getAllPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            return new ApiResponse(false, "No posts found", null);
        }
        return new ApiResponse(true, "Posts retrieved successfully", posts);
    }
}
