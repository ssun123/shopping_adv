package com.kt.ai.commerce.common.domain;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kt.ai.commerce.common.utils.CommProperty;
import com.kt.ai.commerce.common.utils.StringUtil;

/**
 * 페이징 관련.
 * 
 * @version Paging.java - 2009. 1. 30
 */
public class Paging extends ArrayList<Object> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1328138461068955044L;

	/** The log. */
	private final Log log;

	/** The block first page. */
	private int blockFirstPage; // 페이지 시작페이지..번호

	/** The block last page. */
	private int blockLastPage; // 페이지 끝페이지 번호.

	/** The current page. */
	private int currentPage; // 현재 페이지 번호..

	/** The next page. */
	private int nextPage; // 다음 페이지 번호..

	/** The pre page. */
	private int prePage; // 이전 페이지 번호..

	/** The last page. */
	private int lastPage; // 마지막 페이지 번호..

	/** The row per page. */
	private int rowPerPage; // 한페이지에 보여지는 줄수..

	/** The page per page. */
	private int pagePerPage; // 페이지 번호 갯수..

	/** The total row. */
	private int totalRow; // 총 데이타수..

	/** The index no. */
	private int indexNo; // 게시번호..

	/** The page num col. */
	private Collection pageNumCol;

	/**
	 * Instantiates a new paging.
	 */
	public Paging() {
		this(new ArrayList(), 0, 0, -1);
	}

	/**
	 * 페이지당 보여지는 ROW수를 따로 지정안하고 기본 값을 사용 한다. 데이타 LIST, 총 데이타 ROW , 시작ROW
	 * 
	 * @param dataList
	 *            the data list
	 * @param totalRow
	 *            the total row
	 * @param currentPage
	 *            the current page
	 */
	public Paging(List dataList, int totalRow, int currentPage) {
		this(dataList, totalRow, currentPage, -1);
	}

	/**
	 * 페이지당 보여지는 ROW수를 따로 지정한다. 데이타 LIST, 총 데이타 ROW , 시작ROW, 페이지당 보여지는 ROW수
	 * 
	 * @param dataList
	 *            the data list
	 * @param totalRow
	 *            the total row
	 * @param currentPage
	 *            the current page
	 * @param rowPerPage
	 *            the row per page
	 */
	public Paging(List dataList, int totalRow, int currentPage, int rowPerPage) {
		log = LogFactory.getLog(getClass());
		this.rowPerPage = StringUtil.parseInt("10");
		this.pagePerPage = StringUtil.parseInt("10");
		// RowPerpage를 따로 지정할시 rowPerPage를 설정한다.
		if (rowPerPage > 0)
			this.rowPerPage = rowPerPage;
		this.totalRow = totalRow;
		this.currentPage = currentPage;
		// dataList를 ListArray에 넣기...
		addAll(dataList);
		if (currentPage == 0) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
		lastPage = (int) Math.ceil((double) getTotalRow() / (double) getRowPerPage());

		if (lastPage < this.currentPage)
			this.currentPage = lastPage;

		// 이전 페이지 구하기..
		int pre = (this.currentPage / pagePerPage);
		if ((this.currentPage % pagePerPage) == 0) {
			pre = pre - 1;
		}
		if (pre == 0) {
			prePage = 1;
		} else {
			prePage = (pre * pagePerPage);
		}
		if (prePage < 1) {
			prePage = 0;
		}
		// 다음 페이지 구하기.
		nextPage = (pre + 1) * pagePerPage + 1;
		if (nextPage > lastPage) {
			nextPage = lastPage;
		}

		// 페이지 시작 페이지 번호..
		blockFirstPage = pre * pagePerPage + 1;
		blockLastPage = (pre + 1) * pagePerPage;

		if (blockLastPage > lastPage) {
			blockLastPage = lastPage;
		}

		pageNumCol = new ArrayList();
		if (this.currentPage > 0) {
			for (int pageNum = blockFirstPage; blockLastPage >= pageNum; pageNum++)
				pageNumCol.add(new Integer(pageNum));
		}
	}

	/**
	 * Gets the index no.
	 * 
	 * @return the index no
	 */
	public int getIndexNo() {
		return totalRow - (currentPage - 1) * rowPerPage;
	}

	/**
	 * Gets the block first page.
	 * 
	 * @return the block first page
	 */
	public int getBlockFirstPage() {
		return blockFirstPage;
	}

	/**
	 * Sets the block first page.
	 * 
	 * @param blockFirstPage
	 *            the new block first page
	 */
	public void setBlockFirstPage(int blockFirstPage) {
		this.blockFirstPage = blockFirstPage;
	}

	/**
	 * Gets the block last page.
	 * 
	 * @return the block last page
	 */
	public int getBlockLastPage() {
		return blockLastPage;
	}

	/**
	 * Sets the block last page.
	 * 
	 * @param blockLastPage
	 *            the new block last page
	 */
	public void setBlockLastPage(int blockLastPage) {
		this.blockLastPage = blockLastPage;
	}

	/**
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets the current page.
	 * 
	 * @param currentPage
	 *            the new current page
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * Gets the next page.
	 * 
	 * @return the next page
	 */
	public int getNextPage() {
		return nextPage;
	}

	/**
	 * Sets the next page.
	 * 
	 * @param nextPage
	 *            the new next page
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * Gets the pre page.
	 * 
	 * @return the pre page
	 */
	public int getPrePage() {
		return prePage;
	}

	/**
	 * Sets the pre page.
	 * 
	 * @param prePage
	 *            the new pre page
	 */
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	/**
	 * Gets the last page.
	 * 
	 * @return the last page
	 */
	public int getLastPage() {
		return lastPage;
	}

	/**
	 * Sets the last page.
	 * 
	 * @param lastPage
	 *            the new last page
	 */
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	/**
	 * Gets the row per page.
	 * 
	 * @return the row per page
	 */
	public int getRowPerPage() {
		return rowPerPage;
	}

	/**
	 * Sets the row per page.
	 * 
	 * @param rowPerPage
	 *            the new row per page
	 */
	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}

	/**
	 * Gets the page per page.
	 * 
	 * @return the page per page
	 */
	public int getPagePerPage() {
		return pagePerPage;
	}

	/**
	 * Sets the page per page.
	 * 
	 * @param pagePerPage
	 *            the new page per page
	 */
	public void setPagePerPage(int pagePerPage) {
		this.pagePerPage = pagePerPage;
	}

	/**
	 * Gets the total row.
	 * 
	 * @return the total row
	 */
	public int getTotalRow() {
		return totalRow;
	}

	/**
	 * Sets the total row.
	 * 
	 * @param totalRow
	 *            the new total row
	 */
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	/**
	 * Gets the page num col.
	 * 
	 * @return the page num col
	 */
	public Collection getPageNumCol() {
		return pageNumCol;
	}

	/**
	 * Sets the page num col.
	 * 
	 * @param pageNumCol
	 *            the new page num col
	 */
	public void setPageNumCol(Collection pageNumCol) {
		this.pageNumCol = pageNumCol;
	}

	/**
	 * Sets the index no.
	 * 
	 * @param indexNo
	 *            the new index no
	 */
	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

}