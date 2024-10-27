package com.example.userimage;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointsListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		handlerMethods.forEach((key, value) -> {
			System.out.println(key.getMethodsCondition().toString() + ' ' + key.getDirectPaths().toString());
		});
	}
}
