package simpleshop.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import simpleshop.common.StringUtils;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SortInfo implements Serializable {

    private boolean ascending = true;
    private String alias = "";
    private String property;

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return (StringUtils.isNullOrEmpty(alias) ? " " : " " + alias + ".") + property + (ascending ? "" : " desc");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortInfo sortInfo = (SortInfo) o;

        if (ascending != sortInfo.ascending) return false;
        if (!alias.equals(sortInfo.alias)) return false;
        return property.equals(sortInfo.property);

    }

    @Override
    public int hashCode() {
        int result = (ascending ? 1 : 0);
        result = 31 * result + alias.hashCode();
        result = 31 * result + property.hashCode();
        return result;
    }
}
