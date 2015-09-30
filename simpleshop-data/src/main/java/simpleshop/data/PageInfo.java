package simpleshop.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Sort;
import simpleshop.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a simple bean that contains how to sort a list and which page is being requested.
 */
@JsonFilter("propNameFilter")
@JsonIgnoreProperties({"pageSizePlusOne"})
public class PageInfo implements Serializable{
    private int pageIndex;
    private int pageSize;
    private List<SortInfo> sortInfoList;

    /**
     * Whether to retrieve 1 additional record to find out if this page is the last one.
     */
    private boolean pageSizePlusOne = false;

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

    /**
     * Add a SortInfo instance. Order matters of course.
     * @param sortInfo what to sort on.
     */
    public void addSortInfo(SortInfo sortInfo){
        if(sortInfo == null)
            return;
        if(this.sortInfoList == null){
            this.sortInfoList = new ArrayList<>();
        }
        sortInfoList.add(sortInfo);
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

    public List<SortInfo> getSortInfoList() {
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
        return !(sortInfoList != null ? !sortInfoList.equals(pageInfo.sortInfoList) : pageInfo.sortInfoList != null);

    }

    @Override
    public int hashCode() {
        int result = pageIndex;
        result = 31 * result + pageSize;
        result = 31 * result + (sortInfoList != null ? sortInfoList.hashCode() : 0);
        return result;
    }
}
