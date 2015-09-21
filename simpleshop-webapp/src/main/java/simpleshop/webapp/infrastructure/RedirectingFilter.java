package simpleshop.webapp.infrastructure;


import simpleshop.common.Pair;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hard coded redirects
 */
public class RedirectingFilter implements Filter {

    private List<Pair<String, String>> mapping = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String value = filterConfig.getInitParameter("mapping");
        if(value == null){
            return;
        }

        String[] entries = value.split(";");
        if(entries.length == 0)
            return;

        ArrayList<Pair<String, String>> result = new ArrayList<>();
        for(String entry : entries){
            String[] pair = entry.split(",");
            if(pair.length != 2)
                throw new IllegalArgumentException("RedirectingFilter mapping entry should be separated by a comma; '" + entry + "' is invalid.");
            result.add(new Pair<>(pair[0], pair[1]));
        }
        mapping = Collections.unmodifiableList(result);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(mapping == null || !(servletRequest instanceof HttpServletRequest))
            return;

        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String requestUrl = httpServletRequest.getRequestURI();
        for(Pair<String, String> pair : mapping){
            if(requestUrl.matches(pair.getKey())){
                httpServletRequest.getRequestDispatcher(pair.getValue()).forward(servletRequest, servletResponse);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        mapping = null;
    }
}
