package ca.michalwozniak.jiraflow.service.Error;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Michal Wozniak on 28/8/2016.
 *
 * http://bytes.babbel.com/en/articles/2016-03-16-retrofit2-rxjava-error-handling.html
 * https://gist.github.com/koesie10/bc6c62520401cc7c858f#file-retrofitexception-java
 *
 * added extra  specific type
 */

// This is RetrofitError converted to Retrofit 2
public class RetrofitException extends RuntimeException {
    public static RetrofitException httpError(String url, Response response, Retrofit retrofit) {
        String message = response.code() + " " + response.message();
        if (message.contains("401")) {
            return new RetrofitException(message, url, response, Kind.HTTP, null, Type.UNAUTHORIZED, retrofit);
        } else if (message.contains("403")) {
            return new RetrofitException(message, url, response, Kind.HTTP, null, Type.FORBIDDEN, retrofit);
        } else if (message.contains("404")) {
            return new RetrofitException(message, url, response, Kind.HTTP, null, Type.NOT_FOUND, retrofit);
        } else
            return new RetrofitException(message, url, response, Kind.HTTP, null, Type.UNKNOWN, retrofit);
    }

    public static RetrofitException networkError(IOException exception) {

        if (exception instanceof UnknownHostException) {
            return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, Type.UNKNOWN_HOST, null);
        } else if (exception.getMessage().contains("No address")) {
            return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, Type.NO_ASSOCIATED_ADDRESS, null);
        } else if (exception.getMessage().contains("not verified")) {
            return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, Type.HOSTNAME_NOT_VERIFIED, null);
        } else if (exception.getMessage().contains("failed to connect") || exception.getMessage().contains("timed out")) {
            return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, Type.TIMEOUT, null);
        }

        return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, Type.UNKNOWN, null);
    }


    public static RetrofitException unexpectedError(Throwable exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.UNEXPECTED, exception, Type.UNKNOWN, null);
    }

    /**
     * Identifies the event kind which triggered a {@link RetrofitException}.
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    /**
     * add extra information to the type of event kind which triggered the error. if it was HTTP, what type?
     */
    public enum Type {

        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        HOSTNAME_NOT_VERIFIED,
        NO_ASSOCIATED_ADDRESS,
        TIMEOUT,
        UNKNOWN_HOST,
        /**
         * else
         */
        UNKNOWN

    }

    private final String url;
    private final Response response;
    private final Kind kind;
    private final Type type;
    private final Retrofit retrofit;

    RetrofitException(String message, String url, Response response, Kind kind, Throwable exception, Type type, Retrofit retrofit) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        this.type = type;
        this.retrofit = retrofit;
    }

    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * The event kind which triggered this error.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * specific information about the kind of error
     */
    public Type getType() {
        return type;
    }

    /**
     * The Retrofit this request was executed on
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);
        return converter.convert(response.errorBody());
    }
}