package edu.uclm.esi.tecsistweb.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;

public class HelperController {


    protected void manageCookies(HttpServletRequest request, HttpSession session) {

        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            if (request.getCookies() != null) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    String cookieValue = cookie.getValue();
                    if (cookieName.equals("id_user") && StringUtils.isNotBlank(cookieValue)) {
                        session.setAttribute("id_user", cookieValue);
                        break;
                    }
                }
            }
        }
    }
}