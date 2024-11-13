package by.vstu.auth.models.tokens;

import by.vstu.auth.models.UserModel;
import jakarta.persistence.*;
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
@Table(name = "user_tokens")
@Entity
public class UserTokenModel {

    @Id
    @Column(name = "jti", nullable = false, unique = true, updatable = false)
    private UUID jti;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "u_id", nullable = false)
    private UserModel user;

    @Column(nullable = false, unique = true, length = 2048)
    private String token;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime expiresAt;

    private boolean blocked;



}
