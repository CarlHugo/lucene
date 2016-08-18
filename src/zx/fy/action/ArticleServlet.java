package zx.fy.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryParser.ParseException;

import zx.fy.entity.Page;
import zx.fy.service.ArticleService;

public class ArticleServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			request.setCharacterEncoding("utf-8");
			
			String keywords=request.getParameter("keywords");
			if(keywords==null||keywords.trim().length()==0){
				keywords="培训";
			}
			
			String temp=request.getParameter("currPageNO");
			if(temp==null||keywords.trim().length()==0){
				temp="1";
			}
			//调用业务层
			ArticleService articleService = new ArticleService();
			Page page = articleService.show(keywords, Integer.parseInt(temp));
			
			//将page对象绑定到request域中
			request.setAttribute("PAGE", page);
			//将keywords绑定到request域中
			request.setAttribute("KEYWORDS", keywords);
			
			request.getRequestDispatcher("/list.jsp").forward(request, response);
		}  catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
		
	}

}
