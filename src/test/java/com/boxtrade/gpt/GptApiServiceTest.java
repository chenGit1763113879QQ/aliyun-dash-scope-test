package com.boxtrade.gpt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.boxtrade.gpt.constant.DashScope;

/**
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/4 16:41
 */
public class GptApiServiceTest {

    @Test
    public void test(){
        GptApiService gptApiService = new GptApiService();
        String queryMessage = "分析一下宁波银行的股票近期的走势？";
        String result = gptApiService.query(DashScope.API_KEY,queryMessage);
        System.out.println(result);
    }
}
