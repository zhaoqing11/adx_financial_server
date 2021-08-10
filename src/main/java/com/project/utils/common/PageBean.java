package com.project.utils.common;

public class PageBean<T> {
    public PageBean() {
    }

    // 已知数据
    private int pageNum = 1; // 当前页,从请求那边传过来。
    private int pageSize = 10; // 每页显示的数据条数。 如果是1000代表不用分页
    private int totalRecord = 0; // 总的记录条数。查询数据库得到的数据

    // 需要计算得来
    private int totalPage; // 总页数，通过totalRecord和pageSize计算可以得来
    // 开始索引，也就是我们在数据库中要从第几行数据开始拿，有了startIndex和pageSize，
    // 就知道了limit语句的两个数据，就能获得每页需要显示的数据了
    private int startIndex;

    // 将每页要显示的数据放在list集合中
    private Object dataLists;

    // 通过pageNum，pageSize，totalRecord计算得来tatalPage和startIndex
    // 构造方法中将pageNum，pageSize，totalRecord获得
    public PageBean(int pageNum, int pageSize, int totalRecord) {
        // 防止传入负数或者0 导致出现负数
        if (pageNum >= 0) {
            this.pageNum = pageNum;
        }
        // 防止没有传入参数，或者传入错误参数 给定默认值15
        if (pageSize >= 0) {
            this.pageSize = pageSize;
        }

        this.totalRecord = totalRecord;

        // totalPage 总页数
        if (pageSize > 0) {
            this.totalPage = this.totalRecord % this.pageSize == 0 ? this.totalRecord / this.pageSize
                    : this.totalRecord / this.pageSize + 1;
            // 开始索引
            this.startIndex = (this.pageNum - 1) * this.pageSize;
        }
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public Object getList() {
        return dataLists;
    }

    public void setList(Object dataLists) {
        this.dataLists = dataLists;
    }

    @Override
    public String toString() {
        return "PageBean [pageNum=" + pageNum + ", pageSize=" + pageSize + ", totalRecord=" + totalRecord
                + ", totalPage=" + totalPage + ", startIndex=" + startIndex + ", dataLists=" + dataLists + "]";
    }
}
