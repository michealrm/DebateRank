package net.debaterank.webrest.services;

import net.debaterank.webrest.dao.RankingDAO;
import net.debaterank.webrest.models.home.DBDebaterTableSearchResult;
import net.debaterank.webrest.models.home.DebaterRanking;
import net.debaterank.webrest.models.home.SchoolSearchResult;
import net.debaterank.webrest.models.home.TableSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private RankingDAO rankingDAO;

    public SchoolSearchResult[] schoolSearch(String query) {
        return rankingDAO.schoolSearch(query);
    }

    public DebaterRanking[] getRankings(int page, int pageSize, String event, Optional<Integer> season) {
        if(event.equals("CX") || event.equals("PF") || event.equals("LD"))
            return rankingDAO.getRankingTable(page, pageSize, season, event);
        else
            return new DebaterRanking[0];
    }

    public long getPagesLength(int pageSize, String event, Optional<Integer> season) {
        return rankingDAO.getPagesLength(pageSize, season, event);
    }

    public Integer[] getSeasons() {
        return new Integer[] {2020, 2019, 2018, 2017, 2016, 2015, 2014, 2013, 2012, 2011, 2010, 2009,
                                2008, 2007, 2006, 2005};
    }

    public TableSearchResult[] debaterTableSearch(int pageSize, String query, Optional<Integer> season, Optional<Integer> school) {
        DBDebaterTableSearchResult[] dbResults = rankingDAO.debaterTableSearch(query, school, "LD", season);
        if(dbResults == null)
            return new TableSearchResult[0];
        else {
            TableSearchResult[] searchResults = new TableSearchResult[dbResults.length];
            for(int i = 0; i < dbResults.length; i++) {
                DBDebaterTableSearchResult dbResult = dbResults[i];
                TableSearchResult searchResult = new TableSearchResult();
                searchResult.name = String.format(
                        "%s %s %s",
                        dbResult.schoolName,
                        dbResult.first,
                        dbResult.last);
                searchResult.page = (dbResult.ranking - 1) / pageSize;
                searchResult.index = (dbResult.ranking - 1) % pageSize;
                searchResults[i] = searchResult;
            }
            return searchResults;
        }
        /*
        if(query.isEmpty()) {
            return new TableSearchResult[0];
        }
        else {
            List<TableSearchResult> result = new ArrayList<>();
            result.add(new TableSearchResult("Micheal Myers", 13, 13));
            result.add(new TableSearchResult("Geoffrey Okolo", 4, 7));
            return result.toArray(new TableSearchResult[0]);
        }

         */
    }
}
