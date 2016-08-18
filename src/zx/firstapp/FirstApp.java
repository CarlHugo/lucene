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
	
	
	//根据关键字搜索符合条件的内容
	@Test
	public void findIndexDB()throws Exception{
		String keywords="培训";
		List<Article> list = new ArrayList<Article>();
		FSDirectory directory = FSDirectory.open(new File("F:/IndexLib"));
		Version version = Version.LUCENE_30;
		StandardAnalyzer analyzer = new StandardAnalyzer(version);
		MaxFieldLength maxFieldLength = MaxFieldLength.LIMITED;
		
		//创建IndexSearcher字符流对象
		IndexSearcher indexSearcher = new IndexSearcher(directory);
		//创建查询解析器对象
		//参数一： Lucene版本 参数二： 对xcontent内容检索 参数三：对应使用的分词器
		QueryParser queryParser = new QueryParser(version, "xcontent", analyzer);
		//创建查询对象，封装关键字
		Query query = queryParser.parse(keywords);
		//根据关键字去索引库词汇表中查询
		//参数一：封装后的关键字
		//参数二：查询出的最大关键字 不足则以实际为主
		int MAX_RECORD=100;
		TopDocs topDocs = indexSearcher.search(query, MAX_RECORD);
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			//取出封装编号和分数的scoreDoc对象
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			//取出每一个编号
			int no=scoreDoc.doc;
			//去索引库中原始记录表中查询对应的document对象
			Document document = indexSearcher.doc(no);
			//获取document对象中三个属性值
			String xid=document.get("xid");
			String xtitle=document.get("xtitle");
			String xcontent=document.get("xcontent");
			//封装到Article对象中
			Article article = new Article(Integer.parseInt(xid),xtitle,xcontent);
			//将Article加入list集合
			list.add(article);
			
		}
		
		for(Article a:list){
			System.out.println(a);
		}
		
	}
	
	@Test
	//创建索引库
	public void createIndexDB() throws IOException{
		Article article = new Article(1,"培训","华师是一家培训机构");
		
		Document document = new Document();
		//将article对象的三个属性存入document对象
		//参数1：都document对象中的属性 参数二 ：document对象中属性的值 参数三: 是否由原始记录表转存入词汇表
		//参数四： 是否对属性值进行分词算法
		document.add(new Field("xid",article.getId().toString(),Store.YES,Index.ANALYZED));
		document.add(new Field("xtitle",article.getTitle().toString(),Store.YES,Index.ANALYZED));
		document.add(new Field("xcontent",article.getContent().toString(),Store.YES,Index.ANALYZED));
		
		//创建IndexWriter字符流对象
		//创建索引库存放位置
		FSDirectory directory = FSDirectory.open(new File("F:/IndexLib"));
		//version
		Version version = Version.LUCENE_30;
		//创建标准分词器
		StandardAnalyzer analyzer = new StandardAnalyzer(version);
		//将文本拆分成词汇后，去前1W个词汇
		MaxFieldLength 	maxLength = MaxFieldLength.LIMITED;
		//将document写入Lucene索引库
		IndexWriter indexWriter = new IndexWriter(directory, analyzer, maxLength);
		indexWriter.addDocument(document);
		
		indexWriter.close();
		
	}

}
