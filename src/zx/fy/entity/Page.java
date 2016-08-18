package zx.fy.entity;

import java.util.ArrayList;
import java.util.List;

public class Page {
	
	private Integer currPageNO;//当前页
	private Integer perPageSize=2;//每页的记录条数
	private Integer allPageNO;//总的页数
	private Integer allRecordNO;//总记录数
	private List<Article> articleList=new ArrayList<Article>();//内容
	
	
	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public Page(){}

	public Integer getCurrPageNO() {
		return currPageNO;
	}

	public void setCurrPageNO(Integer currPageNO) {
		this.currPageNO = currPageNO;
	}

	public Integer getPerPageSize() {
		return perPageSize;
	}

	public void setPerPageSize(Integer perPageSize) {
		this.perPageSize = perPageSize;
	}

	public Integer getAllPageNO() {
		return allPageNO;
	}

	public void setAllPageNO(Integer allPageNO) {
		this.allPageNO = allPageNO;
	}

	public Integer getAllRecordNO() {
		return allRecordNO;
	}

	public void setAllRecordNO(Integer allRecordNO) {
		this.allRecordNO = allRecordNO;
	}

	public Page(Integer currPageNO, Integer perPageSize, Integer allPageNO,
			Integer allRecordNO, List<Article> articleList) {
		this.currPageNO = currPageNO;
		this.perPageSize = perPageSize;
		this.allPageNO = allPageNO;
		this.allRecordNO = allRecordNO;
		this.articleList = articleList;
	}
	
	

}
