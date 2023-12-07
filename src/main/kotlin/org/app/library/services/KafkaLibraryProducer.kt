package org.app.library.services


import org.app.common.dto.IssueBookRequest
import org.app.common.dto.StudentCommonDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class KafkaLibraryProducer(
    val kafkaTemplate: KafkaTemplate<String, String>,
) {
    fun sendMessage(studentDto: StudentCommonDto) {
        val message: Message<StudentCommonDto> =

            MessageBuilder
                .withPayload(studentDto)
                .setHeader(KafkaHeaders.TOPIC, "testTopic")
                .build()
        kafkaTemplate.send(message)
    }


    fun getStudentDetails(
        issueBookRequest: IssueBookRequest,
        topic:String)
    {
        val message: Message<IssueBookRequest> =
            MessageBuilder
                .withPayload(issueBookRequest)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build()
        kafkaTemplate.send(message)
    }

}