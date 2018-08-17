package it.etoken.component.admin.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.etoken.cache.service.CacheService;

@Component
@WebFilter(urlPatterns = "/admin/*", filterName = "filter2")
public class Filter2 implements Filter {

	final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Filter2.class);

	@Autowired
	CacheService cacheService;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		try {
			if("/api/admin/upload".equals(request.getRequestURI()) || "/admin/upload".equals(request.getRequestURI())) {
				chain.doFilter(req, res);
				return;
			}

			String uid = request.getHeader("uid");
			String token = request.getHeader("token");
			if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token)) {
				doResponse(response);
				return;
			}
			if (!token.equals(cacheService.getAdminToken(Long.parseLong(uid)))) {
				doResponse(response);
				return;
			}
			chain.doFilter(req, res);
		} catch (Exception e) {
			logger.error("", e);
			doResponse(response);
		}
	}

	private void doResponse(HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println("{\"code\":403,\"msg\":\"请重新登陆\"}");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}

}