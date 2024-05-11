package com.example.lostandfoundbackground.utils;

import com.aliyun.tea.TeaException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author archi
 */
@Slf4j
public class AliYunSmsUtil {
    private static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {

        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }


    /*
        发送短信的代码
        返回值表示发送成功与否
     */
    public static void sendSms(String phoneNumber, String code) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("CALLAF")
                .setTemplateCode("SMS_465900374")
                .setPhoneNumbers(phoneNumber)
                .setTemplateParam("{\"code\":\"%s\"}".formatted(code));
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            //发送验证码
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            // 打印出错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 打印出错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }

    public static Boolean sendSmsAndSave(String key,String phone,String smsCode){
        try {
            //AliYunSmsUtil.sendSms(phone,smsCode);
            log.info("验证码:"+smsCode+"\t发送到手机号:"+phone);
        }catch (Exception e){
            log.info(e.getMessage());
            return false;
        }
        //把验证码存放到redis里，失效时间设置为5min
        Map<Object,Object> smsMap = new HashMap<>();
        smsMap.put("code",smsCode);
        smsMap.put("verified","false");
        RedisUtils.hmset(key+phone,smsMap,5L);
        return true;
    }
}
