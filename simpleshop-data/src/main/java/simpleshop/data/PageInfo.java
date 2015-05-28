package simpleshop.data;

import org.apache.commons.collections.ListUtils;
import simpleshop.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * This is a simple bean that contains how to sort a list and which page is being requested.
 */
public class PageInfo implements Serializable{
    private int pageIndex;
    private int pageSize;
    private List<SortInfo> sortInfoList;

    public PageInfo(){
        pageIndex = 0;
        pageSize = Constants.DEFAULT_PAGE_SIZE;
    }

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

    public List<SortInfo> getSortInfoList() {
        return sortInfoList;
    }

    public void setSortInfoList(List<SortInfo> sortInfo) {
        this.sortInfoList = sortInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageInfo pageInfo = (PageInfo) o;

        if (pageIndex != pageInfo.pageIndex) return false;
        if (pageSize != pageInfo.pageSize) return false;

        if (!ListUtils.isEqualList(sortInfoList,pageInfo.sortInfoList ))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageIndex;
        result = 31 * result + pageSize;
        result = 31 * result + ListUtils.hashCodeForList(sortInfoList);
        return result;
    }
}
