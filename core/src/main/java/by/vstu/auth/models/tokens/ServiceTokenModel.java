package by.vstu.auth.models.tokens;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_tokens")
@Entity
public class ServiceTokenModel {

    @Id
    @Column(name = "jti", nullable = false, unique = true, updatable = false)
    private UUID jti;

    @Column(name = "resource_ids", nullable = false)
    private String resourceIds;

    @Column(nullable = false, unique = true, length = 2048)
    private String token;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    private boolean blocked = false;



}
