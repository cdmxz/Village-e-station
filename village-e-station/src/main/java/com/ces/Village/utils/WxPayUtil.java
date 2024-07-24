package com.ces.Village.utils;


import com.alibaba.fastjson2.JSONObject;
import com.ces.Village.properties.WxApiProperties;
import com.ces.Village.properties.WxPayProperties;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * 微信支付工具类
 */
@Log4j2
@Component
public class WxPayUtil {

    private final WxPayProperties wxPayProperties;
    private final JsapiService service;
    private final RSAAutoCertificateConfig config;

    @Autowired
    public WxPayUtil(WxPayProperties wxPayProperties) {
        this.wxPayProperties = wxPayProperties;
        config = createConfig();
        service = new JsapiService.Builder().config(config).build();
    }


    private RSAAutoCertificateConfig createConfig() {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        String result = "";
        try {
            InputStream is = new ClassPathResource(wxPayProperties.getPrivateKeyFilePath()).getInputStream();
            result = new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            log.error("加载证书私钥（.pem）失败");
        }
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayProperties.getMchid())
                .privateKey(result)
                .merchantSerialNumber(wxPayProperties.getMchSerialNo())
                .apiV3Key(wxPayProperties.getApiV3Key())
                .build();
    }

    /**
     * jsapi生成预付订单
     *
     * @param outTradeNo  商户订单号
     * @param total       总金额
     * @param description 商品描述
     * @param openid      微信用户的openid
     * @return
     */
    public String jsapiPrepay(String outTradeNo, BigDecimal total, String description, String openid) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(total.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue());
        request.setAmount(amount);
        request.setAppid(wxPayProperties.getKeys().getAppid());
        request.setMchid(wxPayProperties.getMchid());
        request.setDescription(description);
        request.setNotifyUrl(wxPayProperties.getNotifyUrl());
        request.setOutTradeNo(outTradeNo);
        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        return response.getPrepayId();
    }

    /**
     * 小程序支付
     *
     * @param orderNum    商户订单号
     * @param total       金额，单位 元
     * @param description 商品描述
     * @param openid      微信用户的openid
     * @return
     */
    public JSONObject miniProgramPay(String orderNum, BigDecimal total, String description, String openid) throws FileNotFoundException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        try {
            // 生成预支付交易单
            String prepayId = jsapiPrepay(orderNum, total, description, openid);
            // 签名，构造数据给小程序端
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonceStr = RandomStringUtils.randomNumeric(32);
            ArrayList<Object> list = new ArrayList<>();
            list.add(wxPayProperties.getKeys().getAppid());
            list.add(timeStamp);
            list.add(nonceStr);
            list.add("prepay_id=" + prepayId);
            StringBuilder stringBuilder = new StringBuilder();
            for (Object o : list) {
                stringBuilder.append(o).append("\n");
            }
            String signMessage = stringBuilder.toString();
            byte[] message = signMessage.getBytes();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(PemUtil.loadPrivateKey(new FileInputStream(wxPayProperties.getPrivateKeyFilePath())));
            signature.update(message);
            String packageSign = Base64.getEncoder().encodeToString(signature.sign());

            // 构造数据给微信小程序，用于在小程序端调起微信支付
            JSONObject jo = new JSONObject();
            jo.put("timeStamp", timeStamp);
            jo.put("nonceStr", nonceStr);
            jo.put("package", "prepay_id=" + prepayId);
            jo.put("signType", "RSA");
            jo.put("paySign", packageSign);
            return jo;
        } catch (Exception e) {
            log.error("小程序API下单失败，原因：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 申请退款
     *
     * @param outTradeNo  商户订单号
     * @param outRefundNo 商户退款单号
     * @param refund      退款金额
     * @param total       原订单金额
     * @return
     */
    public Refund refund(String outTradeNo, String outRefundNo, BigDecimal refund, BigDecimal total) {
        Config config = createConfig();
        // 初始化服务
        RefundService service = new RefundService.Builder().config(config).build();
        CreateRequest request = new CreateRequest();

        // 退款金额
        com.wechat.pay.java.service.refund.model.AmountReq amountReq = new com.wechat.pay.java.service.refund.model.AmountReq();
        amountReq.setRefund(refund.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).longValue());
        request.setAmount(amountReq);
        amountReq.setTotal(total.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).longValue());
        amountReq.setCurrency("CNY");

        request.setAmount(amountReq);
        request.setOutTradeNo(outTradeNo);
        request.setOutRefundNo(outRefundNo);

        // 设置退款回调
        //        request.setNotifyUrl(wxProperties.getRefundNotifyUrl());
        // 调用接口
        Refund resp = service.create(request);
        //调用申请退款接口
        return resp;
    }


    /**
     * 关闭订单
     *
     * @param outTradeNo 商户订单号
     * @return
     */
    public void CloseOrder(String outTradeNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(wxPayProperties.getMchid());
        request.setOutTradeNo(outTradeNo);
        service.closeOrder(request);
    }

    /**
     * 微信支付订单号查询订单
     */
    public Transaction queryOrderById(String id) {
        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        request.setTransactionId(id);
        request.setMchid(wxPayProperties.getMchid());
        Transaction transaction = service.queryOrderById(request);
        return transaction;
    }

    /**
     * 商户订单号查询订单
     */
    public Transaction queryOrderByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(outTradeNo);
        request.setMchid(wxPayProperties.getMchid());
        Transaction transaction = service.queryOrderByOutTradeNo(request);
        return transaction;
    }

    /**
     * 解析微信支付回调数据
     *
     * @return
     */
    public Transaction parseNotify(HttpServletRequest request) {
        String wechatPaySerial = request.getHeader("Wechatpay-Serial");
        String wechatpayNonce = request.getHeader("Wechatpay-Nonce");
        String wechatSignature = request.getHeader("Wechatpay-Signature");
        String wechatTimestamp = request.getHeader("Wechatpay-Timestamp");
        String requestBody = getPostData(request);
        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(wechatPaySerial)
                .nonce(wechatpayNonce)
                .signature(wechatSignature)
                .timestamp(wechatTimestamp)
                .body(requestBody)
                .build();
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(config);
        Transaction transaction;
        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
            transaction = parser.parse(requestParam, Transaction.class);
        } catch (com.wechat.pay.java.core.exception.ValidationException e) {
            log.error("解析微信api回调数据失败，原因：{}", e.getMessage());
            throw e;
        }
        return transaction;
    }

    /**
     * 解析微信支付 退款 回调数据
     *
     * @return
     */
    public RefundNotification parseRefundNotify(HttpServletRequest request) {
        String wechatPaySerial = request.getHeader("Wechatpay-Serial");
        String wechatpayNonce = request.getHeader("Wechatpay-Nonce");
        String wechatSignature = request.getHeader("Wechatpay-Signature");
        String wechatTimestamp = request.getHeader("Wechatpay-Timestamp");
        String requestBody = getPostData(request);
        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(wechatPaySerial)
                .nonce(wechatpayNonce)
                .signature(wechatSignature)
                .timestamp(wechatTimestamp)
                .body(requestBody)
                .build();
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(config);
        RefundNotification notification;
        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
            notification = parser.parse(requestParam, RefundNotification.class);
        } catch (com.wechat.pay.java.core.exception.ValidationException e) {
            log.error("解析微信api 退款回调数据失败，原因：{}", e.getMessage());
            throw e;
        }
        return notification;
    }

    /**
     * 获取post请求的body数据
     *
     * @param request
     * @return
     */
    private static String getPostData(HttpServletRequest request) {
        StringBuilder data = new StringBuilder();
        String line;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine())) {
                data.append(line);
            }
        } catch (IOException e) {
            return null;
        }
        return data.toString();
    }
}
