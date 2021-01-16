package com.sample.ec.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/index").setViewName("index");
		registry.addViewController("/setting").setViewName("setting");
		registry.addViewController("/product").setViewName("product");
		registry.addViewController("/index.html").setViewName("index");
		registry.addViewController("/add-flist").setViewName("add-flist");
		registry.addViewController("/add-cart").setViewName("add-cart");
		registry.addViewController("/see-cart").setViewName("see-cart");
		registry.addViewController("/re-register").setViewName("re-register");
		registry.addViewController("/conversion").setViewName("conversion");
		registry.addViewController("/registration").setViewName("registration");
		registry.addViewController("/delete-item").setViewName("delete-item");
		registry.addViewController("/see-favorites").setViewName("see-favorites");
		registry.addViewController("/purchase-now").setViewName("purchase-now");
		registry.addViewController("/purchase-items").setViewName("purchase-items");
		registry.addViewController("/delete-cart-item").setViewName("delete-cart-item");
	}

}