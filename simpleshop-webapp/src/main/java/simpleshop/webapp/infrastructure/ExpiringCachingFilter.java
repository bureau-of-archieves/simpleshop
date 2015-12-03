package simpleshop.webapp.infrastructure;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Turn on caching for /ng/*.jsp
 */
public class ExpiringCachingFilter implements Filter {

    private Integer seconds;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String value = filterConfig.getInitParameter("seconds");
        if(value != null && !value.isEmpty()) {
            seconds = Integer.parseInt(value);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if(seconds != null && servletResponse instanceof HttpServletResponse){
            HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

            httpServletResponse.setHeader("Cache-Control", "private,max-age=" + seconds); //HTTP 1.1
            httpServletResponse.setHeader("Pragma", "private");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.SECOND, seconds);
            httpServletResponse.setDateHeader("Expires", calendar.getTimeInMillis());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
