package ar.com.tbf.web.generic.filter;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestResponseAccessibility {

	private static ThreadLocal<RequestResponseContainer> requestResponseContainer = new ThreadLocal<RequestResponseContainer>() {
		
		protected RequestResponseContainer initialValue() {
			return new RequestResponseAccessibility().new RequestResponseContainer();
		}
	};
	
	public static void setRequestResponse( HttpServletRequest request, HttpServletResponse response ) {
		
		requestResponseContainer.get().setRequest(  request  );
		requestResponseContainer.get().setResponse( response );
		
		
	}
	
	public static HttpServletRequest getRequest() {
		
		return requestResponseContainer.get().getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		
		return requestResponseContainer.get().getResponse();
	}

	public static String getQuery() {
		
		return requestResponseContainer.get().getQuery();
	}
	
	public static String getUrl() {
		
		return requestResponseContainer.get().getUrl();
	}
	
	public static String getRemoteIp() {
		
		return requestResponseContainer.get().remoteAddr;
	}

	public static String getRemoteHost() {
		
		return requestResponseContainer.get().remoteHost;
	}
	
	public static String getMyIp() throws UnknownHostException {
		
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	public static boolean mismoDominio( ) {
	
		boolean ok = false;
		
		try {
			
			String hostAddress = getMyIp();
			String remoteIp    = getRemoteIp();
			
			ok = hostAddress.equals(remoteIp);
								
			if( ! ok ) {
				
				ok = remoteIp.equals("0:0:0:0:0:0:0:1");
			}
			
		} catch (UnknownHostException e) {
		}
		
		return ok;
	}
	
	public static String getRealPath() {
		
		return getRealPath( File.separator );
	}

	public static String getRealPath( String path ) {
		
		return requestResponseContainer.get().request.getSession().getServletContext().getRealPath( path );
	}

	/**
	 * request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort() + request.getContextPath()
	 * 
	 * @return
	 */
	public static String getApplicationURL() {
		
		return requestResponseContainer.get().getApplicationURL();
	}
	
	/**
	 * @return el nombre de la aplicación obtenida del request.
	 */
	public static String whoIAm() {
		
		return requestResponseContainer.get().whoIAm();
	}

	public class RequestResponseContainer {
		
		private String url = null;
		private String query = "";
		private HttpServletRequest  request  = null;
		private HttpServletResponse response = null;
		private String remoteAddr = "";
		private String remoteHost = "";
		
		public HttpServletRequest getRequest() {
			return request;
		}
		public void setRequest(HttpServletRequest request) {
			
			String[] requestUriParts = request.getRequestURI().split("/");
			String   contextPath     = requestUriParts.length == 0 ? "" : requestUriParts[1];
			String   url             = request.getRequestURL().toString().replace(request.getRequestURI(), "");

			this.setUrl(   url +"/"+ contextPath    );
			this.setQuery( request.getQueryString() == null ? "" : request.getQueryString() );
			
			this.request = request;

	        if (request != null) {
	        	
	            remoteAddr = request.getHeader("X-FORWARDED-FOR");
	            
	            if (remoteAddr == null || remoteAddr.isEmpty() ) {
	            	
	                remoteAddr = request.getRemoteAddr();
	            }
	            
	            remoteHost = request.getRemoteHost();	            
	        }
		}
		public HttpServletResponse getResponse() { 
			return response;
		}
		public void setResponse(HttpServletResponse response) {
			this.response = response;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		
		public String getApplicationURL(){
	    	
	        return request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort() + request.getContextPath();
	    }

		public String whoIAm(){
	    	
	        return this.getRequest().getContextPath();
	    }	
	}
}
