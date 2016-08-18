package zx.secondapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import zx.entity.Article;
import zx.util.LuceneUtil;

public class SecondApp {
	
	
	//���ݹؼ���������������������
	@Test
	public void findIndexDB()throws Exception{
		String keywords="��ѵ";
		List<Article> list = new ArrayList<Article>();
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
		QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(), "content", LuceneUtil.getAnalyzer());
		Query query = queryParser.parse(keywords);
		int MAX_RECORD=100;
		TopDocs topDocs = indexSearcher.search(query, MAX_RECORD);
		
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no=scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = (Article)LuceneUtil.document2javabean(document, Article.class);
			list.add(article);
			
		}
		
		for(Article a:list){
			System.out.println(a);
		}
		
	}
	
	@Test
	//����������
	public void createIndexDB() throws Exception{
		Article article = new Article(1,"��ѵ","��ʦ��һ����ѵ����");
		
		Document document = LuceneUtil.javabean2document(article);
		//��documentд��Lucene������
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(),LuceneUtil.getAnalyzer(),LuceneUtil.getMaxFieldLength());
		indexWriter.addDocument(document);
		
		indexWriter.close();
		
	}

}
