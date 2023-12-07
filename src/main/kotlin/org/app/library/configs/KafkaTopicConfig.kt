package org.app.library.configs

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig {
    @Bean
    fun newTopic(): NewTopic {
        return TopicBuilder
            .name("testTopic")
            .build()
    }

    @Bean
    fun upsTrackingTopic(): NewTopic {
        return TopicBuilder
            .name("upsTrackingTopic")
            .build()
    }

    @Bean
    fun earthQuakeTopic(): NewTopic {
        return TopicBuilder
            .name("earthQuakeTopic")
            .build()
    }


    @Bean
    fun issTopic(): NewTopic {
        return TopicBuilder
            .name("issTopic") //json data
            .build()
    }
}