package org.imooc.mq;

import com.alibaba.fastjson.JSON;
import org.imooc.dto.CommentForSubmitDto;
import org.imooc.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@Component
public class MessageConsumer implements MessageListener {

    private Logger logger= LoggerFactory.getLogger(MessageProducer.class);
    @Resource
    private CommentService commentService;

    @Override
    public void onMessage(Message message) {
        logger.info("receive message:{}", message);
        String data = null;
        try {
            data = new String(message.getBody(), "UTF-8");
            CommentForSubmitDto commentForSubmitDto = JSON.parseObject(data, CommentForSubmitDto.class);
            commentService.add(commentForSubmitDto);
        } catch (UnsupportedEncodingException e) {
            logger.error("接受失败");
        }
        logger.info("receive data :{}", data);
    }
}
