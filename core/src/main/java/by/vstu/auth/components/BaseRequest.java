package by.vstu.auth.components;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * Базовый класс для выполнения HTTP-запросов.
 * <a href=https://github.com/yamaxila/Dean-backend/>original</a>
 */
public class BaseRequest<B> {

    @Getter
    private String url;

    private final HttpHeaders headers;
    private HttpMethod method = HttpMethod.POST;

    @Getter
    private HttpStatusCode responseStatusCode;

    @Setter
    @Getter
    private boolean bodyPathVariable = false;

    /**
     * Конструктор класса BaseRequest.
     *
     * @param url URL для выполнения запроса.
     */
    public BaseRequest(String url) {
        this.url = url;
        this.headers = new HttpHeaders();
    }

    /**
     * Выполнить HTTP-запрос.
     *
     * @param entity Сущность, отправляемая в запросе.
     * @return Результат запроса в виде строки.
     */
    public String run(B entity) {

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<B> request =
                new HttpEntity<>(entity, headers);

        ResponseEntity<String> response = restTemplate.exchange(String.format(this.url, entity), this.method, request, String.class);

        this.responseStatusCode = response.getStatusCode();

        if (response.getStatusCode().value() != 200) {
            return null;
        }


        return response.getBody();
    }

    /**
     * Добавить HTTP-заголовок.
     *
     * @param key   Ключ заголовка.
     * @param value Значение заголовка.
     * @return Объект BaseRequest с добавленным заголовком.
     */
    @SuppressWarnings({"unused"})
    public BaseRequest<B> addHeader(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    /**
     * Установить URL.
     *
     * @param url Новый URL.
     * @return Объект BaseRequest с установленным URL.
     */
    public BaseRequest<B> setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Установить аутентификационные заголовки Basic.
     *
     * @param clientId     Id клиента
     * @param clientSecret Секретный ключ клиента
     * @return Объект BaseRequest с установленными аутентификационными заголовками Basic.
     */
    public BaseRequest<B> setAuthHeaders(String clientId, String clientSecret) {

        byte[] encodedAuth = Base64.encodeBase64(
                (clientId + ":" + clientSecret).getBytes(StandardCharsets.US_ASCII));

        this.headers.set(HttpHeaders.AUTHORIZATION, "Basic " + new String(encodedAuth));
        return this;
    }

    /**
     * Установить токен авторизации.
     *
     * @param token Токен авторизации.
     * @return Объект BaseRequest с установленным токеном авторизации.
     */
    public BaseRequest<B> setToken(String token) {
        this.headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return this;
    }

    /**
     * Установить метод HTTP-запроса.
     *
     * @param method Метод HTTP-запроса.
     * @return Объект BaseRequest с установленным методом запроса.
     */
    public BaseRequest<B> setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    /**
     * Установить тип медиа в заголовке.
     *
     * @param type Тип медиа.
     * @return Объект BaseRequest с установленным типом медиа в заголовке.
     */
    public BaseRequest<B> setMediaType(MediaType type) {
        this.headers.setContentType(type);
        return this;
    }
}