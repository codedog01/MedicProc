package com.yibai.medicproc.web.controller.dataease;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yibai.medicproc.common.core.domain.entity.SysUser;
import com.yibai.medicproc.common.core.domain.model.LoginUser;
import com.yibai.medicproc.common.utils.encrypt.EncryptUtils;
import com.yibai.medicproc.common.utils.encrypt.RsaUtil;
import com.yibai.medicproc.common.utils.http.HttpUtils;
import com.yibai.medicproc.framework.web.service.TokenService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/dataEase")
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
    @GetMapping("/login")
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
        String passwordEn = RsaUtil.encryptByPublicKey(publicKey, PASSWORD);
        //查询用户
        String userId = getUserIdInDataEase(loginUser.getUsername());
        //  没有则注册用户
        if (StringUtils.isEmpty(userId)) {
            SysUser user = loginUser.getUser();

            String body = "{\"id\":null," +
                    "\"username\":\"" + user.getUserName()+"\","+
                    "\"nickName\":\"" + user.getNickName()+"\","+
                    "\"gender\":\"男\"," +
                    "\"email\":\""  + user.getPhonenumber()+"\","+
                    "\"enabled\":1," +
                    "\"deptId\":\""+user.getDeptId() +"\","+
                    "\"phone\":\"" + user.getPhonenumber()+"\","+
                    "\"phonePrefix\":\"+86\"," +
                    "\"roleIds\":[2]," +
                    "\"sysUserAssist\":{\"wecomId\":null,\"dingtalkId\":null,\"larkId\":null}}";
            String result = HttpUtils.sendPost(dataeaseEndpoint + "/api/user/create", body, headerMap);
            userId = getUserIdInDataEase(loginUser.getUsername());
        }
        //  修改密码
        updatePassword(userId, PASSWORD);

        //  登录获取token
        String body = "{\n" +
                "  \"loginType\": 0,\n" +
                "  \"password\": \"" + passwordEn + "\",\n" +
                "  \"username\": \"" + usernameEn + "\"\n" +
                "}";
        String result = HttpUtils.sendPost(dataeaseEndpoint + "/api/auth/login", body, headerMap);
        return JSONObject.parseObject(result).getJSONObject("data").getString("token");
    }

    /**
     * 修改用户密码
     *
     * @param userId
     * @param password
     */
    private void updatePassword(String userId, String password) {
        /* 1.18.8 以后的版本需要使用 Base64 加密 password, */
        String body = "{\n" +
                "  \"newPassword\": \"" + Base64Util.encode(password)+ "\",\n" +
                "  \"userId\": " + userId + "\n" +
                "}";
        String result = HttpUtils.sendPost(dataeaseEndpoint + "/api/user/adminUpdatePwd", body, headerMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        Boolean success = jsonObject.getBoolean("success");
        Object data = jsonObject.get("data");


        if (data == null) {

        }

        if (BooleanUtils.isNotTrue(success)) {
            throw new RuntimeException("密码修改失败！");
        }
    }

//    regist

    /**
     * 根据用户名获取用户 ID
     *
     * @param username 用户名(唯一标识)
     * @return
     */
    private String getUserIdInDataEase(String username) {
        String body = "{\n" +
                "\"username\": \"" + username + "\"\n" +
                "}";
        String result = HttpUtils.sendPost(dataeaseEndpoint + "/api/user/userGrid/1/1", body,headerMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("listObject");
        if (jsonArray.isEmpty()) {
            return "";
        }
        return jsonArray.getJSONObject(0).getString("userId");
    }
}
