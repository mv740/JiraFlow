package ca.michalwozniak.jiraflow.service;

import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ca.michalwozniak.jiraflow.service.Error.RxErrorHandlingCallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Michal Wozniak on 4/10/2016.
 */
public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = getHttpClient();

    private static OkHttpClient.Builder getHttpClient() {
        //created this function if we later want to add some custom options
        // specific timeout ...
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return client;
    }

    private static Request getAuthenticationHeader(String username, String password, Request original) {
        String credentials = username + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", basic)
                .header("Accept", "application/json")
                .method(original.method(), original.body());
        return requestBuilder.build();
    }

    private static Retrofit.Builder getBuilder(String jiraBaseUrl) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        return new Retrofit.Builder()
                .baseUrl(jiraBaseUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());
    }

    private static Retrofit.Builder getBuilderXML(String jiraBaseUrl) {
        return new Retrofit.Builder()
                .baseUrl(jiraBaseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());
    }

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null, null);
    }

    public static <S> S createService(final Class<S> serviceClass, final String username, final String password, String url) {
        if (username != null && password != null) {
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Log.e("urlLog", original.url().toString());
                return chain.proceed(getAuthenticationHeader(username, password, original));
            });

        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getBuilder(url).client(client).build();

        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceXML(Class<S> serviceClass, final String username, final String password, String url) {
        if (username != null && password != null) {

            httpClient.addInterceptor(chain -> chain.proceed(getAuthenticationHeader(username, password, chain.request())));
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getBuilderXML(url).client(client).build();
        return retrofit.create(serviceClass);
    }

}
