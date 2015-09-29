package simpleshop.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import simpleshop.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a simple bean that contains how to sort a list and which page is being requested.
 */
@JsonFilter("propNameFilter")
@JsonIgnoreProperties({"pageSizePlusOne", "sortInfoList"})
public class PageInfo implements Serializable{
    private int pageIndex;
    private int pageSize;
    private SortInfo sortInfo;

    private boolean pageSizePlusOne = false;
    private List<SortInfo> sortInfoList;

    public PageInfo(){
        pageIndex = 0;
        pageSize = Constants.DEFAULT_PAGE_SIZE;
    }

    /**
     * Data structure that contains how to apply paging to a search result.
     * @param pageIndex 0 based index.
     * @param pageSize items in a page.
     */
    public PageInfo(int pageIndex, int pageSize){
        setPageIndex(pageIndex);
        setPageSize(pageSize);
    }


    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPageSizePlusOne() {
        return pageSizePlusOne;
    }

    public void setPageSizePlusOne(boolean pageSizePlusOne) {
        this.pageSizePlusOne = pageSizePlusOne;
    }

    public SortInfo getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }

    public List<SortInfo> getSortInfoList() {
        if(sortInfoList == null && sortInfo != null){
            sortInfoList = new ArrayList<>();
            sortInfoList.add(sortInfo);
        }
        return sortInfoList;
    }

    public void setSortInfoList(List<SortInfo> sortInfoList) {
        this.sortInfoList = sortInfoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageInfo pageInfo = (PageInfo) o;

        if (pageIndex != pageInfo.pageIndex) return false;
        if (pageSize != pageInfo.pageSize) return false;
        if (pageSizePlusOne != pageInfo.pageSizePlusOne) return false;
        return !(sortInfo != null ? !sortInfo.equals(pageInfo.sortInfo) : pageInfo.sortInfo != null);

    }

    @Override
    public int hashCode() {
        int result = pageIndex;
        result = 31 * result + pageSize;
        result = 31 * result + (pageSizePlusOne ? 1 : 0);
        result = 31 * result + (sortInfo != null ? sortInfo.hashCode() : 0);
        return result;
    }
}
