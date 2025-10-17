package com.example.Jobhunter.util;

import org.springframework.boot.autoconfigure.jersey.JerseyProperties.Servlet;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.Jobhunter.domain.RestResponse;
import com.example.Jobhunter.util.annotation.ApiMessage;

import jakarta.servlet.http.HttpServletResponse;

// @RestControllerAdvice
// Nó giống như @ControllerAdvice, nhưng dành riêng cho REST API.
// Có thể can thiệp vào response hoặc exception để format lại dữ liệu.

// FormatRestResponse
// Là interceptor cho mọi response từ controller.
// Nếu success (200-399) → bọc response trong RestResponse.
// Nếu error (>=400) → không bọc nữa, để GlobalException xử lý
@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedConvertype,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(status);

        // RestResponse = cái “khuôn” chuẩn của API response.
        // Đoạn if (body instanceof RestResponse) = check xem dữ liệu đã ở trong cái
        // khuôn đó chưa, nếu rồi thì để nguyên, chưa thì wrap lại.
        if (body instanceof RestResponse) {
            // Case đã được format rồi
            return body;
        }

        if (status >= 400) {
            // Case error
            return body;
        } else {
            // Case success
            res.setData(body);
            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
            if (apiMessage != null) {
                res.setMessage(apiMessage.value());
            } else {
                res.setMessage("Call API success");
            }
        }

        return res;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

}
