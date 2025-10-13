package sc.liste.noel.liste_noel.front.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static sc.liste.noel.liste_noel.front.constante.ConstantesSession.CONNECTED;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Récupère la session et ajoute "connected" au Model pour toutes les pages
        Boolean isConnected = (Boolean) request.getSession().getAttribute(CONNECTED);
        request.setAttribute(CONNECTED, isConnected != null && isConnected);
        return true;
    }
}
