package ar.com.tbf.web.generic.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Este filtro está para establecer el request y response en web apps que no usan anotaciones para establecer filtros.
 *
 */
public class RequestResponseAccessibilityFilter implements Filter{
    
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
        	
        	RequestResponseAccessibility.setRequestResponse( (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse );
        	
        	filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    
    	RequestResponseAccessibility.setServletContext( filterConfig.getServletContext() );
    }
    

    @Override
    public void destroy() {
    }	

}
