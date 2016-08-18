package zx.firstapp;

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

public class FirstApp {
	
	
	//���ݹؼ���������������������
	@Test
	public void findIndexDB()throws Exception{
		String keywords="��ѵ";
		List<Article> list = new ArrayList<Article>();
		FSDirectory directory = FSDirectory.open(new File("F:/IndexLib"));
		Version version = Version.LUCENE_30;
		StandardAnalyzer analyzer = new StandardAnalyzer(version);
		MaxFieldLength maxFieldLength = MaxFieldLength.LIMITED;
		
		//����IndexSearcher�ַ�������
		IndexSearcher indexSearcher = new IndexSearcher(directory);
		//������ѯ����������
		//����һ�� Lucene�汾 �������� ��xcontent���ݼ��� ����������Ӧʹ�õķִ���
		QueryParser queryParser = new QueryParser(version, "xcontent", analyzer);
		//������ѯ���󣬷�װ�ؼ���
		Query query = queryParser.parse(keywords);
		//���ݹؼ���ȥ������ʻ���в�ѯ
		//����һ����װ��Ĺؼ���
		//����������ѯ�������ؼ��� ��������ʵ��Ϊ��
		int MAX_RECORD=100;
		TopDocs topDocs = indexSearcher.search(query, MAX_RECORD);
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			//ȡ����װ��źͷ�����scoreDoc����
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			//ȡ��ÿһ�����
			int no=scoreDoc.doc;
			//ȥ��������ԭʼ��¼���в�ѯ��Ӧ��document����
			Document document = indexSearcher.doc(no);
			//��ȡdocument��������������ֵ
			String xid=document.get("xid");
			String xtitle=document.get("xtitle");
			String xcontent=document.get("xcontent");
			//��װ��Article������
			Article article = new Article(Integer.parseInt(xid),xtitle,xcontent);
			//��Article����list����
			list.add(article);
			
		}
		
		for(Article a:list){
			System.out.println(a);
		}
		
	}
	
	@Test
	//����������
	public void createIndexDB() throws IOException{
		Article article = new Article(1,"��ѵ","��ʦ��һ����ѵ����");
		
		Document document = new Document();
		//��article������������Դ���document����
		//����1����document�����е����� ������ ��document���������Ե�ֵ ������: �Ƿ���ԭʼ��¼��ת����ʻ��
		//�����ģ� �Ƿ������ֵ���зִ��㷨
		document.add(new Field("xid",article.getId().toString(),Store.YES,Index.ANALYZED));
		document.add(new Field("xtitle",article.getTitle().toString(),Store.YES,Index.ANALYZED));
		document.add(new Field("xcontent",article.getContent().toString(),Store.YES,Index.ANALYZED));
		
		//����IndexWriter�ַ�������
		//������������λ��
		FSDirectory directory = FSDirectory.open(new File("F:/IndexLib"));
		//version
		Version version = Version.LUCENE_30;
		//������׼�ִ���
		StandardAnalyzer analyzer = new StandardAnalyzer(version);
		//���ı���ֳɴʻ��ȥǰ1W���ʻ�
		MaxFieldLength 	maxLength = MaxFieldLength.LIMITED;
		//��documentд��Lucene������
		IndexWriter indexWriter = new IndexWriter(directory, analyzer, maxLength);
		indexWriter.addDocument(document);
		
		indexWriter.close();
		
	}

}
