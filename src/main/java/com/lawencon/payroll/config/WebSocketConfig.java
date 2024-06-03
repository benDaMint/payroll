// package com.lawencon.payroll.config;

// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// // import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// // import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// // import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// // @Configuration
// // @EnableWebSocketMessageBroker
// // public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

// //     @Override
// //     public void configureMessageBroker(MessageBrokerRegistry config) {
// //         config.enableSimpleBroker("/topic");
// //         config.setApplicationDestinationPrefixes("/app");
// //     }

// //     @Override
// //     public void registerStompEndpoints(StompEndpointRegistry registry) {
// //         // registry.addEndpoint("/chat").setAllowedOriginPatterns("*");
// //         registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
// //         // registry.addEndpoint("/chat-websocket").setAllowedOrigins("*").withSockJS();
// //         // registry.addEndpoint("/chat")
// //         //         .setAllowedOrigins("http://localhost:4200")
// //         //         .withSockJS();

// //     }
// // }

// import org.springframework.context.annotation.Configuration;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// @Configuration
// @EnableWebSocketMessageBroker
// @CrossOrigin("*")
// public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//     @Override
//     public void configureMessageBroker(MessageBrokerRegistry config) {
//         config.enableSimpleBroker("/send");
//         config.setApplicationDestinationPrefixes("/");
//     }

//     @Override
//     public void registerStompEndpoints(StompEndpointRegistry registry) {
//         registry.addEndpoint("/livechat").setAllowedOriginPatterns("http://localhost:4200", "*");
//         registry.addEndpoint("/livechat").setAllowedOriginPatterns("http://localhost:4200", "*").withSockJS();
//         // registry.addEndpoint("/chat/{clientId}").setAllowedOrigins("*").withSockJS();

//     }

// }

package com.lawencon.payroll.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin("*")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/send");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/livechat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
