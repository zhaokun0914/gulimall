package com.atguigu.gulimall.thirdparty.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OssController {

    @Autowired
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKey;

    /**
     * 服务端签名后直传：浏览器拿到 policy 后直接 POST 到 OSS，文件不经过本服务。
     */
    @GetMapping("/oss/policy")
    public Map<String, String> policy() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = date + "/";
        String host = "https://" + bucket + "." + endpoint;
        long expireEndTime = System.currentTimeMillis() + 30 * 60 * 1000;
        Date expiration = new Date(expireEndTime);

        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
        String encodedPolicy = BinaryUtil.toBase64String(postPolicy.getBytes(StandardCharsets.UTF_8));
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> result = new LinkedHashMap<>();
        result.put("accessId", accessKey);
        result.put("policy", encodedPolicy);
        result.put("signature", postSignature);
        result.put("dir", dir);
        result.put("host", host);
        result.put("expire", String.valueOf(expireEndTime / 1000));
        return result;
    }

}
