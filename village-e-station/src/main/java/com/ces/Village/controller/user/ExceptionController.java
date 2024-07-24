//package com.ces.Village.controller.user;
//
//import com.ces.Village.common.R;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.error.ErrorAttributeOptions;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.context.request.ServletWebRequest;
//import org.springframework.web.context.request.WebRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@Log4j2
//@Controller
//public class ExceptionController implements ErrorController {
//    @Autowired
//    private ErrorAttributes errorAttributes;
//    //默认请求地址
//    private static final  String ERROR_PATH = "/error";
//
//
//    /**
//     * web页面异常请求处理
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = ERROR_PATH, produces = MediaType.TEXT_PLAIN_VALUE)
//    public String handlePageError(HttpServletRequest request) {
//        log.info("进入异常跳转");
//        Integer statusCode = getStatus(request);
//        return switch (statusCode) {
//            case 404 -> {
//                log.info("404异常跳转");
//                yield "error/404";
//            }
//            case 403 -> {
//                log.info("403异常跳转");
//                yield "error/403";
//            }
//            case 500 -> {
//                log.info("500异常跳转");
//                yield "error/500";
//            }
//            default -> {
//                log.info("默认异常跳转");
//                yield "error/404";
//            }
//        };
//
//    }
//
////    /**
////     * 其他异常请求处理，例如：contentType：application/json等
////     * @param request
////     * @return
////     */
////    @RequestMapping(ERROR_PATH)
////    @ResponseBody
////    public R<?> handleAllError(HttpServletRequest request) {
////        WebRequest webRequest = new ServletWebRequest(request);
////        Integer statusCode = getStatus(request);
////        ErrorAttributeOptions options = ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.MESSAGE);
////        Map<String, Object> body = this.errorAttributes.getErrorAttributes(webRequest, options);
////        body.put("status", statusCode);
////        return R.success(body);
////    }
//
//    /**
//     * 获取状态码
//     * @param request
//     * @return
//     */
//    public Integer getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return 500;
//        }
//        return  statusCode;
//    }
//
//    /**
//     * Spring 默认错误页路径
//     * @return
//     */
//    public String getErrorPath() {
//        return ERROR_PATH;
//    }
//}
