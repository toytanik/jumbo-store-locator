package com.tuncayturk.storelocator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;

@ConfigurationProperties("store-locator")
public record StoreLocatorProperties(@DefaultValue("classpath:stores.json") Resource dataLocation) {
}