package com.yibai.medicproc.web.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yibai.medicproc.common.core.domain.model.LoginUser;
import com.yibai.medicproc.common.utils.encrypt.EncryptUtils;
import com.yibai.medicproc.common.utils.encrypt.RsaUtil;
import com.yibai.medicproc.common.utils.http.HttpUtils;
import com.yibai.medicproc.framework.web.service.TokenService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/dataEaseSSO")
public class DataEaseLoginController {

    @Autowired
    private TokenService tokenService;

    // 根域名，跨域场景无需配置此项
    private static String domain = "localhost";
    // DataEase 访问地址，如 http://edu.fit2cloud.com，跨域场景下此处填写 Nginx 地址
    private static String dataeaseEndpoint = "http://localhost:8081";
    // 取自 DataEase 源码 application.yml 中的 rsa.public_key
    private static String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANL378k3RiZHWx5AfJqdH9xRNBmD9wGD2iRe41HdTNF8RUhNnHit5NpMNtGL0NPTSSpPjjI1kJfVorRvaQerUgkCAwEAAQ==";
    private static String accessKey = "1234567890123456";
    private static String secretKey = "1234567890123456";
    private static String signature;
    // 模拟登陆使用的密码，建议实际使用时生成随机密码
    private static String PASSWORD = "Root123.";

    private static HashMap<String, String> headerMap= new HashMap<>();


    private static void initSignature() {
        signature = EncryptUtils.aesEncrypt(accessKey + "|" + UUID.randomUUID() + "|" + System.currentTimeMillis(), secretKey, accessKey);
        headerMap.put("accessKey", accessKey);
        headerMap.put("signature", signature);
    }

    // 同域跳转，适用于同一根域名不同系统之间跳转
    @GetMapping("/ssoCall")
    public void toDataEase(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(request);
        Cookie cookie = new Cookie("Authorization", getToken( loginUser));
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    /**
     * 登出时需要清除 cookie，否则会出现一直登录中问题
     */
    @RequestMapping("/logout")
    public void logoutDataEase(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("Authorization", "");
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    /**
     * 模拟登录并获取 Token
     *
     * @return
     * @throws Exception
     */
    private String getToken(LoginUser loginUser) throws Exception {

        initSignature();
        String usernameEn = RsaUtil.encryptByPublicKey(publicKey, loginUser.getUsername());
        String password = RsaUtil.encryptByPublicKey(publicKey, PASSWORD);
        updatePassword(loginUser.getUsername(), PASSWORD);

        String body = "{\n" +
                "  \"loginType\": 0,\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"username\": \"" + usernameEn + "\"\n" +
                "}";
        String result = HttpUtils.sendPost(dataeaseEndpoint + "/api/auth/login", body, headerMap);
        // todo 如果没有用户则注册
        return JSONObject.parseObject(result).getJSONObject("data").getString("token");
    }

    /**
     * 修改用户密码
     *
     * @param username
     * @param password
     */
    private void updatePassword(String username, String password) {
        String userId = getUserId(username);
        String body = "{\n" +
                "  \"newPassword\": \"" + Base64Util.encode(password)/* 1.18.8 以后的版本需要使用 Base64 加密 password, 小于等于 1.18.7 的版本请移除 Base64 加密方法 */ + "\",\n" +
                "  \"userId\": " + userId + "\n" +
                "}";
        String result = HttpUtils.sendPost(dataeaseEndpoint + "/api/user/adminUpdatePwd", body, headerMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        Boolean success = jsonObject.getBoolean("success");
        if (BooleanUtils.isNotTrue(success)) {
            throw new RuntimeException("密码修改失败！");
        }
    }


    /**
     * 根据用户名获取用户 ID
     *
     * @param username 用户名(唯一标识)
     * @return
     */
    private String getUserId(String username) {
        String body = "{\n" +
                "\"keyword\": \"" + username + "\"\n" +
                "}";
        String result = HttpUtils.sendPost(dataeaseEndpoint + "/api/user/userGrid/1/1", body,headerMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getJSONObject("data").getJSONArray("listObject").getJSONObject(0).getString("userId");
    }
}
