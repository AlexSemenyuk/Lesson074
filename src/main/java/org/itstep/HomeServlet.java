package org.itstep;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/", name = "home")
public class HomeServlet extends HttpServlet {
    public static String TEMPLATE;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 1. Создаем TEMPLATE
        ServletContext servletContext = config.getServletContext();
        try (InputStream in = servletContext.getResourceAsStream("/WEB-INF/template/home.html");
             BufferedReader rdr = new BufferedReader(new InputStreamReader(in))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = rdr.readLine()) != null) {
                stringBuilder.append(line);
            }
            TEMPLATE = stringBuilder.toString();
//            System.out.println(TEMPLATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Optional<Cookie[]> optionalCookies = Optional.ofNullable(req.getCookies());
        String cookieList = "";
        String [] theme = new String[1];
        theme[0] = "light";
        String [] backgroundColorMain = new String[1];
        backgroundColorMain[0] = "#fff";
        int [] font = new int[1];
        font[0] = 19;
        if(optionalCookies.isPresent()) {
            Arrays.stream(optionalCookies.get())
                    .forEach(cookie -> {
                        System.out.println("cookie.getName() = " + cookie.getName());
                        switch (cookie.getName()){
                            case "theme" -> theme[0] = cookie.getValue();
                            case "background" -> backgroundColorMain[0] = cookie.getValue();
                            case "font" -> font[0] = Integer.parseInt(cookie.getValue());
                            default -> System.out.println("Mistake");
                        }
                    });
        }

        PrintWriter writer = resp.getWriter();
        writer.printf(TEMPLATE, font[0], backgroundColorMain[0], theme[0], theme[0]);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cookieThemeValue = req.getParameter("theme");
        System.out.println("cookieThemeValue = " + cookieThemeValue);
        if (cookieThemeValue != null) {
            String cookieName = "theme";
            Cookie cookieTheme = new Cookie(cookieName, cookieThemeValue);
            cookieTheme.setMaxAge(100_000);
            resp.addCookie(cookieTheme);
        }

        String cookieColorValue = req.getParameter("color");
        System.out.println("cookieColorValue = " + cookieColorValue);
        if (cookieColorValue != null && !cookieColorValue.equals("#000000")) {
            String cookieName = "background";
            Cookie cookieColor = new Cookie(cookieName, cookieColorValue);
            cookieColor.setMaxAge(100_000);
            resp.addCookie(cookieColor);
        }

        String cookieFontValue = req.getParameter("font");
        System.out.println("cookieFontValue = " + cookieFontValue);
        if (cookieFontValue != null && !cookieFontValue.isBlank()) {
            String cookieName = "font";
            Cookie cookieFont = new Cookie(cookieName, cookieFontValue);
            cookieFont.setMaxAge(100_000);
            resp.addCookie(cookieFont);
        }
//        String cookieValue = req.getParameter("cookie-value");
//        Cookie cookie = new Cookie(cookieName, cookieValue);
//        String cookiePath = req.getParameter("cookie-path");
//        if(cookiePath != null) {
//            cookie.setPath(cookiePath);
//        }
//        String cookieDomain = req.getParameter("cookie-domain");
//        if(cookieDomain != null) {
//            cookie.setDomain(cookieDomain);
//        }
//        String cookieExpired = req.getParameter("cookie-expired");
//        if(cookieExpired != null && !cookieExpired.isBlank()) {
//            int expired = Integer.parseInt(cookieExpired);
//            cookie.setMaxAge(expired);
//        }
//
//        String httpOnly = req.getParameter("cookie-httponly");
//        cookie.setHttpOnly(httpOnly != null && "on".equals(httpOnly.trim()));
//        String security = req.getParameter("cookie-security");
//        cookie.setSecure(security != null && "on".equals(security.trim()));
//        System.out.println("httpOnly = " + httpOnly);
//        resp.addCookie(cookie);

        resp.sendRedirect("/Lesson074/");
    }
}


