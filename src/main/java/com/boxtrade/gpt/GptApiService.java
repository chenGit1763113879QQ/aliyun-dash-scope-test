package com.boxtrade.gpt;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.boxtrade.gpt.constant.DashScope;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/4 16:39
 */
@Slf4j
public class GptApiService {

    public String query(String apiKey,String queryMessage) {
        Constants.apiKey = apiKey;
        try {
            Generation gen = new Generation();
            MessageManager msgManager = new MessageManager(10);
            Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content("你是一个金融Ai专家，熟悉各种市场的基本面，技术面，你也是一个多面手，可以胜任软件开发，企业服务等各方面的工作，所涉及的问题尽量重新从互联网上请求核对下！").build();
            Message userMsg = Message.builder().role(Role.USER.getValue()).content(queryMessage).build();
            msgManager.add(systemMsg);
            msgManager.add(userMsg);
            foreachGpts(gen, msgManager);
            String message = execModelMessage(msgManager, gen,Generation.Models.QWEN_PLUS);
            return message;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "金融Ai专家现在不在线，请稍后再试～";
        }
    }

    public void foreachGpts(Generation gen, MessageManager msgManager) throws NoApiKeyException, InputRequiredException {
        String[] models = DashScope.ALL_MODELS;
        for (int i = 0; i<models.length; i++) {
            String model = models[i];
            String message = execModelMessage(msgManager, gen,model);
            log.info("第【{}】个模型 {}- 的回答为 {}",i+1,model,message);
            log.info("========================================================");
        }
    }

    private String execModelMessage(MessageManager msgManager, Generation gen,String model)
    throws NoApiKeyException, InputRequiredException {
        String messageContent = null;
        try {
            QwenParam param = QwenParam.builder().model(model).messages(msgManager.get()).resultFormat(QwenParam.ResultFormat.MESSAGE).build();
            GenerationResult result = gen.call(param);
            GenerationOutput output = result.getOutput();
            Message message = output.getChoices().get(0).getMessage();
            log.info("模型 {}- 的回答为 {}",model,message);

            return message.getContent();
        } catch (ApiException e) {
            messageContent = e.getMessage();
            e.printStackTrace();
        } catch (NoApiKeyException e) {
            messageContent = e.getMessage();
            e.printStackTrace();
        } catch (InputRequiredException e) {
            messageContent = e.getMessage();
            e.printStackTrace();
        }
        return messageContent;
    }
}
