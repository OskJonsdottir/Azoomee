package com.azoomee.rest;

import com.azoomee.http.RequestFactory;
import com.azoomee.transport.Sibling;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest( rootUrl        = "http://private-anon-ac89d1b6f-androidtest5.apiary-mock.com",
       converters     = { MappingJackson2HttpMessageConverter.class },
       requestFactory = RequestFactory.class )
public interface ProfileApi extends RestClientErrorHandling {
    @Get("/user/profile?token={token}")
    List<Sibling> profile(String token);
}
