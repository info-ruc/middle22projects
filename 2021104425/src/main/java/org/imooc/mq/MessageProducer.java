package org.imooc.mq;

import com.alibaba.fastjson.JSON;
import org.imooc.dto.CommentForSubmitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MessageProducer {
    private final static Logger logger = LoggerFactory
            .getLogger(MessageProducer.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @param  exchange 交换机名称
     * @param  queueKey 队列KEY
     * @param   message 数据源
     * */
    public void sendMessage(String exchange,String queueKey,String message) {
        logger.info("to send message:{}", message);
        rabbitTemplate.convertAndSend("addExchange","addQueueKey", message);

    }
}
