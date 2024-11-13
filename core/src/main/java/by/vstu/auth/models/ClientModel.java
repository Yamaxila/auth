package by.vstu.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
@Entity
public class ClientModel {

    /*
    *  Честно, я это взял из старой авторизации...
    *\ */

    //Id клиента
    @Id
    @Column(name = "client_id", unique = true, nullable = false, length = 50)
    private String clientId;

    //Секрет(пароль) клиента
    @Column(name = "client_secret", nullable = false, length = 60)
    private String clientSecret;

    //Сервисы, к которым имеет доступ клиент
    @Column(name = "resource_ids", nullable = false)
    private String resourceIds;

    //Разрешения клиента
    @Column(name = "scope", nullable = false)
    private String scope;

    //Права доступа
    @Column(name = "authorities")
    private String authorities;

    //Срок действия токена
    @Column(name = "access_token_expiration", nullable = false)
    private Integer accessTokenExpiration;

    //Дополнительная информация
    @Column(name = "additional_information")
    private String additionalInformation;

    //Ни один сервис не умеет посылать запрос на обновление токена на текущий момент.
    //Когда появится, тогда и добавится время жизни refresh-токена.


    public List<String> getScope() {
        List<String> result = new ArrayList<>();
        if (StringUtils.hasText(this.scope)) {
            result = Arrays.stream(this.scope.split(",")).toList();
        }
        return result;
    }

    public List<String> getResourceIds() {
        List<String> result = new ArrayList<>();
        if (StringUtils.hasText(this.resourceIds)) {
            result = Arrays.stream(this.resourceIds.split(",")).toList();
        }
        return result;
    }

    public List<String> getAuthorities() {
        List<String> result = new ArrayList<>();
        if (StringUtils.hasText(this.authorities)) {
            result = Arrays.stream(this.authorities.split(",")).toList();
        }
        return result;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ClientModel that = (ClientModel) o;
        return getClientId() != null && Objects.equals(getClientId(), that.getClientId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
