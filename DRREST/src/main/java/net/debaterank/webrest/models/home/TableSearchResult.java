package net.debaterank.webrest.models.home;

public class TableSearchResult {
    public String name;
    public int page;
    public int index;

    public TableSearchResult(String name, int page, int index) {
        this.name = name;
        this.page = page;
        this.index = index;
    }

    public TableSearchResult() {}
}
