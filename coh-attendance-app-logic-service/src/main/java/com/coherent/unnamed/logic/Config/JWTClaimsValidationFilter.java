package com.coherent.unnamed.logic.Config;


import com.coherent.unnamed.logic.Constants.Constants;
import com.coherent.unnamed.logic.Constants.ErrorCode;
import com.coherent.unnamed.logic.dto.UserContextDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JWTClaimsValidationFilter extends OncePerRequestFilter {


    private Logger logger = LoggerFactory.getLogger(JWTClaimsValidationFilter.class);

    private static final String JWTPREFIX = "Bearer";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String EMPTY_STRING = "";

    @Value("${app.subject}")
    private String subject;

    @Value("${app.issuer}")
    private String issuer;

    private RSAPrivateKey pk;


    public JWTClaimsValidationFilter() {
        Security.addProvider(new BouncyCastleProvider());
    }



    @PostConstruct
    public void init() {
        try {
            Resource resource = new ClassPathResource(Constants.OAUTH);
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String privateKey = new String(bdata, StandardCharsets.UTF_8);
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
            this.pk = (RSAPrivateKey) kf.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            logger.error("Exception occured while loading oauth utills", e);
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (!checker(request)) {
                if (StringUtils.isNotBlank(request.getHeader(AUTHORIZATION_HEADER))) {

                    logger.info("Came to jwtClaimsFilter :::");
                    String token = request.getHeader(AUTHORIZATION_HEADER);

                    if (token.startsWith("Bearer")) {
                        throw new ServletException(Constants.BEARER_STR_MISSING);
                    }

                    String accessToken = token.replaceAll(JWTPREFIX, EMPTY_STRING);

                    // New validation
                    if (accessToken != null && !"".equalsIgnoreCase(accessToken)) {

                        String[] split_string = accessToken.split("\\.");
                        String base64EncodedHeader = split_string[0];
                        String base64EncodedBody = split_string[1];
                        String base64EncodedSignature = split_string[2];

                        if (StringUtils.isBlank(base64EncodedHeader) || StringUtils.isBlank(base64EncodedBody)
                                || StringUtils.isBlank(base64EncodedSignature)) {

                            throw new ServletException(Constants.TAMPERED_TOKEN);

                        }


                        Base64 base64Url = new Base64(true);

                        JSONObject jsonHeader = new JSONObject(new String(base64Url.decode(base64EncodedHeader)));

                        if ("none".equalsIgnoreCase(String.valueOf(jsonHeader.get("alg")))) {

                            logger.debug("going to none loop");
                            throw new ServletException(Constants.TAMPERED_TOKEN);

                        }

                        JSONObject jsonBody = new JSONObject(new String(base64Url.decode(base64EncodedBody)));

                        Date tokenExpAt = new Date(Long.valueOf(String.valueOf(jsonBody.get(Constants.TOKEN_EXPIRE_AT_STR))));

                        logger.debug(":: date exp ::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tokenExpAt));

                        if (tokenExpAt.before(new Date())) {
                            logger.debug("going to expired loop");
                            throw new ServletException(Constants.TOKEN_EXPIRED);

                        }


                        Claims claims = Jwts.parser().setSigningKey(pk).parseClaimsJws(accessToken).getBody();


                        if (claims.getSubject() != null && !subject.equalsIgnoreCase(claims.getSubject())
                                && claims.getIssuer() != null && issuer.equalsIgnoreCase(claims.getIssuer())) {

                            logger.debug("going to invalid token format loop");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constants.INVALID_TOKEN_FORMAT);
                            throw new ServletException( ErrorCode.CAP_1003.getMessage());

                        }

                        Jws parseClaimsJws = Jwts.parser().setSigningKey(pk).parseClaimsJws(accessToken);

                        if (!(parseClaimsJws.getHeader() != null && parseClaimsJws.getBody() != null
                                && parseClaimsJws.getSignature() != null)) {

                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constants.INVALID_TOKEN_FORMAT);
                            throw new ServletException( ErrorCode.CAP_1003.getMessage());

                        } else {
                            String json = jsonBody.toString();

                            ObjectMapper objectMapper = new ObjectMapper();
                            UserContextDTO userDetail = objectMapper.readValue(json, UserContextDTO.class);

                            // Validating the token with already available  logut token
                           UserContextHolder.setUserDto(userDetail);

                        }

                    } else {

                        response.sendError(HttpServletResponse.SC_NOT_FOUND, Constants.EMPTY_TOKEN);
                        throw new ServletException(ErrorCode.CAP_1003.getMessage());

                    }

                } else {

                    logger.info("Came to jwtClaimsFilter ::: in empty token flow");
                    throw new ServletException(Constants.EMPTY_TOKEN);

                }
                filterChain.doFilter(request, response);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            request.getRequestDispatcher("/error?errorCode=401&errorMessage=" + e.getMessage())
                    .forward(request, response);
        }
    }


    private boolean checker(HttpServletRequest request) {
        if (request.getRequestURI().equalsIgnoreCase("/v2/api-docs")) {
            return true;
        }
        if (request.getRequestURI().startsWith("/configuration")) {
            return true;
        }
        if (request.getRequestURI().startsWith("/swagger")) {
            return true;
        }
        if (request.getRequestURI().equalsIgnoreCase("/v2/api-docs")) {
            return true;
        }
        if (request.getRequestURI().startsWith("/webjars")) {
            return true;
        }
        return false;
    }
}