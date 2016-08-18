package zx.curd;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;

import zx.entity.Article;
import zx.util.LuceneUtil;

public class ArticleDao {
	
	@Test
	public void add() throws Exception{
		Article article = new Article(7,"��ѵ����","������һ�������ѵ����");
		Document document = LuceneUtil.javabean2document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(),LuceneUtil.getAnalyzer(),LuceneUtil.getMaxFieldLength());
		indexWriter.addDocument(document);
		indexWriter.close();
	}
	
	@Test
	public void addAll() throws Exception{
		
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(),LuceneUtil.getAnalyzer(),LuceneUtil.getMaxFieldLength());
		
		Article article = new Article(1,"��ѵ","��ʦ��һ�ҽ�����ѵ����");
		Document document = LuceneUtil.javabean2document(article);
		indexWriter.addDocument(document);
		
		Article article2 = new Article(2,"��ѵ","��ʦ��һ��IT��ѵ����");
		Document document2 = LuceneUtil.javabean2document(article2);
		indexWriter.addDocument(document2);
		
		Article article3 = new Article(3,"��ѵ","��ʦ��һ��������ѵ����");
		Document document3 = LuceneUtil.javabean2document(article3);
		indexWriter.addDocument(document3);
		
		Article article4 = new Article(4,"��ѵ","��ʦ��һ��������ѵ����");
		Document document4 = LuceneUtil.javabean2document(article4);
		indexWriter.addDocument(document4);
		
		Article article5 = new Article(5,"��ѵ","��ʦ��һ����ѯ��ѵ����");
		Document document5 = LuceneUtil.javabean2document(article5);
		indexWriter.addDocument(document5);
		
		Article article6 = new Article(6,"��ѵ","��ʦ��һ�ҵ�����ѵ����");
		Document document6 = LuceneUtil.javabean2document(article6);
		indexWriter.addDocument(document6);
		
		indexWriter.close();
		
	}
	@Test
	public void update() throws Exception{
		Article article = new Article(1,"��ѵ","������һ��Ӣ����ѵ����");
		Document document = LuceneUtil.javabean2document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(),LuceneUtil.getAnalyzer(),LuceneUtil.getMaxFieldLength());
		
		indexWriter.updateDocument(new Term("id","1"), document);
		indexWriter.close();
	}
	
	@Test
	public void delete()throws Exception{
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(),LuceneUtil.getAnalyzer(),LuceneUtil.getMaxFieldLength());
		indexWriter.deleteDocuments(new Term("id","3"));
		indexWriter.close();
	}
	
	@Test
	public void deleteAll() throws CorruptIndexException, LockObtainFailedException, IOException{
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(),LuceneUtil.getAnalyzer(),LuceneUtil.getMaxFieldLength());
		indexWriter.deleteAll();
		indexWriter.close();
	}
	
	@Test
	public void findAllByKeywords() throws CorruptIndexException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ParseException{
		String keywords="��ѯ";
		List<Article> list = new ArrayList<Article>();
		QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
		TopDocs topDocs = indexSearcher.search(query, 100);
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			ScoreDoc scoreDoc=topDocs.scoreDocs[i];
			int no=scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article=(Article)LuceneUtil.document2javabean(document, Article.class);
			list.add(article);
		}
		
		for(Article a:list){
			System.out.println(a);
		}
		
	}

}
