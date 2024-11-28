package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
}
