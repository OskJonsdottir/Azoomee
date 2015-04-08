package com.azoomee.rest;

import com.azoomee.http.RequestFactory;
import com.azoomee.transport.Credentials;
import com.azoomee.transport.Token;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest( rootUrl        = "http://private-anon-ac89d1b6f-androidtest5.apiary-mock.com",
       converters     = { MappingJackson2HttpMessageConverter.class },
       requestFactory = RequestFactory.class )
public interface LoginApi extends RestClientErrorHandling {
    @Post("/login")
    Token login(Credentials credentials);
}
