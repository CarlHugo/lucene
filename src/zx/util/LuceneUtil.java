package zx.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import zx.entity.Article;



public class LuceneUtil {
	
	private static Directory directory;
	private static Version version;
	private static Analyzer analyzer;
	private static MaxFieldLength maxFieldLength;
	
	static{
		try {
			directory=FSDirectory.open(new File("F:/IndexLib"));
			version=Version.LUCENE_30;
			analyzer=new StandardAnalyzer(version);
			maxFieldLength=MaxFieldLength.LIMITED;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static Directory getDirectory() {
		return directory;
	}

	public static Version getVersion() {
		return version;
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static MaxFieldLength getMaxFieldLength() {
		return maxFieldLength;
	}
	
	//不让外界new该帮助类
	private LuceneUtil(){}
	
	//将javabean转为document对象
	public static Document javabean2document(Object obj)throws Exception{
		//创建document对象
		Document document = new Document();
		//获取obj对象引用的字节码文件
		Class clazz = obj.getClass();
		//通过对象字节码获取私有的属性
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		//迭代
		for(java.lang.reflect.Field reflectField:fields){
			//强力反射
			reflectField.setAccessible(true);
			//获取属性名
			String name = reflectField.getName();
			//人工拼接方法名
			String methodName="get"+name.substring(0,1).toUpperCase()+name.substring(1);
			//获取方法
			Method method=clazz.getMethod(methodName,null);
			//执行方法
			String value=method.invoke(obj, null).toString();
			//加入到document对象中
			document.add(new Field(name,value,Store.YES,Index.ANALYZED));
			
		}
		return document;
		
	}
	
	//将document对象转换为javabean对象
	public static Object document2javabean(Document document,Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		Object obj=clazz.newInstance();
		java.lang.reflect.Field[] reflectFields=clazz.getDeclaredFields();
		for(java.lang.reflect.Field reflectField:reflectFields){
			reflectField.setAccessible(true);
			String name=reflectField.getName();//id,title,content
			String value=document.get(name);
			BeanUtils.setProperty(obj, name, value);
		}
		return obj;
	}
	
	public static void main(String[] args) throws Exception {
		Article article = new Article(1,"培训","华师是一家培训机构");
		Document document = LuceneUtil.javabean2document(article);
		
		System.out.println("================");
		
		Article article2=(Article)LuceneUtil.document2javabean(document, Article.class);
		System.out.println(article2);
	}
	

}
