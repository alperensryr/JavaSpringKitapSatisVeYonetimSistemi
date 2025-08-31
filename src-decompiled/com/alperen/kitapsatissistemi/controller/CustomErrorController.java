/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.CustomErrorController
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.boot.web.servlet.error.ErrorAttributes
 *  org.springframework.boot.web.servlet.error.ErrorController
 *  org.springframework.http.HttpStatus
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 */
package com.alperen.kitapsatissistemi.controller;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController
implements ErrorController {
    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(value={"/error"})
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("timestamp", (Object)LocalDateTime.now());
            model.addAttribute("status", (Object)statusCode);
            model.addAttribute("error", (Object)HttpStatus.valueOf((int)statusCode).getReasonPhrase());
            model.addAttribute("message", (Object)"Bir hata olu\u015ftu");
            model.addAttribute("path", request.getAttribute("javax.servlet.error.request_uri"));
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            }
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            }
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            }
            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "error/401";
            }
            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error/400";
            }
            if (statusCode == 429) {
                return "error/429";
            }
        }
        return "error/500";
    }

    @GetMapping(value={"/error/400"})
    public String badRequest(Model model) {
        model.addAttribute("timestamp", (Object)LocalDateTime.now());
        model.addAttribute("status", (Object)400);
        model.addAttribute("error", (Object)"Bad Request");
        model.addAttribute("message", (Object)"Ge\u00e7ersiz istek");
        return "error/400";
    }

    @GetMapping(value={"/error/401"})
    public String unauthorized(Model model) {
        model.addAttribute("timestamp", (Object)LocalDateTime.now());
        model.addAttribute("status", (Object)401);
        model.addAttribute("error", (Object)"Unauthorized");
        model.addAttribute("message", (Object)"Giri\u015f yapman\u0131z gerekiyor");
        return "error/401";
    }

    @GetMapping(value={"/error/403"})
    public String forbidden(Model model) {
        model.addAttribute("timestamp", (Object)LocalDateTime.now());
        model.addAttribute("status", (Object)403);
        model.addAttribute("error", (Object)"Forbidden");
        model.addAttribute("message", (Object)"Bu sayfaya eri\u015fim yetkiniz yok");
        return "error/403";
    }

    @GetMapping(value={"/error/404"})
    public String notFound(Model model) {
        model.addAttribute("timestamp", (Object)LocalDateTime.now());
        model.addAttribute("status", (Object)404);
        model.addAttribute("error", (Object)"Not Found");
        model.addAttribute("message", (Object)"Arad\u0131\u011f\u0131n\u0131z sayfa bulunamad\u0131");
        return "error/404";
    }

    @GetMapping(value={"/error/429"})
    public String tooManyRequests(Model model) {
        model.addAttribute("timestamp", (Object)LocalDateTime.now());
        model.addAttribute("status", (Object)429);
        model.addAttribute("error", (Object)"Too Many Requests");
        model.addAttribute("message", (Object)"\u00c7ok fazla istek g\u00f6nderdiniz. L\u00fctfen daha sonra tekrar deneyin.");
        return "error/429";
    }

    @GetMapping(value={"/error/500"})
    public String serverError(Model model) {
        model.addAttribute("timestamp", (Object)LocalDateTime.now());
        model.addAttribute("status", (Object)500);
        model.addAttribute("error", (Object)"Internal Server Error");
        model.addAttribute("message", (Object)"Sunucu hatas\u0131 olu\u015ftu");
        return "error/500";
    }
}

