package uz.backecommers.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backecommers.core.entity.Post;

@Repository
public interface PostRepository  extends JpaRepository<Post,Long> {
}
