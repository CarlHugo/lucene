package zx.fy.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import zx.fy.entity.Article;
import zx.util.LuceneUtil;

public class ArticleDao {
	
	//获取总的记录数
	public int getAllRecord(String keywords) throws IOException, ParseException{
		QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
		TopDocs topDocs = indexSearcher.search(query, 2);
		
		return topDocs.totalHits;
	}
	
	//根据关键字批量查询记录
	public List<Article> findAll(String keywords,int start,int size) throws InstantiationException, IllegalAccessException, InvocationTargetException, CorruptIndexException, IOException, ParseException{
		List<Article> list = new ArrayList<Article>();
		QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		Query parse = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
		
		TopDocs topDocs = indexSearcher.search(parse, 300);
		int min=Math.min(start+size, topDocs.totalHits);
		for(int i=start;i<min;i++){
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no=scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = (Article)LuceneUtil.document2javabean(document, Article.class);
			list.add(article);
		}
		return list;
		
	}
	
	public static void main(String[] args) throws IOException, ParseException, InstantiationException, IllegalAccessException, InvocationTargetException {
		ArticleDao dao = new ArticleDao();
		int record = dao.getAllRecord("培训");
		System.out.println(record);
		
		System.out.println("=================第一页================");
		for(Article a:dao.findAll("训", 0, 2)){
			System.out.println(a);
		}
		
		System.out.println("=================第二页================");
		for(Article a:dao.findAll("训", 2, 2)){
			System.out.println(a);
		}
		
		System.out.println("=================第三页================");
		for(Article a:dao.findAll("训", 4, 2)){
			System.out.println(a);
		}
		
		System.out.println("=================第四页================");
		for(Article a:dao.findAll("训", 6, 2)){
			System.out.println(a);
		}
		
		
		
	}

}
