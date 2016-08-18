package zx.fy.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import zx.fy.dao.ArticleDao;
import zx.fy.entity.Article;
import zx.fy.entity.Page;

public class ArticleService {
	
	private ArticleDao articleDao=new ArticleDao();
	
	//���ݹؼ��ֲ�ѯ����
	public Page show(String keywords,int currPageNO) throws IOException, ParseException, InstantiationException, IllegalAccessException, InvocationTargetException{
		
		Page page = new Page();
		
		//��װ��ǰҳ��
		page.setCurrPageNO(currPageNO);
		//��װ�ܼ�¼��
		int allRecordNO=articleDao.getAllRecord(keywords);
		page.setAllRecordNO(allRecordNO);
		//��װ��ҳ��
		int allPageNO=0;
		if(page.getAllRecordNO()%page.getPerPageSize()==0){
			allPageNO=page.getAllRecordNO()/page.getPerPageSize();
		}else{
			allPageNO=page.getAllRecordNO()/page.getPerPageSize()+1;
		}
		page.setAllPageNO(allPageNO);
		//��װ����
		int size=page.getPerPageSize();
		int start=(page.getCurrPageNO()-1)*size;
		List<Article> list = articleDao.findAll(keywords, start, size);
		page.setArticleList(list);
	
		return page;
		
	}
	
	public static void main(String[] args) throws IOException, ParseException, InstantiationException, IllegalAccessException, InvocationTargetException {
		ArticleService articleService = new ArticleService();
		Page page = articleService.show("ѵ", 2);
		
		System.out.println("getAllPageNO="+page.getAllPageNO());
		System.out.println("getAllRecordNO="+page.getAllRecordNO());
		System.out.println("getCurrPageNO="+page.getCurrPageNO());
		System.out.println("getPerPageSize="+page.getPerPageSize());
		for(Article a:page.getArticleList()){
			System.out.println(a);
			
		}
		
		
	} 
	

}
